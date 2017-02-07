package com.checkedin.container;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.checkedin.R;
import com.checkedin.utility.Utility;

@SuppressLint({"InflateParams", "ValidFragment"})
public class DialogFragmentContainer extends DialogFragment {

    private Fragment dialogFragment;

    public static boolean isDialogOpen = false;
    private static DialogFragmentContainer dialogFrgContainer = null;
    private boolean isKeyboard;

    public static DialogFragmentContainer getInstance() {
        if (dialogFrgContainer == null)
            dialogFrgContainer = new DialogFragmentContainer();
        return dialogFrgContainer;
    }

    public static void setInstance(DialogFragmentContainer dialogFrgContainer) {
        DialogFragmentContainer.dialogFrgContainer = dialogFrgContainer;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppTheme);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppTheme);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setupDialog(dialog, R.style.Theme_AppTheme);
        try {
            super.onActivityCreated(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Fragment getDialogFragment() {
        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_dialog_fragment, container, false);
        isDialogOpen = true;
        fragmentTransition(dialogFragment, false);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(!isKeyboard) {
                        popFragment();
                        return true;
                    }
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            isKeyboard=false;

        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            isKeyboard=true;

        }
    }
    public void init(Fragment dialogFragment) {
        this.dialogFragment = dialogFragment;
    }

    public void fragmentTransition(Fragment dialogFragment, boolean isAnimated) {
        if (dialogFragment != null) {
            this.dialogFragment = dialogFragment;
            try {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(dialogFragment.getClass().getSimpleName());
                if (isAnimated) {
                    transaction.setCustomAnimations(R.anim.right2middle, R.anim.middle2left, R.anim.left2middle, R.anim.middle2right);
                }
                transaction.replace(R.id.fl_container2, dialogFragment, dialogFragment.getClass().getSimpleName());
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {
                dialogFrgContainer = null;
                isDialogOpen = false;
                dismiss();
                e.printStackTrace();
            }
        } else {
            dismiss();
        }
    }

    public void popFragment() {
        try {
            Utility.hideKeyboard(getActivity());
            if (getChildFragmentManager().getBackStackEntryCount() > 1)
                getChildFragmentManager().popBackStack();
            else {
                isDialogOpen = false;
                dialogFrgContainer = null;
                dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        isDialogOpen = false;
        super.onDestroyView();
    }

    @Override
    public void dismiss() {
        Utility.logUtils("DialogFragmentContainer->dismiss");
        isDialogOpen = false;
        super.dismiss();
    }
}
