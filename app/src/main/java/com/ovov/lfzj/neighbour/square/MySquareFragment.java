package com.ovov.lfzj.neighbour.square;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.ovov.lfzj.R;

import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.DataInfo;
import com.ovov.lfzj.base.bean.ListInfo;
import com.ovov.lfzj.base.bean.LoginUserBean;
import com.ovov.lfzj.base.bean.SquareListInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.utils.ActivityUtils;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.utils.UIUtils;
import com.ovov.lfzj.base.widget.DeleteSquarepopupWindow;
import com.ovov.lfzj.base.widget.EditDialog;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.TransmitpopupWindow;
import com.ovov.lfzj.event.DeleteEvent;
import com.ovov.lfzj.event.RefreshEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.neighbour.MyCommunityActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.functions.Action1;

import static com.ovov.lfzj.CatelApplication.LOADMORE;
import static com.ovov.lfzj.CatelApplication.REFRESH;
import static com.ovov.lfzj.CatelApplication.isGood;
import static com.ovov.lfzj.CatelApplication.noGood;

/***
 * 活动fragment父容器，标题
 * Created by 袁磊 on 2017/7/3.
 */
public class MySquareFragment extends BaseFragment {

    @BindView(R.id.activity_list_recycler)
    ListView mActivityListRecycler;
    @BindView(R.id.activity_list_swf)
    SmartRefreshLayout mActivityListSwf;
    @BindView(R.id.tv_put)
    TextView mTvPut;
    Unbinder unbinder;
    Unbinder unbinder1;
    private String userid;
    private int page;
    private String id;
    private List<SquareListInfo> mData;
    private CommonAdapter<SquareListInfo> mAdapter;
    private int col;
    private String img;
    private String tranimg;
    private String square_id;

    public MySquareFragment() {
    }

    public static MySquareFragment newInstance() {
        MySquareFragment activityDetailFragment = new MySquareFragment();
        return activityDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_square, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();

        return view;
    }

    @Override
    public void init() {
        super.init();

        //getMySquareList();
        mTvPut.setText("发布消息");
        mTvPut.setVisibility(View.VISIBLE);
        initList();
        mActivityListSwf.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getMySquareList(REFRESH);
            }
        });
        mActivityListSwf.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getMySquareList(LOADMORE);
            }
        });
        mActivityListSwf.autoRefresh();
        if (userid.equals(LoginUserBean.getInstance().getUserId())) {
            mTvPut.setVisibility(View.VISIBLE);
        } else {
            mTvPut.setVisibility(View.GONE);
        }
        addRxBusSubscribe(RefreshEvent.class, new Action1<RefreshEvent>() {
            @Override
            public void call(RefreshEvent refreshEvent) {
                mActivityListSwf.autoRefresh();
            }
        });
        addRxBusSubscribe(DeleteEvent.class, new Action1<DeleteEvent>() {
            @Override
            public void call(DeleteEvent deleteEvent) {
                deleteSquare();
            }
        });

    }

    private void deleteSquare() {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().deleteSquare(square_id)
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
                        showToast("删除成功");
                        mActivityListSwf.autoRefresh();
                    }
                });
        addSubscrebe(subscription);
    }

    //设置头像
    private void showDelete() {
        DeleteSquarepopupWindow pop = new DeleteSquarepopupWindow(mActivity);
        // 开启 popup 时界面透明
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.7f;
//                if (bgAlpha == 1) {
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
//                } else {
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
//                }
        mActivity.getWindow().setAttributes(lp);
        // popupwindow 第一个参数指定popup 显示页面
        pop.showAtLocation(mActivity.findViewById(R.id.layout_square), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);     // 第一个参数popup显示activity页面
        // popup 退出时界面恢复
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);
            }
        });
    }

    private void initList() {
        mData = new ArrayList<>();

        mAdapter = new CommonAdapter<SquareListInfo>(this.getActivity(), mData, R.layout.square_list_item) {
            @Override
            public void convert(ViewHolder viewHolder, final SquareListInfo squareListInfo, final int i) {

                LinearLayout linFoot = viewHolder.getView(R.id.lin_foot);
                linFoot.setVisibility(View.VISIBLE);
                ImageView ivDelete = viewHolder.getView(R.id.iv_delete);
                if (squareListInfo.user_id.equals(LoginUserBean.getInstance().getUserId())){
                    ivDelete.setVisibility(View.VISIBLE);
                    ivDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            square_id = squareListInfo.id;
                            showDelete();

                        }
                    });
                }else {
                    ivDelete.setVisibility(View.GONE);
                }

                TextView tvListComment = viewHolder.getView(R.id.tv_list_comment);
                tvListComment.setVisibility(View.GONE);
                viewHolder.setText(R.id.tv_nickname, squareListInfo.userInfo.nickname);
                viewHolder.setText(R.id.tv_content, squareListInfo.comment);

                viewHolder.setText(R.id.tv_time, squareListInfo.time);
                RelativeLayout reGood = viewHolder.getView(R.id.re_good);

                RelativeLayout reTransmit = viewHolder.getView(R.id.re_transmit_content);


                ImageView ivGood = viewHolder.getView(R.id.iv_good);
                if (squareListInfo.isZan == isGood) {
                    ivGood.setSelected(true);

                } else if (squareListInfo.isZan == noGood) {
                    ivGood.setSelected(false);

                }
                NoScrollGridView mTransmitImage = viewHolder.getView(R.id.transmit_gridView);
                NoScrollGridView mImage = viewHolder.getView(R.id.gridView);
                List<String> mGridData = new ArrayList<>();

                CommonAdapter<String> mGridAdapter = new CommonAdapter<String>(getActivity(), mGridData, R.layout.user_img_item) {
                    @Override
                    public void convert(ViewHolder viewHolder, String s, int i) {
                        ImageView ivGrid = viewHolder.getView(R.id.iv_user_img);
                        int width = ActivityUtils.getWidth(getActivity());
                        width = width / col;
                        int height = width;
                        ivGrid.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
                        Picasso.with(mActivity).load(s).into(ivGrid);
                    }
                };
                if (squareListInfo.transpondInfo != null) {
                    reTransmit.setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_transmit_nickname, "@" + squareListInfo.transpondInfo.userInfo.nickname + ":");
                    viewHolder.setText(R.id.tv_transmit_content, squareListInfo.transpondInfo.comment);
                    if (squareListInfo.transpondInfo.imgUrl != null && squareListInfo.transpondInfo.imgUrl.size() > 0)
                        mGridData.addAll(squareListInfo.transpondInfo.imgUrl);
                    if (mGridData.size() == 1 || mGridData.size() == 2 || mGridData.size() == 4) {
                        mTransmitImage.setNumColumns(2);
                        col = 2;
                    } else {
                        mTransmitImage.setNumColumns(3);
                        col = 3;
                    }
                    mTransmitImage.setAdapter(mGridAdapter);
                    mTransmitImage.setVisibility(View.VISIBLE);
                    mImage.setVisibility(View.GONE);
                } else {
                    reTransmit.setVisibility(View.GONE);
                    if (squareListInfo.imgUrl != null && squareListInfo.imgUrl.size() > 0)
                        mGridData.addAll(squareListInfo.imgUrl);
                    if (mGridData.size() == 1 || mGridData.size() == 2 || mGridData.size() == 4) {
                        mImage.setNumColumns(2);
                        col = 2;
                    } else {
                        mImage.setNumColumns(3);
                        col = 3;
                    }
                    mImage.setAdapter(mGridAdapter);
                    mTransmitImage.setVisibility(View.GONE);
                    mImage.setVisibility(View.VISIBLE);
                }


                CircleImageView ivHeader = viewHolder.getView(R.id.iv_head);
                if (squareListInfo.userInfo.user_logo != null && squareListInfo.userInfo.user_logo != "")
                    Picasso.with(mActivity).load(squareListInfo.userInfo.user_logo).into(ivHeader);
                else
                    Picasso.with(mActivity).load(R.mipmap.ic_default_head).into(ivHeader);
                ivHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (squareListInfo.userInfo.user_logo == null || squareListInfo.userInfo.user_logo.equals("")) {
                            img = "";
                        } else {
                            img = squareListInfo.userInfo.user_logo;
                        }
                        MyCommunityActivity.toUserActivity(getActivity(), squareListInfo.userInfo.nickname, img, "2", squareListInfo.user_id, squareListInfo.userInfo.signature);
                    }
                });
                TextView tvTransmitName = viewHolder.getView(R.id.tv_transmit_nickname);
                tvTransmitName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (squareListInfo.transpondInfo.userInfo.user_logo == null || squareListInfo.userInfo.user_logo.equals("")) {
                            tranimg = "";
                        } else {
                            tranimg = squareListInfo.userInfo.user_logo;
                        }
                        MyCommunityActivity.toUserActivity(mActivity, squareListInfo.transpondInfo.userInfo.nickname, tranimg, "2", squareListInfo.transpondInfo.user_id, squareListInfo.userInfo.signature);
                    }
                });
                mTransmitImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //SquareDetailActivity.toActivity(mActivity,  i, squareListInfo);
                    }
                });
                reTransmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //SquareDetailActivity.toActivity(mActivity,  i, squareListInfo,squareListInfo.transpondInfo.id);
                    }
                });


                mImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SquareDetailActivity.toActivity(mActivity,  i, squareListInfo,squareListInfo.id);
                    }
                });
                LinearLayout mRecontainer = viewHolder.getView(R.id.container);
                mRecontainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SquareDetailActivity.toActivity(mActivity,  i, squareListInfo,squareListInfo.id);
                    }
                });


            }
        };
        mActivityListRecycler.setAdapter(mAdapter);
    }

    private void getMySquareList(final int type) {
        if (type == REFRESH) {
            page = 1;
            id = "";
        } else {
            page = page + 1;
            id = mData.get((page - 1) * 10 - 1).id;
        }
        Subscription subscription = RetrofitHelper.getInstance().getUserSquareList(userid, page, id)
                .compose(RxUtil.<ListInfo<SquareListInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<SquareListInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        mActivityListSwf.setEnableLoadmore(false);
                        if (type == REFRESH) {
                            mActivityListSwf.finishRefresh(false);

                        } else {
                            mActivityListSwf.finishRefresh(false);
                        }
                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            mData.clear();
                            showToast(dataResultException.errorInfo);
                        } else {

                            doFailed();
                            //showError(e.getMessage());
                            Log.e("show", e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(ListInfo<SquareListInfo> dataInfo) {
                        if (dataInfo.datas().size() < 10) {
                            mActivityListSwf.setEnableLoadmore(false);
                        }
                        if (type == REFRESH) {
                            mActivityListSwf.finishRefresh(true);
                            mData.clear();
                            mData.addAll(dataInfo.datas());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mActivityListSwf.finishRefresh(true);
                            mData.addAll(dataInfo.datas());
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                });
        addSubscrebe(subscription);
    }


    @OnClick(R.id.tv_put)
    public void onViewClicked() {
        PutSquareActivity.toActivity(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userid = ((MyCommunityActivity) getActivity()).getUserid();
    }
}
