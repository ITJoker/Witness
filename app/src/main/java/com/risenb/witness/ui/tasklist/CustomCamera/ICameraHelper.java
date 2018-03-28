package com.risenb.witness.ui.tasklist.CustomCamera;

import android.hardware.Camera;

/**
 * CameraHelper的统一接口
 */
public interface ICameraHelper {
    int getNumberOfCameras();

    Camera openCameraFacing(int facing) throws Exception;

    boolean hasCamera(int facing);

    void getCameraInfo(int cameraId, Camera.CameraInfo cameraInfo);
}
