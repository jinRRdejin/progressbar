package com.wzx.testvolume;

import java.math.BigDecimal;

import com.example.testvolume.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

//import com.android.internal.R;

public class NumSeekBar extends View {
    private static final String TAG = "NumSeekBar";
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

    Bitmap mTempBitmap;
    Canvas mTempCanvas;
    private Canvas mMuteCanvas;
    Bitmap mTempCanvas2;
    Bitmap mMuteTemp;
    Bitmap muteIcon;

    private boolean isMute = false;;

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

    public NumSeekBar(Context context) {
        this(context, null);
    }

    public NumSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
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
        mTickBarHeight = 4;
        mTickBarColor = 0x77ffffff;
        mCircleButtonColor = 0xffffffff;
        mCircleButtonTextColor = 0xff000000;
        mCircleButtonTextSize = 16;
        mCircleButtonRadius = 12;
        mProgressHeight = 4;
        mProgressColor = 0xffffffff;
        mSelectProgress = 0;
        mStartProgress = 0;
        mMaxProgress = 100;
        mIsShowButtonText = true;
        mIsShowButton = true;
        mIsRound = false;
        muteIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_51_mute_rc_n_18x18);
        muteIcon = Bitmap.createScaledBitmap(muteIcon, 18, 18, true);
        initView();

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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        float x = event.getX();
        float y = event.getY();
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

        if (x >= start) {
            double result = (x - start) / mViewWidth * (float) mMaxProgress;
            BigDecimal bigDecimal = new BigDecimal(result).setScale(0,
                    BigDecimal.ROUND_HALF_UP);

            progress = bigDecimal.intValue();
            if (progress > mMaxProgress) {
                progress = mMaxProgress;
            }
        } else if (x < start) {
            progress = 0;
        }
        if (progress != mSelectProgress) {
            setSelectProgress(progress, false);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTempBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);

        mTempCanvas = new Canvas(mTempBitmap);

        mMuteTemp = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);

        mMuteCanvas = new Canvas(mMuteTemp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mTempCanvas.drawColor(0xffffff, PorterDuff.Mode.CLEAR);

        int width = getWidth();
        int height = getHeight()+10;
        initValues(width, height);
        if (mIsRound) {
            mTempCanvas.drawRoundRect(mTickBarRecf, mProgressHeight / 2 ,
                    mProgressHeight / 2 , mTickBarPaint);
            mTempCanvas.drawRoundRect(mProgressRecf, mProgressHeight / 2 ,
                    mProgressHeight / 2 , mProgressPaint);
        } else {
            mTempCanvas.drawRect(mTickBarRecf, mTickBarPaint);
            mTempCanvas.drawRect(mProgressRecf, mProgressPaint);
        }
		if (mIsShowButton) {

			mCircleButtonPaint.setXfermode(new PorterDuffXfermode(
					PorterDuff.Mode.CLEAR));
			mTempCanvas.drawCircle(mCirclePotionX, mViewHeight / 2,
					mCircleButtonRadius + 4, mCircleButtonPaint);
			if (isMute) {
				// mCircleButtonPaint.setColor(mTickBarColor);
				mTempCanvas2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.volume_badge_adjustment_normal);
			} else {
				// mCircleButtonPaint.setColor(mCircleButtonColor);
				mTempCanvas2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.volume_badge_adjustment_activated);
			}

			mCircleButtonPaint.setXfermode(new PorterDuffXfermode(
					PorterDuff.Mode.ADD));
			// mTempCanvas.drawCircle(mCirclePotionX, mViewHeight / 2,
			// mCircleButtonRadius, mCircleButtonPaint);
			mTempCanvas2 = Bitmap
					.createScaledBitmap(mTempCanvas2, 30, 24, true);
			mTempCanvas.drawBitmap(mTempCanvas2, mCirclePotionX - 15,
					mViewHeight / 2 - 12, null);

        }
        if (mIsShowButtonText) {
            Paint.FontMetricsInt fontMetrics = mCircleButtonTextPaint
                    .getFontMetricsInt();
            baseline = (int) ((mCircleRecf.bottom + mCircleRecf.top
                    - fontMetrics.bottom - fontMetrics.top) / 2);
            mTempCanvas.drawText(String.valueOf(mSelectProgress),
                    mCircleRecf.centerX(), baseline, mCircleButtonTextPaint);

        }
        canvas.drawBitmap(mMuteTemp, 0, 0, null);
        canvas.drawBitmap(mTempBitmap, 0, 0, null);
    }

    int baseline;

    private void initValues(int width, int height) {
        mViewWidth = width - getPaddingRight() - getPaddingLeft();
        mViewHeight = height;
        mCirclePotionX = (float) (mSelectProgress - mStartProgress)
                / (mMaxProgress - mStartProgress) * mViewWidth
                + getPaddingLeft();

        if (mTickBarHeight > mViewHeight) {
            mTickBarHeight = mViewHeight;
        }
        mTickBarRecf.set(getPaddingLeft(), (mViewHeight - mTickBarHeight) / 2,
                mViewWidth + getPaddingLeft(), mTickBarHeight / 2 + mViewHeight
                        / 2);
        if (mProgressHeight > mViewHeight) {
            mProgressHeight = mViewHeight;
        }

        mProgressRecf.set(getPaddingLeft(),
                (mViewHeight - mProgressHeight) / 2, mCirclePotionX,
                mProgressHeight / 2 + mViewHeight / 2);

        if (mCircleButtonRadius > mViewHeight / 2) {
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
    public int getMax() {
        return mMaxProgress;
    }

    /**
     * 设置最大进度条的值
     * 
     * @param maxProgress
     *            最大进度条的值
     */
    public void setMax(int maxProgress) {
        mMaxProgress = maxProgress;
    }

    /**
     * 设置当前选中的值
     * 
     * @param selectProgress
     *            进度
     */
    public void setProgress(int selectProgress) {
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

    public void setMute(boolean mute) {
        isMute = mute;
        invalidate();
        drawView();
    }

    public float getProgressButtonX() {
        return mCirclePotionX;
    }

    public void drawView() {

        if (mMuteCanvas != null) {
            mMuteCanvas.drawColor(0xffffff, PorterDuff.Mode.CLEAR);
            if (isMute) {// animation_up
                mMuteCanvas.drawBitmap(muteIcon, mCircleRecf.centerX()-7, 0,
                        null);
            } else { // animation_down
                mMuteCanvas.drawColor(0xffffff, PorterDuff.Mode.CLEAR);
            }
        }

    }
}
