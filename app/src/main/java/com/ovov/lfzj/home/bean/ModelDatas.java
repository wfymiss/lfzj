package com.ovov.lfzj.home.bean;

import java.util.ArrayList;
import java.util.List;

public class ModelDatas {

    private static ModelDatas modelDatas;

    private ModelDatas() {
    }

    public static ModelDatas getInstance() {
        if (modelDatas == null) {

            modelDatas = new ModelDatas();
        }
        return modelDatas;
    }

    public List<String> getDatas() {
        List<String> mDatas = new ArrayList<>();
        mDatas.add("http://bpic.wotucdn.com/11/66/23/55bOOOPIC3c_1024.jpg!/fw/780/quality/90/unsharp/true/compress/true/watermark/url/L2xvZ28ud2F0ZXIudjIucG5n/repeat/true");
        mDatas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505470629546&di=194a9a92bfcb7754c5e4d19ff1515355&imgtype=0&src=http%3A%2F%2Fpics.jiancai.com%2Fimgextra%2Fimg01%2F656928666%2Fi1%2FT2_IffXdxaXXXXXXXX_%2521%2521656928666.jpg");

        return mDatas;
    }
}
