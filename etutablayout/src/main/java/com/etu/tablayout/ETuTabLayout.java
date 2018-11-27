package com.etu.tablayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etu.tablayout.listener.CommonTabEnity;
import com.etu.tablayout.listener.TabSelectedListener;
import com.etu.tablayout.utils.FragmentChangeManager;
import com.etu.tablayout.utils.MsgUtils;
import com.etu.tablayout.utils.PixelUtils;

import java.util.ArrayList;
import java.util.List;

public class ETuTabLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {

    private List<CommonTabEnity> mListTabEnty = new ArrayList<>();
    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path mTrianglePath = new Path();
    private GradientDrawable mIndicatorDrawable = new GradientDrawable();
    private int mHeight;


    private static final int INDICATOR_STYLE_NORMAL = 0;
    private static final int INDICATOR_STYLE_TRIANGLE = 1;//三角形指示器
    private int mIndicatorStyle = INDICATOR_STYLE_NORMAL;


    private Rect mIndicatorRect = new Rect();

    private IndicatorPoint mCurrentIndicatorPoint = new IndicatorPoint();
    private IndicatorPoint mLastIndicatorPoint = new IndicatorPoint();


    private LinearLayout mLinearContatir;
    private int mTabCount;
    private Context mContext;
    private int mCurrentIndex;
    private boolean mIsSpaceEqual;//默认tab等分
    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;

    private FragmentChangeManager mFragmentChangeManager;
    private int mLastTabView = 0;
    private ValueAnimator mValueAnimator;
    private OvershootInterpolator mInterpolator = new OvershootInterpolator(1.5f);//弹性效果

    private int mTabViewBgColor;

    /**
     * Indicator
     */
    private int mIndicatorColor;
    private float mIndicatorWidth;
    private float mIndicatorHeight;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginBottom;
    private float mIndicatorMarginRight;
    private int mIndicatorAnimDuration;
    private float mIndicatorCornerRadius;
    private int mIndicatorGravity;
    private boolean mIndicatorBounceEnable = false;
    private boolean mIndicatorAnimEnable = false;

    public final int INDICATOR_GRAVITY_TOP = 0;
    public final int INDICATOR_GRAVITY_BOTTOM = 1;

    /**
     * Under Lines
     */
    private int mUnderLineColor;
    private float mUnderLineHeight;
    private int mUnderLineGravity;
    private int mUnderLineWidth;

    public final int UNDER_LINE_TOP = 0;
    public final int UNDER_LINE_BOTTOM = 1;


    /**
     * Title Style
     */
    public final int TEXT_SELECTED_BOLD = 1;
    public final int TEXT_WHEN_SELECTED_BOLD = 2;
    public final int TEXT_WHEN_SELECTED_ITALIC = 3;
    public final int TEXT_SELECTED_Italic = 4;
    public final int TEXT_NORMAL = 5;
    //Divider
    private int mDividerColor;
    private float mDividerWidth;
    private float mDividerPadding;

    /**
     * Tab Title
     */
    private int mTextSelectedColor;
    private float mTextSize;
    private int mTextUnSelectedColor;
    private int mTextStyle;

    /**
     * Icon
     */
    public static final int ICON_GRAVITY_TOP = 0;
    public static final int ICON_GRAVITY_BOTTOM = 1;
    public static final int ICON_GRAVITY_LEFT = 2;
    public static final int ICON_GRAVITY_RIGHT = 3;


    private float mIconWidth;
    private float mIconHeight;
    private int mIconGravity;
    private float mIconMargin;
    private boolean mIconVisible;


    /**
     * Msg View
     */
    private final int MSG_Gravity_RIGHTTOP = 1;//右上
    private final int MSG_Gravity_LEFTTOP = 2;//左上
    private final int MSG_Gravity_RIGHTBOTTOM = 3;//右下
    private final int MSG_Gravity_LEFTBOTTOM = 4;//左下

    private int mMsgViewColor;
    private int mMsgTextColor;
    private int mMsgTextSize;
    private int mMsgTextMaxNumber;
    private int mMsgVieGravity;


    public ETuTabLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public ETuTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);


    }

    public ETuTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs, defStyleAttr);
    }


    /**
     * 初始化配置
     */
    private void initData(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);

        mLinearContatir = new LinearLayout(context);
        addView(mLinearContatir);

        obtainAttributes(mContext, attrs);

        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        //create ViewPager
        if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }

        mValueAnimator = ValueAnimator.ofObject(new PointEvaluator(), mLastIndicatorPoint, mCurrentIndicatorPoint);
        mValueAnimator.addUpdateListener(this);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ETuTabLayout);
//
        mIndicatorStyle = ta.getInt(R.styleable.ETuTabLayout_indicator_style, 0);
        mIndicatorColor = ta.getColor(R.styleable.ETuTabLayout_indicator_color, Color.parseColor("#FFF68F"));
        mIndicatorHeight = ta.getDimension(R.styleable.ETuTabLayout_indicator_height,
                PixelUtils.dp2px(context, 0));
        mIndicatorWidth = ta.getDimension(R.styleable.ETuTabLayout_indicator_width, PixelUtils.dp2px(context, -1));
        mIndicatorCornerRadius = ta.getDimension(R.styleable.ETuTabLayout_indicator_corner_radius, PixelUtils.dp2px(context, 0));
        mIndicatorMarginLeft = ta.getDimension(R.styleable.ETuTabLayout_indicator_margin_left, PixelUtils.dp2px(context, 0));
        mIndicatorMarginTop = ta.getDimension(R.styleable.ETuTabLayout_indicator_margin_top, PixelUtils.dp2px(context, 0));
        mIndicatorMarginRight = ta.getDimension(R.styleable.ETuTabLayout_indicator_margin_right, PixelUtils.dp2px(context, 0));
        mIndicatorMarginBottom = ta.getDimension(R.styleable.ETuTabLayout_indicator_margin_bottom, PixelUtils.dp2px(context, 0));
        mIndicatorAnimEnable = ta.getBoolean(R.styleable.ETuTabLayout_indicator_anim_enable, true);
        mIndicatorBounceEnable = ta.getBoolean(R.styleable.ETuTabLayout_indicator_bounce_enable, true);
        mIndicatorAnimDuration = ta.getInt(R.styleable.ETuTabLayout_indicator_anim_duration, -1);
        mIndicatorGravity = ta.getInt(R.styleable.ETuTabLayout_indicator_gravity, INDICATOR_GRAVITY_BOTTOM);

        mUnderLineColor = ta.getColor(R.styleable.ETuTabLayout_under_line_color, Color.parseColor("#ffffff"));
        mUnderLineHeight = ta.getDimension(R.styleable.ETuTabLayout_under_line_height, PixelUtils.dp2px(context, 0));
        mUnderLineGravity = ta.getInt(R.styleable.ETuTabLayout_under_line_gravity, UNDER_LINE_BOTTOM);
        mUnderLineWidth = ta.getInt(R.styleable.ETuTabLayout_under_line_width, PixelUtils.dp2px(context, 0));

        mDividerColor = ta.getColor(R.styleable.ETuTabLayout_divideer_color, Color.parseColor("#ffffff"));
        mDividerWidth = ta.getDimension(R.styleable.ETuTabLayout_divider_width, PixelUtils.dp2px(context, 0));
        mDividerPadding = ta.getDimension(R.styleable.ETuTabLayout_divider_padding, PixelUtils.dp2px(context, 12));
        mTabViewBgColor = ta.getColor(R.styleable.ETuTabLayout_tab_bg_color, Color.parseColor("#ffffff"));


        mIconVisible = ta.getBoolean(R.styleable.ETuTabLayout_icon_visible, true);
        mIconGravity = ta.getInt(R.styleable.ETuTabLayout_icon_gravity, ICON_GRAVITY_TOP);
        mIconWidth = ta.getDimension(R.styleable.ETuTabLayout_icon_width, PixelUtils.dp2px(context, 0));
        mIconHeight = ta.getDimension(R.styleable.ETuTabLayout_icon_height, PixelUtils.dp2px(context, 0));
        mIconMargin = ta.getDimension(R.styleable.ETuTabLayout_icon_margin, PixelUtils.dp2px(context, 2.5f));

        mTextSize = ta.getDimension(R.styleable.ETuTabLayout_text_size, PixelUtils.dp2px(context, 13f));
        mTextSelectedColor = ta.getColor(R.styleable.ETuTabLayout_text_selected_color, Color.parseColor("#ffffff"));
        mTextUnSelectedColor = ta.getColor(R.styleable.ETuTabLayout_text_unselected_color, Color.parseColor("#AAffffff"));
        mTextStyle = ta.getInt(R.styleable.ETuTabLayout_text_style, TEXT_NORMAL);

        mIsSpaceEqual = ta.getBoolean(R.styleable.ETuTabLayout_space_equal, true);
        mTabWidth = ta.getDimension(R.styleable.ETuTabLayout_tab_width, PixelUtils.dp2px(context, -1));
        mTabPadding = ta.getDimension(R.styleable.ETuTabLayout_tab_padding, mTabSpaceEqual || mTabWidth > 0 ? PixelUtils.dp2px(context, 0) : PixelUtils.dp2px(context, 0));

        ta.recycle();

    }


    public void setIndicatorWidth(float width) {
        this.mIndicatorWidth = width;
        invalidate();
    }

    public void setIndicatorHeight(float height) {
        this.mIndicatorHeight = height;
        invalidate();
    }

    public void setIndicatorAnimDuration(int duration) {
        this.mIndicatorAnimDuration = duration;
    }

    public void setIndicatorColor(int color) {
        this.mIndicatorColor = color;

    }


    public void setIndicatorStyle(int style) {
        this.mIndicatorStyle = style;
        updateTabStyles();
    }

    public void setmIndicatorGravity(int gravity) {
        this.mIndicatorGravity = gravity;
        updateTabStyles();
    }

    public void setTextSelectedColor(int color) {
        this.mTextSelectedColor = color;
    }


    public void setTextUnSelectedColor(int color) {
        this.mTextUnSelectedColor = color;
    }

    public void setTitleSize(float textSize) {
        this.mTextSize = textSize;
    }

    public void setUnderLineColor(int color) {
        this.mUnderLineColor = color;
    }

    public void setUnderLineHeight(int color) {
        this.mUnderLineHeight = color;
    }

    public void setUnderLineGravity(int gravity) {
        this.mUnderLineGravity = gravity;
    }

    public void setIconVisible(boolean isVisible) {
        this.mIconVisible = isVisible;
        updateTabStyles();
    }


    public void setTabData(ArrayList<CommonTabEnity> tabEntitys, FragmentActivity fa, int containerViewId, ArrayList<Fragment> fragments) {
        mFragmentChangeManager = new FragmentChangeManager(fa.getSupportFragmentManager(), containerViewId, fragments);
        setTabData(tabEntitys);
    }

    public void setTabData(List<CommonTabEnity> listData) {
        if (listData == null || listData.size() == 0) {
            throw new IllegalStateException("TabEntitys can not be null or empty !");
        }

        this.mListTabEnty.clear();
        this.mListTabEnty.addAll(listData);
        notifyDataSetChanged();
    }


    public void notifyDataSetChanged() {
        Log.e("hehe", "---notifyDataSetChanged---START-");

        this.mLinearContatir.removeAllViews();
        this.mTabCount = mListTabEnty.size();
        for (int i = 0; i < mTabCount; i++) {
            View tabView = getItemView(mIconGravity);
            tabView.setTag(i);
            addTabItemView(tabView, i);
        }
        updateTabStyles();
        Log.e("hehe", "---notifyDataSetChanged---END-");

    }

    private void addTabItemView(View itemView, int position) {
        TextView tvTitle = itemView.findViewById(R.id.tv_title);
        ImageView ivIcon = itemView.findViewById(R.id.iv_icon);
        tvTitle.setText(mListTabEnty.get(position).getTabTitle());
        ivIcon.setImageResource(mListTabEnty.get(position).getUnSelectedIcon());
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (Integer) v.getTag();
                if (mCurrentIndex != index) {
                    setCurrenTabView(index);
                    if (mListener != null) {
                        mListener.onTabSelected(index);
                    }
                } else {
                    if (mListener != null) {
                        mListener.onTabSelected(index);
                    }
                }
            }
        });
        LinearLayout.LayoutParams mLayoutParams = mIsSpaceEqual ? new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1)
                : new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearContatir.addView(itemView, position, mLayoutParams);
    }

    /**
     * 这块逻辑调整
     *
     * @param index
     */
    public void setCurrenTabView(int index) {
        Log.e("hehe", "---setCurrenTabView---START-");

        mLastTabView = this.mCurrentIndex;
        this.mCurrentIndex = index;
        Log.e("hehe", "LastTabView----- " + mLastTabView);
        Log.e("hehe", "CurrentIndex----- " + mCurrentIndex);

        updateTabSelect(index);
        if (mFragmentChangeManager != null) {
            mFragmentChangeManager.setCurrentFragments(index);
        }
        if (mIndicatorAnimEnable) {
            calcOffset();
        } else {
            invalidate();
        }
        Log.e("hehe", "---setCurrenTabView---END-");

    }

    private void updateTabSelect(int index) {
        for (int i = 0; i < mListTabEnty.size(); i++) {
            View itemView = mLinearContatir.getChildAt(i);
            final boolean isSelected = i == index;
            TextView tabTitle = itemView.findViewById(R.id.tv_title);
            ImageView ivIcon = itemView.findViewById(R.id.iv_icon);
            tabTitle.setTextColor(isSelected ? mTextSelectedColor : mTextUnSelectedColor);
            CommonTabEnity commonTabEnity = mListTabEnty.get(i);
            ivIcon.setImageResource(isSelected ? commonTabEnity.getSeletedIcon() : commonTabEnity.getUnSelectedIcon());
            if (isSelected && mTextStyle == TEXT_WHEN_SELECTED_BOLD) {
                tabTitle.getPaint().setFakeBoldText(isSelected);
            }
        }
    }


    private View getItemView(int gravity) {
        View view = null;
        switch (gravity) {
            case ICON_GRAVITY_RIGHT:
                view = inflate(mContext, R.layout.ietm_right_view, null);
                break;
            case ICON_GRAVITY_LEFT:
                view = inflate(mContext, R.layout.ietm_left_view, null);
                break;
            case ICON_GRAVITY_TOP:
                view = inflate(mContext, R.layout.ietm_top_view, null);
                break;
            case ICON_GRAVITY_BOTTOM:
                view = inflate(mContext, R.layout.ietm_bottom_view, null);
                break;
        }
//        view.setBackgroundColor(mTabViewBgColor);
        return view;
    }


    private TabSelectedListener mListener;


    public void setOnTabSelectedListener(TabSelectedListener listener) {
        this.mListener = listener;
    }

    private boolean mIsFirstDraw = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTabCount < 0 || isInEditMode()) {
            return;
        }
        int height = getHeight();
        int paddingLeft = getPaddingLeft();

        if (mUnderLineHeight > 0) {
            mRectPaint.setColor(mUnderLineColor);
            if (mUnderLineGravity == UNDER_LINE_BOTTOM) {
                canvas.drawRect(paddingLeft, height - mUnderLineHeight, mLinearContatir.getWidth() + paddingLeft, height, mRectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0, mLinearContatir.getWidth() + paddingLeft, mUnderLineHeight, mRectPaint);
            }
        }
        if (mDividerWidth > 0) {
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mListTabEnty.size()-1; i++) {
                View tabView = mLinearContatir.getChildAt(i);
                canvas.drawRect(tabView.getRight(), mDividerPadding, mDividerWidth + tabView.getRight(), height - mDividerPadding, mDividerPaint);
                Log.e("hehe", "mDividerPadding  " + mDividerPadding);
                Log.e("hehe", "height - mDividerPadding  " +(height - mDividerPadding));

//                canvas.drawLine(paddingLeft+tabView.getRight(), mDividerPadding, paddingLeft + tabView.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }
        calcIndicatorRect();
        //三角形指示器
        if (mIndicatorStyle == INDICATOR_STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.setColor(mIndicatorColor);
                mTrianglePath.reset();
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2, height - mIndicatorHeight);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                mTrianglePath.close();
                canvas.drawPath(mTrianglePath, mTrianglePaint);
            }
        } else {
               /* mRectPaint.setColor(mIndicatorColor);
                calcIndicatorRect();
                canvas.drawRect(getPaddingLeft() + mIndicatorRect.left, getHeight() - mIndicatorHeight,
                        mIndicatorRect.right + getPaddingLeft(), getHeight(), mRectPaint);*/
            if (mIndicatorHeight > 0) {
                mIndicatorDrawable.setColor(mIndicatorColor);
                if (mIndicatorGravity == INDICATOR_GRAVITY_BOTTOM) {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            height - (int) mIndicatorHeight - (int) mIndicatorMarginBottom,
                            paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            height - (int) mIndicatorMarginBottom);
                } else {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            (int) mIndicatorMarginTop,
                            paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            (int) mIndicatorHeight + (int) mIndicatorMarginTop);
                }
                mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);
            }
        }
    }

    private void calcOffset() {
        final View currentTabView = mLinearContatir.getChildAt(this.mCurrentIndex);
        mCurrentIndicatorPoint.setLeft(currentTabView.getLeft());
        mCurrentIndicatorPoint.setRight(currentTabView.getRight());

        final View lastTabView = mLinearContatir.getChildAt(this.mLastTabView);
        mLastIndicatorPoint.setLeft(lastTabView.getLeft());
        mLastIndicatorPoint.setRight(lastTabView.getRight());

        if (mLastIndicatorPoint.getLeft() == mCurrentIndicatorPoint.getLeft() && mLastIndicatorPoint.getRight() == mCurrentIndicatorPoint.getRight()) {
            invalidate();
        } else {
            mValueAnimator.setObjectValues(mLastIndicatorPoint, mCurrentIndicatorPoint);
            if (mIndicatorBounceEnable) {
                mValueAnimator.setInterpolator(mInterpolator);
            }

            if (mIndicatorAnimDuration < 0) {
                mIndicatorAnimDuration = mIndicatorBounceEnable ? 500 : 250;
            }
            mValueAnimator.setDuration(mIndicatorAnimDuration);
            mValueAnimator.start();
        }
    }

    private void calcIndicatorRect() {

        View currentTabView = mLinearContatir.getChildAt(this.mCurrentIndex);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();
        Log.e("hehe", "calcIndicatorRect----  " + currentTabView.getWidth());
        Log.e("hehe", "calcIndicatorRect----  " + currentTabView.getMeasuredWidth());

        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;

        if (mIndicatorWidth < 0) { //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip

        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - mIndicatorWidth) / 2;

            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);

        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        View currentTabView = mLinearContatir.getChildAt(this.mCurrentIndex);
        IndicatorPoint p = (IndicatorPoint) animation.getAnimatedValue();
        mIndicatorRect.left = (int) p.getLeft();
        mIndicatorRect.right = (int) p.getRight();

        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip

        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = p.getLeft() + (currentTabView.getWidth() - mIndicatorWidth) / 2;
            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
        }
        invalidate();
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentIndex);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentIndex = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (mCurrentIndex != 0 && mLinearContatir.getChildCount() > 0) {
                updateTabSelect(mCurrentIndex);
            }
        }
        super.onRestoreInstanceState(state);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View tabView = mLinearContatir.getChildAt(i);
            tabView.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
            TextView tvTitle = (TextView) tabView.findViewById(R.id.tv_title);
            tvTitle.setTextColor(i == mCurrentIndex ? mTextSelectedColor : mTextUnSelectedColor);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

            if (mTextStyle == TEXT_WHEN_SELECTED_BOLD) {
                tvTitle.getPaint().setFakeBoldText(true);
            } else if (mTextStyle == TEXT_NORMAL) {
                tvTitle.getPaint().setFakeBoldText(false);
            }

            ImageView ivIcon = tabView.findViewById(R.id.iv_icon);
            if (mIconVisible) {
                ivIcon.setVisibility(View.VISIBLE);
                CommonTabEnity tabEntity = mListTabEnty.get(i);
                ivIcon.setImageResource(i == mCurrentIndex ? tabEntity.getSeletedIcon() : tabEntity.getUnSelectedIcon());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        mIconWidth <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconWidth,
                        mIconHeight <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconHeight);
                if (mIconGravity == ICON_GRAVITY_LEFT) {
                    lp.rightMargin = (int) mIconMargin;
                } else if (mIconGravity == ICON_GRAVITY_RIGHT) {
                    lp.leftMargin = (int) mIconMargin;
                } else if (mIconGravity == ICON_GRAVITY_BOTTOM) {
                    lp.topMargin = (int) mIconMargin;
                } else {
                    lp.bottomMargin = (int) mIconMargin;
                }

                ivIcon.setLayoutParams(lp);
            } else {
                ivIcon.setVisibility(View.GONE);
            }
        }
    }

    public void showMsgView(int index, int number) {
        View itemView = mLinearContatir.getChildAt(index);
        MessageView msgView = itemView.findViewById(R.id.msg_view);
        MsgUtils.showMessageView(msgView, number);

    }


    public void showCurrenrMsgView(int number) {
        View itemView = mLinearContatir.getChildAt(mCurrentIndex);
        MessageView msgView = itemView.findViewById(R.id.msg_view);
        MsgUtils.showMessageView(msgView, number);
    }

    /**
     * 显示小红点
     */
    public void showRedDot(int position) {
        View itemView = mLinearContatir.getChildAt(position);
        MessageView msgView = itemView.findViewById(R.id.msg_view);
        MsgUtils.showRedDot(msgView);
    }

    /**
     * 显示当前小红点
     */
    public void showCurrentRedDot() {
        View itemView = mLinearContatir.getChildAt(mCurrentIndex);
        MessageView msgView = itemView.findViewById(R.id.msg_view);
        MsgUtils.showRedDot(msgView);
    }

    /**
     * 获取最大宽度
     *
     * @return
     */
    private float getMaxItemWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        float width = dm.widthPixels / 4;
        return width;
    }
}
