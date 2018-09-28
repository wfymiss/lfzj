package com.ovov.lfzj.base.prensenter;


import com.ovov.lfzj.base.bean.ActivityUpImageInfo;
import com.ovov.lfzj.base.net.DataResultException;
import com.ovov.lfzj.base.view.ActivityUpImageView;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 上传图片
 * Created by Administrator on 2017/8/24.
 */

public class ActivityUpImagePresent {
    private ActivityUpImageView imageView;

    public ActivityUpImagePresent(ActivityUpImageView imageView) {
        this.imageView = imageView;
    }
    Callback<ActivityUpImageInfo> callback=new Callback<ActivityUpImageInfo>() {
        @Override
        public void onResponse(Call<ActivityUpImageInfo> call, Response<ActivityUpImageInfo> response) {
            ActivityUpImageInfo infoData=response.body();
            if (infoData!=null && infoData.getStatus()==0){
                imageView.setImage(infoData);
            }
        }

        @Override
        public void onFailure(Call<ActivityUpImageInfo> call, Throwable t) {
            if (t instanceof DataResultException) {
                DataResultException dataResultException = (DataResultException) t;
                //imageView.showMsg(dataResultException.getMsg());
            }
        }
    };

    // 上传图片
    public void getUploadImage(String token, File file){
        MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(), RequestBody.create(null,file));
    //    Call<ActivityUpImageInfo> call= NetClient.getInstance().getLeFUApi().upLoadActivityImage(token,part);
   //     call.enqueue(callback);
    }
}
