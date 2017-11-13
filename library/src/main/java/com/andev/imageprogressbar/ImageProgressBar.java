package com.andev.imageprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class ImageProgressBar extends View {

	private static final String TAG = "ImageProgressBar";

	private int mMaxProgress = 100;

	private int mBgColor;
	private int mProgressBarRes;

	private int width = 0;
	private int height = 0;

	private Bitmap mBitmap;

	private Rect mProgressRect = new Rect();

	private Paint mBitmapPaint;

	private int mCurProgress = 0;

	private OnProgressBarListener mListener;

	public ImageProgressBar(Context context) {
		super(context);
		init(context, null);
	}

	public ImageProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public ImageProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		initAttributes(context, attrs);

		mBitmap = BitmapFactory.decodeResource(getResources(), mProgressBarRes);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mBitmapPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
	}

	private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
		return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
	}

	private void initAttributes(Context context, AttributeSet attrs) {
		TypedArray attr = getTypedArray(context, attrs, R.styleable.ImageProgressBar);
		if (attr == null) {
			return;
		}

		try {
			mBgColor = attr.getColor(R.styleable.ImageProgressBar_ipb_background_color, Color.WHITE);
			mProgressBarRes = attr.getResourceId(R.styleable.ImageProgressBar_ipb_progressbar_res, R.drawable.default_bar);
			mCurProgress = attr.getInt(R.styleable.ImageProgressBar_ipb_progressbar_progress ,0);
			mMaxProgress = attr.getInt(R.styleable.ImageProgressBar_ipb_progressbar_max ,100);
			if(mCurProgress < 0){
				mCurProgress = 0;
			}else if(mCurProgress > mMaxProgress){
				mCurProgress = mMaxProgress;
			}

		} finally {
			attr.recycle();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		width = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		if(heightMode == MeasureSpec.EXACTLY){
			height = MeasureSpec.getSize(heightMeasureSpec);
		}else{
			height = mBitmap.getHeight();
		}

		mProgressRect.bottom = height;

		setMeasuredDimension(width, height);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawColor(mBgColor);

		if (mCurProgress > 0) {
			mProgressRect.right = width * mCurProgress / mMaxProgress;
		}

		canvas.drawRect(0, 0,  mProgressRect.width(), mProgressRect.height(), mBitmapPaint);
	}

	public void setOnProgressBarListener(OnProgressBarListener listener){
		mListener = listener;
	}

	public int getProgress() {
		return mCurProgress;
	}

	public void incrementProgressBy(int by) {
		if (by > 0) {
			setProgress(getProgress() + by);
		}

		if(mListener != null){
			mListener.onProgressChange(getProgress(), mMaxProgress);
		}
	}

	/**
	 *
	 * @param progress
	 */
	public void setProgress(int progress) {
		if(progress < 0){
			mCurProgress = 0;
		}else if(progress > mMaxProgress){
			mCurProgress = mMaxProgress;
		}

		this.mCurProgress = progress;

		postInvalidate();
	}

}
