package com.checkedin.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.databinding.FrgCheckedinPointsBinding;
public class CheckedinPointsFrg extends Fragment {
    private FrgCheckedinPointsBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_checkedin_points, container, false);

        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DialogFragmentContainer) getParentFragment()).popFragment();
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] points = getArguments().getStringArray("stars_points");
        if (points != null) {
            mBinding.tvCheckinStar.setText(String.valueOf(points[0]));
            mBinding.tvActivityStar.setText(String.valueOf(points[1]));
            mBinding.tvPlanningStar.setText(String.valueOf(points[2]));
            mBinding.tvAppOpenStar.setText(String.valueOf(points[3]));
            mBinding.tvPostStar.setText(String.valueOf(points[4]));
        }
    }
}
