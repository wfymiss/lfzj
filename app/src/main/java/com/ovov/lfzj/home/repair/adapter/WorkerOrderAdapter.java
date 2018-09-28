package com.ovov.lfzj.home.repair.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ovov.lfzj.home.repair.fragment.WrokerOrderFragment;


/**
 * 工单列表碎片分页适配器
 * Created by lyy on 2017/8/7.
 */

public class WorkerOrderAdapter extends FragmentPagerAdapter {
    private String[] titles=new String[]{"待处理","处理中","待验收","已处理"};  //数组
    private Context context;;

    public WorkerOrderAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context=context;
    }

    /**
     * 返回要显示的Fragment的某个实例
     */
    @Override
    public Fragment getItem(int position) {
        return WrokerOrderFragment.newInstance(position+1);     // 显示的fragment页面
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    //TabLayout关联viewpager时调用次方法    返回Tab标题
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
