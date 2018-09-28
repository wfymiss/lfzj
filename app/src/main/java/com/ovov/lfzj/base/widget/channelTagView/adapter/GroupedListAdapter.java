package com.ovov.lfzj.base.widget.channelTagView.adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.widget.channelTagView.bean.ChannelItem;
import com.ovov.lfzj.base.widget.channelTagView.bean.GroupItem;
import com.ovov.lfzj.base.widget.channelTagView.view.ChannelTagView;

import java.util.ArrayList;

import cn.bingoogolapple.badgeview.BGABadgeTextView;
import cn.bingoogolapple.badgeview.BGABadgeable;
import cn.bingoogolapple.badgeview.BGADragDismissDelegate;

/**
 * 这是普通的分组Adapter 每一个组都有头部、尾部和子项。
 */
public class GroupedListAdapter extends GroupedRecyclerViewAdapter {
    private int itemTxColor=-1;
    private int itemBg=-1;
    private int categoryBg = -1;
    private int categoryTxColor = -1;
    private int categoryTxSize = 13;
    private ArrayList<GroupItem> mGroups;
    private boolean openCategory;
    private ChannelTagView.RedDotRemainderListener redDotRemainderListener;

    public GroupedListAdapter(Context context, ArrayList<GroupItem> groups, boolean openCategory) {
        super(context);
        mGroups = groups;
        this.openCategory = openCategory;
    }

    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.size();

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChannelItem> children = mGroups.get(groupPosition).getChannelItems();
        return children == null ? 0 : children.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return openCategory;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.item_mulite_banner;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.item_channel_view;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        GroupItem entity = mGroups.get(groupPosition);
        TextView categoryTitle = holder.get(R.id.banner_title);
        categoryTitle.setText(entity.category);
        if(categoryBg!=-1){
            categoryTitle.setBackgroundResource(categoryBg);
        }
        if(categoryTxColor!=-1){
            categoryTitle.setTextColor(categoryTxColor);
        }
        categoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,categoryTxSize);
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        Log.e("aaaa",childPosition+"");
        ChannelItem entity = mGroups.get(groupPosition).getChannelItems().get(childPosition);
        final BGABadgeTextView title = (BGABadgeTextView) holder.get(R.id.item_tv);
        final ImageView itemIm = (ImageView) holder.get(R.id.item_Im);
        title.setText(entity.title);
        itemIm.setImageResource(entity.resource);
        if(itemTxColor!=-1){
            title.setTextColor(itemTxColor);
        }
        if(itemBg!=-1){
            title.setBackgroundResource(itemBg);
        }
        int pos = getPositionForChild(groupPosition,childPosition);
        final int position  = openCategory?pos - (groupPosition + 1) : pos;
        if (redDotRemainderListener.showUnAddedChannelBadge(title, position)) {
            title.setDragDismissDelegage(new BGADragDismissDelegate() {
                @Override
                public void onDismiss(BGABadgeable badgeable) {
                    redDotRemainderListener.OnDragDismiss(title, position);
                }
            });
            redDotRemainderListener.handleUnAddedChannelReddot(title, position);
        } else {
            title.hiddenBadge();
        }

    }

    public void setRedDotRemainderListener(ChannelTagView.RedDotRemainderListener redDotRemainderListener) {
        this.redDotRemainderListener = redDotRemainderListener;
    }

    public void setOpenCategory(boolean openCategory) {
        this.openCategory = openCategory;
        this.changeDataSet();
    }

    public void setItemTxColor(int itemTxColor) {
        this.itemTxColor = itemTxColor;
        changeDataSet();
    }

    public void setItemBg(int itemBg) {
        this.itemBg = itemBg;
        changeDataSet();
    }

    public void setCategoryBg(int categoryBg) {
        this.categoryBg = categoryBg;
        changeDataSet();
    }

    public void setCategoryTxColor(int color) {
        this.categoryTxColor = color;
        changeDataSet();
    }

    public void setCategoryTxSize(int size) {
        this.categoryTxSize = size;
        changeDataSet();
    }
}
