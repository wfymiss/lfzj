package com.ovov.lfzj.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovov.lfzj.R;
import com.ovov.lfzj.base.BaseFragment;
import com.ovov.lfzj.base.utils.UIUtils;
import com.squareup.picasso.Picasso;

/**
 * @author Nate Robinson
 * @Time 2017/12/24
 */

public class GuideFragment extends BaseFragment {

    public static GuideFragment newInstance(int index) {
        GuideFragment fragment = new GuideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int[] guides = new int[]{R.mipmap.guide_view_01, R.mipmap.guide_view_02, R.mipmap.guide_view_03,R.mipmap.guide_view_04};
    //private int[] guidetitles = new int[]{R.string.guide_one_title_txt, R.string.guide_two_title_txt, R.string.guide_three_title_txt};
    private ImageView guideItemIv;
    private TextView guide_tv;
    private View point_one;
    private View point_two;
    private View point_three;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        guideItemIv = (ImageView) view.findViewById(R.id.guide_iv);
        init();
        return view;
    }

    @Override
    public void init() {
        int index = 0;
        if (getArguments() != null) {
            index = getArguments().getInt("index");
        }
        //从res资源文件中加载图片
        Picasso.with(getActivity()).load(guides[index])
                .resize(UIUtils.getScreenWidth(), UIUtils.getScreenHeight())
                .centerInside()
                .into(guideItemIv);
    }

}
