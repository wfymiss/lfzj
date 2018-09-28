package com.ovov.lfzj.neighbour;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.adapter.FragmentBaseAdapter;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.StatusBarUtils;
import com.ovov.lfzj.base.widget.IdentityDialog;
import com.ovov.lfzj.event.NeighbourIdentityEvent;
import com.ovov.lfzj.event.ToIdentityEvent;
import com.ovov.lfzj.login.IdentityConfirmActivity;
import com.ovov.lfzj.neighbour.actiity.ActivityFragment;
import com.ovov.lfzj.neighbour.square.SquareFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.functions.Action1;

import static com.ovov.lfzj.CatelApplication.NEIGHBOUR_IDENTITY;


/**
 * A simple {@link Fragment} subclass.
 */
public class NeighbourFragment extends BaseFragment {


    @BindView(R.id.neigh_viewpage)
    ViewPager viewPager;
    @BindView(R.id.neigh_tablayout)     // 标题
            TabLayout tabLayout;
    @BindView(R.id.activity_put)
    TextView textTitile;
    ActivityUtils activityUtils;
    @BindView(R.id.sitview)
    View mSitview;
    private int changeTitle;          // 活动、论坛页面切换
    private List<String> titles = null;

    Unbinder unbinder;

    public static NeighbourFragment newInstance() {

        Bundle args = new Bundle();

        NeighbourFragment fragment = new NeighbourFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NeighbourFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_neighbour, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();

        return view;
    }


    @Override
    public void init() {
        super.init();
        initSitView();   // 获取占位视图高度
        titles = new ArrayList<>();
        titles.add("广场");
        /*titles.add("活动");*/
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
        }
        tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);   //  标题居中
        List<Fragment> list = new ArrayList<>();
        list.add(new SquareFragment());
        // list.add(new ActivityFragment());
        FragmentBaseAdapter adapter = new FragmentBaseAdapter(getActivity().getSupportFragmentManager(), list, titles);   //绑定fragment
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);  //标题与页面同步
        //切换活动论坛页面
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTitle = tab.getPosition();
                // EventBus.getDefault().post(new EventId(changeTitle));    // 活动、论坛页面切换
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        addRxBusSubscribe(NeighbourIdentityEvent.class, new Action1<NeighbourIdentityEvent>() {
            @Override
            public void call(NeighbourIdentityEvent toIdentityEvent) {
                IdentityConfirmActivity.toActivity(mActivity);
                mActivity.finish();
            }
        });

    }

    @OnClick({R.id.activity_put})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_put:         //  发布活动、论坛
                if (LoginUserBean.getInstance().isIs_auth())
                    MyCommunityActivity.toMyActivity(getActivity(), "1");
            else {
                    IdentityDialog identityDialog = new IdentityDialog(mActivity,NEIGHBOUR_IDENTITY);
                    identityDialog.show();
                }
                break;
            default:
        }
    }

    // 获取占位视图高度
    private void initSitView() {
        // 获取占位视图高度
        ViewGroup.LayoutParams sitParams = mSitview.getLayoutParams();
        sitParams.height = StatusBarUtils.getStatusBarHeight(mActivity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
