package com.ovov.lfzj.neighbour.square;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.adapter.FragmentBaseAdapter;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.SquareDetailInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.widget.EditDialog;
import com.ovov.lfzj.base.widget.IdentityDialog;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.ShareAppPopup;
import com.ovov.lfzj.event.AddCommentEvent;
import com.ovov.lfzj.event.GoodEvent;
import com.ovov.lfzj.event.SendEvent;
import com.ovov.lfzj.event.SquareDetailIdentityEvent;
import com.ovov.lfzj.event.ToIdentityEvent;
import com.ovov.lfzj.event.TransmitEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.login.IdentityConfirmActivity;
import com.ovov.lfzj.neighbour.TransmitActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.functions.Action1;

import static com.ovov.lfzj.CatelApplication.SQUARE_DETAIL_IDENTITY;
import static com.ovov.lfzj.CatelApplication.isGood;

public class SquareDetailActivity extends BaseActivity {


    @BindView(R.id.gridView)
    NoScrollGridView mGridView;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpage)
    ViewPager mViewpage;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.iv_head)
    CircleImageView mIvHead;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_transmit_nickname)
    TextView mTvTransmitNickname;
    @BindView(R.id.tv_transmit_content)
    TextView mTvTransmitContent;
    @BindView(R.id.transmit_gridView)
    NoScrollGridView mTransmitGridView;
    @BindView(R.id.re_transmit_content)
    RelativeLayout mReTransmitContent;
    @BindView(R.id.iv_good)
    ImageView mIvGood;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    private String id;
    private String commentNum;
    private String goodNum;
    private String forwardNum;
    private List<String> mGridData;
    private CommonAdapter<String> mGridAdapter;
    private SquareDetailInfo mSquareDetailInfo;
    private String mImg;
    private EditDialog editDialog;
    private CommonAdapter<String> mTransmitGridAdapter;
    private List<String> mTransmitGridData;
    private boolean mIsGood;
    private int posistion;
    private IdentityDialog identityDialog;
    private int type;

    public static void toActivity(Context context, String id, int posistion, int type) {
        Intent intent = new Intent(context, SquareDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("posistion", posistion);
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_square_detail;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        id = getIntent().getExtras().getString("id");
        posistion = getIntent().getExtras().getInt("posistion");
        type = getIntent().getExtras().getInt("type");

        identityDialog = new IdentityDialog(mActivity, SQUARE_DETAIL_IDENTITY);
        mIvRight.setImageResource(R.mipmap.ic_share);
        mIvRight.setVisibility(View.VISIBLE);

        mGridData = new ArrayList<>();
        mTransmitGridData = new ArrayList<>();
        mGridAdapter = new CommonAdapter<String>(SquareDetailActivity.this, mGridData, R.layout.user_img_item) {
            @Override
            public void convert(ViewHolder viewHolder, String s, int i) {
                ImageView ivGrid = viewHolder.getView(R.id.iv_user_img);
                Picasso.with(SquareDetailActivity.this).load(s).into(ivGrid);
            }
        };
        mGridView.setAdapter(mGridAdapter);
        mTransmitGridAdapter = new CommonAdapter<String>(SquareDetailActivity.this, mTransmitGridData, R.layout.user_img_item) {
            @Override
            public void convert(ViewHolder viewHolder, String s, int i) {
                ImageView ivGrid = viewHolder.getView(R.id.iv_user_img);
                Picasso.with(SquareDetailActivity.this).load(s).into(ivGrid);
            }
        };
        mTransmitGridView.setAdapter(mTransmitGridAdapter);
        mReTransmitContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SquareDetailActivity.toActivity(mActivity, mSquareDetailInfo.transpondInfo.id, posistion, 1);
            }
        });
        mTransmitGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SquareDetailActivity.toActivity(mActivity, mSquareDetailInfo.transpondInfo.id, position, 1);
            }
        });

        getSquareDetail();


//        addRxBusSubscribe(SendEvent.class, new Action1<SendEvent>() {
//            @Override
//            public void call(SendEvent sendEvent) {
//
//
//            }
//        });
        addRxBusSubscribe(TransmitEvent.class, new Action1<TransmitEvent>() {
            @Override
            public void call(TransmitEvent transmitEvent) {
                finish();
            }
        });
        addRxBusSubscribe(SquareDetailIdentityEvent.class, new Action1<SquareDetailIdentityEvent>() {
            @Override
            public void call(SquareDetailIdentityEvent toIdentityEvent) {
                IdentityConfirmActivity.toActivity(mActivity);
                mActivity.finish();
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void send(SendEvent sendEvent) {
        addComment(sendEvent.content);

    }

    private void initTab() {
        List<String> titles = new ArrayList<>();
        titles.add(commentNum);
        titles.add(goodNum);
        titles.add(forwardNum);
        for (int i = 0; i < titles.size(); i++) {
            mTablayout.addTab(mTablayout.newTab().setText(titles.get(i)));
        }
        mTablayout.setTabMode(TabLayout.GRAVITY_CENTER);   //  标题居中
        List<Fragment> list = new ArrayList<>();
        list.add(new SquareCommentFragment());
        list.add(new BaseGoodFragment());
        list.add(new SquareRelayFragment());

        FragmentBaseAdapter adapter = new FragmentBaseAdapter(this.getSupportFragmentManager(), list, titles);   //绑定fragment
        mViewpage.setAdapter(adapter);
        mTablayout.setupWithViewPager(mViewpage);  //标题与页面同步

    }


    @OnClick({R.id.iv_back, R.id.iv_right, R.id.re_pl, R.id.re_good, R.id.re_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right:
                String url = "http://life.catel-link.com/comment/share?id=" + mSquareDetailInfo.id;
                showShareApp(url, "乐福院子", mSquareDetailInfo.comment, "");
                break;
            case R.id.re_pl:
                if (LoginUserBean.getInstance().isIs_auth()) {
                    editDialog = new EditDialog(mActivity, Integer.parseInt(id));
                    editDialog.setWidth((int) (UIUtils.getScreenWidth() * 0.7));
                    editDialog.show();
                } else {
                    identityDialog.show();
                }


                break;
            case R.id.re_good:
                if (LoginUserBean.getInstance().isIs_auth())
                    squareGood();
                else
                    identityDialog.show();
                break;
            case R.id.re_share:
                //showShareApp("","乐福院子","乐福院子","");
                if (LoginUserBean.getInstance().isIs_auth()) {
                    if (mSquareDetailInfo.imgUrl.size() > 0) {
                        mImg = mSquareDetailInfo.imgUrl.get(0);
                    } else {
                        mImg = "";
                    }
                    TransmitActivity.toActivity(mActivity, mSquareDetailInfo.userInfo.nickname, mSquareDetailInfo.comment, mImg, mSquareDetailInfo.id);
                } else {
                    identityDialog.show();
                }

                break;
        }
    }

    private void squareGood() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().squareGood(id)
                .compose(RxUtil.<DataInfo<String>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            //showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DataInfo<String> dataInfo) {
                        dismiss();

                        showToast(dataInfo.datas());
                        if (mSquareDetailInfo.isZan == isGood) {
                            mIvGood.setSelected(false);
                            mIsGood = false;
                        } else {
                            mIvGood.setSelected(true);
                            mIsGood = true;
                        }
                        if (type != 1)
                            RxBus.getDefault().post(new GoodEvent(mIsGood, posistion));

                        getSquareDetail();

                    }
                });
        addSubscrebe(subscription);
    }

    private void showShareApp(String url, String title, String content, String pic) {

        // 开启 popup 时界面透明
        ShareAppPopup shareAppPopup = new ShareAppPopup(mActivity, url, title, content, pic);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        // popupwindow 第一个参数指定popup 显示页面
        shareAppPopup.showAtLocation(findViewById(R.id.layout_square), Gravity.BOTTOM | Gravity.BOTTOM, 0, 0);     // 第一个参数popup显示activity页面
        // popup 退出时界面恢复
        shareAppPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
    }

    private void addComment(String content) {
        showLoadingDialog();

        Subscription subscription = RetrofitHelper.getInstance().addSquareComment(mSquareDetailInfo.id, content)
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {

                            doFailed();
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        editDialog.dismiss();
                        showToast(R.string.text_comment_success);
                        if (type != 1)
                            RxBus.getDefault().post(new AddCommentEvent(posistion));
                        getSquareDetail();
                    }
                });
        addSubscrebe(subscription);
    }

    private void getSquareDetail() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().getSquareDetail(id)
                .compose(RxUtil.<DataInfo<SquareDetailInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<SquareDetailInfo>>() {
                    @Override
                    public void onError(Throwable e) {

                        dismiss();
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            showToast(dataResultException.errorInfo);
                        } else {
                            doFailed();
                            showError(e.getMessage());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(final DataInfo<SquareDetailInfo> squareDetailInfoDataInfo) {
                        dismiss();
                        mSquareDetailInfo = squareDetailInfoDataInfo.datas();
                        setTitleText(squareDetailInfoDataInfo.datas().userInfo.nickname);
                        mTvContent.setText(squareDetailInfoDataInfo.datas().comment);
                        mTvNickname.setText(squareDetailInfoDataInfo.datas().userInfo.nickname);
                        if (squareDetailInfoDataInfo.datas().userInfo.user_logo != null && !TextUtils.isEmpty(squareDetailInfoDataInfo.datas().userInfo.user_logo))
                            Picasso.with(mActivity).load(squareDetailInfoDataInfo.datas().userInfo.user_logo).placeholder(R.mipmap.ic_default_head).error(R.mipmap.ic_default_head).into(mIvHead);
                        mTvTime.setText(squareDetailInfoDataInfo.datas().time);
                        commentNum = String.format(getString(R.string.text_pl_num), String.valueOf(squareDetailInfoDataInfo.datas().replyNum));
                        goodNum = String.format(getString(R.string.text_good_num), String.valueOf(squareDetailInfoDataInfo.datas().zanNum));
                        forwardNum = String.format(getString(R.string.text_replay_num), String.valueOf(squareDetailInfoDataInfo.datas().forwardNum));
                        if (squareDetailInfoDataInfo.datas().isZan == isGood) {
                            mIvGood.setSelected(true);
                        } else {
                            mIvGood.setSelected(false);
                        }

                        if (squareDetailInfoDataInfo.datas().transpondInfo != null) {
                            mReTransmitContent.setVisibility(View.VISIBLE);
                            mGridView.setVisibility(View.GONE);
                            mTvTransmitContent.setText(squareDetailInfoDataInfo.datas().transpondInfo.comment);
                            mTvTransmitNickname.setText("@" + squareDetailInfoDataInfo.datas().transpondInfo.userInfo.nickname + ":");

                            if (squareDetailInfoDataInfo.datas().transpondInfo.imgUrl.size() > 0) {
                                mTransmitGridData.clear();
                                mTransmitGridData.addAll(squareDetailInfoDataInfo.datas().transpondInfo.imgUrl);
                                mTransmitGridAdapter.notifyDataSetChanged();
                            }
                        } else {
                            mReTransmitContent.setVisibility(View.GONE);
                            mGridView.setVisibility(View.VISIBLE);
                            if (squareDetailInfoDataInfo.datas().imgUrl.size() > 0) {
                                mGridData.clear();
                                mGridData.addAll(squareDetailInfoDataInfo.datas().imgUrl);
                                mGridAdapter.notifyDataSetChanged();
                            }
                        }

                        initTab();
                    }
                });
        addSubscrebe(subscription);
    }

    public SquareDetailInfo getSquareDetailinfo() {
        return mSquareDetailInfo;
    }

}
