package com.risenb.witness.ui.vip;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.adapter.HomeSearchPoiAdapter;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.views.newViews.XListView;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.homesearch)
public class HomeSearchUI extends BaseUI implements XListView.IXListViewListener {
    @ViewInject(R.id.search_content_et)
    private EditText search_content_et;
    @ViewInject(R.id.homesearch_xlv)
    private XListView homesearch_xlv;

    private HomeSearchPoiAdapter homeSearchPoiAdapter;

    private GeoCoder mSearch;

    private List<PoiInfo> poiInfoList = new ArrayList<>();

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        homeSearchPoiAdapter = new HomeSearchPoiAdapter();
        homesearch_xlv.setAdapter(homeSearchPoiAdapter);
        homesearch_xlv.setPullRefreshEnable(false);
        homesearch_xlv.setXListViewListener(this);
    }

    @Override
    protected void prepareData() {
        // Intent intent = new Intent(getActivity(), ZUI.class);
        // startActivity(intent);
        homeSearchPoiAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                String str = poiInfoList.get(position).address + poiInfoList.get(position).name;
                intent.putExtra("data", str);
                intent.putExtra("latitude", String.valueOf(poiInfoList.get(position).location.latitude));
                intent.putExtra("longitude", String.valueOf(poiInfoList.get(position).location.longitude));
                setResult(1, intent);
                finish();
            }
        });
    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 搜索
     */
    @OnClick(R.id.search)
    private void search(View v) {

        if (TextUtils.isEmpty(search_content_et.getText().toString().trim())) {
            makeText("请输入地址");
            return;
        }

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(listener);
        mSearch.geocode(new GeoCodeOption()
                .city(application.getCity())
                .address(search_content_et.getText().toString()));

    }

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                makeText("没有检索到结果");
            }
            //获取地理编码结果
//            System.out.println("onGetGeoCodeResult--------->" + result.getLocation().longitude);
//            System.out.println("onGetGeoCodeResult--------->" + result.getLocation().latitude);
            //新建编码查询对象
            mSearch = GeoCoder.newInstance();
            //新建查询对象要查询的条件
            ReverseGeoCodeOption options = new ReverseGeoCodeOption().location(result.getLocation());
            // 发起反地理编码请求
            mSearch.reverseGeoCode(options);
            //设置查询结果监听者
            mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                /**
                 * 反地理编码查询结果回调函数
                 * @param result  反地理编码查询结果
                 */
                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        return;
                    }
                    if (result != null && result.error == SearchResult.ERRORNO.NO_ERROR) {
                        poiInfoList.clear();
                        poiInfoList.addAll(result.getPoiList());
                        homeSearchPoiAdapter.setList(result.getPoiList());
                        System.out.println("onGetGeoCodeResult--------->得到位置-->" + result.getPoiList().get(0).city + result.getPoiList().get(0).address + result.getPoiList().get(0).name);
                    }
                }

                /**
                 * 地理编码查询结果回调函数
                 * @param result  地理编码查询结果
                 */
                @Override
                public void onGetGeoCodeResult(GeoCodeResult result) {

                }
            });

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                makeText("没有找到检索结果");
            }
            //获取反向地理编码结果
            System.out.println("onGetReverseGeoCodeResult--------->" + result.getPoiList().get(0).name);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearch != null)
            mSearch.destroy();
    }

    @Override
    public void onLoad(int i) {

    }
}
