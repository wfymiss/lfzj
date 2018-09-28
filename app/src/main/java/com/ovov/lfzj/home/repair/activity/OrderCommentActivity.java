package com.ovov.lfzj.home.repair.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseActivity;
import com.ovov.lfzj.base.bean.ServerFeedBackInfo;
import com.ovov.lfzj.base.utils.RxUtil;
import com.ovov.lfzj.http.RetrofitHelper;
import com.ovov.lfzj.http.subscriber.CommonSubscriber;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;

/**
 * 评论
 * 2017/10/13
 */
public class OrderCommentActivity extends BaseActivity{
    private Unbinder unbinder;
    @BindView(R.id.order_comment)            // 商家评论内容
            EditText comm_content;
    @BindView(R.id.order_comm_commit)       // 提交商家评论
            TextView comm_commit;
    @BindView(R.id.star_one)                 //  评论星级—一星
            ImageView starOne;
    @BindView(R.id.star_two)
    ImageView starTwo;
    @BindView(R.id.star_three)
    ImageView starThree;
    @BindView(R.id.star_four)
    ImageView starFour;
    @BindView(R.id.star_five)
    ImageView starFive;

    private String token=null;               // 用户token
    private String sub_id=null;
    private String order_type=null;         // 被评论订单类型
    private String seller_id=null;          // 商家id
    private String order_id=null;           // 商品id
    private int mark=0;                      // 评论星级
    private String content=null;            // 评论内容

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_comment;
    }

    @Override
    public void init() {
        setTitleText(R.string.score);
        initData();            // 页面传值
        storageTokenRead();   //获取token
    }


    // 页面传值
    private void initData() {
        Intent intent=getIntent();
        if (intent!=null){
            seller_id=intent.getStringExtra("sellerId");       // 商家id
            order_id=intent.getStringExtra("orderId");         // 订单id
            order_type=intent.getStringExtra("orderType");    // 订单类型
            if (order_type.equals("emall")){
                comm_content.setHint("请对订单进行评价...");
            }
            if (order_type.equals("domestic")){
                comm_content.setHint("请对服务进行评价...");
            }
            if (order_type.equals("work_order")){
                comm_content.setHint("请对物业人员进行评价...");
            }
        }
        mark = 1;
        starOne.setImageResource(R.mipmap.rating_star_on);
    }

    // 获取token
    private void storageTokenRead() {
        SharedPreferences spf=this.getSharedPreferences("indenti", Context.MODE_PRIVATE);
        token=spf.getString("token","");
        sub_id=spf.getString("subId","");
    }

    @OnClick({R.id.order_comm_commit})
    public void OnClickView(View view){
        switch (view.getId()){
            case R.id.order_comm_commit:                        // 提交订单服务
                content=comm_content.getText().toString();
                boolean judge = decideFormate();                 //判断输入信息的格式
                if (judge){
                    if (order_type!=null){
                        /*if (order_type.equals("emall")){
                            present.getUpOrderComm(token,sub_id,seller_id,order_id,mark,content);        // 商城订单评论
                        }
                        if (order_type.equals("domestic")){
                            present.setUpComment(token,sub_id,order_id,mark,content);                     // 家政服务订单评论（token、订单id、星级数、评论内容）
                        }*/
                        if (order_type.equals("work_order")){
                            addComment();
                        }
                    }
                }
                break;
            default:
        }
    }
    private void addComment(){
        showLoadingDialog();
        Subscription subscription = RetrofitHelper.getInstance().addComment(sub_id,order_id,content,mark)
                .compose(RxUtil.<ServerFeedBackInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ServerFeedBackInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        doFailed();

                    }

                    @Override
                    public void onNext(ServerFeedBackInfo serverFeedBackInfo) {
                        dismiss();
                        if (serverFeedBackInfo.getStatus()==0){                // 提交成功
                            finish();
                        }
                    }
                });
        addSubscrebe(subscription);
    }
    //判断输入信息的格式
    private boolean decideFormate() {
        if (mark==0) {
            Toast.makeText(this, "您还没有评分", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请填写评论内容", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @OnClick({R.id.star_one, R.id.star_two, R.id.star_three, R.id.star_four, R.id.star_five})
    public void OnClickStar(View view){
        switch (view.getId()){
            case R.id.star_one:
                mark = 1;
                starOne.setImageResource(R.mipmap.rating_star_on);
                starTwo.setImageResource(R.mipmap.rating_star);
                starThree.setImageResource(R.mipmap.rating_star);
                starFour.setImageResource(R.mipmap.rating_star);
                starFive.setImageResource(R.mipmap.rating_star);
                break;
            case R.id.star_two:
                mark = 2;
                starOne.setImageResource(R.mipmap.rating_star_on);
                starTwo.setImageResource(R.mipmap.rating_star_on);
                starThree.setImageResource(R.mipmap.rating_star);
                starFour.setImageResource(R.mipmap.rating_star);
                starFive.setImageResource(R.mipmap.rating_star);
                break;
            case R.id.star_three:
                mark = 3;
                starOne.setImageResource(R.mipmap.rating_star_on);
                starTwo.setImageResource(R.mipmap.rating_star_on);
                starThree.setImageResource(R.mipmap.rating_star_on);
                starFour.setImageResource(R.mipmap.rating_star);
                starFive.setImageResource(R.mipmap.rating_star);
                break;
            case R.id.star_four:
                mark = 4;
                starOne.setImageResource(R.mipmap.rating_star_on);
                starTwo.setImageResource(R.mipmap.rating_star_on);
                starThree.setImageResource(R.mipmap.rating_star_on);
                starFour.setImageResource(R.mipmap.rating_star_on);
                starFive.setImageResource(R.mipmap.rating_star);
                break;
            case R.id.star_five:
                mark = 5;
                starOne.setImageResource(R.mipmap.rating_star_on);
                starTwo.setImageResource(R.mipmap.rating_star_on);
                starThree.setImageResource(R.mipmap.rating_star_on);
                starFour.setImageResource(R.mipmap.rating_star_on);
                starFive.setImageResource(R.mipmap.rating_star_on);
                break;
            default:
        }
    }

}
