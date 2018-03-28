package com.risenb.witness.ui.tasklist.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RejectTaskAdapter extends RecyclerView.Adapter<RejectTaskAdapter.ViewHolder> {

    private Activity mActivity;
    List<String> list;

    public RejectTaskAdapter(Activity mActivity, List<String> list, String layoutmark){
          this.mActivity=mActivity;
        this.list=list;
    }


    @Override
    public RejectTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RejectTaskAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View view) {
            super(view);
        }
    }
}
