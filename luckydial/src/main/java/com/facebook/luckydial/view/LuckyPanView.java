package com.facebook.luckydial.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.webkit.WebSettings;

import com.facebook.luckydial.R;

/**
 * Created by homelink on 2015/11/2.
 * 抽奖转盘
 */
public class LuckyPanView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    private boolean isRunning;//线程控制开关
    private Thread t;//用于绘制的线程
    private Canvas mCanvas;//与surfaceholder配对的Canvas
    private String[] mStrings = new String[] { "单反相机", "IPAD", "恭喜发财", "IPHONE",
            "妹子一只", "恭喜发财" };
    /**
     * 每个盘块的颜色
     */
    private int[] mColors = new int[]{0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0xFFF17E01};
    /****
     * 每个盘块对应的图片
     */
    private int[] mImages =  new int[] { R.mipmap.danfan, R.mipmap.ipad,
            R.mipmap.f040, R.mipmap.iphone, R.mipmap.meizi,
            R.mipmap.f040 };
    /****
     * 文字图片对应的数组
     */
    private Bitmap[] mBitmap;
    private int itemCount = 6;//盘块的个数
    /****
     * 绘制图片的画笔，绘制文字的画笔
     */
    private Paint mTextPaint, mArcPaint;//绘制文字的画笔，绘制圆弧的画笔
    /***
     * 圆的直径
     */
    private int mRadius;
    /****
     * 每个盘块绘制的区域
     */
    private RectF mRange = new RectF();
    /***
     * 盘块滚动的速度
     */
    private double mSpeed;
    /****
     * 是否按下了停止按钮
     */
    private boolean isShouldEnd;
    /****
     * 起始的绘制角度
     */
    private int mStartAngle = 0;
    /****
     * 控件的中心位置
     */
    private int mCenter;
    /****
     * 控件的padding,以控件的Paddingleft为准
     */
    private int mPadding;
    /***
     * 背景图的bitmap
     */
    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg2);
    /***
     * 文字的大小
     */
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources()
            .getDisplayMetrics());

    /****
     * @param context
     */
    public LuckyPanView(Context context) {
        super(context, null);
    }

    public LuckyPanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        //常量
        this.setKeepScreenOn(true);
    }

    public LuckyPanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LuckyPanView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /****
     * 设置控件的形状为正方形
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredHeight(), getMeasuredWidth());
        //获取圆的直径
        mRadius = width - getPaddingLeft() - getPaddingRight();
        //获得padding
        mPadding = getPaddingLeft();
        //获得中心点位置
        mCenter = width / 2;
        //设置背景图为圆形
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /****
         * 绘制背景图片
         */
    }

    /****
     * 用于绘制工作
     */
    @Override
    public void run() {
        while (isRunning) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            //判断绘制时间差,绘制时间过短，暂停
            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /****
     * 进行绘制工作
     */
    private void draw() {
        /****
         *
         */
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                /****
                 * 绘制背景图
                 */
                drawBg();
                /****
                 * 绘制每个盘块，每个盘块上的文字
                 *
                 */
                float tempAngle = mStartAngle;
                float sweepAngle = (float) 360/itemCount;
                /***
                 * 循环开启绘制,盘块
                 */
                for (int i = 0; i < itemCount; i++) {
                    //绘制盘块
                    mArcPaint.setColor(mColors[i]);
                    mCanvas.drawArc(mRange, tempAngle, sweepAngle, true, mArcPaint);
                    //绘制文本
                    drawText(tempAngle, sweepAngle, mStrings[i]);
                    //绘制盘块中的图片
                    drawIcon(tempAngle,mBitmap[i]);
                    //绘制角度不断增加
                    tempAngle += sweepAngle;
                }
                /****
                 * 增加起始角度，开始旋转
                 */
                mStartAngle += mSpeed;
                /***
                 * 点击停止按钮，速度递减
                 */
                if (isShouldEnd) {
                    mSpeed -= 1;
                }
                if (mSpeed <= 0) {
                    mSpeed = 0;
                    isShouldEnd = false;
                }
                /***
                 * 根据mStartAngle计算滚动停止的区域
                 */
                calInExactArea(mStartAngle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    /****
     * 计算圆盘停止时指针指向的位置
     *
     * @param startAngle
     */
    private void calInExactArea(int startAngle) {
        // 让指针从水平向右开始计算
        float rotate = startAngle + 90;
        rotate %= 360.0;
        for (int i = 0; i < itemCount; i++)
        {
            // 每个的中奖范围
            float from = 360 - (i + 1) * (360 / itemCount);
            float to = from + 360 - (i) * (360 / itemCount);

            if ((rotate > from) && (rotate < to))
            {
                Log.d("TAG", mStrings[i]);
                return;
            }
        }
    }

    /****
     * 开始转动
     *
     * @param luckyIndex
     */
    public void luckyStart(int luckyIndex) {
        /***
         * 每项角度
         */
        float angle = (float) (360 / itemCount);
        /***
         * 中奖的范围
         */
        float from = 270 - (luckyIndex+1) * angle;
        float to = angle + from;
        /***
         * 点击停止后需要旋转的距离
         */
        float targetfrom = 4 * 360 + from;
        float targetto = 4 * 360 + to;
        /***
         * 计算停在某个范围所需要的最大速度和最小速度
         * (v1+0)*(v1/1+1)/2=targetfrom;
         * v1*v1+v1-2targetfrom=0;
         * v1=(-1+Math.sqrt(1-4*1*(-2targetfrom)))/2;
         *  (v2+0)*(v2/1+1)/2=targetto;
         * v2=-1+Math.sqrt(1-4*1*(-2targetto));
         *  取两个速度的中间值，确定中奖范围
         */
        float v1 = (float) ((Math.sqrt(1 + 8 * 1 * targetfrom)-1) / 2);
        float v2 = (float) ((Math.sqrt(1 + 8 * 1 * targetto)-1) / 2);
        mSpeed = v1 + Math.random() * (v2 - v1);
        isShouldEnd = false;
    }

    public void luckyEnd() {
        mStartAngle = 0;
        isShouldEnd = true;
    }

    /***
     * 绘制每个盘块的图片
     *
     * @param mStartAngle
     * @param bitmap
     */
    private void drawIcon(float mStartAngle, Bitmap bitmap) {
        mTextPaint.setColor(0xFFFFFFFF);
        /***
         * 计算水平偏移量，计算垂直偏移量
         */
        int imgwidth = mRadius / 8;//图片宽度为直径八分之一
        //计算绘制角度
        double angle = (double) ((360 / itemCount / 2 + mStartAngle) * (Math.PI / 180));
        /***
         * 寻找图片中心点的位置
         */
        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));
        //确定图片绘制的区域
        Rect imgRect = new Rect(x - imgwidth / 2, y - imgwidth / 2, x + imgwidth / 2, y + imgwidth / 2);
        mCanvas.drawBitmap(bitmap, null, imgRect, null);
    }

    /***
     * 绘制图中的文本
     *
     * @param mStartAngle
     * @param sweepAngle
     * @param string
     */
    private void drawText(float mStartAngle, float sweepAngle, String string) {
        /***
         * 添加文本路径设置
         */
        Path path = new Path();
        path.addArc(mRange, mStartAngle, sweepAngle);
        //获得宽度
        float mTextwidth = mTextPaint.measureText(string);
        //水平偏移量,目的是让文字居中
        float hOffset = (float) (mRadius * Math.PI / itemCount / 2 - mTextwidth / 2);
        //垂直偏移量，自己定义
        float vOffset = (float) (mRadius / 2 / 6);
        mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
    }

    /****
     * 绘制背景
     */
    private void drawBg() {
        mCanvas.drawColor(0xFFFFFFFF);
        /***
         * 设置绘制背景的范围
         */
        mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2, mPadding / 2, getMeasuredWidth() - mPadding / 2,
                getMeasuredHeight() - mPadding / 2), null);

    }

    /****
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        t = new Thread(this);
        isRunning = true;
        t.start();
        /***
         * 初始化圆弧画笔
         */
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        //
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xFFffffff);
        /***
         *圆弧的绘制范围，左上右下四个点
         */
        mRange = new RectF(getPaddingLeft(), getPaddingLeft(), mRadius + getPaddingRight(), mRadius + getPaddingLeft());
        /***
         * 初始化图片
         */
        mBitmap = new Bitmap[itemCount];
        for (int i = 0; i < itemCount; i++) {
            mBitmap[i] = BitmapFactory.decodeResource(getResources(), mImages[i]);
        }
    }

    /***
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /****
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;//停止绘制
    }

    /****
     *
     */
    public boolean isStart() {
        return mSpeed != 0;
    }

    public boolean isShouldEnd() {
        return isShouldEnd;
    }
}
