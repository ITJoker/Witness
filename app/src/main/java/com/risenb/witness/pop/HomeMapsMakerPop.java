package com.risenb.witness.pop;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.risenb.witness.R;
import com.risenb.witness.adapter.HomeMapsMakerAdapter;
import com.risenb.witness.beans.HomeMapsMakerBeans;

import java.util.List;

public class HomeMapsMakerPop extends CommentPopUtils implements View.OnClickListener {

    private Context context;

    private ListView myListView;

    private View homemaps_view;

    private HomeMapsMakerAdapter homeMapsMakerAdapter;

    public HomeMapsMakerPop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }

    @Override
    public void initLayout(View view, Context context) {
        myListView = (ListView) view.findViewById(R.id.homemapsmaker_mlv);
        homemaps_view = view.findViewById(R.id.homemaps_view);
        homeMapsMakerAdapter = new HomeMapsMakerAdapter();
        myListView.setAdapter(homeMapsMakerAdapter);
        homemaps_view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.homemaps_view) {
            dismiss();
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    public void setData(List<HomeMapsMakerBeans> homeMapsMakerBeanses) {
        homeMapsMakerAdapter.setList(homeMapsMakerBeanses);
    }

}
