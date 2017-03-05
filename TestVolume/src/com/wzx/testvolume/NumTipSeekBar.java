package com.wzx.testvolume;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.math.BigDecimal;

import com.example.testvolume.R;

/**
 * author tc date 2016/4/30 9:30 version 1.0.0 description
 * 进度条view,按钮带数字提示的seekbar，设置isEnabled=false，可以禁用触摸设置进度的功能 modify by
 */
public class NumTipSeekBar extends View {
    private static final String TAG = NumTipSeekBar.class.getSimpleName();
    private RectF mTickBarRecf;

    /**
     * 默认的刻度条paint
     */
    private Paint mTickBarPaint;
    /**
     * 默认刻度条的高度
     */
    private float mTickBarHeight;
    /**
     * 默认刻度条的颜色
     */
    private int mTickBarColor;
    // ------------------
    /**
     * 圆形按钮paint
     */
    private Paint mCircleButtonPaint;
    /**
     * 圆形按钮颜色
     */
    private int mCircleButtonColor;
    /**
     * 圆形按钮文本颜色
     */
    private int mCircleButtonTextColor;
    /**
     * 圆形按钮文本大小
     */
    private float mCircleButtonTextSize;
    /**
     * 圆形按钮的半径
     */
    private float mCircleButtonRadius;
    /**
     * 圆形按钮的recf，矩形区域
     */
    private RectF mCircleRecf;
    /**
     * 圆形按钮button文本绘制paint
     */
    private Paint mCircleButtonTextPaint;

    // ----------------------
    /**
     * 进度条高度大小
     */
    private float mProgressHeight;
    /**
     * 进度条paint
     */
    private Paint mProgressPaint;
    /**
     * 进度颜色
     */
    private int mProgressColor;

    /**
     * 进度条的recf，矩形区域
     */
    private RectF mProgressRecf;
    /**
     * 选中的进度值
     */
    private int mSelectProgress;
    /**
     * 进度最大值
     */
    private int mMaxProgress = DEFAULT_MAX_VALUE;
    /**
     * 默认的最大值
     */
    private static final int DEFAULT_MAX_VALUE = 10;

    /**
     * view的总进度宽度，除去paddingtop以及bottom
     */
    private int mViewWidth;
    /**
     * view的总进度高度，除去paddingtop以及bottom
     */
    private int mViewHeight;
    /**
     * 圆形button的圆心坐标，也是progress进度条的最右边的的坐标
     */
    private float mCirclePotionX;
    /**
     * 是否显示圆形按钮的文本
     */
    private boolean mIsShowButtonText;
    /**
     * 是否显示圆形按钮
     */
    private boolean mIsShowButton;
    /**
     * 是否显示圆角
     */
    private boolean mIsRound;
    /**
     * 起始的进度值，比如从1开始显示
     */
    private int mStartProgress;
    /**
     * 圆形按钮外边的光圈
     */
    private int mCircleApertureWidth;
    /**
     * 圆形按钮光圈的颜色
     */
    private int mCircleApertureColor;
    /**
     * 圆形按钮光圈的paint
     */
    private Paint mCircleAperturePaint;
    /**
     * 是否显示圆形光圈
     */
    private boolean mIsShowCircleAperture;

    /**
     * 监听进度条变化
     */
    public interface OnProgressChangeListener {
        void onChange(int selectProgress);
    }

    /**
     * 监听进度条变化
     */
    private OnProgressChangeListener mOnProgressChangeListener;

    /**
     * 设置进度条变化的监听器，当触摸停止时触发
     * 
     * @param onProgressChangeListener
     *            进度条变化的监听器
     */
    public void setOnProgressChangeListener(
            OnProgressChangeListener onProgressChangeListener) {
        mOnProgressChangeListener = onProgressChangeListener;
    }

    public NumTipSeekBar(Context context) {
        this(context, null);
    }

    public NumTipSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumTipSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化view的属性
     * 
     * @param context
     *            上下文
     * @param attrs
     *            attr属性
     */
    private void init(Context context, AttributeSet attrs) {

        TypedArray attr = context.obtainStyledAttributes(attrs,
                R.styleable.NumTipSeekBar);
        mTickBarHeight = attr.getDimensionPixelOffset(
                R.styleable.NumTipSeekBar_tickBarHeight, getDpValue(8));
        mTickBarColor = attr.getColor(R.styleable.NumTipSeekBar_tickBarColor,
                getResources().getColor(R.color.orange_f6));
        Log.e("WZX","mTickBarColor = "+ mTickBarColor);
        mCircleButtonColor = attr.getColor(
                R.styleable.NumTipSeekBar_circleButtonColor, getResources()
                        .getColor(R.color.white));
        mCircleButtonTextColor = attr.getColor(
                R.styleable.NumTipSeekBar_circleButtonTextColor, getResources()
                        .getColor(R.color.purple_82));
        Log.e("WZX","mCircleButtonTextColor = "+ mCircleButtonTextColor);
        mCircleButtonTextSize = attr.getDimension(
                R.styleable.NumTipSeekBar_circleButtonTextSize, getDpValue(16));
        mCircleButtonRadius = attr.getDimensionPixelOffset(
                R.styleable.NumTipSeekBar_circleButtonRadius, getDpValue(16));
        mCircleApertureWidth = attr.getDimensionPixelOffset(
                R.styleable.NumTipSeekBar_circleApertureWidth, getDpValue(0));
        mCircleApertureColor = attr.getColor(
                R.styleable.NumTipSeekBar_circleApertureColor, getResources()
                        .getColor(R.color.white_1a));
        Log.e("WZX","mCircleApertureColor = "+ mCircleApertureColor);
        mProgressHeight = attr.getDimensionPixelOffset(
                R.styleable.NumTipSeekBar_progressHeight, getDpValue(20));
        mProgressColor = attr.getColor(R.styleable.NumTipSeekBar_progressColor,
                getResources().getColor(R.color.white));
        Log.e("WZX","mProgressColor = "+ mProgressColor);
        mSelectProgress = attr.getInt(R.styleable.NumTipSeekBar_selectProgress,
                0);
        mStartProgress = attr
                .getInt(R.styleable.NumTipSeekBar_startProgress, 0);
        mMaxProgress = attr.getInt(R.styleable.NumTipSeekBar_maxProgress, 10);
        mIsShowButtonText = attr.getBoolean(
                R.styleable.NumTipSeekBar_isShowButtonText, true);
        mIsShowButton = attr.getBoolean(R.styleable.NumTipSeekBar_isShowButton,
                true);
        mIsRound = attr.getBoolean(R.styleable.NumTipSeekBar_isRound, true);
        initView();

        attr.recycle();

    }

    private void initView() {
        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setAntiAlias(true);

        mCircleButtonPaint = new Paint();
        mCircleButtonPaint.setColor(mCircleButtonColor);
        mCircleButtonPaint.setStyle(Paint.Style.FILL);
        mCircleButtonPaint.setAntiAlias(true);

        mCircleAperturePaint = new Paint();
        mCircleAperturePaint.setColor(mCircleApertureColor);
        mCircleAperturePaint.setStyle(Paint.Style.FILL);
        mCircleAperturePaint.setAntiAlias(true);

        mCircleButtonTextPaint = new Paint();
        mCircleButtonTextPaint.setTextAlign(Paint.Align.CENTER);
        mCircleButtonTextPaint.setColor(mCircleButtonTextColor);
        mCircleButtonTextPaint.setStyle(Paint.Style.FILL);
        mCircleButtonTextPaint.setTextSize(mCircleButtonTextSize);
        mCircleButtonTextPaint.setAntiAlias(true);

        mTickBarPaint = new Paint();
        mTickBarPaint.setColor(mTickBarColor);
        mTickBarPaint.setStyle(Paint.Style.FILL);
        mTickBarPaint.setAntiAlias(true);

        mTickBarRecf = new RectF();
        mProgressRecf = new RectF();
        mCircleRecf = new RectF();
        setCircleApertureWidth(mCircleApertureWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            // 如果设置不可用，则禁用触摸设置进度
            return false;
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        float x = event.getX();
        float y = event.getY();
        // Log.i(TAG, "onTouchEvent: x：" + x);
        switch (event.getAction()) {
        case MotionEvent.ACTION_MOVE:
            judgePosition(x);
            return true;
        case MotionEvent.ACTION_DOWN:
            judgePosition(x);
            return true;
        case MotionEvent.ACTION_UP:
            if (mOnProgressChangeListener != null) {
                Log.i(TAG, "onTouchEvent: 触摸结束，通知监听器-mSelectProgress："
                        + mSelectProgress);
                mOnProgressChangeListener.onChange(mSelectProgress);
            }
            return true;
        default:
            break;
        }

        return super.onTouchEvent(event);
    }

    private void judgePosition(float x) {
        float end = getPaddingLeft() + mViewWidth;
        float start = getPaddingLeft();
        int progress = mSelectProgress;
        // Log.i(TAG, "judgePosition: x-start：" + (x - start));
        // Log.i(TAG, "judgePosition: start:" + start + "  end:" + end +
        // "  mMaxProgress:" +
        // mMaxProgress);
        if (x >= start) {
            double result = (x - start) / mViewWidth * (float) mMaxProgress;
            BigDecimal bigDecimal = new BigDecimal(result).setScale(0,
                    BigDecimal.ROUND_HALF_UP);
            // Log.i(TAG, "judgePosition: progress:" + bigDecimal.intValue() +
            // "  result:" + result
            // + "  (x - start) / end :" + (x - start) / end);
            progress = bigDecimal.intValue();
            if (progress > mMaxProgress) {
                // Log.i(TAG, "judgePosition:x > end  超出坐标范围:");
                progress = mMaxProgress;
            }
        } else if (x < start) {
            // Log.i(TAG, "judgePosition: x < start 超出坐标范围:");
            progress = 0;
        }
        if (progress != mSelectProgress) {
            // 发生变化才通知view重新绘制
            setSelectProgress(progress, false);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        initValues(width, height);
        if (mIsRound) {
            canvas.drawRoundRect(mTickBarRecf, mProgressHeight / 2,
                    mProgressHeight / 2, mTickBarPaint);
            canvas.drawRoundRect(mProgressRecf, mProgressHeight / 2,
                    mProgressHeight / 2, mProgressPaint);
        } else {
            canvas.drawRect(mTickBarRecf, mTickBarPaint);
            canvas.drawRect(mProgressRecf, mProgressPaint);
        }
        if (mIsShowButton) {
            if (mIsShowCircleAperture) {
                if (mViewHeight / 2 < mCircleButtonRadius
                        + mCircleApertureWidth) {
                    mCircleApertureWidth = (int) (mViewHeight / 2 - mCircleButtonRadius);
                }
                canvas.drawCircle(mCirclePotionX, mViewHeight / 2,
                        mCircleButtonRadius + mCircleApertureWidth,
                        mCircleAperturePaint);
            }
            
            mCircleButtonPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));//显示上层绘制图片
            canvas.drawCircle(mCirclePotionX, mViewHeight / 2,
                    mCircleButtonRadius+4, mCircleButtonPaint);
            mCircleButtonPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));//显示下层绘制图片
            canvas.drawCircle(mCirclePotionX, mViewHeight / 2,
                    mCircleButtonRadius, mCircleButtonPaint);

        }
        if (mIsShowButtonText) {
        	
            Paint.FontMetricsInt fontMetrics = mCircleButtonTextPaint
                    .getFontMetricsInt();
            int baseline = (int) ((mCircleRecf.bottom + mCircleRecf.top
                    - fontMetrics.bottom - fontMetrics.top) / 2);
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
            canvas.drawText(String.valueOf(mSelectProgress),
                    mCircleRecf.centerX(), baseline, mCircleButtonTextPaint);

        }
    }

    private void initValues(int width, int height) {
        mViewWidth = width - getPaddingRight() - getPaddingLeft();
        mViewHeight = height;
        mCirclePotionX = (float) (mSelectProgress - mStartProgress)
                / (mMaxProgress - mStartProgress) * mViewWidth
                + getPaddingLeft();
        // Log.i(TAG, "initValues: mViewWidth=" + mViewWidth + "  mViewHeight="
        // + mViewHeight + "
        // mCirclePotionX=" + mCirclePotionX + "
        // mSelectProgress=" + mSelectProgress + " mMaxProgress="
        // + mMaxProgress + " getPaddingLeft()=" + getPaddingLeft());
        if (mTickBarHeight > mViewHeight) {
            // 如果刻度条的高度大于view本身的高度的1/2，则显示不完整，所以处理下。
            mTickBarHeight = mViewHeight;
        }
        mTickBarRecf.set(getPaddingLeft(), (mViewHeight - mTickBarHeight) / 2,
                mViewWidth + getPaddingLeft(), mTickBarHeight / 2 + mViewHeight
                        / 2);
        if (mProgressHeight > mViewHeight) {
            // 如果刻度条的高度大于view本身的高度的1/2，则显示不完整，所以处理下。
            mProgressHeight = mViewHeight;
        }

        mProgressRecf.set(getPaddingLeft(),
                (mViewHeight - mProgressHeight) / 2, mCirclePotionX,
                mProgressHeight / 2 + mViewHeight / 2);

        if (mCircleButtonRadius > mViewHeight / 2) {
            // 如果圆形按钮的半径大于view本身的高度的1/2，则显示不完整，所以处理下。
            mCircleButtonRadius = mViewHeight / 2;
        }
        mCircleRecf.set(mCirclePotionX - mCircleButtonRadius, mViewHeight / 2
                - mCircleButtonRadius / 2,
                mCirclePotionX + mCircleButtonRadius, mViewHeight / 2
                        + mCircleButtonRadius / 2);
    }

    /**
     * seekbar背后的刻度条高度
     * 
     * @return seekbar背后的刻度条高度
     */
    public float getTickBarHeight() {
        return mTickBarHeight;
    }

    /**
     * 设置seekbar背后的刻度条高度
     * 
     * @param tickBarHeight
     *            seekbar背后的刻度条高度
     */
    public void setTickBarHeight(float tickBarHeight) {
        mTickBarHeight = tickBarHeight;
    }

    /**
     * seekbar背后的刻度条颜色
     * 
     * @return seekbar背后的刻度条颜色
     */
    public int getTickBarColor() {
        return mTickBarColor;
    }

    /**
     * 设置seekbar背后的刻度条颜色
     * 
     * @param tickBarColor
     *            seekbar背后的刻度条颜色
     */
    public void setTickBarColor(int tickBarColor) {
        mTickBarColor = tickBarColor;
        mTickBarPaint.setColor(mTickBarColor);
    }

    /**
     * 圆形按钮颜色
     * 
     * @return 圆形按钮颜色
     */
    public int getCircleButtonColor() {
        return mCircleButtonColor;
    }

    /**
     * 设置圆形按钮颜色
     * 
     * @param circleButtonColor
     *            圆形按钮颜色
     */
    public void setCircleButtonColor(int circleButtonColor) {
        mCircleButtonColor = circleButtonColor;
        mCircleButtonPaint.setColor(mCircleButtonColor);
    }

    /**
     * 圆形按钮文本颜色
     * 
     * @return 圆形按钮文本颜色
     */
    public int getCircleButtonTextColor() {
        return mCircleButtonTextColor;
    }

    /**
     * 设置圆形按钮文本颜色
     * 
     * @param circleButtonTextColor
     *            圆形按钮文本颜色
     */
    public void setCircleButtonTextColor(int circleButtonTextColor) {
        mCircleButtonTextColor = circleButtonTextColor;
        mCircleButtonTextPaint.setColor(mCircleButtonTextColor);
    }

    /**
     * 圆形按钮文本字体大小
     * 
     * @return 圆形按钮文本字体大小
     */
    public float getCircleButtonTextSize() {
        return mCircleButtonTextSize;
    }

    /**
     * 设置圆形按钮文本字体大小
     * 
     * @param circleButtonTextSize
     *            圆形按钮文本字体大小
     */
    public void setCircleButtonTextSize(float circleButtonTextSize) {
        mCircleButtonTextSize = circleButtonTextSize;
        mCircleButtonTextPaint.setTextSize(mCircleButtonTextSize);
    }

    /**
     * 圆形按钮半径
     * 
     * @return 圆形按钮半径
     */
    public float getCircleButtonRadius() {
        return mCircleButtonRadius;
    }

    /**
     * 设置圆形按钮半径
     * 
     * @param circleButtonRadius
     *            圆形按钮半径
     */
    public void setCircleButtonRadius(float circleButtonRadius) {
        mCircleButtonRadius = circleButtonRadius;
    }

    /**
     * 进度条高度
     * 
     * @return 进度条高度
     */
    public float getProgressHeight() {
        return mProgressHeight;
    }

    /**
     * 设置进度条高度
     * 
     * @param progressHeight
     *            进度条高度
     */
    public void setProgressHeight(float progressHeight) {
        mProgressHeight = progressHeight;
    }

    /**
     * 进度条颜色
     * 
     * @return 进度条颜色
     */
    public int getProgressColor() {
        return mProgressColor;
    }

    /**
     * 设置进度条颜色
     * 
     * @param progressColor
     *            进度条颜色
     */
    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
        mProgressPaint.setColor(mProgressColor);
    }

    /**
     * 最大进度条的值
     * 
     * @return 最大进度条的值
     */
    public int getMaxProgress() {
        return mMaxProgress;
    }

    /**
     * 设置最大进度条的值
     * 
     * @param maxProgress
     *            最大进度条的值
     */
    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
    }

    /**
     * 设置当前选中的值
     * 
     * @param selectProgress
     *            进度
     */
    public void setSelectProgress(int selectProgress) {
        this.setSelectProgress(selectProgress, true);
    }

    /**
     * 设置当前选中的值
     * 
     * @param selectProgress
     *            进度
     * @param isNotifyListener
     *            是否通知progresschangelistener
     */
    public void setSelectProgress(int selectProgress, boolean isNotifyListener) {
        getSelectProgressValue(selectProgress);
        Log.i(TAG, "mSelectProgress: " + mSelectProgress + "  mMaxProgress: "
                + mMaxProgress);
        if (mOnProgressChangeListener != null && isNotifyListener) {
            mOnProgressChangeListener.onChange(mSelectProgress);
        }
        invalidate();
    }

    /**
     * 计算当前选中的进度条的值
     * 
     * @param selectProgress
     *            进度
     */
    private void getSelectProgressValue(int selectProgress) {
        mSelectProgress = selectProgress;
        if (mSelectProgress > mMaxProgress) {
            mSelectProgress = mMaxProgress;
        } else if (mSelectProgress <= mStartProgress) {
            mSelectProgress = mStartProgress;
        }
    }

    /**
     * 获取当前的选中值
     * 
     * @return 当前的选中值
     */
    public int getSelectProgress() {
        return mSelectProgress;
    }

    /**
     * 起始的刻度值
     * 
     * @return 起始的刻度值
     */
    public int getStartProgress() {
        return mStartProgress;
    }

    /**
     * 设置起始刻度值
     * 
     * @param startProgress
     *            起始刻度值
     */
    public void setStartProgress(int startProgress) {
        mStartProgress = startProgress;
    }

    /**
     * 设置圆形光圈的宽度，即圆形按钮之外的延伸宽度.超出view高度，自动适配最大值。
     * 
     * @param circleApertureWidth
     *            宽度,dp
     */
    public void setCircleApertureWidth(int circleApertureWidth) {
        mCircleApertureWidth = getDpValue(circleApertureWidth);
        mIsShowCircleAperture = circleApertureWidth > 0;
    }

    /**
     * 圆形光圈颜色
     * 
     * @param circleApertureColor
     *            圆形光圈颜色
     */
    public void setCircleApertureColor(int circleApertureColor) {
        mCircleApertureColor = circleApertureColor;
        mCircleAperturePaint.setColor(mCircleApertureColor);
    }

    /**
     * 获取dp对应的px值
     * 
     * @param value
     *            要转换的值
     * @return dp对应的px值
     */
    private int getDpValue(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, getContext().getResources().getDisplayMetrics());
    }
}
