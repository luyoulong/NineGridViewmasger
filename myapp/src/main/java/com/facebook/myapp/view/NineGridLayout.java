package com.facebook.myapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.myapp.module.Image;
import com.facebook.myapp.utils.ScreenUtils;

import java.util.List;

/**
 * Created by homelink on 2015/10/30.
 */
public class NineGridLayout extends ViewGroup {


    private int rows;//行数
    private int colums;//列数
    //图片之间的间隔
    private int gap = 5;
    private int totalwidth;
    private List listData;

    /****
     * @param context
     */
    public NineGridLayout(Context context) {
        super(context);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ScreenUtils screenUtils = ScreenUtils.getIntance(getContext());
        totalwidth = screenUtils.getScreenWidth() - screenUtils.dip2px(80);//这句话什么意思
    }

    public NineGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NineGridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /****
     * 加入子控件
     */
    public void setImagesData(List list) {
        if (list == null || list.isEmpty())
            return;
        //初始化布局，判断行数，列数
        generateLayout(list.size());
        //判断数据源于源数据源的大小
        if (listData == null) {
            int i = 0;
            while (i < list.size()) {
                FilterImageView fiv = generateImageView();
                addView(fiv, generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldDatasize = listData.size();
            int newDatasize = list.size();
            //原数据源大于新数据源,移除部分View,匹配数据源显示
            if (oldDatasize > newDatasize) {
                //数据源重新赋值
                removeViews(newDatasize - 1, oldDatasize-newDatasize);
            } else {
                //添加部分view,匹配数据源
                for (int i = 0; i < newDatasize - oldDatasize; i++) {
                    FilterImageView fiv =generateImageView();
                    addView(fiv, generateDefaultLayoutParams());
                }
            }
        }
        listData = list;
        layoutChildView();
    }

    /****
     * 改变ViewGroup中的布局
     */
    private void layoutChildView() {
        //获取控件宽度，高度，子控件上下左右的摆放位置
        int childCounts = listData.size();
        int childwidth = (totalwidth - gap * (colums - 1)) / 3;
        int childheight = childwidth;
        //重新设置控件的高度
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = childheight*rows+ (rows - 1) * gap;
        setLayoutParams(params);
        // 设置控件位置
        for (int i = 0; i < childCounts; i++) {
            FilterImageView childView = (FilterImageView)getChildAt(i);
            childView.setImageUrl(((Image) listData.get(i)).getUrl());//设置图片加载地址
            //找到子控件在父容器中的位置
            int[] position = getChildViewPosition(i);
            //按位置排列
            int left=(childwidth+gap)*position[1];
            int top=(childheight+gap)*position[0];
            int right=left+childwidth;
            int bottom=top+childheight;
            childView.layout(left,top,right,bottom);
        }
    }

    /****
     * 获取子控件在父容器的位置
     *
     * @return
     */
    private int[] getChildViewPosition(int childlocation) {
        int[] position = new int[2];
        for (int j = 0; j < rows ; j++) {//行数
            for(int k=0; k<colums ;k++){
                //找出控件在第几行，第几列
                if ((j*colums+k)==childlocation) {//这个判断是否严谨？？？？？
                    position[0]=j;
                    position[1]=k;
                    break;
                }
            }
        }
        return position;
    }

    /****
     * 判断数据源中的数据总数，判断有几行几列
     *
     * @param size
     */
    private void generateLayout(int size) {
        if (size <= 3) {
            colums = size;
            rows = 1;
        } else if (size <= 6) {
            colums = 3;
            rows = 2;
            if(size==4){
                colums=2;
                rows=2;
            }
        } else if (size <= 9) {
            colums = 3;
            rows = 3;
        } else {// 多于9
            colums = 3;
            if (size % 3 == 0) {
                rows = size / 3;
            } else {
                rows = size / 3 + 1;
            }
        }
    }
    private FilterImageView generateImageView() {
        FilterImageView iv = new FilterImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        return iv;
    }

}
