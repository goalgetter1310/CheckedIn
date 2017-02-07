package com.checkedin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.adapter.ActivityTagFriendAdapter;
import com.checkedin.model.TagUser;

import java.util.ArrayList;


public class TagFriendListFrg extends Fragment {

    private RecyclerView rvTagFriend;
    private ArrayList<TagUser> alTagFriend;
    private ActivityTagFriendAdapter adptTagFriend;

//	public TagFriendListFrg(ArrayList<TagUser> alTagFriend) {
//		this.alTagFriend = alTagFriend;
//	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_tag_friend_list, container, false);
        initViews(view);

        if (getArguments() != null) {
            alTagFriend = getArguments().getParcelableArrayList("tag_friend");
            rvTagFriend.setLayoutManager(new LinearLayoutManager(getActivity()));
            adptTagFriend = new ActivityTagFriendAdapter(getActivity(), R.layout.adapter_activity_tag_friend, alTagFriend, this);
            rvTagFriend.setAdapter(adptTagFriend);
        }

        return view;
    }

    private void initViews(View view) {
        alTagFriend=new ArrayList<>();
        rvTagFriend = (RecyclerView) view.findViewById(R.id.rv_tag_friend);
    }

}
