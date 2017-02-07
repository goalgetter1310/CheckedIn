package com.checkedin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.ProfileActivity;
import com.checkedin.adapter.EduInfoAdapter;
import com.checkedin.adapter.LivingPlaceInfoAdapter;
import com.checkedin.adapter.WorkInfoAdapter;
import com.checkedin.databinding.FrgProfileMainBinding;
import com.checkedin.model.EducationInfo;
import com.checkedin.model.LivingPlace;
import com.checkedin.model.UserDetail;
import com.checkedin.model.WorkInfo;
import com.checkedin.utility.Utility;

import java.util.ArrayList;
import java.util.Collections;

public class ProfileMainFrg extends Fragment {
    private FrgProfileMainBinding mBinding;

    private ArrayList<LivingPlace> alLivingPlaceInfo;
    private ArrayList<EducationInfo> alEduInfo;
    private ArrayList<WorkInfo> alWorkInfo;

    private LivingPlaceInfoAdapter adptLivingPlaceinfo;
    private EduInfoAdapter adptEduInfo;
    private WorkInfoAdapter adptWorkInfo;

    private int scrollPositionX;
    private int scrollPositionY;
    private boolean isEditable, isLivingInfoEmpty, isEduInfoEmpty, isWorkInfoEmpty;
    private UserDetail userDetail;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_profile_main, container, false);

        alLivingPlaceInfo = new ArrayList<>();
        alEduInfo = new ArrayList<>();
        alWorkInfo = new ArrayList<>();

        isEditable = getArguments().getBoolean("profile_editable");

        adptLivingPlaceinfo = new LivingPlaceInfoAdapter(getContext(), alLivingPlaceInfo, isEditable);
        adptEduInfo = new EduInfoAdapter(getContext(), alEduInfo, isEditable);
        adptWorkInfo = new WorkInfoAdapter(getContext(), alWorkInfo, isEditable);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.setFragment(this);
        userDetail = ((ProfileActivity) getActivity()).getUserDetail();
        ((ProfileActivity) getActivity()).setTitle(userDetail.getFullName());

        mBinding.llAddCurrentTown.setVisibility(isEditable ? View.VISIBLE : View.GONE);
        mBinding.llAddHomeTown.setVisibility(isEditable ? View.VISIBLE : View.GONE);

        String fullName = userDetail.getFullName();
        String birthdate = userDetail.getDatOfBirth();
        String gender = userDetail.getGender();
        String relationship = userDetail.getMaritalStatus();


        mBinding.tvName.setText(fullName);
        mBinding.tvBirthday.setText(birthdate.equals(getString(R.string.empty_date)) ? getString(R.string.not_mention) : birthdate);
        mBinding.tvGender.setText(TextUtils.isEmpty(gender) ? getString(R.string.not_mention) : gender);
        mBinding.tvRelationship.setText(TextUtils.isEmpty(relationship) ? getString(R.string.not_mention) : relationship);
        mBinding.tvUsername.setText(userDetail.getUsername());
        mBinding.tvEmail.setText(userDetail.getEmail());
        mBinding.tvMobile.setText(userDetail.getMobileNo());

        if (isEditable) {
            mBinding.ivProfileImg.setVisibility(View.GONE);
        } else {
            mBinding.ivProfileImg.setVisibility(View.VISIBLE);
            Utility.loadImageGlide(mBinding.ivProfileImg, userDetail.getFullImage());
        }

        // Living Place Info
        if (userDetail.getLivingPlaceInfoList() != null && userDetail.getLivingPlaceInfoList().size() > 0) {
            isLivingInfoEmpty = false;
            mBinding.rvLivingPlace.setVisibility(View.VISIBLE);
            alLivingPlaceInfo.addAll(userDetail.getLivingPlaceInfoList());
            Collections.sort(alLivingPlaceInfo);
            for (LivingPlace livingPlace : alLivingPlaceInfo) {
                if (livingPlace.getType().equals(LivingPlace.LIVING_PLACE_CURRENT)) {
                    mBinding.llAddCurrentTown.setVisibility(View.GONE);
                } else if (livingPlace.getType().equals(LivingPlace.LIVING_PLACE_HOME)) {
                    mBinding.llAddHomeTown.setVisibility(View.GONE);
                }
            }
        } else {
            isLivingInfoEmpty = true;
            mBinding.rvLivingPlace.setVisibility(View.GONE);
        }
        mBinding.rvLivingPlace.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvLivingPlace.setAdapter(adptLivingPlaceinfo);

        // Education Info
        if (userDetail.getEducationInfoList() != null && userDetail.getEducationInfoList().size() > 0) {
            isEduInfoEmpty = false;
            mBinding.rvEduInfo.setVisibility(View.VISIBLE);
            alEduInfo.addAll(userDetail.getEducationInfoList());
        } else {
            isEduInfoEmpty = true;
            mBinding.rvEduInfo.setVisibility(View.GONE);
        }
        mBinding.rvEduInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvEduInfo.setAdapter(adptEduInfo);

        // Work Info
        if (userDetail.getWorkInfoList() != null && userDetail.getWorkInfoList().size() > 0) {
            isWorkInfoEmpty = false;
            mBinding.rvWork.setVisibility(View.VISIBLE);
            alWorkInfo.addAll(userDetail.getWorkInfoList());
        } else {
            isWorkInfoEmpty = true;
            mBinding.rvWork.setVisibility(View.GONE);
        }
        mBinding.rvWork.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvWork.setAdapter(adptWorkInfo);

        mBinding.getRoot().scrollTo(scrollPositionX, scrollPositionY);
    }

    public boolean isEditable() {
        return isEditable;
    }

    public boolean isLivingInfoEmpty() {
        return !isEditable() && isLivingInfoEmpty;
    }

    public boolean isEduInfoEmpty() {
        return !isEditable() && isEduInfoEmpty;
    }

    public boolean isWorkInfoEmpty() {
        return !isEditable() && isWorkInfoEmpty;
    }

    public void editBasicInfo(View v) {
        ((ProfileActivity) getActivity()).manageFragment(1, null, true);
    }

    public void editContactInfo(View v) {
        ((ProfileActivity) getActivity()).manageFragment(2, null, true);
    }

    public boolean isMobileVisible() {
        return isEditable || userDetail.isShowMobile();
    }

    public void eduSchoolInfoEdit(View v) {
        Bundle argument = new Bundle();
        argument.putInt("edu_type", EducationInfo.EDUCATION_TYPE_SCHOOL);
        ((ProfileActivity) getActivity()).manageFragment(4, argument, true);
    }

    public void eduUnivercityInfo(View v) {
        Bundle argument = new Bundle();
        argument.putInt("edu_type", EducationInfo.EDUCATION_TYPE_UNIVERSITY);
        ((ProfileActivity) getActivity()).manageFragment(4, argument, true);
    }

    public void livingPlaceInfo(View v) {
        ((ProfileActivity) getActivity()).manageFragment(3, null, true);
    }

    public void workInfo(View v) {
        ((ProfileActivity) getActivity()).manageFragment(5, null, true);
    }


    @Override
    public void onDestroyView() {
        scrollPositionX = mBinding.getRoot().getScrollX();
        scrollPositionY = mBinding.getRoot().getScrollY();
        super.onDestroyView();
    }
}
