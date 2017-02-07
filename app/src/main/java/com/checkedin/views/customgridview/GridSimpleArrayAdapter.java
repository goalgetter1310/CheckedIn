package com.checkedin.views.customgridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.model.Friend;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridSimpleArrayAdapter extends BaseAdapter implements GridSimpleAdapter {
    protected static final String TAG = GridSimpleArrayAdapter.class.getSimpleName();

    private int mHeaderResId;

    private LayoutInflater mInflater;

    private int mItemResId;

    private ArrayList<Friend> mItems;
    private Context context;

    public GridSimpleArrayAdapter(Context context, List<Friend> items, int headerResId, int itemResId) {
        init(context, items, headerResId, itemResId);
    }

    public GridSimpleArrayAdapter(Context context, Friend[] items, int headerResId, int itemResId) {
        init(context, Arrays.asList(items), headerResId, itemResId);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public long getHeaderId(int position) {
        String item = mItems.get(position).getFullName();
        CharSequence value;
        if (item != null) {
            value = item;
        } else {
            value = item.toString();
        }

        return value.subSequence(0, 1).charAt(0);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mHeaderResId, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_server_header);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        String item = mItems.get(position).getFullName();
        CharSequence string;
        if (item != null) {
            string = (CharSequence) item;
        } else {
            string = item.toString();
        }

        holder.textView.setText(string.subSequence(0, 1));

        return convertView;
    }

    @Override
    public String getItem(int position) {
        return mItems.get(position).getFullName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mItemResId, parent, false);
            holder = new ViewHolder();
            holder.civUserImg = (CircleImageView) convertView.findViewById(R.id.civ_server_img);
            holder.textViewName = (TextView) convertView.findViewById(R.id.tv_server_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String item = mItems.get(position).getFullName();
        Utility.loadImageGlide(holder.civUserImg,  mItems.get(position).getThumbImage());
//        Picasso.with(context).load(mItems.get(position).getThumbImage()).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder).into(holder.civUserImg);
        holder.textViewName.setText(item);

        return convertView;
    }

    private void init(Context context, List<Friend> items, int headerResId, int itemResId) {
        this.mItems = (ArrayList<Friend>) items;
        this.mHeaderResId = headerResId;
        this.mItemResId = itemResId;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

    protected class ViewHolder {
        public CircleImageView civUserImg;
        public TextView textViewName;
    }
}
