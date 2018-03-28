package com.risenb.witness.ui.showimg;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.newNetwork.BaseBannerBean;
import com.risenb.witness.utils.newUtils.BasePageUtils;
import com.risenb.witness.utils.newUtils.LoadOver;
import com.risenb.witness.utils.newUtils.OnLoadOver;
import com.risenb.witness.utils.newUtils.ShowBean;
import com.risenb.witness.utils.newUtils.ShowUtils;
import com.risenb.witness.views.newViews.BasePageFragment;
import com.risenb.witness.views.newViews.ShowImageView;

@ContentView(R.layout.show_img)
public class ShowImgUI extends BaseUI {

    // Intent intent = new Intent(getActivity(), ShowImgUI.class);
    // intent.putExtra("list", (Serializable)list);
    // intent.putExtra("idx", 0);
    // startActivity(intent);

    DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()//
            .showStubImage(R.drawable.default_image) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.default_image) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.default_image) // 设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true)//
            .cacheOnDisk(true)//
            .build();//

    /**
     * 显示的内容
     */
    @ViewInject(R.id.vp_show_img)
    private ViewPager vp_show_img;

    @ViewInject(R.id.btn_show_img)
    private Button btn_show_img;

    @ViewInject(R.id.tv_show_img)
    private TextView tv_show_img;

    private ShowImageView[] sivArr;
    private ShowBean bean;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void prepareData() {
        final List<BaseBannerBean> list = (List<BaseBannerBean>) getIntent().getSerializableExtra("list");

        bean = new ShowBean();
        bean.setBtn(btn_show_img);
        bean.setViewPager(vp_show_img);


        sivArr = new ShowImageView[list.size()];
        bean.setSivArr(sivArr);
        List<ShowImgBean> listShow = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ShowImgBean showImgBean = new ShowImgBean();
            showImgBean.setUrl(list.get(i).getBannerBeanImage());
            listShow.add(showImgBean);
        }
        for (int i = 0; i < listShow.size(); i++) {
            listShow.get(i).setFragment(new PageFragment(listShow.get(i), i));
        }
        BasePageUtils<ShowImgBean> basePageUtils = new BasePageUtils<>();
        basePageUtils.setActivity(getActivity());
        basePageUtils.setViewPager(vp_show_img);
        basePageUtils.setList(listShow);
        basePageUtils.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
                bean.setPosition(position);
                tv_show_img.setText((position + 1) + "/" + list.size());
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        basePageUtils.setRadioButton00(R.id.radio_button00);
        basePageUtils.setRadioLayoutID(R.layout.rb_page_fragment);
        basePageUtils.info();

        ShowUtils.getShowUtils().setOnTouchListener(bean);
        vp_show_img.setCurrentItem(getIntent().getIntExtra("idx", 0));
    }

    @Override
    public void onLoadOver() {

    }

    @SuppressLint("ValidFragment")
    public class PageFragment extends BasePageFragment<ShowImgBean> implements OnLoadOver {

        @ViewInject(R.id.ll_show_image_show)
        private LinearLayout ll_show_image_show;

        public PageFragment(ShowImgBean baseMenuBean, int position) {
            super(baseMenuBean, position);
        }

        @Override
        protected void loadViewLayout(LayoutInflater inflater, ViewGroup container) {
            view = inflater.inflate(R.layout.show_img_item, null);
        }

        @Override
        protected void setControlBasis() {
            new LoadOver(getActivity(), this);
        }

        @Override
        protected void prepareData() {

        }

        @Override
        public void onLoadOver() {
            ll_show_image_show.removeAllViews();

            int screenW = ll_show_image_show.getWidth(); // 屏幕宽度（像素）
            int screenH = ll_show_image_show.getHeight(); // 屏幕高度（像素）

            sivArr[position] = new ShowImageView(getActivity(), screenW, screenH);
            ll_show_image_show.addView(sivArr[position]);
            if (!TextUtils.isEmpty(baseMenuBean.getUrl()) && baseMenuBean.getUrl().indexOf(":") == -1) {
                ImageLoader.getInstance().displayImage("file:///" + baseMenuBean.getUrl(), sivArr[position], displayImageOptions);
            } else {
                ImageLoader.getInstance().displayImage(baseMenuBean.getUrl(), sivArr[position], displayImageOptions);
            }
        }
    }
}
