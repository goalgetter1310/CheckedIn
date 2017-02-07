package com.checkedin.tablayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.checkedin.R;


public class TintableImageView extends ImageView {

    private ColorStateList tint;
    private int defaultColorId  = R.color.gray;
    private int selectedColorId = R.color.colorPrimaryDark;

    public TintableImageView(Context context) {
        super(context);
        init();
    }

    public TintableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TintableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private static ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        final int[][] states = new int[2][];
        final int[]   colors = new int[2];
        int           i      = 0;

        states[i] = SELECTED_STATE_SET;
        colors[i] = selectedColor;
        i++;

        // Default enabled state
        states[i] = EMPTY_STATE_SET;
        colors[i] = defaultColor;
        i++;

        return new ColorStateList(states, colors);
    }


    public void init() {
        init(defaultColorId, selectedColorId);
    }

    public void init(int defaultColorId, int selectedColorId) {
        this.defaultColorId = defaultColorId;
        this.selectedColorId = selectedColorId;
        setColorFilter(createColorStateList(getResources().getColor(defaultColorId), getResources().getColor(selectedColorId)));
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        init();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (tint != null && tint.isStateful()) updateTintColor();
    }

    public void setColorFilter(ColorStateList tint) {
        this.tint = tint;
        super.setColorFilter(tint.getColorForState(getDrawableState(), R.color.light_black));
    }

    private void updateTintColor() {
        int color = tint.getColorForState(getDrawableState(), R.color.light_black);
        super.setColorFilter(color);
    }
}