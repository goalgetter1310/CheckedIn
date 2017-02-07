package com.checkedin.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.checkedin.AnalyticsTrackers;
import com.checkedin.R;
import com.checkedin.SelectImage;
import com.checkedin.activity.BaseActivity;
import com.checkedin.activity.GalleryActivity;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.TagPhotoAdapter;
import com.checkedin.dialog.ImageChooserDialog;
import com.checkedin.dialog.PrivacyDialog;
import com.checkedin.dialog.TagFriendDialog;
import com.checkedin.dialog.TagFriendDialog.TagFriendListener;
import com.checkedin.model.CheckinActivity;
import com.checkedin.model.Friend;
import com.checkedin.model.Photos;
import com.checkedin.model.Place;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.PostModel;
import com.checkedin.services.ImageUploadService;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.google.android.gms.analytics.HitBuilders;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import java.util.ArrayList;

public class PostCheckinFrg extends Fragment implements OnClickListener, SelectImage.ActivityResult, TagFriendListener, VolleyStringRequest.AfterResponse {

    private TextView tvTags, tvPrivacy;
    private LinearLayout llPostPrivacy;
    private ImageView ivAddPhotos, ivTagFriend;
    private RecyclerView rvImages;
    private TagFriendDialog tagFriendDialog;
    private TagPhotoAdapter adptTagPhoto;
    private CheckinActivity checkinPostActivity;
    private Place venue;
    private CircleImageView civProfileImg;
    private EditText etDesc;


    private WebServiceCall webServiceCall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_post_checkin, container, false);
        setHasOptionsMenu(true);

        initViews(view);
        venue = getArguments().getParcelable("selected_checin_place");
        view.setClickable(true);
        tvPrivacy.setText(R.string.public_str);
        checkinPostActivity.setLocation(venue);
        tvTags.setText(getString(R.string.at_with_place, venue.getName()));
        rvImages.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvImages.setAdapter(adptTagPhoto);
        Utility.loadImageGlide(  civProfileImg,  UserPreferences.fetchProfileImageUrl(getActivity()));
//                Picasso.with(getActivity()).load(UserPreferences.fetchProfileImageUrl(getActivity())).into(civProfileImg);

        llPostPrivacy.setOnClickListener(this);
        ivAddPhotos.setOnClickListener(this);
        ivTagFriend.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            ((MainActivity) getActivity()).tracker.setScreenName(AnalyticsTrackers.ANALYTICS_PAGE_LOCATION);
            ((MainActivity) getActivity()).tracker.send(new HitBuilders.EventBuilder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.itm_menu_notification).setVisible(false);
        menu.findItem(R.id.itm_menu_friend).setVisible(false);
        menu.findItem(R.id.itm_menu_search_place).setVisible(false);
        menu.findItem(R.id.itm_menu_post).setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itm_menu_post) {
           Utility.hideSoftKeyboard(etDesc);
            checkinPostActivity.setDescription(etDesc.getText().toString().trim());
            webServiceCall.checkinWsCall(getContext(), checkinPostActivity, 0, true);
        }
        return false;
    }

    private void initViews(View view) {
        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_location));


        etDesc = (EditText) view.findViewById(R.id.et_checkin_msg);
        civProfileImg = (CircleImageView) view.findViewById(R.id.civ_checkin_img);
        tvTags = (TextView) view.findViewById(R.id.tv_checkin_msg);
        tvPrivacy = (TextView) view.findViewById(R.id.tv_checkin_privacy);
        llPostPrivacy = (LinearLayout) view.findViewById(R.id.ll_checkin_post_privacy);
        ivAddPhotos = (ImageView) view.findViewById(R.id.iv_checkin_add_photo);
        ivTagFriend = (ImageView) view.findViewById(R.id.iv_checkin_tag_friend);
        rvImages = (RecyclerView) view.findViewById(R.id.rv_checkin_photos);

        checkinPostActivity = new CheckinActivity();
        adptTagPhoto = new TagPhotoAdapter(getActivity(), checkinPostActivity.getAlTagPhoto());
        webServiceCall = new WebServiceCall(this);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.ll_checkin_post_privacy:
                    showPostPrivacyDialog();
                    break;
                case R.id.iv_checkin_add_photo:
                    PermissionEverywhere.getPermission(getContext(), new String[]{Manifest.permission.CAMERA}, 101)
                            .enqueue(new PermissionResultCallback() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onComplete(PermissionResponse permissionResponse) {
                                    if (permissionResponse.isGranted()) {
                                        PermissionEverywhere.getPermission(getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101)
                                                .enqueue(new PermissionResultCallback() {
                                                    @Override
                                                    public void onComplete(PermissionResponse permissionResponse) {
                                                        if (permissionResponse.isGranted()) {
                                                            showCameraDialog();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });

                    break;
                case R.id.iv_checkin_tag_friend:
                    showTagDialog();
                    break;
            }
        }
    }

    private void showCameraDialog() {
        GalleryActivity.MAX_SELECTION = 10 - adptTagPhoto.getItemCount();
        ImageChooserDialog imageDialog = new ImageChooserDialog(getActivity(), this, true);
        imageDialog.show();
    }

    private void showTagDialog() {
        if (tagFriendDialog == null) {
            if (!Utility.checkInternetConnectivity(getActivity())) {
                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getActivity().getString(R.string.no_internet_connect));
                return;
            }
            tagFriendDialog = new TagFriendDialog((MainActivity) getActivity(), this);
        }
        tagFriendDialog.show();
    }

    @Override
    public void onResult(Uri uriImagePath) {
        Photos tagPhoto = new Photos();
        tagPhoto.setImageUrl(uriImagePath.getPath());
        checkinPostActivity.getAlTagPhoto().add(tagPhoto);
        adptTagPhoto.notifyDataSetChanged();
    }

    private void showPostPrivacyDialog() {
        final String[] postPrivacy = getContext().getResources().getStringArray(R.array.post_privacy);

        new PrivacyDialog(getContext(), Integer.parseInt(checkinPostActivity.getPrivacy()) - 1, new PrivacyDialog.PrivarySelection() {
            @Override
            public void onPrivacySelection(int privacySelected) {
                checkinPostActivity.setPrivacy(String.valueOf(privacySelected + 1));
                tvPrivacy.setText(postPrivacy[privacySelected]);
            }
        }).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SelectImage.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFriendTagged(ArrayList<Friend> taggedFriends) {
        if (taggedFriends != null && taggedFriends.size() != 0) {
            String location = venue.getName();
            checkinPostActivity.setTagFriendIds(taggedFriends);
            String tagFriend;
            if (taggedFriends.size() == 1) {
                tagFriend = "with " + taggedFriends.get(0).getFullName();
            } else {
                tagFriend = "with ";
                for (int counter = 0; counter < taggedFriends.size() - 1; counter++) {
                    tagFriend += taggedFriends.get(counter).getFullName() + ", ";
                }
                tagFriend += taggedFriends.get(taggedFriends.size() - 1).getFullName() + " ";
            }
            tvTags.setText(tagFriend + " " + location);
        }
    }

    @Override
    public void onResult(ArrayList<Uri> allUriImagePath) {

        for (int counter = 0; counter < allUriImagePath.size(); counter++) {
            Photos tagPhoto = new Photos();
            tagPhoto.setImageUrl(allUriImagePath.get(counter).getPath());
            checkinPostActivity.getAlTagPhoto().add(tagPhoto);
        }
        adptTagPhoto.notifyDataSetChanged();
    }

//    private void lastLocationConfirm(String placeId, long time) {
//        try {
//            UserPreferences.saveLastCheckinTime(getContext(), time);
//            Intent iLastLocationConfirm = new Intent(getContext(), CheckinConfirmReceiver.class);
//            iLastLocationConfirm.putExtra("last_checkin_place_id", placeId);
//
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, iLastLocationConfirm, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//
//            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    public void onResponseReceive(int requestCode) {
//        if (requestCode == WebServiceCall.REQUEST_CODE_LAST_CHECKIN_USER) {
//            LastCheckinModel mLastCheckin = (LastCheckinModel) webServiceCall.volleyRequestInstatnce().getModelObject(LastCheckinModel.class, LastCheckinModel.class.getSimpleName());
//            if (mLastCheckin != null) {
//                long time = System.currentTimeMillis() + (CheckinConfirmActivity.CHECKIN_CONFIRM_POPUP_INTERVAL * 3600 * 1000);
//                lastLocationConfirm(mLastCheckin.getLastCheckin().getPlaceId(), time);
//            }
//        } else


        if (requestCode == WebServiceCall.REQUEST_CODE_POST_ACTIVITY) {
            PostModel mCheckinActivity = (PostModel) webServiceCall.volleyRequestInstatnce().getModelObject(PostModel.class, "Checkin Activity");
            if (mCheckinActivity != null) {
                if (mCheckinActivity.getStatus() == BaseModel.STATUS_SUCCESS) {
                    if (checkinPostActivity.getAlTagPhoto().size() > 1) {
                        checkinPostActivity.getAlTagPhoto().remove(0);
                        checkinPostActivity.setId(mCheckinActivity.getData().getiPostID());
                        UserPreferences.savePostActivity(getActivity(), checkinPostActivity);
                        Intent uploadIntent = new Intent(getActivity(), ImageUploadService.class);
                        getActivity().startService(uploadIntent);
                    }
//                    try {
//                        webServiceCall.lastCheckinUserWsCall(getContext());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    ((MainActivity)getActivity()).openHome();
                }
                Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), mCheckinActivity.getMessage());
            } else {
                Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
            }
        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
    }
}
