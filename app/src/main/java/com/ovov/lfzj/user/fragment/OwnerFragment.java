package com.ovov.lfzj.user.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ovov.lfzj.MainActivity;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.RoomListInfo;
import com.ovov.lfzj.base.bean.SublistInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RegexUtils;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.widget.BuildingListDialog;
import com.ovov.lfzj.base.widget.RoomListDialog;
import com.ovov.lfzj.event.AddFamilySuccessEvent;
import com.ovov.lfzj.event.IdentityEvent;
import com.ovov.lfzj.event.RoomSelectEvent;
import com.ovov.lfzj.event.SubselectEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.tv_select_sub)
    TextView mTvSelectSub;
    @BindView(R.id.tv_select_room)
    TextView mEtBuildingNumber;
    String subid;
    String house_path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();

        return view;
    }

    @Override
    public void init() {
        super.init();

        addRxBusSubscribe(SubselectEvent.class, new Action1<SubselectEvent>() {
            @Override
            public void call(SubselectEvent subselectEvent) {
                subid = subselectEvent.getSub_id();
                mTvSelectSub.setText(subselectEvent.getSub_name());

            }
        });

        addRxBusSubscribe(RoomSelectEvent.class, new Action1<RoomSelectEvent>() {
            @Override
            public void call(RoomSelectEvent roomSelectEvent) {
                mEtBuildingNumber.setText(roomSelectEvent.building_name + "-" + roomSelectEvent.unit + "-" + roomSelectEvent.name);
                house_path = roomSelectEvent.building_name + "-" + roomSelectEvent.unit + "-" + roomSelectEvent.name;
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_add, R.id.tv_select_sub, R.id.tv_select_room})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_add:

                if (TextUtils.isEmpty(mTvSelectSub.getText().toString()) && mTvSelectSub.getText().equals("")) {
                    showToast("请选择小区");
                    return;
                }
                if (TextUtils.isEmpty(mEtBuildingNumber.getText().toString()) && !mEtBuildingNumber.getText().equals("")) {
                    showToast("请填写房间信息");
                    return;
                }

                if (TextUtils.isEmpty(mEtName.getText().toString()) && TextUtils.isEmpty(mEtPhone.getText().toString())) {
                    showToast(R.string.text_please_input_msg);
                    return;
                }
                if (RegexUtils.isMobile(mEtPhone.getText().toString()) != RegexUtils.VERIFY_SUCCESS) {
                    showToast(R.string.text_phone_error);
                    return;
                }
                house_path = subid + "-" + house_path;
                addFamily();

                break;

            case R.id.tv_select_sub:
                getBuildinglist();
                break;
            case R.id.tv_select_room:
                getUserHouse();
                break;
        }


    }

    private void addFamily() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().addFamily(mEtName.getText().toString().trim(), mEtPhone.getText().toString().trim(), house_path)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        showToast("添加成功");
                        RxBus.getDefault().post(new AddFamilySuccessEvent());
                        mActivity.finish();


                    }
                });
        addSubscrebe(subscription);
    }


    private void getBuildinglist() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getSubList()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<SublistInfo>>() {
                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(ListInfo<SublistInfo> buildingListResult) {
                        dismiss();
                        BuildingListDialog buildingListDialog = new BuildingListDialog(mActivity, buildingListResult.datas());
                        buildingListDialog.setWidth((int) (UIUtils.getScreenWidth() * 0.5));
                        buildingListDialog.show();
                        buildingListDialog.setData(buildingListResult.datas());

                    }
                });
        addSubscrebe(subscription);
    }


    private void getUserHouse() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getUserHouse()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<RoomListInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(ListInfo<RoomListInfo> dataInfo) {
                        RoomListDialog roomListDialog = new RoomListDialog(mActivity, dataInfo.datas());
                        roomListDialog.setWidth((int) (UIUtils.getScreenWidth() * 0.5));
                        roomListDialog.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        roomListDialog.show();
                        roomListDialog.setData(dataInfo.datas());
                        dismiss();
                    }
                });
        addSubscrebe(subscription);
    }


}
