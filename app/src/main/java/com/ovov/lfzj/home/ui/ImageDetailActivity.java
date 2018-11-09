//package com.ovov.lfzj.home.ui;
//
//import android.content.Context;
//import android.content.Intent;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.ovov.lfzj.R;
//import com.ovov.lfzj.base.BaseActivity;
//import com.ovov.lfzj.base.bean.LoginUserBean;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
//public class ImageDetailActivity extends BaseActivity {
//
//
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.iv_back)
//    ImageView ivBack;
//    @BindView(R.id.web)
//    WebView web;
//
//
//    public static void toActivity(Context context, String url, String type) {
//        Intent intent = new Intent(context, ImageDetailActivity.class);
//        intent.putExtra("url", url);
//        intent.putExtra("type", type);
//        context.startActivity(intent);
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_image_detail;
//    }
//
//    @Override
//    public void init() {
//
//        String url = getIntent().getExtras().getString("url");
//        String type = getIntent().getExtras().getString("type");
//
//        if (type.equals("1")) {
//            tvTitle.setText("商品详情");
//        } else {
//            tvTitle.setText("商品列表");
//        }
//
//
//        if (url != null) {
//            web.loadUrl(url);
//            web.getSettings().setJavaScriptEnabled(true);  //加上这一行网页为响应式的
//            web.getSettings().setUseWideViewPort(true);
//            web.getSettings().setLoadWithOverviewMode(true);
//            web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//            web.getSettings().setJavaScriptEnabled(true);
//
//            web.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;   //返回true， 立即跳转，返回false,打开网页有延时
//                }
//            });
//        }
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        if (web != null) {
//            web.clearHistory();
//            web.removeAllViews();
//            web.destroy();
//            web = null;
//        }
//        super.onDestroy();
//
//    }
//
//    @OnClick(R.id.iv_back)
//    public void onViewClicked() {
//        finish();
//    }
//}
