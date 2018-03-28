package com.risenb.witness.ui.tasklist.CustomCamera;

/**
 * 相机操作的接口
 */
public interface ICameraOperation {
    /**
     * 切换前置和后置相机
     */
    void switchCamera();

    /**
     * 切换闪光灯模式
     */
    void switchFlashMode();

    /**
     * 拍照
     */
    boolean takePicture();

    /**
     * 相机最大缩放级别
     */
    int getMaxZoom();

    /**
     * 设置当前缩放级别
     */
    void setZoom(int zoom);

    /**
     * 获取当前缩放级别
     */
    int getZoom();

    /**
     * 释放相机
     */
    void releaseCamera();
}
