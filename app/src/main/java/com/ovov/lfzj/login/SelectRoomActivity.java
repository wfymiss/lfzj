package com.ovov.lfzj.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.MainActivity;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.RoomInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.event.IdentityEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by kaite on 2018/9/20.
 */

public class SelectRoomActivity extends BaseActivity {
    @BindView(R.id.list_sub)
    ListView mListSub;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    List<RoomInfo.HousesListBean> mData = new ArrayList<>();
    private String sub_id;
    private String subStrid;
    private CommonAdapter<RoomInfo.HousesListBean> mAdapter;
    private String building_id;
    private String unit_id;
    private String roomId;
    private int posistion = -1;

    public static void toActivity(Context context, String sub_id, String subStrid,String building_id,String unit_id) {

        Intent intent = new Intent(context, SelectRoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("sub_id", sub_id);
        bundle.putString("subStrid",subStrid);
        bundle.putString("building_id",building_id);
        bundle.putString("unit_id", unit_id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_room;
    }

    @Override
    public void init() {
        setTitleText(R.string.text_select_room);
        setRightText("放弃认证");
        Bundle bundle = getIntent().getExtras();
        sub_id = bundle.getString("sub_id");
        subStrid = bundle.getString("subStrid");
        building_id = bundle.getString("building_id");
        unit_id = bundle.getString("unit_id");
        initList();
        initData();

    }

    private void initData() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getRoom(sub_id,building_id,unit_id)
                .compose(RxUtil.<DataInfo<RoomInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<RoomInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            showError(e.getMessage());
                        }

                    }

                    @Override
                    public void onNext(DataInfo<RoomInfo> unitInfoListInfo) {
                        dismiss();
                        tvUnit.setText(unitInfoListInfo.datas().building_unit_name);
                        mData.addAll(unitInfoListInfo.datas().houses_list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
        addSubscrebe(subscription);
    }
    private void authStep2(String house_path){
        showLoadingDialog();
        showError(house_path);
        Subscription subscription = RetrofitHelper.getInstance().authStep2(house_path)
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException){
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        }else {
                            doFailed();
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast("认证成功");
                        RxBus.getDefault().post(new IdentityEvent());
                        LoginUserBean.getInstance().setIs_auth(true);
                        LoginUserBean.getInstance().save();
                        MainActivity.toActivity(mActivity);
                        finish();
                    }
                });
        addSubscrebe(subscription);
    }

    private void initList() {
        mAdapter = new CommonAdapter<RoomInfo.HousesListBean>(mActivity,mData, R.layout.item_room) {
            @SuppressLint("NewApi")
            @Override
            public void convert(ViewHolder viewHolder, final RoomInfo.HousesListBean unitInfo, int i) {
                viewHolder.setText(R.id.tv_room,unitInfo.house_number);
                final RelativeLayout reContainer = viewHolder.getView(R.id.container);
                if (posistion == i){
                    reContainer.setBackground(getDrawable(R.color.common_bg));
                    roomId = String.valueOf(unitInfo.house_number);
                }else {
                    reContainer.setBackground(mActivity.getDrawable(R.drawable.shape_white));
                }
            }
        };
        mListSub.setAdapter(mAdapter);
        mListSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posistion = (int) id;
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.tv_right,R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                MainActivity.toActivity(mActivity);
                break;
            case R.id.btn_commit:
                if (posistion == -1){
                    showToast("请选择房间");
                    return;
                }
                String housepath = sub_id+"-"+building_id+"-"+unit_id+"-"+roomId;
                authStep2(housepath);
                break;
        }
    }
}
