package com.checkedin.container;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.checkedin.R;
import com.checkedin.fragment.ActivityCategoryFrg;
import com.checkedin.fragment.ChatListFrg;
import com.checkedin.fragment.CheckinPlacesFrg;

public class BaseContainerFragment extends Fragment {
	private boolean isAnimated = true;

	public void replaceTabFragment(Fragment fragment, boolean addToBackStack) {

		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

		if (addToBackStack) {
			transaction.addToBackStack(null);
			if (isAnimated) {
				transaction.setCustomAnimations(R.anim.right2middle, R.anim.middle2left, R.anim.left2middle, R.anim.middle2right);
			}
		}
		transaction.replace(R.id.ll_home_tab_container, fragment, fragment.getClass().getSimpleName());
		try {
			transaction.commitAllowingStateLoss();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.commit();
		}

	}

	public void replaceFragment(Fragment fragment, boolean addToBackStack) {

		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

		if (addToBackStack) {
			transaction.addToBackStack(null);
			if (isAnimated) {
				transaction.setCustomAnimations(R.anim.right2middle, R.anim.middle2left, R.anim.left2middle, R.anim.middle2right);
			}
		}
		transaction.replace(R.id.fl_container, fragment, fragment.getClass().getSimpleName());
		try {
			transaction.commitAllowingStateLoss();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.commit();
		}

	}

	public void directreplaceFragment(Fragment fragment, boolean addToBackStack) {

		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.fl_container, fragment, fragment.getClass().getSimpleName());
		try {
			transaction.commitAllowingStateLoss();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.commit();
		}
	}

	public void addFragment(Fragment fragment, boolean addToBackStack) {

			FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

		if (addToBackStack) {
			transaction.addToBackStack(null);
			if (isAnimated) {
				transaction.setCustomAnimations(R.anim.right2middle, R.anim.middle2left, R.anim.left2middle, R.anim.middle2right);
			}
		}
		transaction.add(R.id.fl_container, fragment, fragment.getClass().getSimpleName());
		transaction.commit();
	}

	public boolean popFragment() {
		boolean isPop = false;
		if (getChildFragmentManager().getBackStackEntryCount() > 0) {
			isPop = true;
			getChildFragmentManager().popBackStack();

			CheckinPlacesFrg searchPlaceFrg = (CheckinPlacesFrg) getChildFragmentManager().findFragmentByTag(CheckinPlacesFrg.class.getSimpleName());
			ActivityCategoryFrg activityCategoryFrg = (ActivityCategoryFrg) getChildFragmentManager().findFragmentByTag(ActivityCategoryFrg.class.getSimpleName());
			ChatListFrg chatListFrg = (ChatListFrg) getChildFragmentManager().findFragmentByTag(ChatListFrg.class.getSimpleName());

			if (searchPlaceFrg != null) {
				searchPlaceFrg.onResumeCustom();
			} else if (chatListFrg != null) {
				chatListFrg.onResumeCustom();
			}  else if (activityCategoryFrg != null) {
				activityCategoryFrg.onResumeCustom();
			}

		}
		return isPop;
	}

}
