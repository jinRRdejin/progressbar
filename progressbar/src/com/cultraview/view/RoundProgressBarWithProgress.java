package com.cultraview.view;

import com.example.progressbar.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RoundProgressBarWithProgress extends
		HorizontalprograssbarAndprograss {

	private int mRadius = dp2px(30);

	private int mMaxPaintWith;// 用来记录最大的宽度

	public RoundProgressBarWithProgress(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public RoundProgressBarWithProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public RoundProgressBarWithProgress(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
		mReachText = mUnReachText*2;
        
		
		
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.RoundProgressBarWithProgress);
		mRadius = (int) ta.
				getDimension(
				R.styleable.RoundProgressBarWithProgress_radius, mRadius);
        ta.recycle();
        
       mPaint.setStyle(Style.STROKE);//设置画笔为空心
       mPaint.setAntiAlias(true);//图像边缘相对清晰一点，锯齿痕迹不那么明显
       mPaint.setDither(true);
       mPaint.setStrokeCap(Cap.ROUND);// //画笔笔刷类型 如影响画笔但始末端

	}
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		
	mMaxPaintWith = Math.max(mReachText, mUnReachText);	
	//默认四个paddding 一致
	int expect = mRadius*2 + mMaxPaintWith + getPaddingLeft() + getPaddingRight();
	
	int width = resolveSize(expect, widthMeasureSpec);
	
	int height = resolveSize(expect, heightMeasureSpec);
	
	int readwidth = Math.min(width, height);
	
	mRadius = (readwidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWith)/2;
	
	setMeasuredDimension(readwidth, readwidth);
	}
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		String text = getProgress() + "%";
		float textWidth = mPaint.measureText(text);
		float textHeight =( mPaint.descent() + mPaint.ascent())/2;
		
		canvas.save();
		
		canvas.translate(getPaddingLeft() + mMaxPaintWith/2, getPaddingTop() + mMaxPaintWith/2);
		
		mPaint.setStyle(Style.STROKE);
		//draw unReach Bar
		mPaint.setColor(mUnReachColor);
		mPaint.setStrokeWidth(mUnReachText);
		canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
		
		//draw Reach Bar
		mPaint.setColor(mReachColr);
		mPaint.setStrokeWidth(mReachText);
		
		float sweepAngle = getProgress()*1.0f/getMax()*360;
		canvas.drawArc(new RectF(0,0,mRadius*2,mRadius*2)
		, 0, sweepAngle, false, mPaint);	
		//draw text
		mPaint.setColor(mTextColor);
		mPaint.setStyle(Style.FILL);
		canvas.drawText(text, mRadius - textWidth/2, mRadius - textHeight, mPaint);
		
		canvas.restore();
	}

}
