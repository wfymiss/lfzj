package com.ovov.lfzj.neighbour.square;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.ovov.lfzj.base.utils.CircleCornerForm;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.base.widget.IdentityDialog;
import com.ovov.lfzj.base.widget.NewEditDialog;
import com.ovov.lfzj.base.widget.NoScrollGridView;
import com.ovov.lfzj.base.widget.ScaleImageView;
import com.ovov.lfzj.event.AddCommentEvent;
import com.ovov.lfzj.event.GoodEvent;
import com.ovov.lfzj.event.HasReadEvent;
import com.ovov.lfzj.event.LstSendEvent;
import com.ovov.lfzj.event.NewMsgEvent;
import com.ovov.lfzj.event.RefreshEvent;
import com.ovov.lfzj.event.SendEvent;
import com.ovov.lfzj.event.ToIdentityEvent;
import com.ovov.lfzj.event.TransmitEvent;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import com.ovov.lfzj.login.IdentityConfirmActivity;
import com.ovov.lfzj.neighbour.MyCommunityActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import static com.ovov.lfzj.CatelApplication.SQUARE_FRAGMENT_IDENTITY;
import static com.ovov.lfzj.CatelApplication.isGood;
import static com.ovov.lfzj.CatelApplication.noGood;

/**
 * A simple {@link Fragment} subclass.
 */
public class SquareFragment extends BaseFragment {

    @BindView(R.id.activity_list_recycler)
    ListView mActivityListRecycler;
    @BindView(R.id.activity_list_swf)
    SmartRefreshLayout mActivityListSwf;
    @BindView(R.id.tv_put_image)
    TextView mTvPutImage;
    @BindView(R.id.tv_new)
    TextView mTvNew;
    private int col;
    Unbinder unbinder;
    private List<SquareListInfo> mData;
    private CommonAdapter<SquareListInfo> mAdapter;
    private int page;
    private String id;
    private String img;
    private NewEditDialog editDialog;
    private String tranimg;
    private String mImg;
    IdentityDialog identityDialog;
    private int i;

    public static SquareFragment newInstance() {

        Bundle args = new Bundle();

        SquareFragment fragment = new SquareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SquareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_square, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void init() {
        initlist();
        mTvPutImage.setVisibility(View.VISIBLE);
        identityDialog = new IdentityDialog(mActivity, SQUARE_FRAGMENT_IDENTITY);
        mActivityListSwf.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData(REFRESH);
            }
        });
        mActivityListSwf.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initData(LOADMORE);
            }
        });
        mActivityListSwf.autoRefresh();
        addRxBusSubscribe(RefreshEvent.class, new Action1<RefreshEvent>() {
            @Override
            public void call(RefreshEvent refreshEvent) {
                mActivityListSwf.autoRefresh();
            }
        });
        addRxBusSubscribe(SendEvent.class, new Action1<SendEvent>() {
            @Override
            public void call(SendEvent sendEvent) {
                addComment(sendEvent.content, sendEvent.i);
            }
        });
        addRxBusSubscribe(GoodEvent.class, new Action1<GoodEvent>() {
            @Override
            public void call(GoodEvent goodEvent) {
                if (goodEvent.isGood) {
                    mData.get(goodEvent.posistion).setIszan(1);
                    mData.get(goodEvent.posistion).setZanNum(mData.get(goodEvent.posistion).zanNum + 1);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mData.get(goodEvent.posistion).setIszan(0);
                    mData.get(goodEvent.posistion).setZanNum(mData.get(goodEvent.posistion).zanNum - 1);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        addRxBusSubscribe(TransmitEvent.class, new Action1<TransmitEvent>() {
            @Override
            public void call(TransmitEvent transmitEvent) {
                mActivityListSwf.autoRefresh();
            }
        });
        addRxBusSubscribe(AddCommentEvent.class, new Action1<AddCommentEvent>() {
            @Override
            public void call(AddCommentEvent addCommentEvent) {
                int num = Integer.parseInt(mData.get(addCommentEvent.posistion).replyNum);
                int newNum = num + 1;
                mData.get(addCommentEvent.posistion).setReplyNum(String.valueOf(newNum));
                mAdapter.notifyDataSetChanged();
            }
        });
        addRxBusSubscribe(ToIdentityEvent.class, new Action1<ToIdentityEvent>() {
            @Override
            public void call(ToIdentityEvent toIdentityEvent) {
                IdentityConfirmActivity.toActivity(mActivity);
            }
        });
        addRxBusSubscribe(NewMsgEvent.class, new Action1<NewMsgEvent>() {
            @Override
            public void call(NewMsgEvent newMsgEvent) {
                i = i +1;
                mTvNew.setText(i+"条新消息");
                mTvNew.setVisibility(View.VISIBLE);
            }
        });
        if (LoginUserBean.getInstance().getNewMsg() != 0){
            mTvNew.setText(LoginUserBean.getInstance().getNewMsg()+"条新消息");
            mTvNew.setVisibility(View.VISIBLE);
        }
        mTvNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                LoginUserBean.getInstance().setNewMsg(0);
                LoginUserBean.getInstance().save();
                mTvNew.setVisibility(View.GONE);
                RxBus.getDefault().post(new HasReadEvent());
                SquareMsgctivity.toActivity(mActivity);
            }
        });

    }

    private void initData(final int type) {
        if (type == REFRESH) {
            page = 1;
            id = "";
        } else if (type == LOADMORE) {
            page = page + 1;
            id = mData.get((page - 1) * 10 - 1).id;
        }

        Subscription subscription = RetrofitHelper.getInstance().getSquareList(page, id)
                .compose(RxUtil.<ListInfo<SquareListInfo>>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ListInfo<SquareListInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (type == REFRESH) {
                            mActivityListSwf.finishRefresh();

                        } else {
                            mActivityListSwf.finishLoadmore();
                        }

                        if (e instanceof DataResultException) {
                            DataResultException dataResultException = (DataResultException) e;
                            mActivityListSwf.setEnableLoadmore(false);
                            showToast(dataResultException.errorInfo);
                        } else {

                            doFailed();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNext(ListInfo<SquareListInfo> listInfoDataInfo) {

                        if (listInfoDataInfo.success()) {
                            if (listInfoDataInfo.datas().size() < 10) {
                                mActivityListSwf.setEnableLoadmore(false);
                            } else {
                                mActivityListSwf.setEnableLoadmore(true);
                            }
                            if (type == REFRESH) {
                                mActivityListSwf.finishRefresh();
                                mData.clear();
                                mData.addAll(listInfoDataInfo.datas());
                                mAdapter.notifyDataSetChanged();


                            } else {
                                mActivityListSwf.finishLoadmore();
                                mData.addAll(listInfoDataInfo.datas());
                                mAdapter.notifyDataSetChanged();

                            }
                        } else {
                            if (type == REFRESH) {
                                mActivityListSwf.finishRefresh();

                            } else {
                                mActivityListSwf.finishLoadmore();
                            }
                        }


                    }
                });
        addSubscrebe(subscription);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void send(LstSendEvent sendEvent) {
        addComment(sendEvent.content, sendEvent.i);

    }

    private void initlist() {
        mData = new ArrayList<>();


        mAdapter = new CommonAdapter<SquareListInfo>(this.getActivity(), mData, R.layout.square_list_item) {
            @Override
            public void convert(ViewHolder viewHolder, final SquareListInfo squareListInfo, final int i) {
                viewHolder.setText(R.id.tv_nickname, squareListInfo.userInfo.nickname);
                viewHolder.setText(R.id.tv_content, squareListInfo.comment);
                viewHolder.setText(R.id.tv_pl, "评论");
                viewHolder.setText(R.id.tv_good, "赞");
                viewHolder.setText(R.id.tv_transmit, "转发");
                viewHolder.setText(R.id.tv_time, squareListInfo.time);
                TextView tvCommentlist = viewHolder.getView(R.id.tv_list_comment);
                if (squareListInfo.reply.nickname != null) {
                    tvCommentlist.setVisibility(View.VISIBLE);
                    if (squareListInfo.reply.nickname.equals("") && TextUtils.isEmpty(squareListInfo.reply.nickname)) {
                        viewHolder.setText(R.id.tv_list_comment, squareListInfo.reply.mobile + ":" + squareListInfo.reply.content);
                    } else {
                        viewHolder.setText(R.id.tv_list_comment, squareListInfo.reply.nickname + ":" + squareListInfo.reply.content);
                    }
                } else {
                    tvCommentlist.setVisibility(View.GONE);
                }
                RelativeLayout reGood = viewHolder.getView(R.id.re_good);
                reGood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (LoginUserBean.getInstance().isIs_auth())
                            squareGood(squareListInfo.id, i);
                        else
                            identityDialog.show();
                    }
                });
                RelativeLayout mReTransmit = viewHolder.getView(R.id.re_trasmit);
                mReTransmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (LoginUserBean.getInstance().isIs_auth()) {
                            SquareDetailActivity.toActivity(mActivity, i, squareListInfo, squareListInfo.id);
                        } else {
                            identityDialog.show();
                        }

                    }
                });
                RelativeLayout reComment = viewHolder.getView(R.id.re_pl);
                reComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SquareDetailActivity.toActivity(mActivity, i, squareListInfo, squareListInfo.id);

                    }
                });
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
                if (squareListInfo.imgUrl.size() > 0) {
                    mImage.setVisibility(View.VISIBLE);
                } else {
                    mImage.setVisibility(View.GONE);
                }
                CommonAdapter<String> mGridAdapter = new CommonAdapter<String>(getActivity(), mGridData, R.layout.user_img_item) {
                    @Override
                    public void convert(ViewHolder viewHolder, String s, int i) {
                        ImageView ivGrid = viewHolder.getView(R.id.iv_user_img);
                        int width = ActivityUtils.getWidth(getActivity());
                        width = width / col;
                        int height = width;
                        ivGrid.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
                        Picasso.with(mActivity).load(s).transform(new CircleCornerForm()).into(ivGrid);
                        ivGrid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ScaleImageView scaleImageView = new ScaleImageView(mActivity);
                                scaleImageView.setUrls(mGridData, i);
                                scaleImageView.create();
                            }
                        });
                    }
                };
                CommonAdapter<String> mTransmitGridAdapter = new CommonAdapter<String>(getActivity(), mGridData, R.layout.user_img_item) {
                    @Override
                    public void convert(ViewHolder viewHolder, String s, int i) {
                        ImageView ivGrid = viewHolder.getView(R.id.iv_user_img);
                        int width = ActivityUtils.getWidth(getActivity());
                        width = width / col;
                        int height = width;
                        ivGrid.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
                        Picasso.with(mActivity).load(s).transform(new CircleCornerForm()).into(ivGrid);
                        ivGrid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ScaleImageView scaleImageView = new ScaleImageView(mActivity);
                                scaleImageView.setUrls(mGridData, i);
                                scaleImageView.create();
                            }
                        });
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
                    mTransmitImage.setAdapter(mTransmitGridAdapter);
                    if (squareListInfo.transpondInfo.imgUrl.size() > 0)
                        mTransmitImage.setVisibility(View.VISIBLE);
                    else
                        mTransmitImage.setVisibility(View.GONE);
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
                if (squareListInfo.userInfo.user_logo != null && !squareListInfo.userInfo.user_logo.equals(""))
                    Picasso.with(mActivity).load(squareListInfo.userInfo.user_logo).placeholder(R.mipmap.ic_default_head).into(ivHeader);
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
                        //SquareDetailActivity.toActivity(mActivity, i, squareListInfo);
                    }
                });
                reTransmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SquareDetailActivity.toActivity(mActivity, i, squareListInfo, squareListInfo.transpondInfo.id);

                    }
                });
                TextView tvTransmitContent = viewHolder.getView(R.id.tv_transmit_content);
                tvTransmitContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SquareDetailActivity.toActivity(mActivity, i, squareListInfo, squareListInfo.transpondInfo.id);
                    }
                });


                mImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //SquareDetailActivity.toActivity(mActivity, i, squareListInfo);
                    }
                });
                LinearLayout mRecontainer = viewHolder.getView(R.id.container);
                mRecontainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SquareDetailActivity.toActivity(mActivity, i, squareListInfo, squareListInfo.id);
                    }
                });


            }
        };
        mActivityListRecycler.setAdapter(mAdapter);
        /*View view = LayoutInflater.from(getActivity()).inflate(R.layout.neighbour_list_header, null, false);
        mActivityListRecycler.addHeaderView(view);
        List<Integer> mGridData = new ArrayList<>();
        mGridData.add(R.mipmap.ic_square_flower);
        RecyclerView mGameRecycler = (RecyclerView) view.findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGameRecycler.setLayoutManager(linearLayoutManager);
        mGameRecycler.addItemDecoration(new SpacesItemDecoration(20));
        com.mcxtzhang.commonadapter.rv.CommonAdapter<Integer> mGameAdapter = new com.mcxtzhang.commonadapter.rv.CommonAdapter<Integer>(getActivity(),mGridData,R.layout.item_game) {
            @Override
            public void convert(com.mcxtzhang.commonadapter.rv.ViewHolder viewHolder, Integer integer) {
                ImageView ivGame = viewHolder.getView(R.id.iv_game);
                Picasso.with(getActivity()).load(integer).into(ivGame);
                ivGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameActivity.toActivity(mActivity);
                    }
                });
            }
        };
        mGameRecycler.setAdapter(mGameAdapter);*/

    }

    @OnClick(R.id.tv_put_image)
    public void onViewClicked() {
        PutSquareActivity.toActivity(mActivity);
    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            /*outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) == 0)
                outRect.top = space;*/
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    private void squareGood(String id, final int posistion) {
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
                        if (mData.get(posistion).isZan == isGood) {
                            mData.get(posistion).setZanNum(mData.get(posistion).zanNum - 1);
                            mData.get(posistion).setIszan(noGood);
                        } else {
                            mData.get(posistion).setZanNum(mData.get(posistion).zanNum
                                    + 1);
                            mData.get(posistion).setIszan(isGood);
                        }


                        mAdapter.notifyDataSetChanged();

                    }
                });
        addSubscrebe(subscription);
    }

    private void addComment(String content, final int i) {
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().addSquareComment(mData.get(i).id, content)
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
                            //showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DataInfo dataInfo) {
                        dismiss();
                        editDialog.dismiss();
                        showToast(R.string.text_comment_success);
                        int num = Integer.parseInt(mData.get(i).replyNum);
                        int newNum = num + 1;
                        mData.get(i).setReplyNum(String.valueOf(newNum));
                        mAdapter.notifyDataSetChanged();
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
