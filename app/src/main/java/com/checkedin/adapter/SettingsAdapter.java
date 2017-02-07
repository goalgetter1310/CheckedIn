package com.checkedin.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.fragment.BlockListFrg;
import com.checkedin.fragment.ChangePasswordFrg;
import com.checkedin.fragment.PrivacyFrg;
import com.checkedin.fragment.SettingsFrg;
import com.checkedin.fragment.SupportFrg;
import com.checkedin.utility.Utility;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
    private Activity activity;
    private String[] items;
    private SettingsFrg settingsFrg;

    public SettingsAdapter(Activity activity, SettingsFrg settingsFrg) {
        this.activity = activity;
        this.settingsFrg = settingsFrg;
        items = activity.getResources().getStringArray(R.array.settings);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_settings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSetings.setText(items[position]);
        holder.itemView.setTag(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private TextView tvSetings;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSetings = (TextView) itemView.findViewById(R.id.tv_settings);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                Utility.doubleTapTime = System.currentTimeMillis();
                int position = (Integer) v.getTag();
                switch (position) {
                    case 0:
                        ((BaseContainerFragment) settingsFrg.getParentFragment()).replaceFragment(new ChangePasswordFrg(), true);
                        break;
                    case 1:
                        ((BaseContainerFragment) settingsFrg.getParentFragment()).replaceFragment(new SupportFrg(), true);
                        break;
                    case 2:
                        ((BaseContainerFragment) settingsFrg.getParentFragment()).replaceFragment(new BlockListFrg(), true);
                        break;
                    case 3:
                        ((BaseContainerFragment) settingsFrg.getParentFragment()).replaceFragment(new PrivacyFrg(), true);
                        break;

                }
            }
        }

    }
}
