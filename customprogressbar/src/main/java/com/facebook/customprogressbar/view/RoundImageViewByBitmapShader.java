package com.facebook.customprogressbar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by homelink on 2015/11/11.
 */
public class RoundImageViewByBitmapShader extends ImageView{

    public RoundImageViewByBitmapShader(Context context) {
        super(context);
    }

    public RoundImageViewByBitmapShader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImageViewByBitmapShader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundImageViewByBitmapShader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
