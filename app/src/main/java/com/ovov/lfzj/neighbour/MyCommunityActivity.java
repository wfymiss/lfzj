package com.ovov.lfzj.neighbour;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;

import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.adapter.FragmentBaseAdapter;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.SquareDetailInfo;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.neighbour.square.MyActivityFragment;
import com.ovov.lfzj.neighbour.square.MySquareFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyCommunityActivity extends BaseActivity {

    @BindView(R.id.iv_header)
    CircleImageView mIvHeader;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.tv_sign)
    TextView mTvSign;
    @BindView(R.id.neigh_tablayout)
    TabLayout mNeighTablayout;
    @BindView(R.id.viewpage)
    ViewPager mViewpage;
    private List<String> titles;
    String MY = "1";
    String OTHER = "2";
    private String userid;

    public static void toMyActivity(Context context,String type) {
        Intent intent = new Intent(context, MyCommunityActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public static void toUserActivity(Context context, String name,String img,String type,String user_id) {
        Intent intent = new Intent(context, MyCommunityActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("img",img);
        bundle.putString("type",type);
        bundle.putString("userid",user_id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_my_community;
    }

    @Override
    public void init() {

        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("type");
        if (type.equals(MY)){
            setTitleText(R.string.text_my_community);
            userid = LoginUserBean.getInstance().getUserId();
            mTvNickname.setText(LoginUserBean.getInstance().getUserInfoBean().nickname);
            if (!LoginUserBean.getInstance().getUserInfoBean().user_logo.equals(""))
                Picasso.with(mActivity).load(LoginUserBean.getInstance().getUserInfoBean().user_logo).into(mIvHeader);
        }else {
            String name = bundle.getString("name");
            String img = bundle.getString("img");
            userid = bundle.getString("userid");
            mTvNickname.setText(name);
            if (!img.equals("")){
                Picasso.with(mActivity).load(img).into(mIvHeader);
            }
            setTitleText(name);
        }
        initTab();
    }
    private void initTab() {
        titles = new ArrayList<>();
        titles.add("广场");
        /*titles.add("活动");
        titles.add("点赞");
        titles.add("评论");*/
        for (int i = 0; i < titles.size(); i++) {
            mNeighTablayout.addTab(mNeighTablayout.newTab().setText(titles.get(i)));
        }
        mNeighTablayout.setTabMode(TabLayout.GRAVITY_CENTER);   //  标题居中
        List<Fragment> list = new ArrayList<>();
        list.add(new MySquareFragment());
        /*list.add(new MyActivityFragment());
        list.add(new MySquareFragment());
        list.add(new MyActivityFragment());*/
        FragmentBaseAdapter adapter = new FragmentBaseAdapter(this.getSupportFragmentManager(), list, titles);   //绑定fragment
        mViewpage.setAdapter(adapter);
        mNeighTablayout.setupWithViewPager(mViewpage);  //标题与页面同步

    }



    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    public  String getUserid(){
        return userid;
    }
}
