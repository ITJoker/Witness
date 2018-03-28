package com.risenb.witness.ui.tasklist.CustomCamera;

import android.content.pm.PackageManager;
import android.hardware.Camera;

import com.risenb.witness.MyApplication;

/**
 * API 9 以下使用  只支持一个摄像头
 */
public class CameraHelperBaseImpl implements ICameraHelper {
    @Override
    public int getNumberOfCameras() {
        return hasCameraSupport() ? 1 : 0;
    }

    @Override
    public Camera openCameraFacing(int facing) {
        if (facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            return Camera.open();
        }
        return null;
    }

    @Override
    public boolean hasCamera(int facing) {
        if (facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            return hasCameraSupport();
        }
        return false;
    }

    @Override
    public void getCameraInfo(int cameraId, Camera.CameraInfo cameraInfo) {
        cameraInfo.facing = Camera.CameraInfo.CAMERA_FACING_BACK;
        cameraInfo.orientation = 90;
    }

    private boolean hasCameraSupport() {
        return MyApplication.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
