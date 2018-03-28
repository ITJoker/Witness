package com.risenb.witness.ui.tasklist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.risenb.witness.R;
import com.risenb.witness.beans.CheckBoxState;
import com.risenb.witness.beans.ExecTaskInfo;
import com.risenb.witness.beans.TaskProblemBean;
import com.risenb.witness.utils.MyListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListparentsAdapter extends BaseAdapter {

    private Context context;
    private List<TaskProblemBean> list;
    //    private AnswerAdapterItem mAdapteritem;
    private List<ExecTaskInfo.AnswerBean> Answerlist;
    HashMap<Integer, Boolean> result;
    private CheckBoxState CheckBoxStateresult;
    private String mEmptyStr;
    private AnswerAdapterItem mAdapteritem;
    Map<Integer, Objects> h = new HashMap<>();

    public ListparentsAdapter(List<TaskProblemBean> list, Context context) {
        this.list = list;
        this.context = context;
        result = new HashMap<Integer, Boolean>();
    }

    public void setList(List<ExecTaskInfo.AnswerBean> Answerlist) {
        this.Answerlist = Answerlist;
        notifyDataSetChanged();
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listparent_list, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mTextViewProblem.setText(list.get(position).getTitle() + "");
        list.get(position).getProblem();
        mAdapteritem = new AnswerAdapterItem(context, list.get(position).getProblem(), list.get(position).getType(), list.get(position).getTestId());
        viewHolder.mListView.setAdapter(mAdapteritem);
        if (null != Answerlist) {
            mAdapteritem.setList(Answerlist);
            mAdapteritem.notifyDataSetChanged();
        }
//        mEmptyStr= mAdapteritem.getTextContent();
//        setmEmptyStr(mEmptyStr);
        if (null != mAdapteritem.getCheckBoxState().getIsSelected()) {
            CheckBoxStateresult = mAdapteritem.getCheckBoxState();
            setCheckBoxStateresult(CheckBoxStateresult);
        }

        viewHolder.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getAdapter().getItem(position);
//                mAdapteritem.selectCheckBoxPosition(position);
//                mAdapteritem.notifyDataSetInvalidated();	//更新UI界面
//                Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
            }
        });
        //viewHolder.mListView.setFocusable(false);
        // setListViewHeightBasedOnChildren(viewHolder.mListView);
        return view;
    }

    public class ViewHolder {
        @BindView(R.id.single_problem)
        TextView mTextViewProblem;
        @BindView(R.id.list_parent)
        MyListView mListView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    // @param
    //  此方法是本次listview嵌套listview的核心方法：计算parentlistview item的高度。
    //  如果不使用此方法，无论innerlistview有多少个item，则只会显示一个item。

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public HashMap<Integer, Boolean> getResult() {
        return result;
    }

    public void setResult(HashMap<Integer, Boolean> result) {
        this.result = result;
    }

    public CheckBoxState getCheckBoxStateresult() {
        return CheckBoxStateresult;
    }

    public void setCheckBoxStateresult(CheckBoxState checkBoxStateresult) {
        CheckBoxStateresult = checkBoxStateresult;
    }

//    public String getmEmptyStr() {
//        if(null==mEmptyStr||"".equals(mEmptyStr)){
//            mEmptyStr= mAdapteritem.getTextContent();
//        }
//        return mEmptyStr;
//    }
//
//    public void setmEmptyStr(String mEmptyStr) {
//        this.mEmptyStr = mEmptyStr;
//    }
}
