package com.checkedin.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.container.HomeFrgContainer;
import com.checkedin.fragment.ActivityCategoryFrg;
import com.checkedin.fragment.ChatListFrg;
import com.checkedin.fragment.CheckinLoungeFrg;
import com.checkedin.fragment.FriendFrg;
import com.checkedin.fragment.NotificationListFrg;
import com.checkedin.fragment.PostDetailsFrg;
import com.checkedin.model.Notification;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;

import java.util.ArrayList;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {
    private Context context;
    private int resource;
    private ArrayList<Notification> items;
    private NotificationListFrg notificationListFrg;

    public NotificationListAdapter(Context context, int resource, ArrayList<Notification> items, NotificationListFrg notificationListFrg) {
        this.context = context;
        this.resource = resource;
        this.items = items;
        this.notificationListFrg = notificationListFrg;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup root, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, root, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvMsg.setText(items.get(position).getvMessage());
        holder.tvTime.setText(items.get(position).getDtCreatedDate());

        Utility.loadImageGlide(holder.civProfileImg, items.get(position).getvImage());
//        Picasso.with(context).load(items.get(position).getvImage()).into(holder.civProfileImg);

        holder.itemView.setTag(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private TextView tvMsg, tvTime;
        private View itemView;
        private CircleImageView civProfileImg;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            civProfileImg = (CircleImageView) itemView.findViewById(R.id.civ_notify_list_img);
            tvMsg = (TextView) itemView.findViewById(R.id.tv_notify_list_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_notify_list_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            notificationClick(items.get(position).getvType(), items.get(position).getiPostID());
//            if (!TextUtils.isEmpty(items.get(position).getId())) {
//                if (items.get(position).getId().equals(Notification.NOTIFY_TYPE_TAG_ACTIVITY) || items.get(position).getId().equals(Notification.NOTIFY_TYPE_POST_COMMENT)) {
//                    ((DialogFragmentContainer) notificationListFrg.getParentFragment()).fragmentTransition(new PostDetailsDialog(), true);
//                }
//            }
        }

    }

    public void notificationClick(String notifyType, String postId) {
        Utility.currentFragment = context.getResources().getString(R.string.title_notifications);
        switch (notifyType) {
            case Notification.NOTIFY_TYPE_IMG_MSG:
            case Notification.NOTIFY_TYPE_TEXT_MSG: {
//                FragmentTransaction fTrans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
//                fTrans.replace(R.id.fl_container, new HomeFrgContainer(new ChatListFrg()), Utility.currentFragment);
//                fTrans.commit();
//                ((DialogFragmentContainer) notificationListFrg.getParentFragment()).dismiss();
                break;
            }
            case Notification.NOTIFY_TYPE_FRIEND_IN_LOUNGE: {

//                FragmentTransaction fTrans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
//                fTrans.replace(R.id.fl_container, new HomeFrgContainer(new CheckinLoungeFrg()), Utility.currentFragment);
//                fTrans.commit();
//                ((DialogFragmentContainer) notificationListFrg.getParentFragment()).dismiss();

                ((MainActivity)context).setToolbarTitle(context.getString(R.string.title_checkiedn_lounge));
                CheckinLoungeFrg friendFrg = new CheckinLoungeFrg();
                ((BaseContainerFragment)notificationListFrg.getParentFragment()).replaceFragment(friendFrg,true);
                break;
            }
            case Notification.NOTIFY_TYPE_ACCEPT_FRIEND_REQUEST:
            case Notification.NOTIFY_TYPE_FRIEND_REQUEST:
//                FragmentTransaction fTrans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
//                fTrans.replace(R.id.fl_container, new HomeFrgContainer(new ActivityCategoryFrg()), Utility.currentFragment);
//                fTrans.commit();
//
                FriendFrg friendFrg = new FriendFrg();
                DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
                dialogFrgContainer.init(friendFrg);
                dialogFrgContainer.show(((MainActivity) context).getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());


//                ((DialogFragmentContainer) notificationListFrg.getParentFragment()).fragmentTransition(new FriendFrg(), true);
                break;

            case Notification.NOTIFY_TYPE_STAR_POST:
            case Notification.NOTIFY_TYPE_TAG_ACTIVITY:
            case Notification.NOTIFY_TYPE_FAV_CHECKIN:
            case Notification.NOTIFY_TYPE_FAV_ACTIVITY:
            case Notification.NOTIFY_TYPE_FAV_PLANNING:
            case Notification.NOTIFY_TYPE_OWNER_COMMENT:
            case Notification.NOTIFY_TYPE_POST_COMMENT: {
                PostDetailsFrg postDetailsFrg = new PostDetailsFrg();
                Bundle argument = new Bundle();
                argument.putString("post_id", postId);
                postDetailsFrg.setArguments(argument);

                DialogFragmentContainer fragmentContainer = DialogFragmentContainer.getInstance();
                fragmentContainer.init(postDetailsFrg);
                fragmentContainer.show(((MainActivity) context).getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());

//                ((BaseContainerFragment) notificationListFrg.getParentFragment()).replaceFragment(postDetailsFrg, true);
                break;
            }
        }
    }
}
