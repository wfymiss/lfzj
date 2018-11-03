package com.ovov.lfzj.neighbour.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ovov.lfzj.R;

import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.event.ClickEvent;
import com.ovov.lfzj.event.DeleteImageEvent;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * gridview发布选择图片适配器
 * Created by 刘永毅 on 2017/7/26.
 */

public class GridPopupAdapter extends BaseAdapter {
    private Context context;      // 上下文参数
    public List<File> list = new ArrayList<>();   //图片路径集合
    public int imageSum;    //最多可上传图片
    private Bitmap addbitmap;    // 默认图片

    /**
     * 图片发布选择适配器
     *
     * @param context
     * @param imageSum
     */
    public GridPopupAdapter(Context context, int imageSum) {
        this.context = context;
        this.imageSum = imageSum;
        addbitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ac_paizhao);
    }

    /**
     * 添加图片
     *
     * @param path
     */
    public void setGridImagePath(File path) {
        list.add(path);
        notifyDataSetChanged();
    }

    /*
     *  删除图片
     *
     * */
    public void setDelectPath(int i) {
        list.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = list == null ? 1 : list.size() + 1;
        if (count > imageSum) {
            return imageSum;
        } else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_popup_gridview_item, null);
            vh = new viewHolder();
            vh.imageViews = (ImageView) convertView.findViewById(R.id.list_popup_fabu_grid);
            vh.ivClose = (ImageView) convertView.findViewById(R.id.iv_close);
            convertView.setTag(vh);
        } else {
            vh = (viewHolder) convertView.getTag();
        }
       /* if (list!=null && list.size()>position){
            //vh.imageViews.setImageBitmap(PhotoBitmapUtil.getCompressPhoto(list.get(position)));
            Picasso.with(context).load(list.get(position)).into(vh.imageViews);
        }else {
            if (list.size()!=imageSum){
                //vh.imageViews.setImageBitmap(addbitmap);          // 添加GridView 图片按钮
                Picasso.with(context).load(R.mipmap.ac_paizhao).into(vh.imageViews);
                vh.imageViews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RxBus.getDefault().post(new ClickEvent());
                    }
                });

            }
        }*/
        if (position < list.size()) {
            Picasso.with(context).load(list.get(position)).into(vh.imageViews);
            vh.ivClose.setVisibility(View.VISIBLE);
            vh.ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.remove(position);
                    RxBus.getDefault().post(new DeleteImageEvent(position));
                    notifyDataSetChanged();
                }
            });
            vh.imageViews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            Picasso.with(context).load(R.mipmap.ac_paizhao).into(vh.imageViews);
            vh.ivClose.setVisibility(View.GONE);
            vh.imageViews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RxBus.getDefault().post(new ClickEvent());
                }
            });
        }
        return convertView;
    }

    class viewHolder {
        ImageView imageViews,ivClose;
    }
}
