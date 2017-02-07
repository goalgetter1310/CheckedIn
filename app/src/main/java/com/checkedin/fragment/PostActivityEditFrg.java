package com.checkedin.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.SelectImage;
import com.checkedin.activity.GalleryActivity;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.TagPhotoAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.dialog.ImageChooserDialog;
import com.checkedin.dialog.PrivacyDialog;
import com.checkedin.dialog.TagFriendDialog;
import com.checkedin.dialog.TagFriendDialog.TagFriendListener;
import com.checkedin.model.Friend;
import com.checkedin.model.MyActivity;
import com.checkedin.model.Photos;
import com.checkedin.model.PostActivity;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.PostModel;
import com.checkedin.services.ImageUploadService;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import java.util.ArrayList;
import java.util.Locale;

public class PostActivityEditFrg extends Fragment implements OnClickListener, SelectImage.ActivityResult, TagFriendListener, VolleyStringRequest.AfterResponse {

    private TextView tvTags, tvPrivacy;
    private TagFriendDialog tagFriendDialog;
    private EditText etDescription;

    private TagPhotoAdapter adptTagPhoto;
    private MyActivity myActivityPost;
    private String tagFriend;

    private WebServiceCall webServiceCall;
    private ArrayList<Photos> alPhotos;
    private TextView tvPost;
    private boolean isTitleVisible;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_activity));

        webServiceCall = new WebServiceCall(this);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.frg_post_activity_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CircleImageView civProfileImg = (CircleImageView) view.findViewById(R.id.civ_myactivity_img);
        LinearLayout llPostPrivacy = (LinearLayout) view.findViewById(R.id.ll_myactivity_post_privacy);
        ImageView ivAddPhotos = (ImageView) view.findViewById(R.id.iv_myactivity_add_photo);
        ImageView ivTagFriend = (ImageView) view.findViewById(R.id.iv_myactivity_tag_friend);
        RecyclerView rvImages = (RecyclerView) view.findViewById(R.id.rv_myactivity_photos);
        RelativeLayout rlTitle = (RelativeLayout) view.findViewById(R.id.rl_title);
        etDescription = (EditText) view.findViewById(R.id.et_myactivity_msg);
        tvTags = (TextView) view.findViewById(R.id.tv_myactivity_msg);
        tvPrivacy = (TextView) view.findViewById(R.id.tv_myactivity_privacy);
        tvPost = (TextView) view.findViewById(R.id.tv_post);

        tagFriend = "";
        Bundle arguments = getArguments();
        myActivityPost = arguments.getParcelable("my_activity_post");
         isTitleVisible = arguments.getBoolean("title_visible");

        if (isTitleVisible) {
            rlTitle.setVisibility(View.VISIBLE);
        } else {
            rlTitle.setVisibility(View.GONE);
        }

        alPhotos = new ArrayList<>();
        alPhotos.addAll(myActivityPost.getAlTagPhoto());
        etDescription.setText(myActivityPost.getDescription());
        adptTagPhoto = new TagPhotoAdapter(getActivity(), myActivityPost.getAlTagPhoto());

        view.setClickable(true);

        @StringRes
        int privacy;
        switch (myActivityPost.getPrivacy()) {
            default:
                privacy = R.string.public_str;
                break;
            case PostActivity.POST_PRIVACY_FRIEND:
                privacy = R.string.friend;
                break;
            case PostActivity.POST_PRIVACY_ONLY_ME:
                privacy = R.string.only_me;
                break;

        }
        tvPrivacy.setText(privacy);
        myActivityPost.setType(PostActivity.POST_TYPE_ACTIVITY);
        rvImages.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvImages.setAdapter(adptTagPhoto);
        Utility.loadImageGlide(civProfileImg, UserPreferences.fetchProfileImageUrl(getActivity()));
        setDescription();

        tagFriends((ArrayList<Friend>) myActivityPost.getTagFriends());

        llPostPrivacy.setOnClickListener(this);
        ivAddPhotos.setOnClickListener(this);
        ivTagFriend.setOnClickListener(this);
        tvPost.setOnClickListener(this);


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
            String desc = etDescription.getText().toString().trim();
            myActivityPost.setDescription(desc);

            for (int count = 0; count < myActivityPost.getAlTagPhoto().size(); count++) {
                for (int counter = 0; counter < alPhotos.size(); counter++) {
                    String id = myActivityPost.getAlTagPhoto().get(count).getId();
                    if (!TextUtils.isEmpty(id) && id.equals(alPhotos.get(counter).getId())) {
                        alPhotos.remove(counter);
                    }
                }
            }

            ArrayList<Photos> alPhotes = new ArrayList<>();
            for (int counter = 0; counter < myActivityPost.getAlTagPhoto().size(); counter++) {
                if (TextUtils.isEmpty(myActivityPost.getAlTagPhoto().get(counter).getId())) {
                    alPhotes.add(myActivityPost.getAlTagPhoto().get(counter));
                }
            }
            myActivityPost.setAlTagPhoto(alPhotes);

            ArrayList<String> alRemovePhotoId = new ArrayList<>();
            for (int counter = 0; counter < alPhotos.size(); counter++) {
                alRemovePhotoId.add(alPhotos.get(counter).getId());
            }

            myActivityPost.setRemovePhotoId(alRemovePhotoId);
            webServiceCall.editPostWsCall(getContext(), myActivityPost, true);
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_post:
                String desc = etDescription.getText().toString().trim();
                myActivityPost.setDescription(desc);

                for (int count = 0; count < myActivityPost.getAlTagPhoto().size(); count++) {
                    for (int counter = 0; counter < alPhotos.size(); counter++) {
                        String id = myActivityPost.getAlTagPhoto().get(count).getId();
                        if (!TextUtils.isEmpty(id) && id.equals(alPhotos.get(counter).getId())) {
                            alPhotos.remove(counter);
                        }
                    }
                }

                ArrayList<Photos> alPhotes = new ArrayList<>();
                for (int counter = 0; counter < myActivityPost.getAlTagPhoto().size(); counter++) {
                    if (TextUtils.isEmpty(myActivityPost.getAlTagPhoto().get(counter).getId())) {
                        alPhotes.add(myActivityPost.getAlTagPhoto().get(counter));
                    }
                }
                myActivityPost.setAlTagPhoto(alPhotes);

                ArrayList<String> alRemovePhotoId = new ArrayList<>();
                for (int counter = 0; counter < alPhotos.size(); counter++) {
                    alRemovePhotoId.add(alPhotos.get(counter).getId());
                }

                myActivityPost.setRemovePhotoId(alRemovePhotoId);
                webServiceCall.editPostWsCall(getContext(), myActivityPost, true);


                break;
            case R.id.ll_myactivity_post_privacy:
                showPostPrivacyDialog();
                break;
            case R.id.iv_myactivity_add_photo:
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
            case R.id.iv_myactivity_tag_friend:
                showTagDialog();
                break;
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
        myActivityPost.getAlTagPhoto().add(tagPhoto);
        adptTagPhoto.notifyDataSetChanged();
    }

    private void showPostPrivacyDialog() {

        final String[] postPrivacy = getContext().getResources().getStringArray(R.array.post_privacy);

        new PrivacyDialog(getContext(), Integer.parseInt(myActivityPost.getPrivacy()) - 1, new PrivacyDialog.PrivarySelection() {
            @Override
            public void onPrivacySelection(int privacySelected) {
                myActivityPost.setPrivacy(String.valueOf(privacySelected + 1));
                tvPrivacy.setText(postPrivacy[privacySelected]);
            }
        }).show();

    }


    private void tagFriends(ArrayList<Friend> taggedFriends) {
        if (taggedFriends != null && taggedFriends.size() != 0) {
            if (taggedFriends.size() == 1) {
                tagFriend = " with " + taggedFriends.get(0).getFullName();
            } else {
                tagFriend = " with ";
                for (int counter = 0; counter < taggedFriends.size() - 1; counter++) {
                    tagFriend += taggedFriends.get(counter).getFullName() + ", ";
                }
                tagFriend += taggedFriends.get(taggedFriends.size() - 1).getFullName() + " ";
            }

            myActivityPost.setTagFriendIds(taggedFriends);
            setDescription();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SelectImage.getInstance().onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onFriendTagged(ArrayList<Friend> taggedFriends) {
        tagFriends(taggedFriends);
    }

    private void setDescription() {
        String text;
        String categoryName = myActivityPost.getCategoryDisplayName();

        if (TextUtils.isEmpty(categoryName)) {
            text = " - " + myActivityPost.getSubCategoryName();
        } else if (TextUtils.isEmpty(myActivityPost.getSubCategoryName())) {
            text = " - " + categoryName;
        } else {
            text = " - " + categoryName + " - " + myActivityPost.getSubCategoryName();
        }

        tvTags.setText(String.format(Locale.getDefault(), "%s", text + tagFriend));
    }

    @Override
    public void onResult(ArrayList<Uri> allUriImagePath) {
        if ((allUriImagePath.size() + myActivityPost.getAlTagPhoto().size()) <= PostActivity.MAX_IMAGES_SELECT) {
            for (int counter = 0; counter < allUriImagePath.size(); counter++) {
                Photos tagPhoto = new Photos();
                tagPhoto.setImageUrl(allUriImagePath.get(counter).getPath());
                myActivityPost.getAlTagPhoto().add(tagPhoto);
            }
            adptTagPhoto.notifyDataSetChanged();
        } else {
            Utility.showSnackBar(getView(), "You can post maximum 10 images only");
        }
    }


    @Override
    public void onResponseReceive(int requestCode) {
        PostModel mCheckinActivity = (PostModel) webServiceCall.volleyRequestInstatnce().getModelObject(PostModel.class, PostModel.class.getSimpleName());
        if (mCheckinActivity != null) {
            if (mCheckinActivity.getStatus() == BaseModel.STATUS_SUCCESS) {
                if (myActivityPost.getAlTagPhoto().size() > 1) {
                    myActivityPost.getAlTagPhoto().remove(0);
                    myActivityPost.setId(mCheckinActivity.getData().getiPostID());
                    UserPreferences.savePostActivity(getActivity(), myActivityPost);
                    Intent uploadIntent = new Intent(getActivity(), ImageUploadService.class);
                    getActivity().startService(uploadIntent);
                }
                if (!isTitleVisible) {
                    getActivity().onBackPressed();
                    getActivity().onBackPressed();
                    getActivity().onBackPressed();
                } else {
                    ((DialogFragmentContainer) getParentFragment()).popFragment();
                }
            }
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), mCheckinActivity.getMessage());
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
    }

}
