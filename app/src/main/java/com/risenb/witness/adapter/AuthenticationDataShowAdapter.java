package com.risenb.witness.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.risenb.witness.R;
import com.risenb.witness.beans.AuthenticationBean;

import java.util.List;

public class AuthenticationDataShowAdapter extends RecyclerView.Adapter<AuthenticationDataShowViewHolder> {
    private List<AuthenticationBean.DataBean> dataList;

    public AuthenticationDataShowAdapter(List<AuthenticationBean.DataBean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public AuthenticationDataShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.authentication_data_show_item, parent, false);
        return new AuthenticationDataShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AuthenticationDataShowViewHolder holder, int position) {
        holder.authentication_data_show_tv.setText(dataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
