package com.checkedin.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.model.PhoneContact;
import com.checkedin.utility.Utility;

import java.util.ArrayList;

public class ContactInviteAdapter extends RecyclerView.Adapter<ContactInviteAdapter.Viewholder> {

    private ArrayList<PhoneContact> contacts;
    private Context context;

    public ContactInviteAdapter(Context context, ArrayList<PhoneContact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {

        if (!TextUtils.isEmpty(contacts.get(position).getImageUrl())) {
            Utility.loadImageGlide(holder.imgPhoto, contacts.get(position).getImageUrl());
        } else {
            holder.imgPhoto.setImageResource(R.drawable.ic_placeholder);
        }
        holder.tvName.setText(contacts.get(position).getName());
        holder.tvContact.setText(contacts.get(position).getMobileNo());

        holder.ivSms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contacts.get(position).getMobileNo()));
                intent.putExtra("sms_body", context.getResources().getString(R.string.invite_friend_sms));
                context.startActivity(intent);
            }
        });

    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView tvContact;
        private TextView tvName;
        private ImageView imgPhoto;
        private ImageView ivSms;

        public Viewholder(View itemView) {
            super(itemView);
            tvContact = (TextView) itemView.findViewById(R.id.inflater_invite_tv_number);
            tvName = (TextView) itemView.findViewById(R.id.inflater_invite_tv_person_name);
            imgPhoto = (ImageView) itemView.findViewById(R.id.inflater_invite_iv_photo);
            ivSms = (ImageView) itemView.findViewById(R.id.inflater_invite_iv_sms);
        }
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_contact_invite, parent, false);
        return new Viewholder(v);
    }


}