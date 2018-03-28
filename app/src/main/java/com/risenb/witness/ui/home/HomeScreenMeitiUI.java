package com.risenb.witness.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SectionIndexer;

import com.alibaba.fastjson.JSONArray;
import com.lidroid.xutils.view.annotation.ContentView;
import com.risenb.witness.R;
import com.risenb.witness.beans.HomeScreenMediatypeBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.HomeScreen.CharacterParser;
import com.risenb.witness.ui.HomeScreen.PinyinComparatorMT;
import com.risenb.witness.ui.HomeScreen.SideBar;
import com.risenb.witness.ui.HomeScreen.SortGroupMemberMTAdapter;
import com.risenb.witness.utils.newUtils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.homescreenmeiti)
public class HomeScreenMeitiUI extends Activity implements SectionIndexer {
    private ListView sortListView;
    private SideBar sideBar;
    private SortGroupMemberMTAdapter adapter;

    private List<HomeScreenMediatypeBean> homeScreenCompanyBeen;

    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<HomeScreenMediatypeBean> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparatorMT pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreenmeiti);
        View viw = findViewById(R.id.view_overy);
        viw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        media();
    }

    private void media() {
        Utils.getUtils().showProgressDialog(this);
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.mediatype));
        Map<String, String> param = new HashMap<>();
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                homeScreenCompanyBeen = JSONArray.parseArray(wBaseBean.getData(), HomeScreenMediatypeBean.class);
                initViews();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    private void initViews() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparatorMT();
        sideBar = (SideBar) findViewById(R.id.homescreen_msb);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.homescreenmeiti_lv);
        SourceDateList = filledData(homeScreenCompanyBeen);

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortGroupMemberMTAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Intent intent = getIntent();
                intent.putExtra("name", SourceDateList.get((int) id).getMedianame());
                intent.putExtra("id", SourceDateList.get((int) id).getMediaid());
                setResult(2, intent);
                finish();
            }
        });

        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int section = getSectionForPosition(firstVisibleItem);
                int nextSection = getSectionForPosition(firstVisibleItem + 1);
                int nextSecPosition = getPositionForSection(+nextSection);
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    /**
     * 为ListView填充数据
     */
    private List<HomeScreenMediatypeBean> filledData(List<HomeScreenMediatypeBean> date) {
        List<HomeScreenMediatypeBean> mSortList = new ArrayList<>();

        for (int i = 0; i < date.size(); i++) {
            HomeScreenMediatypeBean sortModel = new HomeScreenMediatypeBean();
            sortModel.setMedianame(date.get(i).getMedianame());
            sortModel.setMediaid(date.get(i).getMediaid());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getMedianame());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        List<HomeScreenMediatypeBean> filterDateList = new ArrayList<HomeScreenMediatypeBean>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (HomeScreenMediatypeBean sortModel : SourceDateList) {
                String name = sortModel.getMedianame();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return SourceDateList.get(position).getSortLetters().charAt(0);
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            String sortStr = SourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
