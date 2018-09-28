package com.ovov.lfzj.opendoor.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.moments.WechatMoments;

import static cn.sharesdk.wechat.friends.Wechat.NAME;

/**
 * 生成访客通行二维码弹框
 * Created by Administrator on 2017/7/27.
 */

public class InviterKeyDialog extends Dialog {
//    private Context context;

    public InviterKeyDialog(@NonNull Context context) {
        super(context);
    }

    public InviterKeyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private Bitmap bitmap;

        public Builder(Context mContext) {
            this.context = mContext;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public Bitmap getBitmap() {     // 获取传值图片
            return bitmap;
        }

        public InviterKeyDialog creeate() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final InviterKeyDialog dialog = new InviterKeyDialog(context, R.style.Dialog);
            View view = inflater.inflate(R.layout.layout_invite_key_dialog, null);
            dialog.addContentView(view, new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dialog.setContentView(view);
            ImageView img = (ImageView) view.findViewById(R.id.lock_invite_key_image);
            img.setImageBitmap(getBitmap());
            TextView tv = (TextView) view.findViewById(R.id.key_share_visitor);               // 确定分享按钮
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context,"分享成功",Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                    if (dialogListener != null) {
                        dialogListener.visitorShareClick();
                    }
                    showShare();
                }
            });
            return dialog;
        }

        private VisitorDialogListener dialogListener;     // 定义访客二维码分享事件

        // 访客二维码分享事件接口
        public interface VisitorDialogListener {
            void visitorShareClick();
        }

        public void setVisitorShareListener(VisitorDialogListener listener) {
            this.dialogListener = listener;
        }

        private void showShare() {
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();
            // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
            oks.setTitle("乐福院子访客二维码");
            // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
//            oks.setTitleUrl("http://sharesdk.cn");
            // text是分享文本，所有平台都需要这个字段
            oks.setText("扫描二维码码开启门禁");
            //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
            //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            oks.setImagePath("/storage/emulated/0/AndyDemo/ScreenImage/yaoShi.png");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
//            oks.setUrl("http://sharesdk.cn");
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setPlatform(NAME);
            oks.setComment("乐福院子访客二维码");
            // site是分享此内容的网站名称，仅在QQ空间使用
//            oks.setSite("ShareSDK");
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//            oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
            oks.show(context);
        }
    }
}
