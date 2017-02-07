package com.checkedin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.fragment.FeedbackFrg;
import com.checkedin.fragment.HelpFrg;
import com.checkedin.fragment.SupportFrg;
import com.checkedin.fragment.TermsConditionFrg;
import com.checkedin.utility.Utility;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.ViewHolder> {
    private Context context;
    private String[] items;
    private SupportFrg supportFrg;

    public SupportAdapter(Context context, SupportFrg supportFrg) {
        this.context = context;
        this.supportFrg = supportFrg;
        items = context.getResources().getStringArray(R.array.support);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_support, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSupport.setText(items[position]);
        holder.itemView.setTag(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private TextView tvSupport;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSupport = (TextView) itemView.findViewById(R.id.tv_support);
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



                        ((BaseContainerFragment) supportFrg.getParentFragment()).replaceFragment(new HelpFrg(), true);

                        break;
                    case 1:
                        ((BaseContainerFragment) supportFrg.getParentFragment()).replaceFragment(new FeedbackFrg(), true);
                        break;
                    case 2:
                        ((BaseContainerFragment) supportFrg.getParentFragment()).replaceFragment(new TermsConditionFrg(), true);
                        break;
                }
            }
        }

    }
}
