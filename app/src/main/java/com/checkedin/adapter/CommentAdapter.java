package com.checkedin.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.databinding.AdapterCommentBinding;
import com.checkedin.fragment.TimelineFrg;
import com.checkedin.model.Comment;
import com.checkedin.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<Comment> items;

    public CommentAdapter(Activity activity, ArrayList<Comment> items) {
        this.activity = activity;
        this.items = items;

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Utility.loadImageGlide( holder.mBinding.civProfileImg                         ,items.get(position).getProfileImg());
//        Picasso.with(activity).load(items.get(position).getProfileImg()).error(R.drawable.ic_placeholder).into(holder.mBinding.civProfileImg);
        holder.mBinding.tvName.setText(items.get(position).getFullName());
        holder.mBinding.tvComment.setText(items.get(position).getText());
        holder.mBinding.tvTime.setText(items.get(position).getTimeStr());
        holder.itemView.setTag(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int resource) {
        AdapterCommentBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.adapter_comment, parent, false);

        return new ViewHolder(mBinding);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private final AdapterCommentBinding mBinding;

        public ViewHolder(AdapterCommentBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                Utility.doubleTapTime = System.currentTimeMillis();
                TimelineFrg friendProfile = new TimelineFrg();
                Bundle argument = new Bundle();
                argument.putString("friend_id", items.get((Integer) v.getTag()).getId());
                friendProfile.setArguments(argument);
                if (DialogFragmentContainer.isDialogOpen) {
                    DialogFragmentContainer.getInstance().fragmentTransition(friendProfile, true);
                } else {
                    DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
                    dialogFrgContainer.init(friendProfile);
                    dialogFrgContainer.show(((MainActivity) activity).getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                }
            }
        }
    }

}
