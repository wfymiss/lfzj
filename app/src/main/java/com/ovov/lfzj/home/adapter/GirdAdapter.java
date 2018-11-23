package com.ovov.lfzj.home.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;

import static com.ovov.lfzj.base.utils.UIUtils.getResources;

/**
 * Created by kaite on 2018/9/20.
 */

public class GirdAdapter extends BaseAdapter {

    private String[] proName = getResources().getStringArray(R.array.find_gv_title);
    private Integer[] mImages = {R.drawable.paymoney, R.mipmap.repairs, R.drawable.go, R.drawable.pay, R.drawable.game};

    private Context context;
    public GirdAdapter(Context homeFragment) {
        this.context =homeFragment;
    }

    @Override
    public int getCount() {
        return proName.length;
    }


    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取一个在数据集中指定索引的视图来显示数据
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            // 根据自定义的布局来加载布局
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.menu_item, null);
            holder.product_cost = (TextView) convertView.findViewById(R.id.tv_new_seed_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_new_seed_ic);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);

        } else {

            holder = (Holder) convertView.getTag();

        }
        holder.product_cost.setText(proName[position]);
        holder.imageView.setImageResource(mImages[position]);

        return convertView;
    }

    private static final class Holder {
        private TextView product_title;
        TextView product_cost;
        ImageView imageView;

    }

}

