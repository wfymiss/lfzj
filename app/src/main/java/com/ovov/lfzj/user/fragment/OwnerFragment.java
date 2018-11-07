package com.ovov.lfzj.user.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RegexUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_phone)
    EditText mEtPhone;

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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        if (TextUtils.isEmpty(mEtName.getText().toString()) && TextUtils.isEmpty(mEtPhone.getText().toString())){
            showToast(R.string.text_please_input_msg);
            return;
        }
        if (RegexUtils.isMobile(mEtPhone.getText().toString()) != RegexUtils.VERIFY_SUCCESS) {
            showToast(R.string.text_phone_error);
            return;
        }
        addFamily();
    }
    private void addFamily(){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().addFamily(mEtName.getText().toString().trim(),mEtPhone.getText().toString().trim())
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
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
                    public void onNext(DataInfo dataInfo) {

                        dismiss();
                        showToast("添加成功");
                    }
                });
        addSubscrebe(subscription);
    }
}
