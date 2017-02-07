package com.checkedin.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import com.checkedin.activity.MainActivity;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.fragment.TagFriendListFrg;
import com.checkedin.dialog.MapViewDialog;
import com.checkedin.fragment.TimelineFrg;
import com.checkedin.model.FriendsActivity;
import com.checkedin.model.TagUser;
import com.checkedin.model.WordAction;
import com.checkedin.utility.Utility;

import java.util.ArrayList;

public class TextView extends android.widget.TextView {
    private Activity activity;

    public TextView(Context context) {
        super(context);
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void addSpanClick(final FriendsActivity friendActivity, ArrayList<WordAction> alClick, String title) {
        SpannableString ssTitle = new SpannableString(title);

        int[] position;
        for (int counter = 0; counter < alClick.size(); counter++) {
            position = getSpanPosition(title, alClick.get(counter).getWord());
            if (alClick.get(counter).getAction() == 0) {
                ssTitle.setSpan(userClick(alClick.get(counter)), position[0], position[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (alClick.get(counter).getAction() == 1) {
                ssTitle.setSpan(locationClick(friendActivity), position[0], position[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                ssTitle.setSpan(otherFriends(friendActivity), position[0], position[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        setMovementMethod(LinkMovementMethod.getInstance());
        setText(ssTitle);
    }

    private ClickableSpan otherFriends(final FriendsActivity friendActivity) {

        return new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                    Utility.doubleTapTime = System.currentTimeMillis();
                    TagFriendListFrg activityTagFriend = new TagFriendListFrg();
                    Bundle argument = new Bundle();
                    ArrayList<TagUser> tagFriend = (ArrayList<TagUser>) friendActivity.getTagUsers();
                    argument.putParcelableArrayList("tag_friend", tagFriend);
                    activityTagFriend.setArguments(argument);

                    if (DialogFragmentContainer.isDialogOpen) {
                        DialogFragmentContainer.getInstance().fragmentTransition(activityTagFriend, true);
                    } else {
                        DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
                        dialogFrgContainer.init(activityTagFriend);
                        dialogFrgContainer.show(((MainActivity) activity).getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                    }
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.BLACK);
            }
        };
    }

    private ClickableSpan userClick(final WordAction wordAction) {
        return new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                    Utility.doubleTapTime = System.currentTimeMillis();
                    TimelineFrg friendProfile = new TimelineFrg();
                    Bundle argument = new Bundle();
                    argument.putString("friend_id", wordAction.getUserId());
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

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.BLACK);
            }
        };
    }

    private ClickableSpan locationClick(final FriendsActivity friendActivity) {
        return new ClickableSpan() {

            @Override
            public void onClick(View textView) {
                if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                    Utility.doubleTapTime = System.currentTimeMillis();
                    new MapViewDialog(activity, Float.parseFloat(friendActivity.getLatitude()), Float.parseFloat(friendActivity.getLongitude())).show();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.BLACK);
            }
        };
    }

    private int[] getSpanPosition(String title, String word) {
        int[] position = {0, 0};
        for (int counter = 0; counter < title.length() - word.length() + 1; counter++) {
            if (title.substring(counter, counter + word.length()).equals(word)) {
                position[0] = counter;
                position[1] = counter + word.length();
                break;
            }
        }
        return position;
    }

}