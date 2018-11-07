package com.ovov.lfzj.property.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class MyRecommendActivity extends BaseActivity {

    @BindView(R.id.list_recommend)
    ListView mListRecommend;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, MyRecommendActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_recommend;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_recommend);
        initList();
    }

    private void initList() {
        List<String> mData = new ArrayList<>();
        mData.add("原磊");
        mData.add("原磊1");
        CommonAdapter<String> mAdapter = new CommonAdapter<String>(mActivity,mData,R.layout.item_recommend) {
            @Override
            public void convert(ViewHolder viewHolder, String s, int i) {
                viewHolder.setText(R.id.tv_nickname,s);
            }
        };
        mListRecommend.setAdapter(mAdapter);


    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
