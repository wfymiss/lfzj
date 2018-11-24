package com.ovov.lfzj.neighbour.square;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.SquareDetailInfo;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.KeyBoardShowListener;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.widget.IdentityDialog;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.ScaleImageView;
import com.ovov.lfzj.base.widget.ShareAppPopup;
import com.ovov.lfzj.base.widget.TransmitpopupWindow;
import com.ovov.lfzj.event.GoodEvent;
import com.ovov.lfzj.event.ShareSquareEvent;
import com.ovov.lfzj.event.SquareDetailIdentityEvent;
import com.ovov.lfzj.event.TransmitSquareEvent;
import com.ovov.lfzj.home.repair.WorkerOrderActivity;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.api.CatelApiService;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.login.IdentityConfirmActivity;
import com.ovov.lfzj.neighbour.MyCommunityActivity;
import com.ovov.lfzj.neighbour.TransmitActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.functions.Action1;

import static com.ovov.lfzj.CatelApplication.SQUARE_DETAIL_IDENTITY;
import static com.ovov.lfzj.CatelApplication.UAER_FRAGMENT_IDENTITY;
import static com.ovov.lfzj.CatelApplication.isGood;
import static com.ovov.lfzj.CatelApplication.noGood;

public class SquareDetailActivity extends BaseActivity {


    @BindView(R.id.list_square_detail)
    ListView mListSquareDetail;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefresh;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.tv_send)
    TextView mTvSend;
    @BindView(R.id.iv_good)
    ImageView mIvGood;
    @BindView(R.id.re_good)
    RelativeLayout mReGood;
    @BindView(R.id.activity_share)
    ImageView mActivityShare;
    @BindView(R.id.re_trasmit)
    RelativeLayout mReTrasmi;
    private String id;
    List<SquareDetailInfo.ReplyBean> mData = new ArrayList<>();
    private CommonAdapter<SquareDetailInfo.ReplyBean> mAdapter;
    private List<SquareDetailInfo.ReplyBean.RevertBean> mChildData = new ArrayList<>();
    private View view;
    private List<String> mGridData = new ArrayList<>();
    private CommonAdapter<String> mGridAdapter;
    private int TYPE_COMMENT = 1;
    private int TYPE_REPLY = 2;
    private int replyType;
    private String reply_id;
    private KeyBoardShowListener keyBoardShowListener;
    private InputMethodManager imm;
    private CircleImageView mIvHeader;
    private TextView mTvNickname;
    private TextView mTvTime;
    private TextView mTvContent;
    private NoScrollGridView mGridSelf;
    private RelativeLayout mReTransmit;
    private TextView mTvTransmitContent;
    private NoScrollGridView mGridTransmit;
    private TextView mTvGoods;
    private boolean good;
    private int posistion;
    private LinearLayout mLayoutsquare;
    private SquareDetailInfo squareDetailInfo;
    private SquareListInfo squareListInfo;

    public static void toActivity(Context context, int posistion, SquareListInfo squareListInfo, String id) {
        Intent intent = new Intent(context, SquareDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("posistion", posistion);
        bundle.putSerializable("squarelist", squareListInfo);
        bundle.putString("id", id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_square_detail;
    }


    @Override
    public void init() {
        replyType = TYPE_COMMENT;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setTitleText("");
        Bundle bundle = getIntent().getExtras();
        squareListInfo = (SquareListInfo) bundle.getSerializable("squarelist");
        posistion = bundle.getInt("posistion");
        String type = bundle.getString("id");
        id = String.valueOf(type);
        setTitleText(squareListInfo.userInfo.nickname);
        view = LayoutInflater.from(mActivity).inflate(R.layout.square_list_header, null, false);
        mGridAdapter = new CommonAdapter<String>(mActivity, mGridData, R.layout.user_img_item) {
            @Override
            public void convert(ViewHolder viewHolder, String s, int i) {
                ImageView ivImage = viewHolder.getView(R.id.iv_user_img);
                Picasso.with(mActivity).load(s).into(ivImage);
                ivImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScaleImageView scaleImageView = new ScaleImageView(mActivity);
                        scaleImageView.setUrls(mGridData, i);
                        scaleImageView.create();
                    }
                });
            }
        };
        initList();
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getSquareDetail();
            }
        });
        mRefresh.setEnableLoadmore(false);
        mRefresh.autoRefresh();
        keyBoardShowListener = new KeyBoardShowListener(mActivity);
        keyBoardShowListener.setKeyboardListener(
                new KeyBoardShowListener.OnKeyboardVisibilityListener() {
                    @Override
                    public void onVisibilityChanged(boolean visible) {
                        if (visible) {
                            //软键盘已弹出
                            mReGood.setVisibility(View.GONE);
                            mReTrasmi.setVisibility(View.GONE);
                            mTvSend.setVisibility(View.VISIBLE);
                        } else {
                            //软键盘未弹出
                            if (TextUtils.isEmpty(mEtComment.getText().toString().trim())) {
                                replyType = TYPE_COMMENT;
                                mEtComment.setHint("写评论...");
                                mReGood.setVisibility(View.VISIBLE);
                                mReTrasmi.setVisibility(View.VISIBLE);
                                mTvSend.setVisibility(View.GONE);
                            } else {
                                mReGood.setVisibility(View.GONE);
                                mReTrasmi.setVisibility(View.GONE);
                                mTvSend.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                }, mActivity);


        addRxBusSubscribe(TransmitSquareEvent.class, new Action1<TransmitSquareEvent>() {
            @Override
            public void call(TransmitSquareEvent transmitSquareEvent) {
                if (squareDetailInfo.imgUrl.size() > 0)
                    TransmitActivity.toActivity(mActivity, squareDetailInfo.userInfo.nickname, squareDetailInfo.comment, squareDetailInfo.imgUrl.get(0), squareDetailInfo.id);
                else
                    TransmitActivity.toActivity(mActivity, squareDetailInfo.userInfo.nickname, squareDetailInfo.comment, "", squareDetailInfo.id);

            }
        });
        addRxBusSubscribe(ShareSquareEvent.class, new Action1<ShareSquareEvent>() {
            @Override
            public void call(ShareSquareEvent shareSquareEvent) {
                String url = CatelApiService.HOST + "v1/user/share?id=" + squareDetailInfo.id + "&s=" + "/v1/user/share";
                showShareApp(url, "乐福院子", squareDetailInfo.comment, "");
            }
        });
        addRxBusSubscribe(SquareDetailIdentityEvent.class, new Action1<SquareDetailIdentityEvent>() {
            @Override
            public void call(SquareDetailIdentityEvent toIdentityEvent) {
                IdentityConfirmActivity.toActivity(mActivity);
                finish();
            }
        });


    }

    private void initList() {

        mAdapter = new CommonAdapter<SquareDetailInfo.ReplyBean>(mActivity, mData, R.layout.item_activity_comment) {
            @Override
            public void convert(ViewHolder viewHolder, SquareDetailInfo.ReplyBean replyBean, int i) {
                viewHolder.setText(R.id.tv_nickname, replyBean.userInfo.nickname);
                viewHolder.setText(R.id.tv_content, replyBean.content);
                viewHolder.setText(R.id.tv_time, replyBean.time);
                CircleImageView ivHeader = viewHolder.getView(R.id.iv_header);
                NoScrollGridView mChildGrid = viewHolder.getView(R.id.grid_child);
                RelativeLayout mReComment = viewHolder.getView(R.id.re_comment);
                mReComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        reply_id = String.valueOf(replyBean.id);
                        replyType = TYPE_REPLY;
                        mEtComment.setHint("回复:" + replyBean.userInfo.nickname);
                        mEtComment.setFocusable(true);
                        mEtComment.setFocusableInTouchMode(true);
                        mEtComment.requestFocus();
                    }
                });
                if (replyBean.revert.size() > 0) {
                    mChildGrid.setVisibility(View.VISIBLE);
                } else {
                    mChildGrid.setVisibility(View.GONE);
                }
                mChildData.clear();
                mChildData.addAll(replyBean.revert);
                CommonAdapter<SquareDetailInfo.ReplyBean.RevertBean> mChildAdapter = new CommonAdapter<SquareDetailInfo.ReplyBean.RevertBean>(mActivity, mChildData, R.layout.item_square_detail_child) {
                    @Override
                    public void convert(ViewHolder viewHolder, SquareDetailInfo.ReplyBean.RevertBean revertBean, int i) {
                        TextView tvContent = viewHolder.getView(R.id.tv_content);
                        String content;
                        if (TextUtils.isEmpty(revertBean.nickname)) {
                            content = "" + "<font color='#3385FF'>" + revertBean.mobile + ":</font>" + "<font color='#6F6F6F'>" + revertBean.content + "</font>";
                            tvContent.setText(Html.fromHtml(content));
                        } else {
                            content = "" + "<font color='#3385FF'>" + revertBean.nickname + ":</font>" + "<font color='#6F6F6F'>" + revertBean.content + "</font>";
                            tvContent.setText(Html.fromHtml(content));
                        }
                    }
                };
                mChildGrid.setAdapter(mChildAdapter);
                if (replyBean.userInfo.user_logo != null && !replyBean.userInfo.user_logo.equals("")) {
                    Picasso.with(mActivity).load(replyBean.userInfo.user_logo).into(ivHeader);
                } else {
                    Picasso.with(mActivity).load(R.mipmap.ic_default_head).into(ivHeader);
                }
                ivHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyCommunityActivity.toUserActivity(mActivity, replyBean.userInfo.nickname, replyBean.userInfo.user_logo, "2", replyBean.user_id, replyBean.userInfo.signature);
                    }
                });
            }
        };

        mListSquareDetail.setAdapter(mAdapter);
        mListSquareDetail.addHeaderView(view);
        mIvHeader = view.findViewById(R.id.iv_head);
        mTvNickname = view.findViewById(R.id.tv_nickname);
        mTvTime = view.findViewById(R.id.tv_time);
        mTvContent = view.findViewById(R.id.tv_content);
        mGridSelf = view.findViewById(R.id.gridView);
        mReTransmit = view.findViewById(R.id.re_transmit_content);
        mTvTransmitContent = view.findViewById(R.id.tv_transmit_content);
        mGridTransmit = view.findViewById(R.id.transmit_gridView);
        mTvGoods = view.findViewById(R.id.tv_goods);
        mLayoutsquare = view.findViewById(R.id.layout_square);

        mTvGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodActivity.toActivity(mActivity, squareDetailInfo.id);
            }
        });
        if (id.equals(squareListInfo.id)) {
            mLayoutsquare.setVisibility(View.VISIBLE);
            if (squareListInfo.userInfo.user_logo != null && !TextUtils.isEmpty(squareListInfo.userInfo.user_logo)) {
                Picasso.with(mActivity).load(squareListInfo.userInfo.user_logo).into(mIvHeader);
            }
            mTvNickname.setText(squareListInfo.userInfo.nickname);
            mTvTime.setText(squareListInfo.time);
            mTvContent.setText(squareListInfo.comment);
            if (squareListInfo.imgUrl.size() > 0) {
                mGridData.clear();
                mGridData.addAll(squareListInfo.imgUrl);
                mGridSelf.setAdapter(mGridAdapter);
            }


            if (squareListInfo.transpondInfo != null) {
                mReTransmit.setVisibility(View.VISIBLE);
                mTvTransmitContent.setText(squareListInfo.transpondInfo.comment);
                if (squareListInfo.transpondInfo.imgUrl.size() > 0) {
                    mGridData.clear();
                    mGridData.addAll(squareListInfo.transpondInfo.imgUrl);
                    mGridTransmit.setAdapter(mGridAdapter);
                }
            } else {
                mReTransmit.setVisibility(View.GONE);
            }
        }
    }


    @OnClick({R.id.iv_back, R.id.iv_right, R.id.re_trasmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right:
               /* String url = "http://life.catel-link.com/comment/share?id=" + mSquareDetailInfo.id;
                showShareApp(url, "乐福院子", mSquareDetailInfo.comment, "");*/
                break;
            case R.id.re_trasmit:
                if (LoginUserBean.getInstance().isIs_auth())
                    showTransmit();
                else {
                    IdentityDialog identityDialog = new IdentityDialog(mActivity, SQUARE_DETAIL_IDENTITY);
                    identityDialog.show();
                }

                break;
        }
    }

    //设置头像
    private void showTransmit() {
        TransmitpopupWindow pop = new TransmitpopupWindow(mActivity);
        // 开启 popup 时界面透明
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.7f;
//                if (bgAlpha == 1) {
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
//                } else {
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
//                }
        this.getWindow().setAttributes(lp);
        // popupwindow 第一个参数指定popup 显示页面
        pop.showAtLocation(this.findViewById(R.id.layout_square_1), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);     // 第一个参数popup显示activity页面
        // popup 退出时界面恢复
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
    }

    private void squareGood() {
        Subscription subscription = RetrofitHelper.getInstance().squareGood(id)
                .compose(RxUtil.<DataInfo<String>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<String>>() {
                    @Override
                    public void onError(Throwable e) {
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
                        showToast(dataInfo.datas());
                        mRefresh.autoRefresh();
                        RxBus.getDefault().post(new GoodEvent(good, posistion));
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
        shareAppPopup.showAtLocation(findViewById(R.id.layout_square_1), Gravity.BOTTOM | Gravity.BOTTOM, 0, 0);     // 第一个参数popup显示activity页面
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

        Subscription subscription = RetrofitHelper.getInstance().addSquareComment(id, content)
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
                        showToast("评论成功");
                        mRefresh.autoRefresh();
                        mEtComment.setText("");
                        imm.hideSoftInputFromWindow(mEtComment.getWindowToken(), 0);

                    }
                });
        addSubscrebe(subscription);
    }

    private void addReply(String content) {

        Subscription subscription = RetrofitHelper.getInstance().addSquareReply(reply_id, content)
                .compose(RxUtil.<DataInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo>() {
                    @Override
                    public void onError(Throwable e) {
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
                        showToast("回复成功");
                        replyType = TYPE_COMMENT;
                        mRefresh.autoRefresh();
                        mEtComment.setText("");
                        mEtComment.setHint("写评论...");
                        imm.hideSoftInputFromWindow(mEtComment.getWindowToken(), 0);

                    }
                });
        addSubscrebe(subscription);
    }

    private void getSquareDetail() {
        Log.e("id", id);
        Subscription subscription = RetrofitHelper.getInstance().getSquareDetail(id)
                .compose(RxUtil.<DataInfo<SquareDetailInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DataInfo<SquareDetailInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        mRefresh.finishRefresh();
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

                        squareDetailInfo = squareDetailInfoDataInfo.datas();

                        mRefresh.finishRefresh();
                        mData.clear();
                        mData.addAll(squareDetailInfoDataInfo.datas().replys);
                        mAdapter.notifyDataSetChanged();
                        if (!TextUtils.isEmpty(squareDetailInfoDataInfo.datas().zanStr)) {
                            mTvGoods.setVisibility(View.VISIBLE);
                            mTvGoods.setText(squareDetailInfoDataInfo.datas().zanStr);
                        } else {
                            mTvGoods.setVisibility(View.GONE);
                        }

                        mTvSend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(mEtComment.getText().toString().trim())) {
                                    showToast("请输入评论");
                                    return;
                                }
                                Log.e("typeeeeee", replyType + "");
                                if (replyType == TYPE_COMMENT) {
                                    addComment(mEtComment.getText().toString().trim());
                                } else {
                                    if (LoginUserBean.getInstance().isIs_auth())
                                        addReply(mEtComment.getText().toString().trim());
                                    else {
                                        IdentityDialog identityDialog = new IdentityDialog(mActivity, SQUARE_DETAIL_IDENTITY);
                                        identityDialog.show();
                                    }

                                }
                            }
                        });
                        if (squareDetailInfoDataInfo.datas().isZan == isGood) {
                            mIvGood.setSelected(true);
                        } else {
                            mIvGood.setSelected(false);
                        }
                        mReGood.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (LoginUserBean.getInstance().isIs_auth()) {
                                    squareGood();
                                    if (squareDetailInfoDataInfo.datas().isZan == isGood) {
                                        good = false;
                                    } else {
                                        good = true;
                                    }
                                } else {
                                    IdentityDialog identityDialog = new IdentityDialog(mActivity, SQUARE_DETAIL_IDENTITY);
                                    identityDialog.show();
                                }

                            }
                        });

                        if (!squareDetailInfo.id.equals(squareListInfo.id)) {
                            mLayoutsquare.setVisibility(View.VISIBLE);
                            setTitleText(squareDetailInfo.userInfo.nickname);
                            if (squareDetailInfo.userInfo.user_logo != null && !TextUtils.isEmpty(squareDetailInfo.userInfo.user_logo)) {
                                Picasso.with(mActivity).load(squareDetailInfo.userInfo.user_logo).into(mIvHeader);
                            }
                            mTvNickname.setText(squareDetailInfo.userInfo.nickname);
                            mTvTime.setText(squareDetailInfo.time);
                            mTvContent.setText(squareDetailInfo.comment);
                            if (squareDetailInfo.imgUrl.size() > 0) {
                                mGridData.clear();
                                mGridData.addAll(squareDetailInfo.imgUrl);
                                mGridSelf.setAdapter(mGridAdapter);
                            }


                            if (squareDetailInfo.transpondInfo != null) {
                                mReTransmit.setVisibility(View.VISIBLE);
                                mTvTransmitContent.setText(squareDetailInfo.transpondInfo.comment);
                                if (squareDetailInfo.transpondInfo.imgUrl.size() > 0) {
                                    mGridData.clear();
                                    mGridData.addAll(squareDetailInfo.transpondInfo.imgUrl);
                                    mGridTransmit.setAdapter(mGridAdapter);
                                }
                            } else {
                                mReTransmit.setVisibility(View.GONE);
                            }
                        }


                    }
                });
        addSubscrebe(subscription);
    }

}
