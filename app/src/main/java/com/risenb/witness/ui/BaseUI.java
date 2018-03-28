package com.risenb.witness.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.utils.newUtils.CrashHandler;
import com.risenb.witness.utils.newUtils.LoadOver;
import com.risenb.witness.utils.newUtils.Log;
import com.risenb.witness.utils.newUtils.MUtils;
import com.risenb.witness.utils.newUtils.OnLoadOver;
import com.risenb.witness.utils.newUtils.UIManager;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：自定义Activity
 */
public abstract class BaseUI extends FragmentActivity implements OnLoadOver {
    /**
     * 申请权限返回值
     */
    private static final int REQUEST_CODE_ASK_ACCESS_FINE_LOCATION_PERMISSIONS = 30;
    private static final int REQUEST_CODE_ASK_ACCESS_COARSE_LOCATION_PERMISSIONS = 31;
    private static final int REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE_PERMISSIONS = 32;
    private static final int REQUEST_CODE_ASK_READ_EXTERNAL_STORAGE_PERMISSIONS = 33;
    private static final int REQUEST_CODE_ASK_READ_PHONE_STATE_PERMISSIONS = 34;
    private static final int REQUEST_CODE_ASK_RECORD_AUDIO_PERMISSIONS = 35;
    private static final int REQUEST_CODE_ASK_GET_ACCOUNTS_PERMISSIONS = 36;
    private static final int REQUEST_CODE_ASK_CAMERA_PERMISSIONS = 37;
    private long exitTime = 0;
    protected MyApplication application;
    private boolean isContentView = false;
    protected boolean isDestroy = true;
    private RelativeLayout back;
    private RelativeLayout rl_title;

    /**
     * 描述：返回
     */
    protected abstract void back();

    /**
     * 描述：设置控件基础
     */
    protected abstract void setControlBasis();

    /**
     * 描述：准备数据
     */
    protected abstract void prepareData();

    /**
     * 描述：创建
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashHandler.getInstance().init(getApplication());
        onCreate();
        x.view().inject(this);
        ViewUtils.inject(this);
        application = (MyApplication) getApplication();
        onInitCreate(savedInstanceState);
        Log.mem();
        UIManager.getInstance().pushActivity(this);
        new LoadOver(getActivity(), this);
        back = findViewById(R.id.back);
        rl_title = findViewById(R.id.rl_title);
        if (back != null) {
            back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    back();
                }
            });
        }
        setControlBasis();
        prepareData();
    }

    /**
     * 标题背景颜色设置
     */
    protected void setTitleBackground(int red, int green, int blue) {
        rl_title.setBackgroundColor(Color.rgb(red, green, blue));
    }

    protected void setTitleDrawableBackground(int ID) {
        rl_title.setBackgroundResource(ID);
    }


    protected void onInitCreate(Bundle savedInstanceState) {

    }

    @Override
    protected void onDestroy() {
        onDetachedDestroy();
        if (isDestroy) {
            UIManager.getInstance().popActivity(getClass());
        }
        super.onDestroy();
    }

    protected void onDetachedDestroy() {

    }

    protected void onCreate() {
        /*
         * Android6.0将android.permission.READ_EXTERNAL_STORAGE列于危险权限,因此使用时需先检查App是否拥有该权限,如果没有进行动态获取!
         */
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> list = new ArrayList();
            ;
            // 位置权限
            list.add(Manifest.permission.ACCESS_FINE_LOCATION);
            list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            // 读取SD卡数据权限
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            // 记录音频视频权限
            list.add(Manifest.permission.RECORD_AUDIO);
            // 开启摄像头权限
            list.add(Manifest.permission.CAMERA);
            // 读取电话状态
            list.add(Manifest.permission.READ_PHONE_STATE);
            /*list.add(Manifest.permission.GET_ACCOUNTS);*/
            for (String permission : list) {
                // Android6.0,检查用户是否拥有该权限
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplication(), permission);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    // 请求android.permission.READ_EXTERNAL_STORAGE权限
                    switch (permission) {
                        case Manifest.permission.ACCESS_FINE_LOCATION:
                            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_CODE_ASK_ACCESS_FINE_LOCATION_PERMISSIONS);
                            break;
                        case Manifest.permission.ACCESS_COARSE_LOCATION:
                            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_CODE_ASK_ACCESS_COARSE_LOCATION_PERMISSIONS);
                            break;
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
                            break;
                        case Manifest.permission.READ_EXTERNAL_STORAGE:
                            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_CODE_ASK_READ_EXTERNAL_STORAGE_PERMISSIONS);
                            break;
                        case Manifest.permission.RECORD_AUDIO:
                            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_CODE_ASK_RECORD_AUDIO_PERMISSIONS);
                            break;
                        case Manifest.permission.CAMERA:
                            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_CODE_ASK_CAMERA_PERMISSIONS);
                            break;
                        case Manifest.permission.READ_PHONE_STATE:
                            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_CODE_ASK_READ_PHONE_STATE_PERMISSIONS);
                            break;
                        /*case Manifest.permission.GET_ACCOUNTS:
                            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_CODE_ASK_GET_ACCOUNTS_PERMISSIONS);
                            break;*/
                    }
                    return;
                } else {
                    // 说明拥有该权限
                }
            }
        } else {
            // 说明当前手机系统为Android6.0以下,使用清单文件中配置的权限即可
        }
    }

    /**
     * onRequestPermissionsResult()为requestPermissions()被执行后的回调机制
     */
    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_ACCESS_FINE_LOCATION_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限获取成功
                } else {
                    // 权限获取失败
                    Toast.makeText(getApplication(), "请允许读取SD卡权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/
    @Override
    public void setContentView(int layoutResID) {
        if (!isContentView) {
            isContentView = true;
            super.setContentView(layoutResID);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected FragmentActivity getActivity() {
        return BaseUI.this;
    }

    /**
     * 退出程序
     */
    protected void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            if (MyApplication.isTaskListScheduleFinish) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.ImageloadingDialogStyle);
                dialog.setTitle("人人提示");
                dialog.setMessage("任务正在批量上传，此时退出可能会中断上传造成数据丢失，确定退出程序?");
                dialog.setIcon(R.drawable.ic_launcher);
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UIManager.getInstance().popAllActivity();
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
            } else {
                UIManager.getInstance().popAllActivity();
            }
        }
    }

    /**
     * 描述：设置标题
     */
    protected void setTitle(String text) {
        TextView tv_title = findViewById(R.id.title);
        if (tv_title != null) {
            // tv_title.setTypeface(application.typeFace);
            tv_title.setText(text);
        }
    }

    /**
     * 描述:隐藏返回按钮
     */
    protected void backGone() {
        RelativeLayout back = findViewById(R.id.back);
        if (back != null) {
            back.setVisibility(View.GONE);
        }
    }

    /**
     * 描述:显示左菜单全部
     */
    protected void leftVisible(String title, int drawable) {
        backGone();
        leftVisible(title);
        leftVisible(drawable);
    }

    /**
     * 描述:显示左菜单文字
     */
    protected void leftVisible(String title) {
        LinearLayout ll_left = findViewById(R.id.ll_left);
        if (ll_left != null) {
            ll_left.setVisibility(View.VISIBLE);
        }
        TextView tv_left = findViewById(R.id.tv_left);
        if (tv_left != null) {
            tv_left.setVisibility(View.VISIBLE);
            tv_left.setText(title);
        }
    }

    /**
     * 描述:显示左菜单图片
     */
    protected void leftVisible(int drawable) {
        LinearLayout ll_left = findViewById(R.id.ll_left);
        if (ll_left != null) {
            ll_left.setVisibility(View.VISIBLE);
        }
        ImageView iv_left = findViewById(R.id.iv_left);
        if (iv_left != null) {
            iv_left.setVisibility(View.VISIBLE);
            iv_left.setImageResource(drawable);
        }
    }

    /**
     * 描述:显示右菜单全部
     */
    protected void rightVisible(String title, int drawable) {
        rightVisible(title);
        rightVisible(drawable);
    }

    /**
     * 描述:显示右菜单文字
     */
    protected void rightVisible(String title) {
        LinearLayout ll_right = findViewById(R.id.ll_right);
        if (ll_right != null) {
            ll_right.setVisibility(View.VISIBLE);
        }
        TextView tv_right = findViewById(R.id.tv_right);
        if (tv_right != null) {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(title);
        }
    }

    /**
     * 描述:显示右菜单图片
     */
    public void rightVisible(int drawable) {
        LinearLayout ll_right = findViewById(R.id.ll_right);
        if (ll_right != null) {
            ll_right.setVisibility(View.VISIBLE);
        }
        ImageView iv_right = findViewById(R.id.iv_right);
        if (iv_right != null) {
            iv_right.setVisibility(View.VISIBLE);
            iv_right.setImageResource(drawable);
        }
    }

    protected void makeText(final String content) {
        MUtils.getMUtils().getHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
            }
        });
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

    /**
     * 此方法只是关闭软键盘
     */
    protected void hintKbTwo() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive() && getActivity().getCurrentFocus() != null) {
            if (getActivity().getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 登录错误
     */
    protected void errorLogin() {
        application.setC("");
        final UserOutPop userOutPop = new UserOutPop(back, getActivity(), R.layout.useroutpop);
        userOutPop.showAsDropDownInstance();
        userOutPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.useroutpop_qx_tv:
                        userOutPop.dismiss();
                        break;
                    case R.id.useroutpop_ok_tv:
                        startActivity(new Intent(getActivity(), LoginUI.class));
                        userOutPop.dismiss();
                        break;
                }
            }
        });
    }
}
