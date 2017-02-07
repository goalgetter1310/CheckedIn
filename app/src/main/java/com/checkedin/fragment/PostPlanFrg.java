package com.checkedin.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.SelectImage;
import com.checkedin.activity.GalleryActivity;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.TagPhotoAdapter;
import com.checkedin.databinding.FrgPostPlanningBinding;
import com.checkedin.dialog.ImageChooserDialog;
import com.checkedin.dialog.PrivacyDialog;
import com.checkedin.dialog.TagFriendDialog;
import com.checkedin.dialog.TagFriendDialog.TagFriendListener;
import com.checkedin.model.Friend;
import com.checkedin.model.FutureActivity;
import com.checkedin.model.Photos;
import com.checkedin.model.PlanCategory;
import com.checkedin.model.PlanSubCategory;
import com.checkedin.model.PostActivity;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.PostModel;
import com.checkedin.services.ImageUploadService;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.material.app.DatePickerDialog;
import com.material.app.DialogFragment;
import com.material.app.TimePickerDialog;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
public class PostPlanFrg extends Fragment implements OnClickListener, SelectImage.ActivityResult, TagFriendListener, VolleyStringRequest.AfterResponse {


    private TagPhotoAdapter adptTagPhoto;
    private FutureActivity futurePostActivity;
    private WebServiceCall webServiceCall;
    private Calendar dateTime;
    private DialogFragment timeFragment;
    private String tagFriend;

    private FrgPostPlanningBinding mBinding;
    private PlanCategory planCategory;
    private PlanSubCategory planSubCategory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_post_planning, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_future_planning));
        mBinding.getRoot().setClickable(true);

        webServiceCall = new WebServiceCall(this);
        futurePostActivity = new FutureActivity();
        planCategory = getArguments().getParcelable("planning_category");
        planSubCategory = getArguments().getParcelable("planning_sub_category");
        adptTagPhoto = new TagPhotoAdapter(getActivity(), futurePostActivity.getAlTagPhoto());

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tagFriend = "";

        mBinding.tvPrivacy.setText(R.string.public_str);
        futurePostActivity.setType(PostActivity.POST_TYPE_PLANNING);
        mBinding.rvTagPhotos.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.rvTagPhotos.setAdapter(adptTagPhoto);

        setDescription();
        Utility.loadImageGlide(         mBinding.civProfileImg,                 UserPreferences.fetchProfileImageUrl(getActivity()));
//        Picasso.with(getActivity()).load(UserPreferences.fetchProfileImageUrl(getActivity())).into(mBinding.civProfileImg);
        futurePostActivity.setiFpCategoryID(planCategory.getId());
        if (planCategory.isContainSubCategory())
            futurePostActivity.setiFpSubcategortID(String.valueOf(planSubCategory.getId()));

        mBinding.tvPrivacy.setOnClickListener(this);
        mBinding.ivAddPhoto.setOnClickListener(this);
        mBinding.ivTagFriend.setOnClickListener(this);
        mBinding.ivPlanDateTime.setOnClickListener(this);
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
            String desc = mBinding.etDesc.getText().toString().trim();
            futurePostActivity.setDescription(desc);

            webServiceCall.planingWsCall(getContext(), futurePostActivity, 0, true);
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.iv_plan_dateTime:
                    TimePicker timePicker = new TimePicker();
                    timePicker.positiveAction("OK").negativeAction("CANCEL");
                    timeFragment = DialogFragment.newInstance(timePicker);

                    Calendar mCalendar = Calendar.getInstance(Locale.getDefault());
                    mCalendar.set(Calendar.YEAR, 2050);

                    DatePicker datePicker = new DatePicker();
                    datePicker.dateRange(new Date().getTime(), mCalendar.getTime().getTime());
                    datePicker.positiveAction("OK").negativeAction("CANCEL");

                    DialogFragment dateFragment = DialogFragment.newInstance(datePicker);
                    dateFragment.show(getChildFragmentManager(), null);

                    break;
                case R.id.tv_privacy:
                    showPostPrivacyDialog();
                    break;
                case R.id.iv_add_photo:
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
                case R.id.iv_tag_friend:
                    new TagFriendDialog((MainActivity) getActivity(), this).show();
                    break;
            }
        }
    }

    private void showCameraDialog() {
        GalleryActivity.MAX_SELECTION = 10 - adptTagPhoto.getItemCount();
        ImageChooserDialog imageDialog = new ImageChooserDialog(getActivity(), this, true);
        imageDialog.show();
    }


    @Override
    public void onResult(Uri uriImagePath) {
        Photos tagPhoto = new Photos();
        tagPhoto.setImageUrl(uriImagePath.getPath());
        futurePostActivity.getAlTagPhoto().add(tagPhoto);
        adptTagPhoto.notifyDataSetChanged();
    }

    private void showPostPrivacyDialog() {
        final String[] postPrivacy = getResources().getStringArray(R.array.post_privacy);

        new PrivacyDialog(getContext(), Integer.parseInt(futurePostActivity.getPrivacy()) - 1, new PrivacyDialog.PrivarySelection() {
            @Override
            public void onPrivacySelection(int privacySelected) {
                futurePostActivity.setPrivacy(String.valueOf(privacySelected + 1));
                mBinding.tvPrivacy.setText(postPrivacy[privacySelected]);
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
            if (taggedFriends.size() == 1) {
                tagFriend = " - with " + taggedFriends.get(0).getFullName();
            } else {
                tagFriend = " - with ";
                for (int counter = 0; counter < taggedFriends.size() - 1; counter++) {
                    tagFriend += taggedFriends.get(counter).getFullName() + ", ";
                }
                tagFriend += taggedFriends.get(taggedFriends.size() - 1).getFullName() + " ";
            }
            futurePostActivity.setTagFriendIds(taggedFriends);
            setDescription();
        }
    }

    private void setDescription() {

        String categoryName = " - " + (planCategory.isContainSubCategory() ? planSubCategory.getName() : planCategory.getName());

        if (!TextUtils.isEmpty(futurePostActivity.getEventStart())) {
            mBinding.tvDesc.setText(categoryName + " - " + new SimpleDateFormat("MMM dd, yyyy hh:mm aa", Locale.getDefault()).format(dateTime.getTime()) + tagFriend);
        } else {
            mBinding.tvDesc.setText(String.format(Locale.getDefault(), "%s", categoryName + tagFriend));
        }
    }

    @Override
    public void onResult(ArrayList<Uri> allUriImagePath) {

        for (int counter = 0; counter < allUriImagePath.size(); counter++) {
            Photos tagPhoto = new Photos();
            tagPhoto.setImageUrl(allUriImagePath.get(counter).getPath());
            futurePostActivity.getAlTagPhoto().add(tagPhoto);
        }
        adptTagPhoto.notifyDataSetChanged();
    }


    @Override
    public void onResponseReceive(int requestCode) {
        PostModel postModel = (PostModel) webServiceCall.volleyRequestInstatnce().getModelObject(PostModel.class, PostModel.class.getSimpleName());
        if (postModel != null) {
            if (postModel.getStatus() == BaseModel.STATUS_SUCCESS) {
                if (futurePostActivity.getAlTagPhoto().size() > 1) {
                    futurePostActivity.getAlTagPhoto().remove(0);
                    futurePostActivity.setId(postModel.getData().getiPostID());
                    UserPreferences.savePostActivity(getActivity(), futurePostActivity);
                    Intent uploadIntent = new Intent(getActivity(), ImageUploadService.class);
                    getActivity().startService(uploadIntent);
                }
                ((MainActivity)getActivity()).openHome();
            }
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), postModel.getMessage());
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }

    @SuppressLint("ParcelCreator")
    private class DatePicker extends DatePickerDialog.Builder {

        private DatePicker() {
            super(R.style.Material_App_Dialog_DatePicker_Light);
        }

        @Override
        public void onPositiveActionClicked(DialogFragment fragment) {
            try {
                DatePickerDialog datePickerDialog = ((DatePickerDialog) fragment.getDialog());
                dateTime = Calendar.getInstance();
                dateTime.set(Calendar.DAY_OF_MONTH, datePickerDialog.getDay());
                dateTime.set(Calendar.MONTH, datePickerDialog.getMonth());
                dateTime.set(Calendar.YEAR, datePickerDialog.getYear());
                timeFragment.show(getChildFragmentManager(), null);

                super.onPositiveActionClicked(fragment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNegativeActionClicked(DialogFragment fragment) {
            super.onNegativeActionClicked(fragment);
        }
    }

    @SuppressLint("ParcelCreator")
    private class TimePicker extends TimePickerDialog.Builder {

        private TimePicker() {
            super(R.style.Material_App_Dialog_DatePicker_Light, 0, 0);
        }

        @Override
        public void onPositiveActionClicked(DialogFragment fragment) {
            TimePickerDialog dialog = (TimePickerDialog) fragment.getDialog();
            dateTime.set(Calendar.HOUR_OF_DAY, dialog.getHour());
            dateTime.set(Calendar.MINUTE, dialog.getMinute());
            dateTime.set(Calendar.SECOND, 0);
            dateTime.set(Calendar.MILLISECOND, 0);
            if (Utility.isPastDate(dateTime)) {
                Calendar currentDate = Calendar.getInstance(Locale.getDefault());
                currentDate.setTime(new Date());
                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), "Invalid Date-Time");
                dateTime = currentDate;
            }

            SimpleDateFormat outputFormt = new SimpleDateFormat(Utility.SERVER_DATE_FORMAT, Locale.getDefault());
            outputFormt.setTimeZone(TimeZone.getTimeZone(Utility.SERVER_TIMEZONE));
            futurePostActivity.setEventStart(outputFormt.format(dateTime.getTime()));


            setDescription();

            super.onPositiveActionClicked(fragment);
        }

        @Override
        public void onNegativeActionClicked(DialogFragment fragment) {
            super.onNegativeActionClicked(fragment);
        }

    }


}


//4N294WDN
