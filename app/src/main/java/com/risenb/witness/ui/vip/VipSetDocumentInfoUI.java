package com.risenb.witness.ui.vip;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.beans.VipSetDocumentBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.vipsetdocumentinfo)
public class VipSetDocumentInfoUI extends BaseUI {
    @ViewInject(R.id.vipsetdocument_elv)
    private ExpandableListView vipsetdocument_elv;

    private myAdapter vipSetDocumentExpanAdapter;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        setTitle("帮助文档");
        setTitleBackground(255, 255, 255);
    }

    @Override
    protected void prepareData() {
        int width = getWindowManager().getDefaultDisplay().getWidth();
        vipsetdocument_elv.setIndicatorBounds(width - 40, width - 10);
        vipsetdocument_elv.setGroupIndicator(this.getResources().getDrawable(R.drawable.ex_smallimage));
        vipSetDocumentExpanAdapter = new myAdapter(this);
        vipsetdocument_elv.setAdapter(vipSetDocumentExpanAdapter);
        //去掉默认的箭头
        vipsetdocument_elv.setGroupIndicator(null);

        helpDocument();

    }

    @Override
    public void onLoadOver() {

    }

    private void helpDocument() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.helpDocument));
        Map<String, String> param = new HashMap<>();
        param.put("typeid", getIntent().getStringExtra("getTypeId"));
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    List<VipSetDocumentBean> vipSetDocumentBeen = JSONArray.parseArray(wBaseBean.getData(), VipSetDocumentBean.class);
                    vipSetDocumentExpanAdapter.setList(vipSetDocumentBeen);
                } else {
                    makeText(wBaseBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();

            }
        });
    }

    private LayoutInflater layoutInflater = null;

    class myAdapter extends BaseExpandableListAdapter {
        Activity activity;

        List<VipSetDocumentBean> vipSetDocumentBeen;

        public myAdapter(Activity activity) {
            this.activity = activity;
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setList(List<VipSetDocumentBean> vipSetDocumentBeen) {
            this.vipSetDocumentBeen = vipSetDocumentBeen;
            notifyDataSetChanged();
        }

        public Object getChild(int ParentPosition, int childPosition) {
            return null;
        }

        public long getChildId(int ParentPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int ParentPosition) {
            return 1;
        }

        //填充子item
        public View getChildView(int ParentPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.document_child, null);
                holder.fourmText = convertView.findViewById(R.id.document_child_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.fourmText.setText(vipSetDocumentBeen.get(ParentPosition).getContent());
            return convertView;
        }

        public Object getGroup(int ParentPosition) {
            return ParentPosition;
        }

        public int getGroupCount() {
            return vipSetDocumentBeen == null ? 0 : vipSetDocumentBeen.size();
        }

        public long getGroupId(int ParentPosition) {
            return ParentPosition;
        }

        //填充父item
        public View getGroupView(int ParentPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.document_group, null);
                holder.imageView = convertView.findViewById(R.id.document_group_iv);
                holder.fourmText = convertView.findViewById(R.id.document_group_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.fourmText.setText(vipSetDocumentBeen.get(ParentPosition).getTitle());
            //如果展开时 替换右边的箭头
            if (isExpanded) {
                holder.imageView.setBackgroundResource(R.drawable.bangzhuwendangshang);
            } else {
                holder.imageView.setBackgroundResource(R.drawable.bangzhuwendangxia);
            }
            return convertView;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    class ViewHolder {
        ImageView imageView;
        TextView fourmText;
    }
}
