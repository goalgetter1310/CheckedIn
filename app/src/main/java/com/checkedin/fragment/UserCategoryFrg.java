package com.checkedin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.adapter.CategoryUserAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.model.ActivityCategory;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.CategoryModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;
public class UserCategoryFrg extends Fragment implements  VolleyStringRequest.AfterResponse {

    private ArrayList<ActivityCategory> alMyActivity;
    private CategoryUserAdapter adptMyActivity;
    private WebServiceCall webServiceCall;
    private String friendId;


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category, container, false);
        webServiceCall = new WebServiceCall(this);

        friendId = getArguments().getString("friend_id");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvCategory = (RecyclerView) view.findViewById(R.id.rv_category);
        alMyActivity = new ArrayList<>();
        adptMyActivity = new CategoryUserAdapter(getActivity(), alMyActivity, this,friendId);

        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvCategory.setAdapter(adptMyActivity);

        view.findViewById(R.id.iv_user_activity_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                    Utility.doubleTapTime = System.currentTimeMillis();
                    ((DialogFragmentContainer) getParentFragment()).popFragment();
                }
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webServiceCall.activityCategoryWsCall(getContext(), UserPreferences.fetchUserId(getContext()), friendId);
    }


    @Override
    public void onResponseReceive(int requestCode) {
        CategoryModel mCategory = (CategoryModel) webServiceCall.volleyRequestInstatnce().getModelObject(CategoryModel.class, "CategoryModel Activity");
        if (mCategory != null) {
            if (mCategory.getStatus() == BaseModel.STATUS_SUCCESS) {
                alMyActivity.clear();
                alMyActivity.addAll(mCategory.getCategoryList());
                adptMyActivity.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), mCategory.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorReceive() {
        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
//        UserActivityFrg userActivity = new UserActivityFrg(friendId, alMyActivity.get(position).getId());
//        ((DialogFragmentContainer) getParentFragment()).fragmentTransition(userActivity, true);
//    }

}
