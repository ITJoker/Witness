package com.risenb.witness.ui.tasklist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.risenb.witness.R;
import com.risenb.witness.adapter.MyPageAdapter;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class ExecGalleryActivity extends Activity {
    // 返回按钮
    private Button back_bt;
    // 发送按钮
    private Button send_bt;
    //删除按钮
    private Button del_bt;
    //顶部显示预览图片位置的textView
    private TextView positionTextView;
    //获取前一个activity传过来的position
    private int position;
    //当前的位置
    private int location = 0;

    private ArrayList<View> listViews = null;
    private ViewPagerFixed pager;
    private MyPageAdapter adapter;

    public List<Bitmap> bmp = new ArrayList<>();

    private TextView show_bt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_camera_gallery);// 切屏到主界面
        back_bt = findViewById(R.id.gallery_back);
        send_bt = findViewById(R.id.send_button);
        show_bt = findViewById(R.id.show_button);
        del_bt = findViewById(R.id.gallery_del);
        back_bt.setOnClickListener(new BackListener());
        send_bt.setOnClickListener(new GallerySendListener());
        del_bt.setOnClickListener(new DelListener());
        Intent intent = getIntent();
        position = Integer.parseInt(intent.getStringExtra("position"));
        isShowOkBt();
        // 为发送按钮设置文字
        pager = findViewById(R.id.gallery01);
        pager.setOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < UncertainExecEvidence.tempSelectBitmap.size(); i++) {
            initListViews(UncertainExecEvidence.tempSelectBitmap.get(i).getBitmap());
        }
        adapter = new MyPageAdapter(listViews, UncertainExecEvidence.tempSelectBitmap, getApplication());
        pager.setAdapter(adapter);
        pager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.dm001));
        int id = intent.getIntExtra("ID", 0);
        pager.setCurrentItem(id);
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
            show_bt.setText("第" + (arg0 + 1) + "张");
        }

        public void onPageScrollStateChanged(int arg0) {

        }

    };

    private void initListViews(Bitmap bm) {
        if (listViews == null)
            listViews = new ArrayList<>();
        PhotoView img = new PhotoView(this);
        img.setBackgroundColor(0xff000000);
        img.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        listViews.add(img);
    }

    // 返回按钮添加的监听器
    private class BackListener implements OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    // 删除按钮添加的监听器
    private class DelListener implements OnClickListener {
        public void onClick(View v) {
            try {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ExecGalleryActivity.this, R.style.ImageloadingDialogStyle);
                dialog.setTitle("操作提示");
                dialog.setMessage("确定删除照片?");
                dialog.setIcon(R.drawable.ic_launcher);
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePhotograph();
                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                //设置对话框消失监听事件
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } catch (InflateException e) {
                return;
            }
        }
    }

    private void deletePhotograph() {
        if (listViews.size() == 1) {
            new File(UncertainExecEvidence.tempSelectBitmap.get(0).getImagePath()).delete();
            UncertainExecEvidence.tempSelectBitmap.clear();
            UncertainExecEvidence.max = 0;
            send_bt.setText("完成" + "(" + UncertainExecEvidence.tempSelectBitmap.size() + "/" + UncertainExecEvidence.num + ")");
            Intent intent = new Intent("data.broadcast.action");
            sendBroadcast(intent);
            finish();
        } else {
            /*
             * 删除内存以及本地的照片文件
             */
            File file = new File(UncertainExecEvidence.tempSelectBitmap.get(location).getImagePath());
            file.delete();
            UncertainExecEvidence.tempSelectBitmap.remove(location);
            UncertainExecEvidence.max--;
            pager.removeAllViews();
            listViews.remove(location);
            adapter.setListViews(listViews);
            send_bt.setText("完成" + "(" + UncertainExecEvidence.tempSelectBitmap.size() + "/" + UncertainExecEvidence.num + ")");
            adapter.notifyDataSetChanged();
        }
        Glide.get(getApplication()).clearMemory();
    }

    // 完成按钮的监听
    private class GallerySendListener implements OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    public void isShowOkBt() {
        if (UncertainExecEvidence.tempSelectBitmap.size() > 0) {
            send_bt.setText("完成" + "(" + UncertainExecEvidence.tempSelectBitmap.size() + "/" + UncertainExecEvidence.num + ")");
            send_bt.setPressed(true);
            send_bt.setClickable(true);
            send_bt.setTextColor(Color.WHITE);
        } else {
            send_bt.setPressed(false);
            send_bt.setClickable(false);
            send_bt.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    /**
     * 监听返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (position == 1) {
                this.finish();
            } else if (position == 2) {
                this.finish();
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
