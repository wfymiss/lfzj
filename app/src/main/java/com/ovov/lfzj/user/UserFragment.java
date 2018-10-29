package com.ovov.lfzj.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.IdentityDialog;
import com.ovov.lfzj.event.UpdateEvent;
import com.ovov.lfzj.event.UserFragmentIdentityEvent;
import com.ovov.lfzj.home.bean.SubListBean;
import com.ovov.lfzj.home.payment.activity.PayMentRecordActivity;
import com.ovov.lfzj.home.repair.RepairActivity;
import com.ovov.lfzj.home.repair.WorkerOrderActivity;
import com.ovov.lfzj.home.ui.MessageListActivity;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.login.IdentityConfirmActivity;
import com.ovov.lfzj.login.MySubActivity;
import com.ovov.lfzj.market.order.OrderActivity;
import com.ovov.lfzj.user.activity.FamilyActivity;
import com.ovov.lfzj.user.activity.HealthSubscribeActivity;
import com.ovov.lfzj.user.setting.AdvicesActivity;
import com.ovov.lfzj.user.setting.SettingActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.functions.Action1;

import static com.ovov.lfzj.CatelApplication.UAER_FRAGMENT_IDENTITY;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends BaseFragment {


    Unbinder unbinder;
    /* @BindView(R.id.tv_shop_order)
     TextView tvShopOrder;*/
//    @BindView(R.id.tv_shop_order)
//    TextView tvShopOrder;
    @BindView(R.id.tv_username_info)
    TextView tvUsernameInfo;
    /* @BindView(R.id.tv_order)
     TextView tvOrder;*/
   /* @BindView(R.id.tv_service_order)
    TextView tvServiceOrder;*/
    @BindView(R.id.tv_my)
    TextView tvMy;
    @BindView(R.id.mine_family)
    TextView mine_family;
    @BindView(R.id.my_circle_images)
    CircleImageView mCircleImages;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.tv_sign)
    TextView mTvSign;
//    @BindView(R.id.tv_myhealth)
//    TextView tvMyhealth;
    /*@BindView(R.id.tv_unitiy_message)
    TextView tvUnitiyMessage;*/
    private ActivityUtils mUtils;
    private SubListBean subListBean;

    public static UserFragment newInstance() {

        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();

        return view;
    }


    @Override
    public void init() {
        super.init();
        mUtils = new ActivityUtils(this);
        getUserInfo();
        addRxBusSubscribe(UpdateEvent.class, new Action1<UpdateEvent>() {
            @Override
            public void call(UpdateEvent updateEvent) {
                getUserInfo();
            }
        });
        addRxBusSubscribe(UserFragmentIdentityEvent.class, new Action1<UserFragmentIdentityEvent>() {
            @Override
            public void call(UserFragmentIdentityEvent userFragmentIdentityEvent) {
                IdentityConfirmActivity.toActivity(mActivity);
                mActivity.finish();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }


    @OnClick({R.id.tv_username_info, R.id.iv_right, R.id.tv_identity, R.id.my_circle_images, R.id.tv_my, R.id.mine_family, R.id.tv_worker_order,R.id.mine_advice})

    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.tv_username_info:
//                mUtils.startActivity(MessageActivity.class);
//                break;
//            case R.id.tv_order:
//                mUtils.startActivity(EvaluateActivity.class);
//                break;
        //    case R.id.tv_shop_order:
             //   mUtils.startActivity(OrderActivity.class);
           //     break;
            case R.id.mine_advice:
                AdvicesActivity.toActivity(mActivity);
                break;
//            case R.id.tv_my:
//                mUtils.startActivity(OrdersActivity.class);
//                break;
            case R.id.tv_worker_order:
                WorkerOrderActivity.toActivity(mActivity);
                break;

            case R.id.iv_right:
                SettingActivity.toActivity(mActivity);
                break;
            case R.id.tv_identity:
                MySubActivity.toActivity(mActivity);
               // IdentityConfirmActivity.toActivity(mActivity);
            //    mActivity.finish();
                break;
            case R.id.my_circle_images:
                UserInfoActivity.toActivity(mActivity, subListBean.getDatas().getUser());
                break;
            case R.id.tv_username_info:
                MessageListActivity.toActivity(mActivity);
                break;
            case R.id.tv_my:
                if (LoginUserBean.getInstance().isIs_auth())
                    PayMentRecordActivity.toActivity(mActivity);
                else {
                    IdentityDialog identityDialog = new IdentityDialog(mActivity, UAER_FRAGMENT_IDENTITY);
                    identityDialog.show();
                }
                break;

            case R.id.mine_family:
                FamilyActivity.toActivity(mActivity);
                break;

//            case R.id.tv_myhealth:
//                HealthSubscribeActivity.toActivity(mActivity);
//                break;
        }
    }

    public void getUserInfo() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().gethomeList()
                .compose(RxUtil.<SubListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<SubListBean>() {
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
                    public void onNext(SubListBean listInfoDataInfo) {

                        dismiss();
                        if (listInfoDataInfo.getCode().equals("200")) {
                            subListBean = listInfoDataInfo;
                            mTvNickname.setText(listInfoDataInfo.getDatas().getUser().nickname);
                            if (listInfoDataInfo.getDatas().getUser().signature != null && !TextUtils.isEmpty(listInfoDataInfo.getDatas().getUser().signature)) {
                                mTvSign.setText(listInfoDataInfo.getDatas().getUser().signature);
                            }
                            if (listInfoDataInfo.getDatas().getUser().user_logo != null && !listInfoDataInfo.getDatas().getUser().user_logo.equals(""))
                                Picasso.with(mActivity).load(listInfoDataInfo.getDatas().getUser().user_logo).placeholder(R.mipmap.ic_default_head).into(mCircleImages);
                            LoginUserBean.getInstance().setUserInfoBean(listInfoDataInfo.getDatas().getUser());
                            LoginUserBean.getInstance().save();
                        }
                    }
                });
        addSubscrebe(subscription);

    }
}
