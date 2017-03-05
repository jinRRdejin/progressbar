package com.cultraview.view;

import com.example.progressbar.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

public class HorizontalprograssbarAndprograss extends ProgressBar {
	//自定义属性的获取
	private static final int DEFAULT_TEXT_SIZE=10;//sp
	private static final int DEFAULT_TEXT_COLOR=0xFFFC00D1;
	private static final int DEFAULT_UNREACH_COLOR=0xFFD3D6DA;
	private static final int DEFAULT_UNREACH_HEIGHT=2;//dp
	private static final int DEFAULT_REACH_COLOR=DEFAULT_TEXT_COLOR;//sp
	private static final int DEFAULT_REACH_HEIGHT=2;//
	private static final int DEFAULT_TEXT_OFFSET=10;
	private static final int DEFAULT_CIRCLEBUTTON_COLOR=0xFF827a99;
	private static final int DEFAULT_CIRCLEBUTTON_RADIUS=10;
	//初始化环境变量
	protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
	protected int mTextColor = DEFAULT_TEXT_COLOR;
	protected int mUnReachColor = DEFAULT_UNREACH_COLOR;
	protected int mUnReachText = dp2px(DEFAULT_UNREACH_HEIGHT);
	protected int mReachColr = DEFAULT_REACH_COLOR;
	protected int mReachText = dp2px(DEFAULT_REACH_HEIGHT);
	protected int mTextOffSet = dp2px(DEFAULT_TEXT_OFFSET);
	protected int mCircleButtonColor = DEFAULT_CIRCLEBUTTON_COLOR;
	protected int mCicleButtonRadius = dp2px(DEFAULT_CIRCLEBUTTON_RADIUS);
	
	protected Paint mPaint = new Paint();//绘制
	
	private int mRealwidth;//规定长度减去padding间隙=实际长度
         
	public HorizontalprograssbarAndprograss(Context context, AttributeSet attrs) {
		this(context, attrs,0);	//拿两个参数的构造方法调用三个参数的构造方法	
			
	}

	public HorizontalprograssbarAndprograss(Context context) {
		this(context,null);//拿一个参数的构造方法调用两个参数的构造方法
		
	}
	
	
	public HorizontalprograssbarAndprograss(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		obtainStyleAttrs(attrs);
	}

	private void obtainStyleAttrs(AttributeSet attrs) {
		//获得自定义属性
		TypedArray ta = getContext().obtainStyledAttributes(attrs, 
				R.styleable.HorizontalprograssbarAndprograss);
		mTextSize = (int) ta.getDimension(R.styleable.HorizontalprograssbarAndprograss_progress_text_size,
				mTextSize);
		mTextColor = (int) ta.getColor(R.styleable.HorizontalprograssbarAndprograss_progress_text_color, 
				mTextColor);
		mUnReachColor = (int) ta.getColor(R.styleable.HorizontalprograssbarAndprograss_progress_unreach_color,
				mUnReachColor);
		mUnReachText = (int) ta.getDimension(R.styleable.HorizontalprograssbarAndprograss_progress_unreach_height, 
				mUnReachText);
		mReachColr = (int) ta.getColor(R.styleable.HorizontalprograssbarAndprograss_progress_reach_color,
				mReachColr);
		mReachText = (int) ta.getDimension(R.styleable.HorizontalprograssbarAndprograss_progress_reach_height, 
				mReachText);
		mTextOffSet = (int) ta.getDimension(R.styleable.HorizontalprograssbarAndprograss_progress_text_offset,
				mTextOffSet);
		
		mPaint.setTextSize(mTextSize);//字体的size
		ta.recycle();
	}
	//测量
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
//	widthMode = MeasureSpec.getMode(widthMeasureSpec);//进度条的宽度肯定是一个明确的值
		int width = MeasureSpec.getSize(widthMeasureSpec);		
		int height = maesureHeight(heightMeasureSpec);		
		setMeasuredDimension(width, height);//拿到view的宽和高
		mRealwidth = getMeasuredWidth() -getPaddingLeft() -getPaddingRight();
	}

	private int maesureHeight(int heightMeasureSpec) {
		int result = 0;
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		int size = MeasureSpec.getSize(heightMeasureSpec);
		if(mode == MeasureSpec.EXACTLY){
		  result = size;
		  
		}else{
			
		int textHeight = (int) (mPaint.descent() - mPaint.ascent());
		result = getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mReachText, mUnReachText), 
				Math.abs(textHeight));
		
		
		if(mode == MeasureSpec.AT_MOST){
			result = Math.min(result, size);
		}
		}
		return result;
	}
	//进度条的绘制
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		
		canvas.save();
		canvas.translate(getPaddingLeft(), getHeight()/2);
		boolean noNeedRech = false;
		//draw reach bar
		String text = getProgress() + "%";
		int textWidth = (int) mPaint.measureText(text);
		
		float radio = getProgress()* 1.0f/100;//当前进度  
//		float endX = radio*mRealwidth -mTextOffSet;
		float progressX =(int)( radio*mRealwidth);///真实刻度所在位置
		
		if(progressX + textWidth > mRealwidth){
			
			progressX = mRealwidth - textWidth ;
			noNeedRech = true;
			
		}	
		float endX = progressX - mTextSize/2;//Reach BAr 画到的位置
		
		if( endX > 0){
			mPaint.setColor(mReachColr);
			mPaint.setStrokeWidth(mReachText);
			canvas.drawLine(0, 0, endX, 0, mPaint);
		}
		
		//draw text
		mPaint.setColor(mTextColor);
		int y = (int) ( -(mPaint.descent() + mPaint.ascent())/2);
		canvas.drawText(text, progressX, y, mPaint);
		canvas.restore();
		
		//draw unreach bar
		if(!noNeedRech){
			
			float start = progressX + mTextOffSet*2 +  textWidth ;
			mPaint.setColor(mUnReachColor);
			mPaint.setStrokeWidth(mUnReachText);
			canvas.drawLine(start,  getHeight()/2, mRealwidth, getHeight()/2, mPaint);
		}
	  
	}

	protected int dp2px(int dpVal){
		
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, getResources().getDisplayMetrics());
               //TypedValue.COMPLEX_UNIT_DIP表示想要得到的单位，dpVal是想要得到单位的数值
		       //getResources()这个方法就可以获取存在系统的资源
		       //getDisplayMetrics()		
	}
	protected int sp2px(int spVal){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 
				spVal, getResources().getDisplayMetrics());
		
	}

	
}
