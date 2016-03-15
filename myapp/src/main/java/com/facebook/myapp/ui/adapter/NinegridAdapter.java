package com.facebook.myapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.myapp.R;
import com.facebook.myapp.module.Image;
import com.facebook.myapp.utils.ScreenUtils;
import com.facebook.myapp.view.FilterImageView;
import com.facebook.myapp.view.NineGridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by homelink on 2015/10/30.
 */
public class NinegridAdapter extends BaseAdapter{
    /****
     *
     */
    private Context context;
    private List<List<Image>>  list;
    private LayoutInflater  inflater;
    public  NinegridAdapter(Context context,List<List<Image>> list){
        this.context=context;
        this.list=list;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void  setData(List<List<Image>> list){
        this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list!=null&& list.size()>0?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder  viewholder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_ninegridlayout,parent,false);
            viewholder=new Viewholder();
            viewholder.fiv= (FilterImageView) convertView.findViewById(R.id.iv_oneimage);
            viewholder.nineGridLayout=(NineGridLayout)convertView.findViewById(R.id.iv_ngrid_layout);
            convertView.setTag(viewholder);
        }else{
            viewholder= (Viewholder) convertView.getTag();
        }
        List<Image>  imageList=list.get(position);
        if(imageList.isEmpty()){
            viewholder.fiv.setVisibility(View.GONE);
            viewholder.nineGridLayout.setVisibility(View.GONE);
        }else{
            if(imageList.size()==1){
                viewholder.nineGridLayout.setVisibility(View.GONE);
                viewholder.fiv.setVisibility(View.VISIBLE);
                //处理单张图片的情况
                handOneImage(viewholder,imageList.get(0));
            }else{
                //处理多张图片你
                viewholder.nineGridLayout.setVisibility(View.VISIBLE);
                viewholder.fiv.setVisibility(View.GONE);
                viewholder.nineGridLayout.setImagesData(imageList);
            }
        }
        return convertView;
    }

    /****
     * 处理一张图片的情况
     * @param viewholder
     * @param image
     */
    private void handOneImage(Viewholder viewholder, Image image) {
         int totalwidth,imageheight,imagewidth;
        ScreenUtils  screenUtils=ScreenUtils.getIntance(context);
        totalwidth=screenUtils.getScreenWidth()-screenUtils.dip2px(80);
        //得到宽高
        imageheight=screenUtils.dip2px(image.getWidth());
        imagewidth=screenUtils.dip2px(image.getHeight());
        //
        if(image.getWidth()<=image.getHeight()){
              if(imageheight>totalwidth){
                  imageheight=totalwidth;
                  imagewidth=(imageheight*image.getWidth())/image.getHeight();
              }
        }else{
              if(imagewidth>totalwidth){
                  imagewidth=totalwidth;
                  imageheight=(imagewidth*image.getHeight())/image.getWidth();
              }
        }
        ViewGroup.LayoutParams  params=viewholder.fiv.getLayoutParams();
        params.height=imageheight;
        params.width=imagewidth;
        viewholder.fiv.setLayoutParams(params);
        viewholder.fiv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        viewholder.fiv.setImageUrl(image.getUrl());
    }

    static   class  Viewholder{
        NineGridLayout  nineGridLayout;
        FilterImageView fiv;
    }
}
