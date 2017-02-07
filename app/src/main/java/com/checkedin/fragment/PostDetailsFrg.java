package com.checkedin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.databinding.DialogPostDetailsBinding;
import com.checkedin.dialog.BottomActionDialog;
import com.checkedin.dialog.ImageListDialog;
import com.checkedin.dialog.ReportContentDialog;
import com.checkedin.dialog.ViewPhotoPagerDialog;
import com.checkedin.model.CheckinActivity;
import com.checkedin.model.Friend;
import com.checkedin.model.FriendsActivity;
import com.checkedin.model.FutureActivity;
import com.checkedin.model.MyActivity;
import com.checkedin.model.Photos;
import com.checkedin.model.Place;
import com.checkedin.model.PostActivity;
import com.checkedin.model.TagUser;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.PostDetailsModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.views.CropSquareTransformation;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;
import java.util.Locale;


public class PostDetailsFrg extends Fragment implements OnClickListener, VolleyStringRequest.AfterResponse {


    private WebServiceCall webServiceCall;
    private FriendsActivity friendsActivity;

    private DialogPostDetailsBinding mBinding;
    private ImageView[] ivPhoto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.dialog_post_details, container, false);
        ivPhoto = new ImageView[5];

        ivPhoto[0] = mBinding.includeMain.ivImg1;
        ivPhoto[1] = mBinding.includeMain.ivImg2;
        ivPhoto[2] = mBinding.includeMain.ivImg3;
        ivPhoto[3] = mBinding.includeMain.ivImg4;
        ivPhoto[4] = mBinding.includeMain.ivImg5;

        webServiceCall = new WebServiceCall(this);

        mBinding.includeMain.setItems(friendsActivity);


        mBinding.includeMain.getRoot().setVisibility(View.GONE);

        mBinding.ivBack.setOnClickListener(this);
        mBinding.includeMain.ivMore.setOnClickListener(this);
        mBinding.includeMain.llPhotos.setOnClickListener(this);
        mBinding.includeMain.tvComment.setOnClickListener(this);
        mBinding.includeMain.tvStarOMeter.setOnClickListener(this);
        mBinding.includeMain.tvStarOMeterCount.setOnClickListener(this);
        mBinding.includeMain.tvCommentCount.setOnClickListener(this);
        return mBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            String postId = getArguments().getString("post_id");
            webServiceCall.postDetailsWsCall(getContext(), postId);
        }
    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.iv_back:
                    ((DialogFragmentContainer) getParentFragment()).popFragment();
                    break;
                case R.id.tv_starOMeter:
                    if (friendsActivity.getStarByme()) {
                        friendsActivity.setStarByme(0);
                        friendsActivity.setStarOMeterCount(friendsActivity.getStarOMeterCount() - 1);
                    } else {
                        friendsActivity.setStarByme(1);
                        friendsActivity.setStarOMeterCount(friendsActivity.getStarOMeterCount() + 1);
                    }
                    setValues();
                    webServiceCall.addStarViewOMeterWsCall(getContext(), friendsActivity.getPostId(), true);

                    break;
                case R.id.tv_starOMeterCount:
                    StarViewUserFrg starViewUserFrg = new StarViewUserFrg();
                    Bundle argumentS = new Bundle();
                    argumentS.putBoolean("is_star", true);
                    argumentS.putString("post_id", friendsActivity.getPostId());
                    starViewUserFrg.setArguments(argumentS);
                    if (DialogFragmentContainer.isDialogOpen) {
                        DialogFragmentContainer.getInstance().fragmentTransition(starViewUserFrg, true);
                    } else {
                        DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
                        dialogFrgContainer.init(starViewUserFrg);
                        dialogFrgContainer.show(((MainActivity) getContext()).getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                    }
                    break;
                case R.id.iv_more:

                    if (friendsActivity.getUserId().equals(UserPreferences.fetchUserId(getContext()))) {

                        boolean isGrofie=friendsActivity.getCategoryName().equals("Groufie");
                        new BottomActionDialog(getContext(),!isGrofie, new BottomActionDialog.PostAction() {
                            @Override
                            public void onEditSelect() {
                                editPost();
                            }

                            @Override
                            public void onDeleteSelect() {
                                webServiceCall.deletePostWsCall(getContext(), friendsActivity.getPostId(), friendsActivity.getPostType());
                            }
                        }).show();
                    } else {
                        new ReportContentDialog(getContext(), friendsActivity.getPostId()).show();
                    }
                    break;
                case R.id.tv_commentCount:
                case R.id.tv_comment:
                    CommentFrg activityComment = new CommentFrg();
                    Bundle argument = new Bundle();
                    argument.putParcelable("friend_activity", friendsActivity);
                    argument.putParcelable("comment_post_listener", new CommentFrg.CommentPost() {
                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {

                        }

                        @Override
                        public void onCommentPosted(boolean isPosted) {
                            if (isPosted) {
                                setValues();
                            }
                        }
                    });
                    activityComment.setArguments(argument);

                    if (DialogFragmentContainer.isDialogOpen) {
                        DialogFragmentContainer.getInstance().fragmentTransition(activityComment, true);
                    } else {
                        DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
                        dialogFrgContainer.init(activityComment);
                        dialogFrgContainer.show(((MainActivity) getContext()).getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                    }
                    break;
                case R.id.ll_photos:
                    if (friendsActivity.getTagPhotos().size() == 1) {
                        ViewPhotoPagerDialog dialog = new ViewPhotoPagerDialog(getContext(), 0, (ArrayList<Photos>) friendsActivity.getTagPhotos());
                        dialog.show();
                    } else {
                        ImageListDialog activityPhoto = new ImageListDialog(getContext(), (ArrayList<Photos>) friendsActivity.getTagPhotos());
                        activityPhoto.show();
                    }
                    break;

            }
        }
    }

    private void editPost() {
        switch (friendsActivity.getPostType()) {
            case PostActivity.POST_TYPE_CHECKIN:
                CheckinActivity checkinActivity = new CheckinActivity();
                checkinActivity.setId(friendsActivity.getPostId());
                checkinActivity.setType(friendsActivity.getPostType());
                checkinActivity.setPrivacy(friendsActivity.getPostPrivacy());
                checkinActivity.setDescription(friendsActivity.getPostDescription());
                checkinActivity.setAlTagPhoto((ArrayList<Photos>) friendsActivity.getTagPhotos());

                Place place = new Place();
                place.setId(friendsActivity.getLocationId());
                place.setName(friendsActivity.getLocationName());
                place.setLongitude(Double.parseDouble(friendsActivity.getLongitude()));
                place.setLatitude(Double.parseDouble(friendsActivity.getLatitude()));
                place.setCity(friendsActivity.getCity());
                place.setCountry(friendsActivity.getCountry());
                checkinActivity.setPlace(place);

                ArrayList<Friend> alTagFriends = new ArrayList<>();
                for (int counter = 0; counter < friendsActivity.getTagUsers().size(); counter++) {
                    TagUser tagUser = friendsActivity.getTagUsers().get(counter);
                    Friend friend = new Friend();
                    friend.setId(tagUser.getiTaggedUserID());
                    friend.setFirstName(tagUser.getvFirstName());
                    friend.setLastName(tagUser.getvLastName());
                    friend.setImageUrl(tagUser.getvImage());
                    alTagFriends.add(friend);
                }
                checkinActivity.setTagFriends(alTagFriends);

                PostCheckinEditFrg postCheckinEditFrg = new PostCheckinEditFrg();
                Bundle argumentC = new Bundle();
                argumentC.putParcelable("checkin_post", checkinActivity);
                argumentC.putBoolean("title_visible", true);
                postCheckinEditFrg.setArguments(argumentC);
                ((DialogFragmentContainer) getParentFragment()).fragmentTransition(postCheckinEditFrg, true);

                break;
            case PostActivity.POST_TYPE_ACTIVITY:
                MyActivity myActivity = new MyActivity();
                myActivity.setCategoryName(friendsActivity.getCategoryName());
                myActivity.setSubCategoryName(friendsActivity.getSubcategoryName());
                myActivity.setId(friendsActivity.getPostId());
                myActivity.setType(friendsActivity.getPostType());
                myActivity.setPrivacy(friendsActivity.getPostPrivacy());
                myActivity.setDescription(friendsActivity.getPostDescription());
                myActivity.setEdit(true);

                myActivity.setAlTagPhoto((ArrayList<Photos>) friendsActivity.getTagPhotos());

                ArrayList<Friend> alTagFriend = new ArrayList<>();
                for (int counter = 0; counter < friendsActivity.getTagUsers().size(); counter++) {
                    TagUser tagUser = friendsActivity.getTagUsers().get(counter);
                    Friend friend = new Friend();
                    friend.setId(tagUser.getiTaggedUserID());
                    friend.setFirstName(tagUser.getvFirstName());
                    friend.setLastName(tagUser.getvLastName());
                    friend.setImageUrl(tagUser.getvImage());
                    alTagFriend.add(friend);
                }
                myActivity.setTagFriends(alTagFriend);


                PostActivityEditFrg postActivityEditFrg = new PostActivityEditFrg();
                Bundle argument = new Bundle();
                argument.putParcelable("my_activity_post", myActivity);
                argument.putBoolean("title_visible", true);
                postActivityEditFrg.setArguments(argument);
                ((DialogFragmentContainer) getParentFragment()).fragmentTransition(postActivityEditFrg, true);


                break;
            case PostActivity.POST_TYPE_PLANNING:
                FutureActivity futureActivity = new FutureActivity();
                futureActivity.setId(friendsActivity.getPostId());
                futureActivity.setType(friendsActivity.getPostType());
                futureActivity.setPrivacy(friendsActivity.getPostPrivacy());
                futureActivity.setDescription(friendsActivity.getPlanningPostDescription());
                futureActivity.setEventStart(friendsActivity.getPlanningPostDate());
                futureActivity.setCategoryName(friendsActivity.getCategoryName());
                futureActivity.setSubCategoryName(friendsActivity.getSubcategoryName());
                futureActivity.setAlTagPhoto((ArrayList<Photos>) friendsActivity.getTagPhotos());

                PostPlanEditFrg postPlanEditFrg = new PostPlanEditFrg();
                Bundle argumentP = new Bundle();
                argumentP.putParcelable("plan_activity_post", futureActivity);
                argumentP.putBoolean("title_visible", true);
                postPlanEditFrg.setArguments(argumentP);

                ((DialogFragmentContainer) getParentFragment()).fragmentTransition(postPlanEditFrg, true);

                break;
        }
    }




    void setValues() {

        String title = friendsActivity.getTitle();

        mBinding.includeMain.setItems(friendsActivity);
        mBinding.includeMain.tvTitle.setText(title);
        if (friendsActivity.getAlWordAction().size() > 0)
            mBinding.includeMain.tvTitle.addSpanClick(friendsActivity, friendsActivity.getAlWordAction(), title);

        Utility.loadImageGlide(mBinding.includeMain.civProfileImg, friendsActivity.getProfileImg());

        // Tag Activity Photos
        ArrayList<Photos> alPhotos = (ArrayList<Photos>) friendsActivity.getTagPhotos();
        if (alPhotos != null && alPhotos.size() != 0) {
            showPhotos(alPhotos);
        } else {
            mBinding.includeMain.llPhotos.setVisibility(LinearLayout.GONE);
            mBinding.includeMain.llImgtop.setVisibility(LinearLayout.GONE);
        }

        mBinding.includeMain.tvStarOMeter.
                setCompoundDrawablesWithIntrinsicBounds(friendsActivity.getStarByme() ? R.drawable.ic_star_plus_orange_20dp : R.drawable.ic_star_plus_grey_20dp, 0, 0, 0);
    }

    private void showPhotos(ArrayList<Photos> alPhotes) {
        int countPhotos = alPhotes.size();
        mBinding.includeMain.llPhotos.setVisibility(LinearLayout.VISIBLE);
        mBinding.includeMain.llImgtop.setVisibility(LinearLayout.VISIBLE);

        switch (countPhotos) {
            case 1:
                ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[1].setVisibility(LinearLayout.GONE);
                ivPhoto[2].setVisibility(LinearLayout.GONE);
                ivPhoto[3].setVisibility(LinearLayout.GONE);
                ivPhoto[4].setVisibility(LinearLayout.GONE);
                mBinding.includeMain.llImgbottom.setVisibility(LinearLayout.GONE);
                mBinding.includeMain.flImg5.setVisibility(LinearLayout.GONE);
                mBinding.includeMain.tvImgmore.setVisibility(LinearLayout.GONE);
                imageLoader(1, alPhotes);
                break;
            case 2:
                ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                mBinding.includeMain.llImgbottom.setVisibility(LinearLayout.GONE);
                ivPhoto[2].setVisibility(LinearLayout.GONE);
                ivPhoto[3].setVisibility(LinearLayout.GONE);
                mBinding.includeMain.flImg5.setVisibility(LinearLayout.GONE);
                ivPhoto[4].setVisibility(LinearLayout.GONE);
                mBinding.includeMain.tvImgmore.setVisibility(LinearLayout.GONE);
                imageLoader(2, alPhotes);
                break;
            case 3:
                ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[2].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[3].setVisibility(LinearLayout.GONE);
                mBinding.includeMain.flImg5.setVisibility(LinearLayout.GONE);
                ivPhoto[4].setVisibility(LinearLayout.GONE);
                mBinding.includeMain.llImgbottom.setVisibility(LinearLayout.VISIBLE);
                mBinding.includeMain.tvImgmore.setVisibility(LinearLayout.GONE);
                imageLoader(3, alPhotes);
                break;
            case 4:
                ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[2].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[3].setVisibility(LinearLayout.VISIBLE);
                mBinding.includeMain.flImg5.setVisibility(LinearLayout.GONE);
                ivPhoto[4].setVisibility(LinearLayout.GONE);
                mBinding.includeMain.llImgbottom.setVisibility(LinearLayout.VISIBLE);
                mBinding.includeMain.tvImgmore.setVisibility(LinearLayout.GONE);
                imageLoader(4, alPhotes);
                break;
            case 5:
                ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[2].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[3].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[4].setVisibility(LinearLayout.VISIBLE);
                mBinding.includeMain.llImgbottom.setVisibility(LinearLayout.VISIBLE);
                mBinding.includeMain.tvImgmore.setVisibility(LinearLayout.GONE);
                mBinding.includeMain.flImg5.setVisibility(LinearLayout.VISIBLE);
                imageLoader(5, alPhotes);
                break;

            default:
                ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[2].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[3].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[4].setVisibility(LinearLayout.VISIBLE);
                mBinding.includeMain.llImgbottom.setVisibility(LinearLayout.VISIBLE);
                mBinding.includeMain.tvImgmore.setVisibility(LinearLayout.VISIBLE);
                mBinding.includeMain.flImg5.setVisibility(LinearLayout.VISIBLE);
                mBinding.includeMain.tvImgmore.setText(String.format(Locale.getDefault(), "+%d", countPhotos - 4));
                imageLoader(5, alPhotes);
                break;
        }

    }

    @SuppressWarnings("unchecked")
    private void imageLoader(int size, ArrayList<Photos> alPhotes) {
        for (int i = 0; i < size; i++) {
            Utility.loadImageGlide(ivPhoto[i], alPhotes.get(i).getOriginalSizePath(), new CropSquareTransformation(getActivity()));
        }

    }


    @Override
    public void onResponseReceive(int requestCode) {
        switch (requestCode) {
            case WebServiceCall.REQUEST_CODE_PARTICULAR_POST_DETAILS:
                PostDetailsModel mPostDetails = (PostDetailsModel) webServiceCall.volleyRequestInstatnce().getModelObject(PostDetailsModel.class, PostDetailsModel.class.getSimpleName());
                if (mPostDetails != null) {
                    if (mPostDetails.getStatus() == BaseModel.STATUS_SUCCESS) {
                        friendsActivity = mPostDetails.getData();
                        mBinding.includeMain.getRoot().setVisibility(View.VISIBLE);
                        setValues();
                    } else {
                        Toast.makeText(getActivity(), mPostDetails.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case WebServiceCall.REQUEST_CODE_ADD_STAR_VIEW_METER:
                BaseModel baseModel = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
                if (baseModel != null && baseModel.getStatus() == BaseModel.STATUS_SUCCESS) {
                    setValues();
                }
                break;

            case WebServiceCall.REQUEST_CODE_DELETE_POST:
                ((DialogFragmentContainer) getParentFragment()).popFragment();
                break;
        }
    }

    @Override
    public void onErrorReceive() {
        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).toggleActionBarIcon(1, true);
    }
}
