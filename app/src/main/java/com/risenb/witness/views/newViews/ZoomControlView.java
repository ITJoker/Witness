package com.risenb.witness.views.newViews;

import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.risenb.witness.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class ZoomControlView extends RelativeLayout implements OnClickListener {
    private ImageButton mButtonZoomin;
    private ImageButton mButtonZoomout;
    private MapView mapView;
    private int maxZoomLevel;
    private int minZoomLevel;
    private Context context;

    public ZoomControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomControlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.zoom_controls_layout, null);
        mButtonZoomin = (ImageButton) view.findViewById(R.id.zoomin);
        mButtonZoomout = (ImageButton) view.findViewById(R.id.zoomout);
        mButtonZoomin.setOnClickListener(this);
        mButtonZoomout.setOnClickListener(this);
        addView(view);
    }

    @Override
    public void onClick(View v) {
        if (mapView == null) {
            throw new NullPointerException("you can call setMapView(MapView mapView) at first");
        }
        float zoomLevel = mapView.getMap().getMapStatus().zoom;
        switch (v.getId()) {
            case R.id.zoomin:
                if (zoomLevel <= 18) {
                    // MapStatusUpdateFactory.zoomIn();
                    mapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomIn());
                    mButtonZoomout.setEnabled(true);
                    mButtonZoomin.setEnabled(true);
                } else {
                    Toast.makeText(context, "已经放至最大！", Toast.LENGTH_SHORT).show();
                    mButtonZoomin.setEnabled(false);
                }
                break;

            case R.id.zoomout:
                if (zoomLevel > 4) {
                    mapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomOut());
                    mButtonZoomin.setEnabled(true);
                    mButtonZoomout.setEnabled(true);
                } else {
                    mButtonZoomout.setEnabled(false);
                    Toast.makeText(context, "已经缩至最小！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * ��MapView���ù���
     */
    public void setMapView(MapView mapView) {
        this.mapView = mapView;
        // ��ȡ�������ż���
        maxZoomLevel = mapView.getMapLevel();
        // ��ȡ�������ż���
        minZoomLevel = mapView.getMapLevel();
    }

    /**
     * ���MapView�����ż���������Ű�ť��״̬�����ﵽ������ż�������mButtonZoomin Ϊ���ܵ������֮����mButtonZoomout
     */
    public void refreshZoomButtonStatus(int level) {
        if (mapView == null) {
            throw new NullPointerException("you can call setMapView(MapView mapView) at first");
        }
        if (level > minZoomLevel && level < maxZoomLevel) {
            if (!mButtonZoomout.isEnabled()) {
                mButtonZoomout.setEnabled(true);
            }
            if (!mButtonZoomin.isEnabled()) {
                mButtonZoomin.setEnabled(true);
            }
        } else if (level == minZoomLevel) {
            mButtonZoomout.setEnabled(false);
        } else if (level == maxZoomLevel) {
            mButtonZoomin.setEnabled(false);
        }
    }

}
