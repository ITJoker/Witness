package com.risenb.witness.ui.tasklist.CustomCamera;

import java.io.IOException;
import java.util.ArrayList;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.newUtils.UIManager;

import static android.hardware.Camera.Parameters.FLASH_MODE_OFF;
import static android.hardware.Camera.Parameters.FLASH_MODE_TORCH;

public class TakePhotoActivity extends BaseUI{
    private SurfaceView surfaceView;
    //Surface的控制器
    private SurfaceHolder surfaceHolder;

    private ShutterCallback shutterCallback;
    private PictureCallback rawCallback;
    //拍照的回调接口，存储JPG文件到SD卡
    private PictureCallback pictureCallback;

    private Camera camera;
    private AutoFocusCallback autoFocusCallback;
    private Button cameraFlashlightButton;

    private static final int REQUEST_CODE_ASK_CAMERA_PERMISSIONS = 37;
    private String photoSavePath;
    private boolean flashModeType = false;

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_take_photo);
    }

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void exit() {
        back();
    }

    @Override
    protected void setControlBasis() {

    }

    @Override
    protected void prepareData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initViews();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CAMERA_PERMISSIONS);
        }
    }

    private void initViews() {
        photoSavePath = getIntent().getStringExtra("photoSavePath");
        cameraFlashlightButton = findViewById(R.id.camera_flash_light);
        final Button saveButton = findViewById(R.id.camera_save);
        surfaceView = findViewById(R.id.camera_preview);
        surfaceView.setVisibility(View.VISIBLE);
        surfaceHolder = surfaceView.getHolder();
        //给SurfaceView当前的持有者，SurfaceHolder一个回调对象。
        //用户可以实现此接口接收surface变化的消息，当用在一个SurfaceView中时，
        //它只在SurfaceHolder.Callback.surfaceCreated()和SurfaceHolder.Callback.surfaceDestroyed()之间有效
        //设置Callback的方法是SurfaceHolder.addCallback.
        //实现过程一般继承SurfaceView并实现SurfaceHolder.Callback接口
        surfaceHolder.addCallback(surfaceCallback);
        //设置surface不需要自己的维护缓存区
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置surfaceView分辨率
        surfaceHolder.setFixedSize(1280, 720);
        pictureCallback = new PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                byte[] bytes = photoSavePath.getBytes();
                SavePictureTask savePictureTask = new SavePictureTask();
                savePictureTask.setSavePictureTaskCompletedListener(new SavePictureTaskCompletedListener() {
                    @Override
                    public void savePictureTaskCompleted() {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                savePictureTask.execute(data, bytes);
            }
        };

        rawCallback = new PictureCallback() {
            public void onPictureTaken(byte[] _data, Camera camera) {
                /* 如需要处理raw则在这里写代码 */
            }
        };

        shutterCallback = new ShutterCallback() {
            public void onShutter() {
                /* 按快门瞬间会执行这里的代码 */
            }
        };

        // 自动对焦
        autoFocusCallback = new AutoFocusCallback() {
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

        cameraFlashlightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openOrCloseFlashLight();
            }
        });

        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flashModeType) {
                    Parameters cameraParameters = camera.getParameters();
                    cameraParameters.setFlashMode(FLASH_MODE_TORCH);
                    camera.setParameters(cameraParameters);
                }
                MyApplication.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 从Camera捕获图片
                        camera.takePicture(null, null, pictureCallback);
                        /*camera.takePicture(shutterCallback, rawCallback, pictureCallback);*/
                    }
                }, 300);
            }
        });

        SeekBar seekbar = findViewById(R.id.seekbar);
        SeekBar.OnSeekBarChangeListener onZoomChangeListener = new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progressState = progress / 5;
                Camera.Parameters parameters = camera.getParameters();
                if (Boolean.parseBoolean(parameters.get("zoom-supported"))) {
                    parameters.setZoom(progressState);
                }
                camera.setParameters(parameters);
            }
        };
        seekbar.setOnSeekBarChangeListener(onZoomChangeListener);
    }

    //SurfaceView当前的持有者的回调接口的实现
    Callback surfaceCallback = new Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // 开启相机
            if (camera == null) {
                camera = Camera.open();
                camera.setDisplayOrientation(90);
                Camera.Parameters parameters = camera.getParameters();
                /*parameters.set("orientation", "landscape");
                parameters.set("rotation", 90);*/
                parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                camera.autoFocus(autoFocusCallback);
                camera.cancelAutoFocus();
                try {
                    camera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Parameters cameraParameters = camera.getParameters();
            cameraParameters.setPictureFormat(ImageFormat.JPEG);
            cameraParameters.setPictureSize(1280, 720);
            camera.setParameters(cameraParameters);
            camera.startPreview();
            camera.cancelAutoFocus();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            /*
             * 聚焦区域
             */
            parameters.setFocusAreas(new ArrayList<Camera.Area>());
            camera.autoFocus(autoFocusCallback);
        }
        return true;
    }

    private void openOrCloseFlashLight() {
        /*String flashMode = camera.getParameters().getFlashMode();
        Parameters cameraParameters = camera.getParameters();
        if (FLASH_MODE_OFF.equals(flashMode)) {
            //闪光灯由关闭状态改变为开启状态
            cameraParameters.setFlashMode(FLASH_MODE_TORCH);
            flashModeType = true;
            cameraFlashlightButton.setText("关闭闪光灯");
        } else if (FLASH_MODE_TORCH.equals(flashMode)) {
            //闪光灯由开启状态改变为关闭状态
            cameraParameters.setFlashMode(FLASH_MODE_OFF);
            flashModeType = false;
            cameraFlashlightButton.setText("开启闪光灯");
        }
        cameraParameters.setPictureFormat(ImageFormat.JPEG);
        cameraParameters.setPictureSize(1280, 720);
        camera.setParameters(cameraParameters);*/
        if (flashModeType) {
            flashModeType = false;
            cameraFlashlightButton.setText("开启闪光灯");
        } else {
            flashModeType = true;
            cameraFlashlightButton.setText("关闭闪光灯");
        }
    }

    @Override
    public void onLoadOver() {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CAMERA_PERMISSIONS:
                if (permissions[0].equals(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限获取成功
                    initViews();
                } else {
                    // 权限获取失败
                    back();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

