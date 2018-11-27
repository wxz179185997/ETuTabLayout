package com.etu.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.etu.tablayout.utils.PixelUtils;

public class MessageView extends TextView {
    private Context mContext;
    private GradientDrawable gd_background = new GradientDrawable();
    private int backgroundColor;
    private int cornerRadius;
    private int strokeWidth;
    private int strokeColor;
    private int maxNumber;

    private boolean isRadiusHalfHeight;
    private boolean isWidthHeightEqual;

    public MessageView(Context context) {
        this(context, null);
    }

    public MessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        obtainAttributes(context, attrs);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MessageView);
        backgroundColor = ta.getColor(R.styleable.MessageView_bg_color, Color.TRANSPARENT);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.MessageView_corner_radius, 0);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.MessageView_stroke_width, 0);
        strokeColor = ta.getColor(R.styleable.MessageView_stroke_color, Color.TRANSPARENT);
        isRadiusHalfHeight = ta.getBoolean(R.styleable.MessageView_is_radius_halfheight, false);
        isWidthHeightEqual = ta.getBoolean(R.styleable.MessageView_is_width_heightEqual, false);
        maxNumber = ta.getInt(R.styleable.ETuTabLayout_msg_text_max_number, 99);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isWidthHeightEqual() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isRadiusHalfHeight()) {
            setCornerRadius(getHeight() / 2);
        } else {
            setBgSelector();
        }
    }


    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBgSelector();
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = PixelUtils.dp2px(mContext, cornerRadius);
        setBgSelector();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = PixelUtils.dp2px(mContext, strokeWidth);
        setBgSelector();
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        setBgSelector();
    }

    public void setIsRadiusHalfHeight(boolean isRadiusHalfHeight) {
        this.isRadiusHalfHeight = isRadiusHalfHeight;
        setBgSelector();
    }

    public void setIsWidthHeightEqual(boolean isWidthHeightEqual) {
        this.isWidthHeightEqual = isWidthHeightEqual;
        setBgSelector();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public boolean isRadiusHalfHeight() {
        return isRadiusHalfHeight;
    }

    public boolean isWidthHeightEqual() {
        return isWidthHeightEqual;
    }

    private void setDrawable(GradientDrawable gd, int color, int strokeColor) {
        gd.setColor(color);
        gd.setCornerRadius(cornerRadius);
        gd.setStroke(strokeWidth, strokeColor);
    }

    public void setBgSelector() {
        StateListDrawable bg = new StateListDrawable();

        setDrawable(gd_background, backgroundColor, strokeColor);
        bg.addState(new int[]{-android.R.attr.state_pressed}, gd_background);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
            setBackground(bg);
        } else {
            //noinspection deprecation
            setBackgroundDrawable(bg);
        }
    }


    public static class Builder {
        private Context context;
        private int bgColor;
        private int cornerRadius;
        private int strokeWidth;
        private int strokeColor;
        private boolean isRadiusHalfHeight;
        private boolean isWidthHeightEqual;


        public Builder setBgColor(int color) {
            this.bgColor = color;
            return this;
        }


        public Builder setConerRadius(int radius) {
            this.cornerRadius = radius;
            return this;
        }

        public Builder setStrokeWidth(int width) {
            this.strokeWidth = width;
            return this;
        }

        public Builder setStokeColor(int color) {
            this.strokeColor = color;
            return this;
        }

        public Builder setIsRadiusHalfHeight(boolean type) {
            this.isRadiusHalfHeight = type;
            return this;
        }

        public Builder setIsWidthHeightEqual(boolean type) {
            this.isWidthHeightEqual = type;
            return this;
        }

        public Builder(Context context) {
            this.context = context;

        }

    }


}
