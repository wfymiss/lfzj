package com.ovov.lfzj.user.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.bean.LoginUserBean;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import static cn.sharesdk.wechat.friends.Wechat.NAME;
import static com.ovov.lfzj.base.utils.UIUtils.getResources;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            dismiss();
            //大部分的回调方法都处于网络线程，因此可以简单默认为回调方法都不在主线程.
            Toast.makeText(mActivity, "分享成功！", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.e("TAG", throwable.getMessage());
            Toast.makeText(mActivity, throwable.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(Platform platform, int i) {
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family, container, false);

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

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
       Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_share_family);
        Wechat.ShareParams wechatshare = new Wechat.ShareParams();
        wechatshare.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
        wechatshare.setTitle("您的住户邀请您协助完成【乐福院子】APP认证"); //分享标题
        wechatshare.setText("缴费、报修、社交、购物\n乐福院子一'触'搞定");   //分享文本
        wechatshare.setUrl("http://api_test.catel-link.com/v1/personal/shareowner?user_id="+ LoginUserBean.getInstance().getUserId());
        wechatshare.setImageData(bitmap);
        //wechatshare.setImagePath("android.resource://" + mContext.getPackageName() + "/" + R.mipmap.splash_logo);
                /*if (!TextUtils.isEmpty("http://file03.sg560.com/upimg01/2016/03/840617/Title/1244507471580637840617.jpg"))
                    wechatshare.setImageUrl("http://file03.sg560.com/upimg01/2016/03/840617/Title/1244507471580637840617.jpg");*/
        //wechatshare.setImageUrl(UserBean.getInstance().getHead_pic());
        // wechatshare.setUrl("http://www.baidu.com");   //网友点进链接后，可以看到分享的详情
        Platform wechat = ShareSDK.getPlatform(NAME);
        wechat.setPlatformActionListener(platformActionListener); // 设置分享事件回调
        // 执行分享
        wechat.share(wechatshare);
    }
}
