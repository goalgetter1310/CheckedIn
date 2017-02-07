package com.checkedin.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObservable;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.checkedin.ManageDrawerMenu;
import com.checkedin.R;
import com.checkedin.SelectImage;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.container.HomeFrgContainer;
import com.checkedin.contentobeserver.ContactObserver;
import com.checkedin.dialog.CheckinLoungeFilterDialog;
import com.checkedin.fragment.ChatListFrg;
import com.checkedin.fragment.FriendFrg;
import com.checkedin.fragment.NotificationListFrg;
import com.checkedin.fragment.SearchFriendFrg;
import com.checkedin.fragment.TimelineFrg;
import com.checkedin.model.ContactList;
import com.checkedin.model.Notification;
import com.checkedin.services.ContactService;
import com.checkedin.services.ImageUploadService;
import com.checkedin.services.SendMessageService;
import com.checkedin.utility.ManageDatabase;
import com.checkedin.utility.MyPrefs;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements VolleyStringRequest.AfterResponse {//}, OnNavigationItemSelectedListener {


    private DrawerLayout dlMain;
    private Toolbar tbMain;
    private TextView tvTitle;
    private ImageView ivSearch;

    public Tracker tracker;
    private boolean doubleTapExit;
    private WebServiceCall webServiceCall;
    private CheckinLoungeFilterDialog.LoungeAddress loungeAddress;

    private ManageDrawerMenu manageDrawerMenu;
    private boolean isRestart;
    public ImageView ivChat;

    //    private ContactObserver contentObservable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setSupportActionBar(tbMain);
        removeFragment();

        manageDrawerMenu.init(tbMain, dlMain);
        manageDrawerMenu.openHome();

        ivSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                    Utility.doubleTapTime = System.currentTimeMillis();
                    SearchFriendFrg friendDialog = new SearchFriendFrg();
                    DialogFragmentContainer dialogFrgContainerN = DialogFragmentContainer.getInstance();
                    dialogFrgContainerN.init(friendDialog);
                    dialogFrgContainerN.show(getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                }
            }
        });

        webServiceCall.appOpenWsCall(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionEverywhere.getPermission(this, new String[]{Manifest.permission.READ_CONTACTS}, 101).enqueue(new PermissionResultCallback() {
                @Override
                public void onComplete(PermissionResponse permissionResponse) {
                    if (permissionResponse.isGranted()) {
                        String day = MyPrefs.getInstance(MainActivity.this).get("dayContact");
                        if (day == null || !day.equals(Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_MONTH) + ""))
//                            new ContactCall().execute();
                        {
                            Intent intent = new Intent(MainActivity.this, ContactService.class);
                            startService(intent);
                        }
                    }
                }
            });
        }
        else{
            String day = MyPrefs.getInstance(MainActivity.this).get("dayContact");
            if (day == null || !day.equals(Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_MONTH) + ""))
//                new ContactCall().execute();
            {
                Intent intent = new Intent(MainActivity.this, ContactService.class);
                startService(intent);
            }
        }

    }

    private void removeFragment() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI,false,contentObservable);

        if (!UserPreferences.isImageUploadCompleted(this)) {
            Intent imageUpload = new Intent(this, ImageUploadService.class);
            startService(imageUpload);
        }
        ManageDatabase database = new ManageDatabase();
        database.openDatabase(this);
        if (!database.isEmptyChatMsg()) {
            Intent sendMessageIntent = new Intent(MainActivity.this, SendMessageService.class);
            startService(sendMessageIntent);
        }
        database.closeDatabase();

        if(getIntent()!=null && getIntent().getBooleanExtra("IsNotification",false))
        {
            String notifyType = UserPreferences.fetchNotifyType(this);
            if (!TextUtils.isEmpty(notifyType)) {
                notificationClick(notifyType);
            }
        }
        manageDrawerMenu.setDrawerHeader();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String notifyType = UserPreferences.fetchNotifyType(this);
        if (!TextUtils.isEmpty(notifyType)) {
            notificationClick(notifyType);
        }
    }



    private void initViews() {
        setTitle("");
        dlMain = (DrawerLayout) findViewById(R.id.dl_main);
        tvTitle = (TextView) findViewById(R.id.tv_main_title);
        tbMain = (Toolbar) findViewById(R.id.tb_main);
        ivSearch = (ImageView) findViewById(R.id.iv_main_search);
        ivChat = (ImageView) findViewById(R.id.iv_main_chat);
        ivChat.setVisibility(View.GONE);
        webServiceCall = new WebServiceCall(this);
        manageDrawerMenu = new ManageDrawerMenu(this);
//        contentObservable=new ContactObserver();

    }

    public void setToolbarTitle(String title) {
        tvTitle.setText("");
        tvTitle.append(title);
        //tvTitle.invalidate();
        Utility.logUtils("Title -> "+tvTitle.getText().toString());
    }

    public void toggleActionBarIcon(int state, boolean animate) {
        manageDrawerMenu.toggleActionBarIcon(state, animate);
    }

    public void showSearch(int isVisible) {
        ivSearch.setVisibility(isVisible);
    }

    public void chatMsgBox() {
        Utility.currentFragment = getString(R.string.title_home);
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fl_container, new HomeFrgContainer(new ChatListFrg()), Utility.currentFragment);
        fTrans.commit();
    }

    private void notificationClick(String notifyType) {
        if (DialogFragmentContainer.isDialogOpen) {
            DialogFragmentContainer.getInstance().dismiss();
            DialogFragmentContainer.setInstance(null);
        }

        try {
            Utility.currentFragment = getString(R.string.title_home);
            switch (notifyType) {
                case Notification.NOTIFY_TYPE_TEXT_MSG:
                case Notification.NOTIFY_TYPE_IMG_MSG: {
                    FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                    fTrans.replace(R.id.fl_container, new HomeFrgContainer(new ChatListFrg()), Utility.currentFragment);
                    fTrans.commit();
                    break;
                }
                case Notification.NOTIFY_TYPE_FRIEND_REQUEST: {

                    FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                    fTrans.replace(R.id.fl_container, new HomeFrgContainer(), Utility.currentFragment);
                    fTrans.commit();

                    FriendFrg friendFrg = new FriendFrg();
                    FriendFrg.isFriendRequest = true;
                    DialogFragmentContainer dialogFrgContainerN = DialogFragmentContainer.getInstance();
                    dialogFrgContainerN.init(friendFrg);
                    dialogFrgContainerN.show(getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                    break;
                }
                case Notification.NOTIFY_TYPE_ACCEPT_FRIEND_REQUEST: {
                    FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                    fTrans.replace(R.id.fl_container, new HomeFrgContainer(), Utility.currentFragment);
                    fTrans.commit();

                    if (DialogFragmentContainer.isDialogOpen) {
                        DialogFragmentContainer.getInstance().dismiss();
                    }
                    TimelineFrg timelineFrg = new TimelineFrg();
                    Bundle argument = new Bundle();
                    argument.putString("friend_id", UserPreferences.fetchFriendConfirmId(this));
                    timelineFrg.setArguments(argument);
                    DialogFragmentContainer dialogFrgContainerN = DialogFragmentContainer.getInstance();
                    dialogFrgContainerN.init(timelineFrg);
                    dialogFrgContainerN.show(getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                    break;
                }
                default: {
//                    FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
//                    fTrans.replace(R.id.fl_container, new HomeFrgContainer(), Utility.currentFragment);
//                    fTrans.commit();
//
//                    NotificationListFrg notificationListFrg = new NotificationListFrg();
//                    DialogFragmentContainer dialogFrgContainerN = DialogFragmentContainer.getInstance();
//                    dialogFrgContainerN.init(notificationListFrg);
//                    dialogFrgContainerN.show(getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());

                    manageDrawerMenu.openNotification();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResponseReceive(int requestCode) {
        if (requestCode == WebServiceCall.REQUEST_CODE_LOGOUT) {
            Intent iLogin = new Intent(this, WelcomeActivity.class);
            startActivity(iLogin);
            finish();
        }
    }

    @Override
    public void onErrorReceive() {


    }

    public void setLoungeAddress(CheckinLoungeFilterDialog.LoungeAddress loungeAddress) {
        this.loungeAddress = loungeAddress;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500) {

            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (loungeAddress != null) {
                    loungeAddress.onLoungeAddresFilter(place.getAddress().toString());
                }
            }
        } else {
            SelectImage.getInstance().onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isRestart = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            if (isRestart && DialogFragmentContainer.isDialogOpen) {
                isRestart = false;
                DialogFragmentContainer dialogFragmentContainer = DialogFragmentContainer.getInstance();
//                dialogFragmentContainer.dismiss();
            }
            super.onSaveInstanceState(outState);
        } catch (Exception e) {

        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(null);
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (!manageDrawerMenu.closeDrawer()) {
                if (!manageDrawerMenu.isStackEmpty()) {
                    if (doubleTapExit) {
                        super.onBackPressed();
                    } else {
                        doubleTapExit = true;
                        Toast.makeText(this, R.string.confirm_exit, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                doubleTapExit = false;
                            }
                        }, 2000);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        getContentResolver().unregisterContentObserver(contentObservable);
        isRestart = false;
    }

    public void openHome() {
    manageDrawerMenu.openHome();
    }



    private String readAllContact() throws Exception{
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        LinkedList<String> stringList=new LinkedList<>();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        stringList.add(phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        ContactList contactList = new ContactList();
        contactList.setContactList(stringList);
        Gson gson = new Gson();
        String json = gson.toJson(contactList);
        return json;
    }

    class ContactCall extends AsyncTask<Void, Integer, String>
    {
        protected String doInBackground(Void...arg0) {
            try {
                return readAllContact();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Utility.logUtils(s);

                MyPrefs.getInstance(MainActivity.this).set("ContactList", s);
                MyPrefs.getInstance(MainActivity.this).set("dayContact", Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_MONTH)+"");
            }
            catch (Exception ae){ae.printStackTrace();}
            }
    }
}
