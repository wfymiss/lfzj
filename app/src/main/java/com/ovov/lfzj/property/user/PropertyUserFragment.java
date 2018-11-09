package com.ovov.lfzj.property.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;

import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.home.bean.SubListBean;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.user.setting.AboutActivity;
import com.ovov.lfzj.user.setting.SettingActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;

/**
 * Created by Administrator on 2017/10/23.
 */

public class PropertyUserFragment extends BaseFragment {
    @BindView(R.id.my_circle_images)
    CircleImageView myCircleImages;
    @BindView(R.id.my_nickname)
    TextView myNickname;
    @BindView(R.id.my_sign)
    TextView mySign;
    @BindView(R.id.my_lv)
    NoScrollGridView myLv;
    @BindView(R.id.my_fragment)
    LinearLayout myFragment;
    Unbinder unbinder;
    private int[] image = {R.mipmap.ic_lfgj_intro, R.mipmap.m_install,R.mipmap.ic_recommend_property};
    private int[] title = {R.string.text_about, R.string.text_set,R.string.text_recommend};
    private List<Map<String, Object>> data_list;
    private ActivityUtils activityUtils;
    private String token = null;


    public static PropertyUserFragment newInstance() {

        Bundle args = new Bundle();

        PropertyUserFragment fragment = new PropertyUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PropertyUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lfgj_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {
        getUserInfo();
        initGrid();
        initData();

    }

    private void initData() {
        /*LFGJInfoPresenter presenter = new LFGJInfoPresenter(this);
        presenter.getInfo(token);*/
    }

    private void initGrid() {
        data_list = new ArrayList<>();
        getbtnData();
        MyLFGJMineGridAdapter adapter = new MyLFGJMineGridAdapter(data_list, getContext());
        myLv.setAdapter(adapter);
        myLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id) {

                    case 0:
                        AboutActivity.toActivity(mActivity);
                        break;
                    case 1:
                        SettingActivity.toActivity(mActivity);
                        break;
                    case 2:
                        MyRecommendActivity.toActivity(mActivity);
                        break;
                    default:
                }
            }
        });
    }

    public List<Map<String, Object>> getbtnData() {
        for (int i = 0; i < image.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", image[i]);
            map.put("text", title[i]);
            data_list.add(map);
        }
        return data_list;
    }
    private void getUserInfo() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().gethomeList()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<SubListBean>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(SubListBean subListBean) {
                        dismiss();
                        myNickname.setText(subListBean.getDatas().admin_roles.name);
                        mySign.setText(subListBean.getDatas().admin_roles.description);

                    }
                });
        addSubscrebe(subscription);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

   /* @Override
    public void setData(PropertyInfo result) {
        Picasso.with(getContext()).load(R.mipmap.icon_property).into(myCircleImages);
        myNickname.setText(result.getData().getUsername());
        mySign.setText(result.getData().getDepartment_name() + result.getData().getPosition_name());
    }

    @Override
    public void error(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }*/
}
