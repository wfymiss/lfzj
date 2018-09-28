package com.ovov.lfzj.home.ui;

import android.content.res.TypedArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.widget.channelTagView.bean.ChannelItem;
import com.ovov.lfzj.base.widget.channelTagView.bean.GroupItem;
import com.ovov.lfzj.base.widget.channelTagView.listener.OnChannelItemClicklistener;
import com.ovov.lfzj.base.widget.channelTagView.view.ChannelTagView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.badgeview.BGABadgeTextView;

public class GoodsActivity extends BaseActivity {

    @BindView(R.id.btn_showdrawableleft)
    TextView btnShow;
    @BindView(R.id.channel_tag_view)
    ChannelTagView channelTagView;

    private ArrayList<ChannelItem> addedChannels = new ArrayList<>();
    private ArrayList<ChannelItem> unAddedChannels = new ArrayList<>();
    private ArrayList<GroupItem> unAddedItems = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods;
    }

    @Override
    public void init() {
        initData();
        initOnclick();
    }

    private void initOnclick() {
        channelTagView.initChannels(addedChannels, unAddedItems, true, new ChannelTagView.RedDotRemainderListener() {

            @Override
            public boolean showAddedChannelBadge(BGABadgeTextView itemView, int position) {

                return false;
            }

            @Override
            public boolean showUnAddedChannelBadge(BGABadgeTextView itemView, int position) {
                return false;
            }

            @Override
            public void handleAddedChannelReddot(BGABadgeTextView itemView, int position) {
                itemView.showCirclePointBadge();
            }

            @Override
            public void handleUnAddedChannelReddot(BGABadgeTextView itemView, int position) {

            }

            @Override
            public void OnDragDismiss(BGABadgeTextView itemView, int position) {
                // Toast.makeText(MainActivity.this, "拖拽取消红点提示-", Toast.LENGTH_SHORT).show();
                itemView.hiddenBadge();
            }

        });

        channelTagView.setOnChannelItemClicklistener(new OnChannelItemClicklistener() {

            @Override
            public void onAddedChannelItemClick(View itemView, int position) {
                Toast.makeText(GoodsActivity.this, "打开-" + addedChannels.get(position).title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnAddedChannelItemClick(View itemView, int position, String f) {
                if (!addedChannels.contains(unAddedChannels.get(position)) || addedChannels.size() < 4) {
                    addedChannels.add(unAddedChannels.get(position));
                }
            }

            //  不在编辑模式下的 回调
            @Override
            public void onUnAddedChangedItemClick(View itemView, int position) {
                Toast.makeText(GoodsActivity.this, "打开-" + unAddedChannels.get(position).title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDrawableClickListener(View itemView, int position) {
                Toast.makeText(GoodsActivity.this, "删除-" + GoodsActivity.this.addedChannels.get(position).title, Toast.LENGTH_SHORT).show();

                addedChannels.remove(position);


            }
        });
    }

    private void initData() {
        String[] chanles = getResources().getStringArray(R.array.service_title);
        final TypedArray proPic = getResources().obtainTypedArray(R.array.service_image);
        List<Integer> mImages = new ArrayList<>();
        for (int a = 0; a < chanles.length; a++) {
            mImages.add(proPic.getResourceId(a, R.drawable.ic_data_picture));
        }
        proPic.recycle();

        GroupItem groupFinance = new GroupItem();
        groupFinance.category = "便民生活";
        for (int i = 0; i < 8; i++) {
            ChannelItem item = new ChannelItem();
            item.id = i;
            item.title = chanles[i];
            item.category = "便民生活";
            item.resource = mImages.get(i);
            unAddedChannels.add(item);
            groupFinance.addChanelItem(item);
        }
        unAddedItems.add(groupFinance);

        GroupItem groupLife = new GroupItem();
        groupLife.category = "购物优选";
        for (int i = 8; i < 10; i++) {
            ChannelItem item = new ChannelItem();
            item.id = i;
            item.title = chanles[i];
            item.category = "购物优选";
            item.resource = mImages.get(i);
            unAddedChannels.add(item);
            groupLife.addChanelItem(item);
        }
        unAddedItems.add(groupLife);

        GroupItem groupEntertainment = new GroupItem();
        groupEntertainment.category = "社区娱乐";
        for (int i = 10; i < 13; i++) {
            ChannelItem item = new ChannelItem();
            item.id = i;
            item.title = chanles[i];
            item.category = "社区娱乐";
            item.resource = mImages.get(i);
            unAddedChannels.add(item);
            groupEntertainment.addChanelItem(item);
        }
        unAddedItems.add(groupEntertainment);

        GroupItem Grouphumanity = new GroupItem();
        Grouphumanity.category = "财富管理";
        for (int i = 13; i < 16; i++) {
            ChannelItem item = new ChannelItem();
            item.id = i;
            item.title = chanles[i];
            item.category = "财富管理";
            item.resource = mImages.get(i);
            unAddedChannels.add(item);
            Grouphumanity.addChanelItem(item);
        }
        unAddedItems.add(Grouphumanity);

        GroupItem money = new GroupItem();
        money.category = "新闻公告";
        for (int i = 16; i < 19; i++) {
            ChannelItem item = new ChannelItem();
            item.id = i;
            item.title = chanles[i];
            item.category = "新闻公告";
            item.resource = mImages.get(i);
            unAddedChannels.add(item);
            money.addChanelItem(item);
        }
        unAddedItems.add(money);

        GroupItem qita = new GroupItem();
        qita.category = "其他";
        for (int i = 19; i < 20; i++) {
            ChannelItem item = new ChannelItem();
            item.id = i;
            item.title = chanles[i];
            item.category = "其他";
            item.resource = mImages.get(i);
            unAddedChannels.add(item);
            qita.addChanelItem(item);
        }
        unAddedItems.add(qita);

    }


}
