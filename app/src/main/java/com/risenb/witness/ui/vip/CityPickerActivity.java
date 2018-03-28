package com.risenb.witness.ui.vip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.risenb.witness.R;
import com.risenb.witness.views.citypicker.City;
import com.risenb.witness.views.citypicker.CityListAdapter;
import com.risenb.witness.views.citypicker.DBManager;
import com.risenb.witness.views.citypicker.ResultListAdapter;
import com.risenb.witness.views.citypicker.SideLetterBar;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import android.support.v7.app.AppCompatActivity;

public class CityPickerActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView mListView;
    private ListView mResultListView;
    private SideLetterBar mLetterBar;
    private ImageView clearBtn;
    private ImageView backBtn;
    private ViewGroup emptyView;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        initData();
        initView();
    }

    private void initData() {
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        mAllCities = dbManager.getAllCities();
        mCityAdapter = new CityListAdapter(this, mAllCities);
//        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
//            @Override
//            public void onCityClick(String name) {
//                back(name);
//            }
//
//            @Override
//            public void onLocateClick() {
//                Log.e("onLocateClick", "重新定位...");
//                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
//                // mLocationClient.startLocation();
//            }
//        });

        mResultAdapter = new ResultListAdapter(this, null);
    }

    private void initView() {
        mListView = findViewById(R.id.listview_all_city);
        mListView.setAdapter(mCityAdapter);
        // View view_over =findViewById(R.id.view_overy);
        // view_over.getBackground().setAlpha(125);//0~255透明度值
        TextView overlay = findViewById(R.id.tv_letter_overlay);
        mLetterBar = findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        emptyView =  findViewById(R.id.empty_view);
        mResultListView = findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                back(mResultAdapter.getItem(position).getName());
            }
        });

        clearBtn = findViewById(R.id.iv_search_clear);
        backBtn = findViewById(R.id.back);

        clearBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void back(String city) {
        Intent data = getIntent();
        data.putExtra("city", city);
        setResult(1, data);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_clear:
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                mResultListView.setVisibility(View.GONE);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
