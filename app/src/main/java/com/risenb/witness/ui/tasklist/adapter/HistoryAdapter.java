package com.risenb.witness.ui.tasklist.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.risenb.witness.R;
import com.risenb.witness.beans.HistoryCompleted;
import com.risenb.witness.utils.Utility;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<HistoryCompleted.TaskListBean> list;
    public String sign = "";
    public String historyAdapterTaskID = "";
    private String remark;
    private String address;

    public HistoryAdapter(Context context, List<HistoryCompleted.TaskListBean> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.history_task_adapter_item, null, false);
        ImageView markImageView =  view.findViewById(R.id.marke_image);
        TextView pictureDescription = view.findViewById(R.id.pictureDescription);
        TextView pictureRequest = view.findViewById(R.id.pictureRequest);
        TextView mHistoryRequest = view.findViewById(R.id.history_request);
        if (position != 0 || sign.equals("Reject")) {
            if ("1".equals(list.get(position).getIsType())) {
                uploadedDataShow(position, view);
                mHistoryRequest.setText(list.get(position).getTaskRemark());
                markImageView.setBackgroundResource(R.drawable.step_two);
            }
        } else if (position == 0 && !sign.equals("Reject")) {
            if (list.size() == 1) {
                // 固定任务详情展示
                uploadedDataShow(position, view);
                pictureRequest.setText("备注：");
                if (TextUtils.isEmpty(remark)) {
                    mHistoryRequest.setText("当前任务无备注");
                } else {
                    mHistoryRequest.setText(remark);
                }
                if (!TextUtils.isEmpty(address)) {
                    pictureDescription.setText("拍摄地点：" + address);
                } else {
                    pictureDescription.setText("拍摄地点：未知");
                }
                return view;
            }
            if ("2".equals(list.get(position).getIsType())) {
                // 上刊状态
                StringBuffer stringBuffer = new StringBuffer();
                for (String condition : list.get(position).getExampleFile()) {
                    stringBuffer.append("\n" + condition);
                }
                pictureDescription.setText("选择上刊");
                pictureRequest.setText("上刊状态：");
                mHistoryRequest.setText(list.get(position).getReturnfile().get(0));
            }
        }
        return view;
    }

    private void uploadedDataShow(int position, View view) {
        //示例图展示
        /*ListView exampleImageListView = view.findViewById(R.id.history_task_exampleImageListView);
        ImageAdapter exampleImageAdapter = new ImageAdapter(list.get(position).getExampleFile(), "exampleImage", list.get(position).getIsType());
        exampleImageListView.setAdapter(exampleImageAdapter);
        Utility.setListViewHeightBasedOnChildren(exampleImageListView);*/
        //已上传照片
        ListView cameraImageListView = view.findViewById(R.id.history_task_cameraImageListView);
        ImageAdapter cameraImageAdapter = new ImageAdapter(list.get(position).getReturnfile(), "cameraImage", list.get(position).getIsType());
        if (sign.equals("Reject")) {
            cameraImageAdapter.rejectSign = "Reject";
            cameraImageAdapter.imageAdapterTaskID = historyAdapterTaskID;
            cameraImageAdapter.historyAdapter = this;
        }
        cameraImageListView.setAdapter(cameraImageAdapter);
        Utility.setListViewHeightBasedOnChildren(cameraImageListView);
    }

    public void showRemark(String remark) {
        this.remark = remark;
    }

    public void showAddress(String address) {
        this.address = address;
    }
}
