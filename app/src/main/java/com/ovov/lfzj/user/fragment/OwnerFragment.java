package com.ovov.lfzj.user.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ovov.lfzj.MainActivity;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.SublistInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RegexUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.widget.BuildingListDialog;
import com.ovov.lfzj.event.IdentityEvent;
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
    @BindView(R.id.et_building_number)
    EditText mEtBuildingNumber;
    @BindView(R.id.et_unit_number)
    EditText mEtUnitNumber;
    @BindView(R.id.et_room_number)
    EditText mEtRoomNumber;

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
                mTvSelectSub.setText(subselectEvent.getSub_name());
                mEtBuildingNumber.removeTextChangedListener(mTextWatcherbuilding);
                mEtUnitNumber.removeTextChangedListener(mTextWatcherUnit);
                mEtRoomNumber.removeTextChangedListener(mTextWatcherRoom);
                mEtBuildingNumber.setText("");
                mEtUnitNumber.setText("");
                mEtRoomNumber.setText("");
                mEtBuildingNumber.addTextChangedListener(mTextWatcherbuilding);
                mEtUnitNumber.addTextChangedListener(mTextWatcherUnit);
                mEtRoomNumber.addTextChangedListener(mTextWatcherRoom);

            }
        });

        mEtBuildingNumber.addTextChangedListener(mTextWatcherbuilding);
        mEtUnitNumber.addTextChangedListener(mTextWatcherUnit);
        mEtRoomNumber.addTextChangedListener(mTextWatcherRoom);
    }

    TextWatcher mTextWatcherbuilding = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEtUnitNumber.removeTextChangedListener(mTextWatcherUnit);
            mEtRoomNumber.removeTextChangedListener(mTextWatcherRoom);
            mEtUnitNumber.setText("");
            mEtRoomNumber.setText("");
            mEtUnitNumber.addTextChangedListener(mTextWatcherUnit);
            mEtRoomNumber.addTextChangedListener(mTextWatcherRoom);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher mTextWatcherUnit = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEtRoomNumber.removeTextChangedListener(mTextWatcherRoom);
            mEtRoomNumber.setText("");
            mEtRoomNumber.addTextChangedListener(mTextWatcherRoom);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher mTextWatcherRoom = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_add, R.id.tv_select_sub})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_add:

                if (TextUtils.isEmpty(mTvSelectSub.getText().toString()) && mTvSelectSub.getText().equals("")) {
                    showToast("请选择小区");
                    return;
                }
                if (TextUtils.isEmpty(mEtBuildingNumber.getText().toString()) && mEtBuildingNumber.getText().equals("") ) {
                    showToast("请填写房间信息");
                    return;
                }
                if (TextUtils.isEmpty(mEtUnitNumber.getText().toString()) && mEtUnitNumber.getText().equals("")) {
                    showToast("请选择小区");
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
                addFamily();

                break;

            case R.id.tv_select_sub:
                getBuildinglist();
                break;
        }


    }


    private void addFamily() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().addFamily(mEtName.getText().toString().trim(), mEtPhone.getText().toString().trim())
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

}
