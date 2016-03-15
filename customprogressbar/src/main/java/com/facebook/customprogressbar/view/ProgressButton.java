package com.facebook.customprogressbar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.customprogressbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by homelink on 2015/11/16.
 */
public class ProgressButton extends View {

    private Paint.FontMetrics fontMetrics;
    private int textcolor, max;

    public void setTextsize(float textsize) {
        this.textsize = textsize;
    }

    private float textsize;
    private String text;
    private Paint mPaint, mArcPaint;
    private float mRadius, progress;
    private int backgroundcolor, foregroundcolor;
    private int mwidth;
    OnProgressButtonClickListener onProgressButtonClickListener;

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton);
        /***
         *
         */
        textcolor = typedArray.getColor(R.styleable.ProgressButton_textcolor, Color.WHITE);
        text = typedArray.getString(R.styleable.ProgressButton_text);
        textsize = typedArray.getDimension(R.styleable.ProgressButton_textsize, 20);
        max = typedArray.getInteger(R.styleable.ProgressButton_max, 100);
        progress = typedArray.getInteger(R.styleable.ProgressButton_progress, 0);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.ProgressButton_radius, 5);
        backgroundcolor = typedArray.getColor(R.styleable.ProgressButton_backgroundcolor, Color.parseColor("#E3E3E3"));
        foregroundcolor = typedArray.getColor(R.styleable.ProgressButton_foregroundcolor, Color.parseColor("#66CD00"));
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);//设置画笔宽度
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        typedArray.recycle();
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mwidth=getWidth();
    }

    /***
     * 绘制图形
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        /***
         * 绘制背景
         */
        RectF oval = new RectF(0, 0, getWidth(), getHeight());
        mPaint.setColor(this.backgroundcolor);
        canvas.drawRoundRect(oval, mRadius, mRadius, mPaint);
        /***
         * 绘制进度条，分两种情况，小于弧度，大于弧度
         */
        mPaint.setColor(this.foregroundcolor);
        if (progress <= mRadius) {
            /***
             * 绘制两个半圆，一个矩形不是绘制圆角矩形
             *
             */
//            oval = new RectF(0, mRadius - progress, progress * 1.0f / max * getWidth(), getHeight() - (mRadius -
//                    progress));
            /***
             * 圆弧绘制范围,非标准绘制，通过path类来绘制
             * 临时半径
             * */
            float tempwidth=progress/max*getWidth();
//            RectF mArcRectF = new RectF(0, mRadius-tempRadius, tempRadius* 2 , tempRadius * 2+);
//            canvas.drawArc(mArcRectF, 180, 90, true, mPaint);
            Path  mArcPath=new Path();
            mArcPaint.setStyle(Paint.Style.FILL);//设置画笔为填充模式
            mArcPaint.setStrokeWidth(2);
            mArcPaint.setColor(0xffff00ff);
            List<Point>  pointList=new ArrayList<Point>();
            pointList.add(new Point(0, (int) mRadius));
            /**
             * 第一部分绘制圆弧
             */
            mArcPath.moveTo(0, mRadius);
            RectF mArcRectF=new RectF(0,0,mRadius*2,mRadius*2);
            //绘制圆弧
            mArcPath.arcTo(mArcRectF, 180, 90);
            mArcPath.moveTo(mRadius, 0);
            pointList.add(new Point((int) mRadius, 0));
            //绘制线条
            mArcPath.lineTo(mRadius, mRadius);
            pointList.add(new Point((int) mRadius, (int) mRadius));
            //绘制线条
            mArcPath.lineTo(0, mRadius);
            pointList.add(new Point(0, 0));
            //最后绘制Path的连接点，方便我们大家对比观察
            canvas.drawPath(mArcPath,mArcPaint);
            mArcPaint.setStrokeWidth(10);//将点的strokeWidth要设置的比画path时要大
            mArcPaint.setStrokeCap(Paint.Cap.ROUND);//将点设置为圆点状
            mArcPaint.setColor(0xff0000ff);//设置圆点为蓝色
            for(int i=0;i<pointList.size();i++){
                    Point point=pointList.get(i);
                    canvas.drawPoint(point.x,point.y,mArcPaint);
            }

            /***
             * 矩形的绘制范围
             */
//           RectF  mRectF=new RectF(0,);
        } else {
            oval = new RectF(0, 0, progress * 1.0f / max * getWidth(), getHeight());
            canvas.drawRoundRect(oval, mRadius, mRadius, mPaint);
        }

        /***
         * 绘制文本
         */
        mPaint.setColor(textcolor);
        if (TextUtils.isEmpty(text) || text == null)
            return;
        fontMetrics = mPaint.getFontMetrics();
        /***
         * 绘制文本的基准线
         */
        float textCenterVerticalBaselineY = getHeight() / 2 - fontMetrics.descent + (fontMetrics.descent -
                fontMetrics.ascent) / 2;
        canvas.drawText(this.text, getWidth() / 2 - mPaint.measureText(this.text) / 2, textCenterVerticalBaselineY,
                mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:///设置监听
                setOnProgressButtonClickListener(onProgressButtonClickListener);
                break;
            default:
                break;
        }
        return true;
    }

    public int getTextcolor() {
        return textcolor;
    }

    public void setTextcolor(int textcolor) {
        this.textcolor = textcolor;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress > max) {
            progress = max;
            return;
        }
        this.progress = progress;
        postInvalidate();//设置进度后重绘图形
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public OnProgressButtonClickListener getOnProgressButtonClickListener() {
        return onProgressButtonClickListener;
    }

    public void setOnProgressButtonClickListener(OnProgressButtonClickListener onProgressButtonClickListener) {
        this.onProgressButtonClickListener = onProgressButtonClickListener;
    }

    public interface OnProgressButtonClickListener {
        void onProgressButton(View v);
    }
}
