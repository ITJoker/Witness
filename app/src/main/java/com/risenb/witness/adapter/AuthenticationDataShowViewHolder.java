package com.risenb.witness.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.risenb.witness.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthenticationDataShowViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.authentication_data_show_tv)
    TextView authentication_data_show_tv;

    public AuthenticationDataShowViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
