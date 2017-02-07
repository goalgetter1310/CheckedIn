package com.checkedin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.model.Friend;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.checkedin.views.stickylistheaders.StickyListHeadersListView;

import java.util.ArrayList;

public class TagFriendAdapter extends BaseAdapter implements StickyListHeadersListView.StickyListHeadersAdapter, SectionIndexer {


    private Context context;
    private ArrayList<Friend> alFriendList, alFriendSuggestedList;

    private ArrayList<Integer> mSectionIndices;
    private ArrayList<String> mSectionLetters;

    public TagFriendAdapter(Context context, ArrayList<Friend> alFriendList, ArrayList<Friend> alFriendSuggestedList) {
        this.context = context;
        this.alFriendList = alFriendList;
        this.alFriendSuggestedList = alFriendSuggestedList;

        mSectionIndices = new ArrayList<>();
        mSectionLetters = new ArrayList<>();

    }

    private void getSectionData() {
        mSectionIndices.add(0);
        if (alFriendSuggestedList.size() == 0) {
            mSectionLetters.add("All Friends");
            return;
        }
        mSectionLetters.add("Suggestions");
        mSectionIndices.add(alFriendSuggestedList.size());
        mSectionLetters.add("All Friends");
    }

    @Override
    public int getCount() {
        return alFriendSuggestedList.size() + alFriendList.size();
    }

    @Override
    public long getHeaderId(int position) {
        for (int counter = 0; counter < mSectionIndices.size() - 1; counter++) {
            if (position >= mSectionIndices.get(counter) && position < mSectionIndices.get(counter + 1)) {
                return counter;
            }
        }
        if (mSectionIndices.size() > 0 && position < mSectionIndices.get(mSectionIndices.size() - 1))
            return mSectionIndices.size() - 1;
        return -1L;
    }

    @Override
    public View getHeaderView(int position, View view, ViewGroup viewGroup) {
        HeaderHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_friend_list_header, viewGroup, false);
            holder = new HeaderHolder(view);
            view.setTag(holder);
        } else {
            holder = (HeaderHolder) view.getTag();
        }

        if (mSectionLetters.size() == 0) {
            holder.tvHeader.setText(mSectionLetters.get(position));
            return view;
        }

        for (int counter = 0; counter < mSectionLetters.size() - 1; counter++) {
            if (position >= mSectionIndices.get(counter) && position < mSectionIndices.get(counter + 1))
                holder.tvHeader.setText(mSectionLetters.get(counter));
        }
        if (mSectionIndices.size() > 0 && position >= mSectionIndices.get(mSectionIndices.size() - 1))
            holder.tvHeader.setText(mSectionLetters.get(mSectionIndices.size() - 1));
        return view;
    }

    @Override
    public Object getItem(int position) {
        if (position < alFriendSuggestedList.size())
            return alFriendSuggestedList.get(position);
        else
            return alFriendList.get(position - alFriendSuggestedList.size());
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getPositionForSection(int position) {
        if (position >= mSectionIndices.size()) {
            position = mSectionIndices.size() - 1;
        } else if (position < 0) {
            position = 0;
        }
        return mSectionIndices.get(position);
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int counter = 0; counter < mSectionIndices.size(); counter++) {
            if (position < mSectionIndices.get(counter))
                return counter - 1;
        }
        return mSectionIndices.size() - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters.toArray();
    }


    @Override
    public void notifyDataSetChanged() {
        mSectionIndices.clear();
        mSectionLetters.clear();
        getSectionData();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ItemHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_tag_friend_list, viewGroup, false);
            holder = new ItemHolder(view);
            view.setTag(holder);
        } else {
            holder = (ItemHolder) view.getTag();
        }
        Friend friend = position < alFriendSuggestedList.size() ? alFriendSuggestedList.get(position) : alFriendList.get(position - alFriendSuggestedList.size());
        holder.tvFriendName.setText(friend.getFullName());
        Utility.loadImageGlide( holder.civProfilePic       ,friend.getThumbImage());
//        Glide.with(context).load(friend.getThumbImage()).error(R.drawable.ic_placeholder).into(holder.civProfilePic);

        holder.imgButton.setSelected(friend.isSelected());

        return view;
    }

    private class HeaderHolder {
        private TextView tvHeader;

        private HeaderHolder(View headerView) {
            tvHeader = (TextView) headerView.findViewById(R.id.tv_friend_tag_header);
        }
    }


    private class ItemHolder {
        private TextView tvFriendName;
        private CircleImageView civProfilePic;
        private ImageButton imgButton;

        public ItemHolder(View itemView) {
            tvFriendName = (TextView) itemView.findViewById(R.id.tv_adpt_friend_list_name);
            civProfilePic = (CircleImageView) itemView.findViewById(R.id.civ_adpt_friend_list_img);
            imgButton = (ImageButton) itemView.findViewById(R.id.civ_adpt_friend_list_img_btn);
        }


    }


}