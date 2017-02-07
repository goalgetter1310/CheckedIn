package com.checkedin.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.container.InviteFriendContainer;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

@SuppressLint("ValidFragment")
public class InviteFriendFrg extends Fragment implements OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_invite_friend, container, false);

        RelativeLayout rlInviteEmail = (RelativeLayout) view.findViewById(R.id.rl_invite_email);
        RelativeLayout rlInviteContactList = (RelativeLayout) view.findViewById(R.id.rl_invite_contact_list);

        ((MainActivity) getActivity()).toggleActionBarIcon(1, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_invite));

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            rlInviteEmail.setBackground(roundRectShape(getResources().getColor(R.color.colorPrimary)));
            rlInviteContactList.setBackground(roundRectShape(getResources().getColor(R.color.colorPrimary)));
        }

        rlInviteEmail.setOnClickListener(this);
        rlInviteContactList.setOnClickListener(this);

        return view;
    }

    private void initViews() {

    }

    private Drawable roundRectShape(int color) {
        GradientDrawable gdRoundRect = new GradientDrawable();
        gdRoundRect.setColor(color);
        gdRoundRect.setCornerRadius(5);
        return gdRoundRect;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_invite_email:
                ((InviteFriendContainer) getParentFragment()).replaceFragment(new InviteByEmailFrg(), true);
                break;
            case R.id.rl_invite_contact_list:
                PermissionEverywhere.getPermission(
                        getContext(),
                        new String[]{ Manifest.permission.READ_CONTACTS},
                        101)
                        .enqueue(new PermissionResultCallback() {
                            @Override
                            public void onComplete(PermissionResponse permissionResponse) {
                                if (permissionResponse.isGranted()) {
                                    ((InviteFriendContainer) getParentFragment()).replaceFragment(new ContactInviteFrg(), true);
                                }
                            }
                        });

                break;

        }
    }
}
