package com.facebook.myapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by homelink on 2015/10/30.
 */
public class FilterImageView extends ImageView {

    private String url;
    private boolean isAttachToWindow = false;

    public FilterImageView(Context context) {
        super(context, null);
    }

    public FilterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public FilterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FilterImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch (event.getAction()){
            case  MotionEvent.ACTION_DOWN:
                Drawable drawable=getDrawable();
                if(drawable!=null){
                    drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
                break;
            case  MotionEvent.ACTION_MOVE:
                break;
            case  MotionEvent.ACTION_UP:
                Drawable  drawableUp=getDrawable();
                if(drawableUp!=null){
                    drawableUp.mutate().clearColorFilter();//
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachToWindow=false;
        setImageBitmap(null);
        Picasso.with(getContext()).cancelRequest(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachToWindow = true;
        setImageUrl(url);
    }

    public void setImageUrl(String url) {
        if (url != null && !TextUtils.isEmpty(url)) {
            this.url=url;
            //加载图片
            Picasso.with(getContext()).load(url).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(this);
        }
    }


}
