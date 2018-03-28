package com.risenb.witness.ui.tasklist.mediarecorder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.risenb.witness.R;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MediaRecorderActivity extends Activity implements View.OnClickListener,TextureView.SurfaceTextureListener {
    private Camera mCamera;
    private TextureView mPreview;
    private MediaRecorder mMediaRecorder;
    private File mOutputFile;
    private Button mCloseCurrentActivity,mVideoSave;
    private TextView mTimecount;
    private boolean isRecording = false;
    private static final String TAG = "Recorder";
    private Button captureButton;
    private Camera.AutoFocusCallback mAutoFocusCallback;
    private String mVideoPath;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private boolean ispreview=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 强制横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.mediarecorder);

        mPreview = (TextureView) findViewById(R.id.surface_view);
        captureButton = (Button) findViewById(R.id.button_capture);
        mCloseCurrentActivity= (Button) findViewById(R.id.close);
        mVideoSave= (Button) findViewById(R.id.ok_save);
        mCloseCurrentActivity.setOnClickListener(this);
        mVideoSave.setOnClickListener(this);
       // mPreview.setSurfaceTextureListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
               prepareVideoRecorder();
            }
        }).start();
    }

    public void onCaptureClick(View view) {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED)
//        {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CAMERA},
//                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
//        } else
//        {
         //   callPhone();

            onRecorderVideo();
       // }
    }

    public void onRecorderVideo(){
        if (isRecording) {
            // BEGIN_INCLUDE(stop_release_media_recorder)
            // stop recording and release camera
            try {
                mMediaRecorder.stop();  // stop the recording
              //  recordTime=SystemClock.elapsedRealtime()-mTimecount.getBase();//保存这次记录的时间
                //mTimecount.stop();
            } catch (RuntimeException e) {
                // RuntimeException is thrown when stop() is called immediately after start().
                // In this case the output file is not properly constructed ans should be deleted.
                Log.d(TAG, "RuntimeException: stop() is called immediately after start()");
                //noinspection ResultOfMethodCallIgnored
                mOutputFile.delete();
            }
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
            setCaptureButtonText("录制");
            isRecording = false;
            releaseCamera();
            // END_INCLUDE(stop_release_media_recorder)

        } else {

            // BEGIN_INCLUDE(prepare_start_media_recorder)

            new MediaPrepareTask().execute(null, null, null);

            // END_INCLUDE(prepare_start_media_recorder)

        }
    }

    private void setCaptureButtonText(String title) {
        captureButton.setText(title);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
        // release the camera immediately on pause event
        releaseCamera();
        MobclickAgent.onPause(this);
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            mCamera.lock();
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private boolean prepareVideoRecorder(){

        mCamera = CameraHelper.getDefaultCameraInstance();
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                mSupportedPreviewSizes, mPreview.getWidth(), mPreview.getHeight());
        mCamera.setDisplayOrientation(90);
        parameters.setFlashMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对
        Log.e("mPreview.getWidth()",mPreview.getWidth()+"");
        Log.e("mPreview.getHeight()",mPreview.getHeight()+"");

        // Use the same size for recording profile.
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_CIF);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;

        // likewise for the camera object itself.
        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
//
        mCamera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
//        mCamera.autoFocus(mAutoFocusCallback);
        mCamera.setParameters(parameters);
        try {
            // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
            // with {@link SurfaceView}
            mCamera.setPreviewTexture(mPreview.getSurfaceTexture());
        } catch (IOException e) {
            Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.getMessage());
            return false;
        }
        // END_INCLUDE (configure_preview)


        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT );
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(profile);
        mMediaRecorder.setVideoSize(480,320);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
        mMediaRecorder.setMaxDuration(3*60*1000);
       // mMediaRecorder.setMaxDuration(10*1000);
        // Step 4: Set output file
        mOutputFile = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO);
        if (mOutputFile == null) {
            return false;
        }
        mMediaRecorder.setOrientationHint(90);//视频旋转90度
        mMediaRecorder.setOutputFile(mOutputFile.getPath());
        Log.e("mMediaRecorder",mOutputFile.getPath());
        mVideoPath=mOutputFile.getPath();
        // END_INCLUDE (configure_media_recorder)

        // Step 5: Prepare configured MediaRecorder

        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }

        ispreview=true;
        Log.e("prepare","预览");
        return true;
    }

    @Override
    public void onClick(View v) {

       switch (v.getId()){
           case R.id.close:
               if(null!=mVideoPath){

                   mVideoPath =null;
               }

               releaseCamera();
               releaseMediaRecorder();
//               onRecorderVideo();

               this.finish();
//               recordTime=0;
//               mTimecount.setBase(SystemClock.elapsedRealtime());
               break;

           case R.id.ok_save:
               Intent intent = new Intent();//数据是使用Intent返回

               intent.putExtra("result", mVideoPath);//把返回数据存入Intent

               this.setResult(-1,intent);//设置返回数据

               this.finish();//关闭Activity
//               recordTime=0;
//               mTimecount.setBase(SystemClock.elapsedRealtime());
               break;
       }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // initialize video camera
            if (ispreview) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();
                isRecording = true;
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                MediaRecorderActivity.this.finish();
            }
            // inform the user that recording has started
            setCaptureButtonText("停止");

        }
    }

    public static Bitmap getVideoThumb2(String path) {
        return getVideoThumb2(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
    }

    public static Bitmap getVideoThumb2(String path, int kind) {
        return ThumbnailUtils.createVideoThumbnail(path, kind);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

}
