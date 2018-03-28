package com.risenb.witness.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.adapter.TaskInfoPreviewAdapter;
import com.risenb.witness.beans.TaskInfoPreviewBean;
import com.risenb.witness.beans.Tasklist;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.utils.TimesUtils;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.citypicker.CountDownTimerView;
import com.risenb.witness.views.newViews.MyListView;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.taskinfopreview)
public class TaskInfoPreviewUI extends BaseUI {

    @ViewInject(R.id.taskifn_view)
    private View taskifn_view;
    @ViewInject(R.id.back)
    private RelativeLayout back;
    @ViewInject(R.id.taskinfopreview_name_tv)
    private TextView taskinfopreview_name_tv;
    @ViewInject(R.id.taskinfopreview_price_tv)
    private TextView taskinfopreview_price_tv;
    @ViewInject(R.id.taskinfopreview_time_cdtv)
    private CountDownTimerView taskinfopreview_time_cdtv;
    @ViewInject(R.id.taskinfopreview_time_pb)
    private ProgressBar taskinfopreview_time_pb;

    @ViewInject(R.id.taskinfopreview_qz_ll)
    private LinearLayout taskinfopreview_qz_ll;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("任务预览");
    }

    @Override
    protected void prepareData() {
        taskPreview();
    }

    @Override
    public void onLoadOver() {

    }

    private void taskPreview() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskPreview));
        Map<String, String> param = new HashMap<>();
        param.put("taskid", getIntent().getStringExtra("taskid"));
        MyOkHttp.get().post(getActivity(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                TaskInfoPreviewBean taskInfoPreviewBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), TaskInfoPreviewBean.class);
                taskinfopreview_name_tv.setText(taskInfoPreviewBean.getAddress());
                taskinfopreview_price_tv.setText("￥" + taskInfoPreviewBean.getPrice());
                taskinfopreview_time_pb.setMax(Integer.valueOf(taskInfoPreviewBean.getValidity()));
                taskinfopreview_time_pb.setProgress(Integer.valueOf(taskInfoPreviewBean.getRemainTime()));
                String time = TimesUtils.timeDifference(String.valueOf(taskInfoPreviewBean.getRemainTime() * 1000));
                String[] split = time.split(",");
                int dd = Integer.valueOf(split[0]);
                int hh = Integer.valueOf(split[1]);
                int mm = Integer.valueOf(split[2]);
                int ss = Integer.valueOf(split[3]);
                if (dd >= 0 && hh >= 0 && mm >= 0 && ss >= 0) {
                    taskinfopreview_time_cdtv.setTime(dd, hh, mm, ss);
                } else {
                    taskinfopreview_time_cdtv.setTime(0, 0, 0, 0);
                }
                // 开始倒计时
                taskinfopreview_time_cdtv.start();
                setDatas(taskInfoPreviewBean);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    private void setDatas(TaskInfoPreviewBean taskInfoPreviewBean) {
        List<Tasklist> tasklist = taskInfoPreviewBean.getTasklist();
        Collections.sort(tasklist, new Comparator<Tasklist>() {
            @Override
            public int compare(Tasklist o1, Tasklist o2) {
                return o1.getSort() - o2.getSort();
            }
        });
        if (tasklist.size() != 0) {
            taskifn_view.setBackgroundResource(R.color.main);
        }
        for (int i = 0; i < tasklist.size(); i++) {
            if (i == 0) {
                if (tasklist.get(i).getIsType() == 0) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.taskinfoppreview_three, null);
                    TextView name = (TextView) view.findViewById(R.id.three_name_tv);
                    TextView info = (TextView) view.findViewById(R.id.three_info_tv);
                    ImageView image = (ImageView) view.findViewById(R.id.three_image_iv);
                    name.setText("拍摄取证" + tasklist.get(i).getSort());
                    info.setText(tasklist.get(i).getTaskRemark());
                    ImageLoader.getInstance().displayImage(tasklist.get(i).getExampleFile().get(0), image, MyConfig.options);
                    ViewUtils.inject(getActivity(), view);
                    taskinfopreview_qz_ll.addView(view);
                }
                if (tasklist.get(i).getIsType() == 1) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.taskinfoppreview_one, null);
                    TextView name = (TextView) view.findViewById(R.id.one_name_tv);
                    TextView info = (TextView) view.findViewById(R.id.one_info_tv);
                    ImageView image = (ImageView) view.findViewById(R.id.one_image_iv);
                    name.setText("拍摄取证" + tasklist.get(i).getSort());
                    info.setText(tasklist.get(i).getTaskRemark());
                    ImageLoader.getInstance().displayImage(tasklist.get(i).getExampleFile().get(0), image, MyConfig.options);
                    ViewUtils.inject(getActivity(), view);
                    taskinfopreview_qz_ll.addView(view);
                }
                if (tasklist.get(i).getIsType() == 2) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.taskinfoppreview_four, null);
                    ViewUtils.inject(getActivity(), view);
                    taskinfopreview_qz_ll.addView(view);
                    MyListView myListView = view.findViewById(R.id.taskinfopreview_mlv);
                    TaskInfoPreviewAdapter taskAdapter = new TaskInfoPreviewAdapter();
                    myListView.setAdapter(taskAdapter);
                    taskAdapter.setList(tasklist.get(i).getExampleFile());
                }
            } else {
                if (tasklist.get(i).getIsType() == 0) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.taskinfoppreview_two, null);
                    TextView name = (TextView) view.findViewById(R.id.two_name_tv);
                    TextView info = (TextView) view.findViewById(R.id.two_info_tv);
                    ImageView image = (ImageView) view.findViewById(R.id.two_image_iv);
                    name.setText("拍摄取证" + tasklist.get(i).getSort());
                    info.setText(tasklist.get(i).getTaskRemark());
                    ImageLoader.getInstance().displayImage(tasklist.get(i).getExampleFile().get(0), image, MyConfig.options);
                    ViewUtils.inject(getActivity(), view);
                    taskinfopreview_qz_ll.addView(view);
                }
                if (tasklist.get(i).getIsType() == 1) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.taskinfoppreview_five, null);
                    TextView name = (TextView) view.findViewById(R.id.five_name_tv);
                    TextView info = (TextView) view.findViewById(R.id.five_info_tv);
                    ImageView image = (ImageView) view.findViewById(R.id.five_image_iv);
                    name.setText("拍摄取证" + tasklist.get(i).getSort());
                    info.setText(tasklist.get(i).getTaskRemark());
                    ImageLoader.getInstance().displayImage(tasklist.get(i).getExampleFile().get(0), image, MyConfig.options);
                    ViewUtils.inject(getActivity(), view);
                    taskinfopreview_qz_ll.addView(view);
                }
                if (tasklist.get(i).getIsType() == 2) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.taskinfoppreview_four, null);
                    ViewUtils.inject(getActivity(), view);
                    taskinfopreview_qz_ll.addView(view);
                    MyListView myListView = (MyListView) view.findViewById(R.id.taskinfopreview_mlv);
                    TaskInfoPreviewAdapter taskAdapter = new TaskInfoPreviewAdapter();
                    myListView.setAdapter(taskAdapter);
                    taskAdapter.setList(tasklist.get(i).getExampleFile());
                }
            }
        }
    }
}
