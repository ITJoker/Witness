package com.risenb.witness.ui.tasklist.CustomCamera;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.risenb.witness.R;

/**
 * 自定义相机的activity
 */
public class CameraActivity extends Activity {
    public static final String TAG = "CameraActivity";

    private CameraManager mCameraManager;

    private TextView m_tvFlashLight, m_tvCameraDireation;
    private SquareCameraContainer mCameraContainer;

    private int mFinishCount = 2;   //finish计数   当动画和异步任务都结束的时候  再调用finish方法

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mCameraManager = CameraManager.getInstance(this);

        initView();
        initData();
        initListener();
    }

    void initView() {
        m_tvFlashLight = findViewById(R.id.tv_flashlight);
        m_tvCameraDireation = findViewById(R.id.tv_camera_direction);
        mCameraContainer = findViewById(R.id.cameraContainer);
    }

    void initData() {
        mCameraManager.bindOptionMenuView(m_tvFlashLight, m_tvCameraDireation);
        mCameraContainer.setImagePath(getIntent().getStringExtra("photoSavePath"));
        mCameraContainer.bindActivity(this);
    }

    void initListener() {
        if (mCameraManager.canSwitch()) {
            m_tvCameraDireation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_tvCameraDireation.setClickable(false);
                    mCameraContainer.switchCamera();

                    //500ms后才能再次点击
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            m_tvCameraDireation.setClickable(true);
                        }
                    }, 500);
                }
            });
        }

        m_tvFlashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraContainer.switchFlashMode();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCameraContainer != null) {
            mCameraContainer.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraContainer != null) {
            mCameraContainer.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraManager.unbinding();
        mCameraManager.releaseActivityCamera();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //在创建前释放相机
    }

    /**
     * 对一些参数重置
     */
    public void rest() {
        mFinishCount = 2;
    }

    /**
     * 退出按钮点击
     */
    public void onExitClicked(View view) {
        onBackPressed();
    }

    /**
     * 照相按钮点击
     */
    public void onTakePhotoClicked(View view) {
        mCameraContainer.takePicture();
    }

    /**
     * 照完照片提交
     * 提交finish任务和进行计数都在mainThread
     */
    public void postTakePhoto() {
//        mFinishCount--;
//        if (mFinishCount < 0) mFinishCount = 2;
//        if (mFinishCount == 0) {
//            setResult(RESULT_OK);
//            finish();
//        }

//        mCameraManager.releaseActivityCamera();

        Toast.makeText(this, "take photo", Toast.LENGTH_SHORT).show();
    }
}
