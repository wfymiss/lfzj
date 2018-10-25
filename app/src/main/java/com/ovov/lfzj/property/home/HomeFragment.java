package com.ovov.lfzj.property.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.banner.BannerAdapter;
import com.ovov.lfzj.base.banner.BannerLayout;

import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.home.bean.BannerBean;
import com.ovov.lfzj.home.repair.RepairActivity;
import com.ovov.lfzj.property.activity.PaymentQueryActivity;
import com.ovov.lfzj.property.home.adapter.HomeGridAdapter;
import com.ovov.lfzj.property.home.repair.WorkerOrderActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.lfgj_banner)
    BannerLayout lfgjBanner;
    @BindView(R.id.home_lfgj_grid)
    NoScrollGridView homeLfgjGrid;

    private int[] image = {R.mipmap.lfgj_gd, R.mipmap.lfgj_cb, R.mipmap.lfgj_payfee, R.mipmap.lfgj_jy, R.mipmap.lfgj_db, R.mipmap.lfgj_gg, R.mipmap.lfgj_information, R.mipmap.lfgj_phone, R.mipmap.lfgj_db, R.mipmap.icon_service_dispath, R.mipmap.icon_dispatch,R.mipmap.icon_property_commit_workorder,R.mipmap.icon_service_dispath};
    private int[] title = {R.string.gd, R.string.cb, R.string.payfee, R.string.jy, R.string.schema, R.string.sqgg, R.string.nbtz, R.string.phone, R.string.workflow, R.string.dispatch,R.string.zg_dispatch,R.string.property_commit,R.string.text_express};

    private List<Map<String, Object>> data_list;
    private ActivityUtils activityUtils;
    private String token = null;
    private String sub_id = null;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {

        initGrid();
        initBanner();

    }

    private void initBanner() {
       BannerAdapter<BannerBean> bannerAdapter = new BannerAdapter<BannerBean>() {
            @Override
            protected void bind(ViewHolder holder, BannerBean data) {
                Picasso.with(getContext()).load(data.getImg()).into(holder.mImageView);
            }

        };
       lfgjBanner.setAdapter(bannerAdapter);
    }

    private void initGrid() {

        data_list = new ArrayList<>();
        getbtnData();

        HomeGridAdapter adapter = new HomeGridAdapter(data_list, getContext());
        homeLfgjGrid.setAdapter(adapter);
        homeLfgjGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent;
                switch (position) {
                    case 0:
                        //维修工单界面
                        WorkerOrderActivity.toActivity(getContext());
                        break;
                    case 1:
                        break;
                    case 2:
                        if (activityUtils == null) {
                            activityUtils = new ActivityUtils(HomeFragment.this);
                        }
                        activityUtils.startActivity(PaymentQueryActivity.class);
                        break;
                    case 3:
                        //投诉建议界面
                        break;
                    case 4:              //   备忘事项
                        break;
                    case 5:
                        // 社区公告
                        break;
                    case 6:
                        // 内部通知
                        break;
                    case 7:
                        //常用电话界面
                        break;
                    case 8:
                        // 工作流程
                        break;
                    case 9:
                        //维修工单界面
                        break;
                    case 10:
                        //维修工单界面
                        break;
                    case 11:
                        break;
                    case 12:
                        break;
                    default:
                }
            }
        });
    }

    // 首页GridView 功能菜单
    public List<Map<String, Object>> getbtnData() {
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", image[i]);
            map.put("text", title[i]);
            data_list.add(map);
        }
        return data_list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

}
