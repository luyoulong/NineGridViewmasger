package com.facebook.customprogressbar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.facebook.customprogressbar.R;

/**
 * Created by homelink on 2015/11/11.
 */
public class CustomTextView extends View {

    private String mTitleText;
    private int mTitleTextSize;
    private int mTitleTextColor;
    /***
     * Ctrl+Shift+U ---大小写转换快捷键,Ctrl+Alt+L---格式化代码
     */
    private static final int DEFAULT_TEXTSIZE = 18;
    private Paint mPaint;
    private Rect mRect;

    //    private Rect  mRect;
    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /***
         * 得到自定义属性
         */
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        for (int i = 0; i < typedArray.length(); i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.CustomTextView_titletext:
                    mTitleText = typedArray.getString(index);
                    break;
                case R.styleable.CustomTextView_titletextcolor:
                    //获得文本默认颜色
                    mTitleTextColor = typedArray.getColor(index, Color.BLACK);
                    break;
                case R.styleable.CustomTextView_titletextsize:
                    //获得文本尺寸
                    mTitleTextSize = typedArray.getDimensionPixelSize(index, (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXTSIZE, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();//回收
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//消除锯齿
        mRect =new Rect();
        //设置文字区域
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /***
         * 计算MeasureSpec, measuremode
         */
       int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthsize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightsize=MeasureSpec.getSize(heightMeasureSpec);
        /***
         * 判断matchparent wrapcontent 固定宽高
         */
        int width,height;
        if(widthMode==MeasureSpec.EXACTLY){
            /***
             *
             */
            width=widthsize;
        }else{
            mPaint.setTextSize(mTitleTextSize);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mRect);
            int textwidth=(int)mRect.width();
            int desire=(int)(getPaddingLeft()+textwidth+getPaddingRight());
            width=desire;
        }
        if(heightMode==MeasureSpec.EXACTLY){
            height=heightsize;
        }else{
            mPaint.setTextSize(mTitleTextSize);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
            int textheight=(int)mRect.width();
            int desire=(int)(getPaddingLeft()+textheight+getPaddingRight());
            height=desire;
        }
        setMeasuredDimension(width,height);
    }

    /***
     * 重新绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        /***
         * 绘制矩形区域
         */
        mPaint.setColor(getResources().getColor(android.R.color.holo_green_light));
        canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        /***
         * 绘制文字
         */
        mPaint.reset();
        mPaint.setColor(mTitleTextColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        /***
         * 绘制文本区域
         */
        canvas.drawText(mTitleText,getWidth()/2-mRect.width()/2,getHeight()/2+mRect.height()/2,mPaint);
    }
}
