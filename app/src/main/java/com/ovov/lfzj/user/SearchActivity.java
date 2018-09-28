package com.ovov.lfzj.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.utils.RxBus;
import com.ovov.lfzj.base.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class SearchActivity extends BaseActivity {
    public Fragment[] mFragments = new Fragment[2];
    @BindView(R.id.edit_search)
    EditText mEditSearch;
    @BindView(R.id.frame_container)
    FrameLayout mFrameContainer;
    @BindView(R.id.sitview)
    View sitview;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void init() {
        StatusBarUtils.setStatusBar(mActivity, false, false);
        initSitView();
        mFragments[0] = SearchHistoryFragment.newInstance();
        mFragments[1] = SearchResultFragment.newInstance();
        switchContent(1, 0);

        addRxBusSubscribe(SearchHisotryEvent.class, new Action1<SearchHisotryEvent>() {
            @Override
            public void call(SearchHisotryEvent searchHisotryEvent) {
                toSearchResult(searchHisotryEvent.content);
            }
        });

        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                     当按了搜索之后关闭软键盘

                    String content = mEditSearch.getText().toString();
                    if (TextUtils.isEmpty(content)) {
                        doFailed();
                        return true;
                    }
                    toSearchResult(content);
                    return true;
                }
                return false;
            }
        });
        addRxBusSubscribe(SearchHisotryEvent.class, new Action1<SearchHisotryEvent>() {
            @Override
            public void call(SearchHisotryEvent searchHisotryEvent) {
                mEditSearch.setText(searchHisotryEvent.content);
                toSearchResult(searchHisotryEvent.content);
            }
        });
    }

    private void toSearchResult(String content) {
        ((InputMethodManager) mActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mActivity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        Log.e(TAG, "toSearchResult: ");
        switchContent(0, 1);
        RxBus.getDefault().post(new ToSearchResultEvent(content));
    }

    public void switchContent(int fromIndex, int toIndex) {
        Log.e(TAG, "switchContent: form = " + fromIndex + "//toIndex = " + toIndex);
        Fragment from = mFragments[fromIndex];
        Fragment to = mFragments[toIndex];
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (!to.isAdded()) {    // 先判断是否被add过
            if (from != null) {
                if (!from.isAdded()) {
                    transaction = transaction.add(R.id.frame_container, from);
                }
                transaction.hide(from).add(R.id.frame_container, to).show(to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.add(R.id.frame_container, to).commit();
            }
        } else {
            transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }

    }

    @OnClick({R.id.tv_search, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_search:
                if (mEditSearch.getText().toString().equals("")) {
                    showToast("请输入搜索内容");
                    return;
                }
                toSearchResult(mEditSearch.getText().toString());
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    // 获取占位视图高度
    private void initSitView() {
        // 获取占位视图高度
        ViewGroup.LayoutParams sitParams = sitview.getLayoutParams();
        sitParams.height = StatusBarUtils.getStatusBarHeight(mActivity);
    }

}
