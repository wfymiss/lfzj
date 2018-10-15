package com.ovov.lfzj.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.neighbour.square.SquareDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeNeighAdapter extends BaseAdapter {
    private List<SquareListInfo> list = new ArrayList<>();
    private Context context;
    private LayoutInflater mLayoutInflater;
    private InformItemListener informItemListener;

    public HomeNeighAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    // 数据重置
    public void reset(List<SquareListInfo> dataSet) {
        list.clear();
        if (dataSet != null) list.addAll(dataSet);
        notifyDataSetChanged();
    }

    // 数据添加
    public void addAll(List<SquareListInfo> dataSet) {
        if (dataSet != null) list.addAll(dataSet);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyGridViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MyGridViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.user_message_item, parent, false);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.time);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.name_tv);
            viewHolder.tv_loacation = (TextView) convertView.findViewById(R.id.location);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_tilte);
            viewHolder.tv_look = (TextView) convertView.findViewById(R.id.tv_look);
            viewHolder.tv_item_content = (TextView) convertView.findViewById(R.id.tv_item_content);
            viewHolder.tv_coment = (TextView) convertView.findViewById(R.id.tv_coment);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_tilte);
            viewHolder.rl_list_im = (NoScrollGridView) convertView.findViewById(R.id.rl_list_im);
            viewHolder.delect=(ImageView)convertView.findViewById(R.id.delect_im);
            viewHolder.linContainer = convertView.findViewById(R.id.container);
            viewHolder.ivHead = convertView.findViewById(R.id.my_circle_images);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyGridViewHolder) convertView.getTag();
        }
        if (list != null && list.size() > 0 && list.size() > position) {
            viewHolder.tv_title.setText(list.get(position).user_id);
            viewHolder.tv_item_content.setText(list.get(position).comment);
            viewHolder.tvTime.setText(list.get(position).time);
            viewHolder.tv_name.setText(list.get(position).userInfo.nickname);
            viewHolder.tv_look.setText(list.get(position).replyNum);
            // TODO: 2018/10/12
        //    viewHolder.tv_loacation.setText(list.get();
                viewHolder.delect.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
            viewHolder.tv_coment.setText(String.valueOf(list.get(position).zanNum));
            CommonAdapter adapter =new CommonAdapter<String>(context, list.get(position).imgUrl, R.layout.user_img_item) {

                @Override
                public void convert(com.mcxtzhang.commonadapter.lvgv.ViewHolder viewHolder, String s, int i) {
                    ImageView imageView = viewHolder.getView(R.id.iv_user_img);
                    Picasso.with(context).load(s).into(imageView);


                }

            };
            if (!TextUtils.isEmpty(list.get(position).userInfo.user_logo)){
                Picasso.with(context).load(list.get(position).userInfo.user_logo).placeholder(R.mipmap.ic_default_head).into(viewHolder.ivHead);
            }else {
                Picasso.with(context).load(R.mipmap.ic_default_head).placeholder(R.mipmap.ic_default_head).into(viewHolder.ivHead);

            }

            final int index = position;
            viewHolder.rl_list_im.setAdapter(adapter);
            viewHolder.rl_list_im.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (UIUtils.isFastClick()){
                        SquareDetailActivity.toActivity(context,list.get(index).id,position,1);
                    }
                }
            });
            viewHolder.linContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SquareDetailActivity.toActivity(context,list.get(index).id,position,1);
                }
            });

        }

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (informItemListener != null) {
//                    if (list != null && list.size() > 0 && list.size() > position) {
//                        informItemListener.itemClickListener(position);
//                    } else {
//                        Toast.makeText(context, "暂无公告", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
        return convertView;
    }

    public List<SquareListInfo> getData() {
        return list;
    }

    private static class MyGridViewHolder {
        ImageView imageView,delect;
        TextView tv_title, tv_content, tvTime, tv_name, tv_loacation, tv_item_content,tv_coment,tv_look;
        NoScrollGridView rl_list_im;
        LinearLayout linContainer;
        CircleImageView ivHead;
    }

    public interface InformItemListener {
        void itemClickListener(int position);
    }

    public void setInformItemListener(InformItemListener listener) {
        this.informItemListener = listener;
    }
}
