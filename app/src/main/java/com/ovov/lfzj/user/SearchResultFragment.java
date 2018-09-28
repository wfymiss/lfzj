package com.ovov.lfzj.user;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;


import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by kaite on 2018/8/6.
 */

public class SearchResultFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.grid_game)
    GridView mGridGame;
    List<String> mData = new ArrayList<>();
    private CommonAdapter<String> mAdapter;

    public SearchResultFragment() {

    }

    public static SearchResultFragment newInstance() {
        Bundle args = new Bundle();

        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {

        addRxBusSubscribe(ToSearchResultEvent.class, new Action1<ToSearchResultEvent>() {
            @Override
            public void call(ToSearchResultEvent toSearchResultEvent) {
                SpHistoryStorage.getInstance(mActivity, 6).save(toSearchResultEvent.content);
                //getSearchResult(toSearchResultEvent.content);
            }
        });
        initList();
    }

    private void initList() {

       /* mAdapter = new CommonAdapter<String>(mActivity,mData, R.layout.item_grid_game ) {
            @Override
            public void convert(ViewHolder viewHolder, GameInfo bodyBean, int i) {

                ImageView ivGame = viewHolder.getView(R.id.iv_game);
                if (!TextUtils.isEmpty(bodyBean.logoUrl)) {
                    Transformation transformation = new RoundedTransformationBuilder()
                            .borderColor(Color.BLACK)
                            .borderWidthDp(0)
                            .cornerRadiusDp(10)
                            .oval(false)
                            .build();
                    Picasso.with(mActivity).load(bodyBean.logoUrl).fit().transform(transformation)
                            .error(R.mipmap.games1)
                            .placeholder(R.mipmap.games1).into(ivGame);
//                    Glide.with(mActivity).load(s.logoUrl).into(ivGame);
                }
                viewHolder.setText(R.id.tv_title,bodyBean.gameName);
            }
        };
        mGridGame.setAdapter(mAdapter);

        mGridGame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!LoginUserBean.getInstance().isLogin()){
                    LoginActivity.toActivity(mActivity);
                    return;
                }

                GameInfo gameInfo = mData.get(i);

                if (!UIUtils.isPkgInstalled(mActivity, gameInfo.packName)) {
                    ToAppStroreDialog dialog = new ToAppStroreDialog(mActivity);
                    dialog.setGameInfo(gameInfo);
                    dialog.show();
                    return;
                }

                SpeedDetailActivity.toActivity(mActivity,mData.get(i));
            }
        });*/
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
