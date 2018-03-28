package com.risenb.witness.ui.tasklist.PlayVideo;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;

public class PlayVideoActivity extends Activity {
    private SurfaceView surfaceView;
    //显示时间的文本框
    private TextView time_tv;
    private MediaRecorder mRecorder;
    // 记录是否正在录像,fasle为未录像, true 为正在录像
    private boolean recording;
    // 存放视频的文件夹
    private File videoFolder;
    // 视频文件
    private File videoFile;
    private Handler handler;
    // 时间
    private int time;
    // 相机声明
    private Camera myCamera;
    private SurfaceHolder holder;
    // 视频保存路径
    private String videoSavePath = MyApplication.videosSaveUrl;
    /*
     * 录制过程中,调节时间进度
     */
    private Runnable timeRun = new Runnable() {
        @Override
        public void run() {
            time++;
            time_tv.setText(time + "秒");
            handler.postDelayed(timeRun, 1000);
        }
    };

    private Camera.Parameters parameters;
    private Camera.AutoFocusCallback autoFocusCallback;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 强制横屏
        /*setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/
        setContentView(R.layout.activity_play_video);
        String taskID = getIntent().getStringExtra("taskid");
        String savePath = getIntent().getStringExtra("videoSavePath");
        if (savePath != null) {
            videoSavePath = savePath;
        }
        Button playVideo_bt;
        Button stopVideo_bt;
        // 获取控件
        surfaceView = findViewById(R.id.surfaceview);
        playVideo_bt = findViewById(R.id.playVideo_bt);
        stopVideo_bt = findViewById(R.id.stopVideo_bt);
        time_tv = findViewById(R.id.time);

        handler = new Handler();
        holder = surfaceView.getHolder();

        // 提示用户录制时长
        Toast.makeText(this, "视频录制时间为320秒，请保持屏幕常亮录制", Toast.LENGTH_LONG).show();

        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (sdCardExist) {
            // 设定存放视频的文件夹的路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator
                    + this.videoSavePath
                    /*+ File.separator*/
                    + taskID
                    + File.separator;

            // 声明存放视频的文件夹的File对象
            videoFolder = new File(path);

            // 如果不存在此文件夹,则创建
            if (!videoFolder.exists()) {
                videoFolder.mkdirs();
            }

            // 设置surfaceView不管理的缓冲区
            surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            // 设置surfaceView分辨率
            surfaceView.getHolder().setFixedSize(1280, 720);

            // 录像点击事件
            playVideo_bt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!recording) {
                        try {
                            // 关闭预览并释放资源
                            /*myCamera.stopPreview();
                            myCamera.release();
                            myCamera = null;*/
                            myCamera.stopPreview();
                            myCamera.unlock();
                            mRecorder = new MediaRecorder();
                            mRecorder.setCamera(myCamera);
                            // 获取当前时间,作为视频文件的文件名
                            String nowTime = java.text.MessageFormat.format("{0,date,yyyyMMdd_HHmmss}",
                                    new Object[]{new java.sql.Date(System.currentTimeMillis())});
                            // 声明视频文件对象
                            videoFile = new File(videoFolder.getAbsoluteFile() + File.separator + "video" + nowTime + ".mkv");
                            // 创建此视频文件
                            videoFile.createNewFile();
                            System.out.println("视频文件: " + videoFile.getAbsolutePath());
                            // 预览
                            mRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
                            // 视频源
                            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                            // 录音源为麦克风
                            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            /* 引用android.util.DisplayMetrics 获取分辨率 */
                            /**
                             * DisplayMetrics dm = new DisplayMetrics();
                             * getWindowManager().getDefaultDisplay().getMetrics(dm);
                             */
                            // 输出格式为mp4
                            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                            // 视频尺寸
                            mRecorder.setVideoSize(1280, 720);
                            // 视频帧频率
                            // 现象:在2.3上正常,4.0上报错,原因:每秒12-15帧之间足以表示运动,电视是30
                            mRecorder.setVideoFrameRate(30);
                            // 视频编码
                            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                            // 音频编码
                            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                            // 设置录制视频的编码位率
                            mRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
                            // 设置音视频质量
                            // mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                            // 5min20s
                            mRecorder.setMaxDuration(320000);
                            mRecorder.setOutputFile(videoFile.getAbsolutePath());
                            // 准备录像
                            mRecorder.prepare();
                            // 开始录像
                            mRecorder.start();
                            // 设置文本框可见
                            time_tv.setVisibility(View.VISIBLE);
                            // 调用Runable
                            handler.post(timeRun);
                            // 改变录制状态为正在录制
                            recording = true;
                            // 监听最大时长
                            mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                                @Override
                                public void onInfo(MediaRecorder mr, int what, int extra) {
                                    if (what == mr.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                                        videoStop();
                                        Toast.makeText(getApplicationContext(), "视频已达到最大时长", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplication(), "视频正在录制中...", Toast.LENGTH_LONG).show();
                        if (myCamera == null) {
                            myCamera = Camera.open();
                        }
                    }
                }
            });
        } else
            Toast.makeText(this, "未找到sdCard!", Toast.LENGTH_LONG).show();

        // 停止点击事件
        stopVideo_bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (recording) {
                    if (time < 3) {
                        Toast.makeText(getApplication(), "视频时长小于3秒，请继续录制", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    videoStop();
                }
                // 开启相机
                if (myCamera == null) {
                    myCamera = Camera.open();
                    myCamera.setDisplayOrientation(90);
                    try {
                        myCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // 开启预览
                myCamera.startPreview();
                finish();
            }
        });

        // 添加回调
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                System.out.println("------surfaceCreated------");
                // 开启相机
                if (myCamera == null) {
                    myCamera = Camera.open();
                    // 自动对焦
                    autoFocusCallback = new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                // myCamera.setOneShotPreviewCallback(null);
                                Toast.makeText(getApplication(), "自动聚焦成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "自动聚焦失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    try {
                        myCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                System.out.println("------surfaceChanged------");
                // 开始预览
                myCamera.startPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                System.out.println("------surfaceDestroyed------");
                // 关闭预览并释放资源
                if (myCamera != null) {
                    myCamera.stopPreview();
                    myCamera.release();
                    myCamera = null;
                }
            }
        });
    }

    private void videoStop() {
        mRecorder.stop();
        mRecorder.release();
        handler.removeCallbacks(timeRun);
        time_tv.setVisibility(View.GONE);
        int videoTimeLength = time;
        time = 0;
        recording = false;
        /*Toast.makeText(getApplication(), videoFile.getAbsolutePath() + "  " + videoTimeLength + "秒", Toast.LENGTH_SHORT).show();*/
        Intent intent = new Intent();
        intent.putExtra("videoFilePath", videoFile.getPath());
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(timeRun);
        if (mRecorder != null) {
            mRecorder.release();
        }
        if (myCamera != null) {
            myCamera.stopPreview();
            myCamera.release();
            //myCamera.lock();
            myCamera = null;
        }
        super.onDestroy();
    }

    /*// 相机参数的初始化设置
    private void initCamera()
    {
        parameters = myCamera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        // 部分定制手机，无法正常识别该方法
        //parameters.setPictureSize(surfaceView.getWidth(), surfaceView.getHeight());
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        // 连续对焦
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        //setDispaly(parameters, myCamera);
        myCamera.setParameters(parameters);
        myCamera.startPreview();
        // 如果要实现连续的自动对焦，需加这一句
        myCamera.cancelAutoFocus();
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Camera.Parameters parameters = myCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            myCamera.autoFocus(autoFocusCallback);
        }
        return true;
    }

    public void finishActivity(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
