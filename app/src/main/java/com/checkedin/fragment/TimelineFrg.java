package com.checkedin.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.activity.ProfileActivity;
import com.checkedin.adapter.TimelineFriendAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.databinding.FrgTimelineBinding;
import com.checkedin.dialog.ConfirmDialog;
import com.checkedin.dialog.ImageListDialog;
import com.checkedin.dialog.ViewPhotoPagerDialog;
import com.checkedin.model.Friend;
import com.checkedin.model.FriendProfile;
import com.checkedin.model.Photos;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.FriendListModel;
import com.checkedin.model.response.FriendProfileModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.material.widget.LinearLayout;

import java.util.ArrayList;

public class TimelineFrg extends Fragment implements View.OnClickListener, VolleyStringRequest.AfterResponse {

    private WebServiceCall webServiceCall;

    private String friendId;
    private ImageView[] ivPhoto;
    private FriendProfile friendProfile;
    private ArrayList<Friend> alFriends;
    private TimelineFriendAdapter adptTimelineFriend;

    private FrgTimelineBinding mBinding;
    private int totalFriend;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(savedInstanceState), R.layout.frg_timeline, container, false);

        ivPhoto = new ImageView[5];
        ivPhoto[0] = mBinding.ivPhoto1;
        ivPhoto[1] = mBinding.ivPhoto2;
        ivPhoto[2] = mBinding.ivPhoto3;
        ivPhoto[3] = mBinding.ivPhoto4;
        ivPhoto[4] = mBinding.ivPhoto5;

        if (getArguments() != null) {
            friendId = getArguments().getString("friend_id");

            webServiceCall = new WebServiceCall(this);
            alFriends = new ArrayList<>();
            adptTimelineFriend = new TimelineFriendAdapter(getContext(), alFriends);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mBinding.rvFrineds.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mBinding.rvFrineds.setAdapter(adptTimelineFriend);
        mBinding.rvFrineds.setNestedScrollingEnabled(false);

        if (webServiceCall.friendListWsCall(getContext(), friendId, 0, false, false)) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
        }

        mBinding.tvMyActivity.setOnClickListener(this);
        mBinding.tvCheckin.setOnClickListener(this);
        mBinding.tvPlanning.setOnClickListener(this);
        mBinding.tvAddfriend.setOnClickListener(this);
        mBinding.tvChatbox.setOnClickListener(this);
        mBinding.tvBlock.setOnClickListener(this);
        mBinding.llAllPhoto.setOnClickListener(this);
        mBinding.tvProfile.setOnClickListener(this);
        mBinding.tvFavourite.setOnClickListener(this);
        mBinding.llPhotos.setOnClickListener(this);
        mBinding.ivBack.setOnClickListener(this);
        mBinding.llAllFriend.setOnClickListener(this);
        mBinding.tvPoints.setOnClickListener(this);
        mBinding.ivProfileImg.setOnClickListener(this);
        removeViewClick();
    }

    private void removeViewClick() {
        mBinding.tvMyActivity.setClickable(false);
        mBinding.tvCheckin.setClickable(false);
        mBinding.tvPlanning.setClickable(false);
        mBinding.tvAddfriend.setClickable(false);
        mBinding.tvChatbox.setClickable(false);
        mBinding.tvBlock.setClickable(false);
        mBinding.llAllPhoto.setClickable(false);
        mBinding.tvProfile.setClickable(false);
        mBinding.tvFavourite.setClickable(false);
        mBinding.tvPoints.setClickable(false);
        mBinding.ivProfileImg.setClickable(false);
    }

    private void setValueToUI() {
        try {
            mBinding.llFriendTab.setVisibility(friendId.equals(UserPreferences.fetchUserId(getActivity())) ? View.GONE : View.VISIBLE);
            mBinding.tvFavourite.setCompoundDrawablesWithIntrinsicBounds(0, friendProfile.isFavourite() ? R.drawable.ic_fav_fill_orange_28dp : R.drawable.ic_fav_outline_orange_28dp, 0, 0);
            mBinding.tvName.setText(friendProfile.getFullName());
            int checkin = friendProfile.getCheckinCount();
            int activity = friendProfile.getActivityCount();
            int planningCount = friendProfile.getFuturePlanningCount();
            int photoCount = friendProfile.getPhotosList().size();

            if (TextUtils.isEmpty(friendProfile.getvImage())) {
                mBinding.ivProfileImg.setClickable(false);
            } else {
                mBinding.ivProfileImg.setClickable(true);
                Utility.loadImageGlide(mBinding.ivProfileImg, friendProfile.getvImage());
            }


            mBinding.tvPoints.setText(friendProfile.getCheckedinPoint() == 0 ? "No CheckedIn Points" : friendProfile.getCheckedinPoint() + " CheckedIn Points");
            if (photoCount > 0) {
                mBinding.tvPhotosCount.setText(getActivity().getString(R.string.photos_count, photoCount));
                mBinding.llAllPhoto.setVisibility(View.VISIBLE);
                showPhotos();
            } else {
                mBinding.tvPhotosCount.setText(getActivity().getString(R.string.no_photos));
                mBinding.llAllPhoto.setVisibility(View.GONE);
            }


            switch (friendProfile.getFriendRelation()) {
                case Friend.RELATION_STATUS_FRIEND:
                    mBinding.tvAddfriend.setText(friendProfile.getFriendRelation());
                    mBinding.tvAddfriend.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_already_friend_orange_28dp, 0, 0);
                    break;
                case Friend.RELATION_STATUS_REUEST_SENT:
                    mBinding.tvAddfriend.setText(friendProfile.getFriendRelation());
                    mBinding.tvAddfriend.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_request_sent_orange_28dp, 0, 0);
                    break;
                default:
                    mBinding.tvAddfriend.setText(Friend.RELATION_STATUS_ADD_FRIEND);
                    mBinding.tvAddfriend.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_addfriend_orange_28dp, 0, 0);
                    break;
            }


            if (checkin == 0) {
                mBinding.tvCheckin.setClickable(false);
                mBinding.tvCheckin.setText(getActivity().getString(R.string.no_checkin));
            } else {
                mBinding.tvCheckin.setClickable(true);
                mBinding.tvCheckin.setText(getActivity().getString(R.string.checkin_count, checkin));
            }

            if (activity == 0) {
                mBinding.tvMyActivity.setClickable(false);
                mBinding.tvMyActivity.setText(getActivity().getString(R.string.no_activity));
            } else {
                mBinding.tvMyActivity.setClickable(true);
                mBinding.tvMyActivity.setText(getActivity().getString(R.string.activity_count, activity));
            }

            if (planningCount == 0) {
                mBinding.tvPlanning.setClickable(false);
                mBinding.tvPlanning.setText(getActivity().getString(R.string.no_planning));
            } else {
                mBinding.tvPlanning.setClickable(true);
                mBinding.tvPlanning.setText(getActivity().getString(R.string.planning_count, planningCount));
            }


            if (alFriends == null || alFriends.size() <= 0) {
                friendProfile.setFriendList(alFriends);
                mBinding.tvFriendCount.setText(R.string.no_friend);
                mBinding.rvFrineds.setVisibility(View.GONE);
                mBinding.llAllFriend.setVisibility(View.GONE);
                adptTimelineFriend.notifyDataSetChanged();
            } else {
                if (alFriends.size() <= 6) {
                    friendProfile.setFriendList(alFriends);
                    mBinding.tvFriendCount.setText(getString(R.string.friends_with_total, totalFriend));
                    mBinding.rvFrineds.setVisibility(View.VISIBLE);
                    mBinding.llAllFriend.setVisibility(View.GONE);
                    adptTimelineFriend.notifyDataSetChanged();
                } else {
                    friendProfile.setFriendList(alFriends);
                    mBinding.tvFriendCount.setText(getString(R.string.friends_with_total, totalFriend));
                    mBinding.rvFrineds.setVisibility(View.VISIBLE);
                    adptTimelineFriend.notifyDataSetChanged();
                }
            }

            if (alFriends != null && alFriends.size() > 0) {
                friendProfile.setFriendList(alFriends);
                mBinding.tvFriendCount.setText(getString(R.string.friends_with_total, totalFriend));
                mBinding.rvFrineds.setVisibility(View.VISIBLE);
                adptTimelineFriend.notifyDataSetChanged();
            } else {
                friendProfile.setFriendList(alFriends);
                mBinding.tvFriendCount.setText(R.string.no_friend);
                mBinding.rvFrineds.setVisibility(View.GONE);
                mBinding.llAllFriend.setVisibility(View.GONE);
                adptTimelineFriend.notifyDataSetChanged();
            }

            mBinding.ivProfileImg.setClickable(true);
            mBinding.llAllFriend.setClickable(true);
            mBinding.tvFavourite.setClickable(true);
            mBinding.tvAddfriend.setClickable(true);
            mBinding.tvChatbox.setClickable(true);
            mBinding.tvBlock.setClickable(true);
            mBinding.llAllPhoto.setClickable(true);
            mBinding.tvProfile.setClickable(true);
            mBinding.tvPoints.setClickable(true);
        } catch (Exception e) {
            e.printStackTrace();
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
                case R.id.iv_profileImg:
                    Photos photos = new Photos();
                    photos.setImageUrl(friendProfile.getvImage());
                    ArrayList<Photos> alPhoto = new ArrayList<>();
                    alPhoto.add(photos);
                    new ViewPhotoPagerDialog(getContext(), true, 0, alPhoto).show();

                    break;
                case R.id.tv_profile:

                    Intent iProfileActivity = new Intent(getContext(), ProfileActivity.class);
                    iProfileActivity.putExtra("profile_userId", friendProfile.getUserId());
                    iProfileActivity.putExtra("profile_editable", false);
                    startActivity(iProfileActivity);
                    break;
                case R.id.tv_planning:
                    UserPlanningFrg userPlanningFrg = new UserPlanningFrg();
                    Bundle argumentP = new Bundle();
                    argumentP.putString("friend_id", friendId);
                    userPlanningFrg.setArguments(argumentP);
                    ((DialogFragmentContainer) getParentFragment()).fragmentTransition(userPlanningFrg, true);
                    break;
                case R.id.tv_checkin:
                    UserCheckinFrg userActivity = new UserCheckinFrg();
                    Bundle argument = new Bundle();
                    argument.putString("friend_id", friendId);
                    userActivity.setArguments(argument);
                    ((DialogFragmentContainer) getParentFragment()).fragmentTransition(userActivity, true);
                    break;

                case R.id.tv_myActivity:
                    UserCategoryFrg userCategoryFrg = new UserCategoryFrg();
                    Bundle argumentC = new Bundle();
                    argumentC.putString("friend_id", friendId);
                    userCategoryFrg.setArguments(argumentC);

                    ((DialogFragmentContainer) getParentFragment()).fragmentTransition(userCategoryFrg, true);
                    break;

                case R.id.tv_points:
                    String checkinPoints = String.valueOf(friendProfile.getTotalCheckinPoints());
                    String activityPoints = String.valueOf(friendProfile.getTotalActivityPoints());
                    String planningPoints = String.valueOf(friendProfile.getTotalPlanningPoints());
                    String appOpenPoints = String.valueOf(friendProfile.getTotalAppOpenPoints());
                    String starPoints = String.valueOf(friendProfile.getTotalPostPoints());

                    CheckedinPointsFrg checkedinPointsFrg = new CheckedinPointsFrg();
                    Bundle arguments = new Bundle();
                    arguments.putStringArray("stars_points", new String[]{checkinPoints, activityPoints, planningPoints, appOpenPoints, starPoints});
                    checkedinPointsFrg.setArguments(arguments);


                    ((DialogFragmentContainer) getParentFragment()).fragmentTransition(checkedinPointsFrg, true);
                    break;

                case R.id.tv_favourite:
                    if (friendProfile.isFavourite()) {
                        if (webServiceCall.removeFavouriteWsCall(getContext(), friendId)) {
                            mBinding.progressBar.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (webServiceCall.addFavouriteWsCall(getContext(), friendId)) {
                            mBinding.progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case R.id.tv_addfriend:
                    if (friendProfile.getFriendRelation().equals(Friend.RELATION_STATUS_NO_FRIEND) || friendProfile.getFriendRelation().equals(Friend.RELATION_STATUS_ADD_FRIEND) || friendProfile.getFriendRelation().equals(Friend.RELATION_STATUS_DECLINED)) {
                        if (webServiceCall.sendFriendRequestWsCall(getContext(), friendId)) {
                            mBinding.progressBar.setVisibility(View.VISIBLE);
                        }
                    } else if (friendProfile.getFriendRelation().equals(Friend.RELATION_STATUS_FRIEND)) {

                        new ConfirmDialog(getContext(), getString(R.string.unfriend_confirm, friendProfile.getFullName()), new ConfirmDialog.OnConfirmYes() {
                            @Override
                            public void confirmYes() {
                                if (webServiceCall.unFriendWsCall(getContext(), friendProfile.getRelationId())) {
                                    mBinding.progressBar.setVisibility(View.VISIBLE);
                                }
                            }
                        }).show();

                    }
                    break;
                case R.id.tv_chatbox:
                    String[] friendDetails = {friendId, friendProfile.getFullName(), friendProfile.getvImage()};
                    UserPreferences.saveChatFriendId(getActivity(), friendDetails);
                    ((MainActivity) getActivity()).chatMsgBox();
                    ((DialogFragmentContainer) getParentFragment()).dismiss();
                    break;
                case R.id.tv_block:
                    new ConfirmDialog(getContext(), getString(R.string.block_confirm, friendProfile.getFullName()), new ConfirmDialog.OnConfirmYes() {
                        @Override
                        public void confirmYes() {
                            if (webServiceCall.blockUserWsCall(getContext(), friendId, true)) {
                                mBinding.progressBar.setVisibility(View.VISIBLE);
                            }
                        }
                    }).show();
                    break;

                case R.id.ll_photos:
                case R.id.ll_all_photo:
                    if (friendProfile.getPhotosList().size() == 1) {
                        ViewPhotoPagerDialog dialog = new ViewPhotoPagerDialog(getActivity(), 0, (ArrayList<Photos>) friendProfile.getPhotosList());
                        dialog.show();
                    } else {
                        ImageListDialog activityPhoto = new ImageListDialog(getActivity(), (ArrayList<Photos>) friendProfile.getPhotosList());
                        activityPhoto.show();
                    }
                    break;
                case R.id.ll_all_friend:

                    TimelineAllFriendFrg timelineAllFriendFrg = new TimelineAllFriendFrg();
                    Bundle extras = new Bundle();
                    extras.putString("timeline_all_friend_id", friendId);
                    timelineAllFriendFrg.setArguments(extras);
                    ((DialogFragmentContainer) getParentFragment()).fragmentTransition(timelineAllFriendFrg, true);
                    break;

            }
        }
    }

    @Override
    public void onResponseReceive(int requestCode) {
        mBinding.progressBar.setVisibility(View.GONE);
        switch (requestCode) {
            case WebServiceCall.REQUEST_CODE_USER_TIMELINE:
                FriendProfileModel mFriendProfile = (FriendProfileModel) webServiceCall.volleyRequestInstatnce().getModelObject(FriendProfileModel.class, FriendProfileModel.class.getSimpleName());
                if (mFriendProfile != null && mFriendProfile.getStatus() == BaseModel.STATUS_SUCCESS) {
                    if (mFriendProfile.getStatus() == BaseModel.STATUS_SUCCESS) {
                        friendProfile = mFriendProfile.getData();
                        if (!friendProfile.getFriendRelation().equals(Friend.RELATION_STATUS_BLOCK)) {
                            setValueToUI();
                        } else {
                            ((DialogFragmentContainer) getParentFragment()).popFragment();
                        }
                    } else {
                        Toast.makeText(getActivity(), mFriendProfile.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
                }
                break;
            case WebServiceCall.REQUEST_CODE_SEND_FRIEND_REQUEST:
                BaseModel mFriendRequestSent = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
                if (mFriendRequestSent != null) {
                    if (mFriendRequestSent.getStatus() == BaseModel.STATUS_SUCCESS) {
                        mBinding.tvAddfriend.setText(Friend.RELATION_STATUS_REUEST_SENT);
                        mBinding.tvAddfriend.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_request_sent_orange_28dp, 0, 0);
                        mBinding.tvAddfriend.setClickable(false);
                    } else {
                        Toast.makeText(getActivity(), mFriendRequestSent.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
                }
                break;
            case WebServiceCall.REQUEST_CODE_ADD_FAVOURITE:
                BaseModel mAddFav = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
                if (mAddFav != null) {
                    if (mAddFav.getStatus() == BaseModel.STATUS_SUCCESS) {
                        friendProfile.setFavourite("Yes");
                        mBinding.tvFavourite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_fav_fill_orange_28dp, 0, 0);
                    } else {
                        Toast.makeText(getActivity(), mAddFav.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
                }
                break;
            case WebServiceCall.REQUEST_CODE_REMOVE_FAVOURITE:
                BaseModel mRemoveFav = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
                if (mRemoveFav != null) {
                    if (mRemoveFav.getStatus() == BaseModel.STATUS_SUCCESS) {
                        friendProfile.setFavourite("No");
                        mBinding.tvFavourite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_fav_outline_orange_28dp, 0, 0);
                    } else {
                        Toast.makeText(getActivity(), mRemoveFav.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
                }
                break;
            case WebServiceCall.REQUEST_CODE_UNFRIEND:
                BaseModel mUnfriend = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
                if (mUnfriend != null) {
                    if (mUnfriend.getStatus() == BaseModel.STATUS_SUCCESS) {
                        removeViewClick();
                        if (webServiceCall.timelineWsCall(getContext(), friendId)) {
                            mBinding.progressBar.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
                }
                break;
            case WebServiceCall.REQUEST_CODE_BLOCK_UNBLOCK_USER:
                BaseModel mBlockUser = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
                if (mBlockUser != null) {
                    if (mBlockUser.getStatus() == BaseModel.STATUS_SUCCESS) {
                        ((DialogFragmentContainer) getParentFragment()).popFragment();
                    } else {
                        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
                }
                break;
            case WebServiceCall.REQUEST_CODE_FRIEND_LIST:
                FriendListModel mFriendList = (FriendListModel) webServiceCall.volleyRequestInstatnce().getModelObject(FriendListModel.class, FriendListModel.class.getSimpleName());
                if (mFriendList != null) {
                    if (mFriendList.getStatus() == BaseModel.STATUS_SUCCESS) {
                        totalFriend = mFriendList.getTotalFriends();
                        alFriends.addAll(mFriendList.getData());
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
                }
                if (webServiceCall.timelineWsCall(getContext(), friendId)) {
                    mBinding.progressBar.setVisibility(View.VISIBLE);
                }
                break;

        }

    }

    @Override
    public void onErrorReceive() {
        mBinding.progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }

    private void showPhotos() {
        int countPhotos = friendProfile.getPhotosList().size();
        mBinding.llPhotos.setVisibility(LinearLayout.VISIBLE);
        mBinding.llPhotoTop.setVisibility(LinearLayout.VISIBLE);

        switch (countPhotos) {
            case 1:
                ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[1].setVisibility(LinearLayout.GONE);
                ivPhoto[2].setVisibility(LinearLayout.GONE);
                ivPhoto[3].setVisibility(LinearLayout.GONE);
                ivPhoto[4].setVisibility(LinearLayout.GONE);
                mBinding.llPhotoBottom.setVisibility(LinearLayout.GONE);
                imageLoader(1);
                break;
            case 2:
                ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                mBinding.llPhotoBottom.setVisibility(LinearLayout.GONE);
                ivPhoto[2].setVisibility(LinearLayout.GONE);
                ivPhoto[3].setVisibility(LinearLayout.GONE);
                ivPhoto[4].setVisibility(LinearLayout.GONE);
                imageLoader(2);
                break;
            case 3:
                ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[2].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[3].setVisibility(LinearLayout.GONE);
                ivPhoto[4].setVisibility(LinearLayout.GONE);
                mBinding.llPhotoBottom.setVisibility(LinearLayout.VISIBLE);
                imageLoader(3);
                break;
            case 4:
                ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[2].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[3].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[4].setVisibility(LinearLayout.GONE);
                mBinding.llPhotoBottom.setVisibility(LinearLayout.VISIBLE);
                imageLoader(4);
                break;
            default:
                ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[2].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[3].setVisibility(LinearLayout.VISIBLE);
                ivPhoto[4].setVisibility(LinearLayout.VISIBLE);
                mBinding.llPhotoBottom.setVisibility(LinearLayout.VISIBLE);
                imageLoader(5);
                break;
        }
    }

    private void imageLoader(int size) {
        for (int counter = 0; counter < size; counter++) {
            Utility.loadImageGlide(ivPhoto[counter], friendProfile.getPhotosList().get(counter).getOriginalSizePath());
//                    Glide.with(getActivity()).load(friendProfile.getPhotosList().get(counter).getOriginalSizePath()).error(R.drawable.ic_placeholder).into(ivPhoto[counter]);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webServiceCall != null && webServiceCall.volleyRequestInstatnce() != null) {
            webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_USER_TIMELINE);
            webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_SEND_FRIEND_REQUEST);
            webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_ADD_FAVOURITE);
            webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_REMOVE_FAVOURITE);
            webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_UNFRIEND);
            webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_BLOCK_UNBLOCK_USER);
        }

    }
}
