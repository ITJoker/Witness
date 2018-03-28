package com.risenb.witness.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.BatchUploadTaskBean;
import com.risenb.witness.beans.CompletedAnalysis;
import com.risenb.witness.beans.ImageItem;
import com.risenb.witness.beans.UploadFileBean;
import com.risenb.witness.beans.UploadImageBean;
import com.risenb.witness.beans.VideoInfo;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.RawResponseHandler;
import com.risenb.witness.ui.tasklist.fragment.ExecRecentlyDistanceFragment;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.newUtils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchTaskUploadService extends IntentService {
    public BatchTaskUploadService() {
        super("BatchTaskUploadService");
    }

    private static Handler mHandler;
    private ArrayList<BatchUploadTaskBean.DataBean> batchUploadTaskList = new ArrayList<>();
    private ArrayList<BatchUploadTaskBean.DataBean> batchImageUploadTaskList = new ArrayList<>();
    //服务器返回图片路径
    private List<String> returnFile = new ArrayList<>();
    //一条任务的图片或视频上传完成标识
    private int allLoadFinish = 0;
    private Uri cropUri = null;
    private List<VideoInfo> videoInfoList = new ArrayList<>();
    private ArrayList<ImageItem> imageInfoList = new ArrayList<>();
    private int batchUploadTaskNumberSign = 0;
    private int breakPointFileNumber = 10;
    private int breakPointFileCurrentNumber = 0;

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<BatchUploadTaskBean.DataBean> batchImageUploadTaskList = intent.getParcelableArrayListExtra("batchImageUploadTaskList");
        this.batchImageUploadTaskList = batchImageUploadTaskList;
        batchUploadTask();
    }

    private void batchUploadTask() {
        traverseTask(batchImageUploadTaskList.get(0));
        /**
         * IntentService中调用Handler无作用
         */
        /*if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 6:
                            imageInfoList.clear();
                            returnFile.clear();
                            if (batchUploadTaskNumberSign < batchImageUploadTaskList.size()) {
                                Toast.makeText(getApplication(), "已上传" + batchUploadTaskNumberSign + "条任务", Toast.LENGTH_SHORT).show();
                                traverseTask(batchImageUploadTaskList.get(batchUploadTaskNumberSign));
                            }
                            break;
                    }
                }
            };
        }*/
    }

    /**
     * 遍历存放文件的taskID目录
     */
    public void showFiles(String taskIDPath, String isType, String taskID) {
        int fileNumber = 0;
        File dir = new File(taskIDPath);
        if (dir.exists() && isType != null) {
                /*
                 * 路径正确,遍历该文件夹下的所有文件,此处存在BUG,如果文件夹中只有1张图片会反复遍历并显示
                 */
            File[] subFiles = dir.listFiles();
            for (File subFile : subFiles) {
                if (isType.equals("1")) {
                    // 需要展示照片文件
                    if (subFile.isFile()) {
                        if (fileNumber > 29) {
                            Utils.getUtils().dismissDialog();
                            return;
                        }
                        ImageItem takePhoto = new ImageItem();
                        cropUri = Uri.fromFile(subFile);
                        takePhoto.setImagePath(cropUri.getPath());
                        takePhoto.setSelected(true);
                        imageInfoList.add(takePhoto);
                        fileNumber++;
                    }
                } else if (isType.equals("0")) {
                    // 需要展示视频文件
                    if (subFile.isFile()) {
                        if (fileNumber > 3) {
                            return;
                        }
                        VideoInfo videoInfo = new VideoInfo();
                        videoInfo.setTaskid(taskID);
                        cropUri = Uri.fromFile(subFile);
                        videoInfo.setPath(cropUri.getPath());
                        videoInfoList.add(videoInfo);
                        fileNumber++;
                    }
                }
            }
        } else if (dir.isFile()) {
            return;
        } else {
            return;
        }
    }

    private void traverseTask(final BatchUploadTaskBean.DataBean dataBean) {
        if (dataBean.getIs_type().equals("1")) {
            showFiles(SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + dataBean.getTaskId(), dataBean.getIs_type(), dataBean.getTaskId());
            // 照片上传
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.pictureUpload));
            Map<String, String> params = new HashMap<>();
            params.put("taskid", dataBean.getTaskId());
            params.put("sort", "5");
            if (imageInfoList.size() == 0) {
                /**
                 * Toast要求运行在UI主线程中
                 * Service运行在主线程中，因此Toast是正常的
                 * IntentService运行在独立的线程中，因此Toast不正常
                 */
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplication(), "任务ID：".concat(dataBean.getTaskId()).concat("照片为空"), Toast.LENGTH_SHORT).show();
                    }
                });
                batchUploadTaskNumberSign++;
                if (batchUploadTaskNumberSign < batchImageUploadTaskList.size()) {
                    traverseTask(batchImageUploadTaskList.get(batchUploadTaskNumberSign));
                } else {
                    // 控制我的任务页在批量上传后关闭动画
                    MyApplication.isTaskListScheduleFinish = false;
                    stopSelf();
                }
            } else if (imageInfoList.size() > 0) {
                for (ImageItem imageItem : imageInfoList) {
                    Map<String, File> files = new HashMap<>();
                    File file = new File(imageItem.getImagePath());
                    files.put("file", file);
                    uploadImage(url, params, files, dataBean);
                }
            }
        } else if (dataBean.getIs_type().equals("0")) {
            showFiles(SDCardUtils.getSDCardPath() + MyApplication.videosSaveUrl + dataBean.getTaskId(), dataBean.getIs_type(), dataBean.getTaskId());
            // 视频上传
            String videoUrl = getResources().getString(R.string.service_host_address).concat(getString(R.string.videoUpload));
            Map<String, String> params = new HashMap<>();
            params.put("taskid", dataBean.getTaskId());
            params.put("sort", "5");
            for (VideoInfo videoInfo : videoInfoList) {
                fakeVideoUpload(params, videoInfo, dataBean);
            }
        }
    }

    /**
     * 照片上传
     */
    public synchronized void uploadImage(String url, Map<String, String> params, final Map<String, File> files, final BatchUploadTaskBean.DataBean dataBean) {
        // Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().upload(this, url, params, files, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                Gson gson = new Gson();
                UploadImageBean info = gson.fromJson(response, UploadImageBean.class);
                String uploadImagePath = info.getData().getFile_url();
                returnFile.add(uploadImagePath);
                ++allLoadFinish;
                if (allLoadFinish == imageInfoList.size()) {
                    // Utils.getUtils().dismissDialog();
                    uploadTaskParameters(dataBean);
                    allLoadFinish = 0;
                    batchUploadTaskNumberSign++;
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                // Utils.getUtils().dismissDialog();
                MyApplication.isTaskListScheduleFinish = false;
                Toast.makeText(getApplication(), "网络连接不稳定", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 视频上传
     */
    public synchronized void UploadVideo(String videoUrl, Map<String, String> params, final Map<String, File> files, final BatchUploadTaskBean.DataBean dataBean) {
        // Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().upload(this, videoUrl, params, files, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                // Utils.getUtils().dismissDialog();
                Gson gson = new Gson();
                UploadImageBean info = gson.fromJson(response, UploadImageBean.class);
                String uploadVideoPath = info.getData().getFile_url();
                String sort = info.getData().getSort();
                returnFile.add(uploadVideoPath);
                ++allLoadFinish;
                if (allLoadFinish == videoInfoList.size()) {
                    breakPointFileCurrentNumber++;
                    files.get("filename").delete();
                    if (breakPointFileCurrentNumber == breakPointFileNumber) {
                        uploadTaskParameters(dataBean);
                        allLoadFinish = 0;
                        breakPointFileCurrentNumber = 0;
                        batchUploadTaskNumberSign++;
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                // Utils.getUtils().dismissDialog();
                MyApplication.isTaskListScheduleFinish = false;
                Toast.makeText(getApplication(), "网络连接不稳定", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取获取断点上传文件路径
     */
    private void fakeVideoUpload(final Map<String, String> params, final VideoInfo videoInfo, final BatchUploadTaskBean.DataBean dataBean) {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.fakeVideoUpload));
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<UploadImageBean>() {

            @Override
            public void onSuccess(int statusCode, UploadImageBean response) {
                if (response != null) {
                    Map<String, String> params = new HashMap<>();
                    params.put("filepath", videoInfo.getPath());
                    params.put("savepath", response.getData().getFile_url());
                    params.put("taskid", videoInfo.getTaskid());
                    params.put("sort", response.getData().getSort());
                    Map<String, File> files = new HashMap<>();
                    try {
                        File file = new File(videoInfo.getPath());
                        FileInputStream fis = new FileInputStream(file);

                        int fileTotalSize = (int) file.length();
                        int blockSize = fileTotalSize / breakPointFileNumber;
                        byte[] arr = new byte[blockSize];
                        if (fileTotalSize % breakPointFileNumber != 0) {
                            breakPointFileNumber++;
                        }
                        for (int i = 0; i < breakPointFileNumber; i++) {
                            int startIndex = i * blockSize;
                            /*int endIndex = (i + 1) * blockSize - 1;
                            if (i == breakPointFileNumber - 1) {
                                endIndex = fileTotalSize - 1;
                            }*/
                            File uploadFile = new File(SDCardUtils.getSDCardPath() + MyApplication.videosSaveUrl + videoInfo.getTaskid() + "/" + i + ".mkv");
                            if (!uploadFile.exists()) {
                                uploadFile.createNewFile();
                            }
                            FileOutputStream fos = new FileOutputStream(uploadFile);
                            int readBytes;
                            if ((readBytes = fis.read(arr)) != -1) {
                                fos.write(arr, 0, readBytes);
                                fos.flush();
                                fos.close();
                                files.put("filename", uploadFile);
                                breakPointUpload(params, files, dataBean);
                            }
                        }
                        fis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    /**
     * 分段上传
     */
    private void breakPointUpload(Map<String, String> params, Map<String, File> files, BatchUploadTaskBean.DataBean dataBean) {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.videoUpload));
        UploadVideo(url, params, files, dataBean);
    }

    /**
     * 上传参数
     */
    public synchronized void uploadTaskParameters(BatchUploadTaskBean.DataBean dataBean) {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskfilepath));
        List<UploadFileBean> uploadFileBeanList = new ArrayList<>();
        Map<String, String> taskParameters = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        taskParameters.put("taskid", dataBean.getTaskId());
        taskParameters.put("c", MyApplication.getInstance().getC());
        String photoTime = SharedPreferencesUtil.getString(getApplication(), dataBean.getTaskId().concat("PhotoTime"), "NoTime");
        taskParameters.put("phototime", photoTime);
        for (int i = 0; i < returnFile.size(); i++) {
            String path = returnFile.get(i);
            if (i != returnFile.size() - 1) {
                stringBuilder.append(path + ",");
            } else {
                stringBuilder.append(path);
            }
        }
        UploadFileBean uploadFileBean = new UploadFileBean();
        uploadFileBean.setReturnfile(stringBuilder.toString());
        if (dataBean.getIs_type().equals("1")) {
            // 说明文件类型为照片
            uploadFileBean.setType("1");
        } else if (dataBean.getIs_type().equals("0")) {
            // 说明文件类型为视频
            uploadFileBean.setType("0");
        }
        uploadFileBean.setLatitude(dataBean.getLatitude());
        uploadFileBean.setLongtitude(dataBean.getLongitude());
        uploadFileBean.setSort("5");
        uploadFileBeanList.add(uploadFileBean);
        BaseBeans<List<UploadFileBean>> baseBeans = new BaseBeans<>();
        baseBeans.setData(uploadFileBeanList);
        String jsonString = JSON.toJSONString(baseBeans);
        taskParameters.put("taskJson", jsonString);
        SaveFilePathTaskAccessNetWork(url, taskParameters, dataBean.getTaskId());
    }

    private void SaveFilePathTaskAccessNetWork(String url, Map<String, String> params, final String taskID) {
        // Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                // Utils.getUtils().dismissDialog();
                MyApplication.isTaskListScheduleFinish = false;
                Toast.makeText(getApplication(), "请检查网络状态", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                // Utils.getUtils().dismissDialog();
                String success = response.getSuccess();
                if ("1".equals(success)) {
                    String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.uploadTask));
                    Map<String, String> params = new HashMap<>();
                    params.put("taskid", taskID);
                    params.put("c", MyApplication.getInstance().getC());
                    changeStateToUploaded(url, params, taskID);
                }
            }
        });
    }

    /**
     * 改变任务状态为已上传
     */
    private void changeStateToUploaded(String url, Map<String, String> params, final String taskID) {
        // Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<CompletedAnalysis>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                MyApplication.isTaskListScheduleFinish = false;
                Toast.makeText(getApplication(), "网络错误", Toast.LENGTH_SHORT).show();
                // Utils.getUtils().dismissDialog();
            }

            @Override
            public void onSuccess(int statusCode, CompletedAnalysis response) {
                int success = response.getSuccess();
                if (success == 1) {
                    Intent intent = new Intent("BatchTaskUploadToComplete");
                    sendBroadcast(intent);
                    // Utils.getUtils().dismissDialog();
                    if (batchImageUploadTaskList.size() == batchUploadTaskNumberSign) {
                        // 说明批量上传已经完成
                        ExecRecentlyDistanceFragment.isNetRefresh = true;
                        // 控制我的任务页在批量上传后关闭动画
                        MyApplication.isTaskListScheduleFinish = false;
                        Toast.makeText(getApplication(), "批量上传成功", Toast.LENGTH_SHORT).show();
                        stopSelf();
                    } else if (batchUploadTaskNumberSign < batchImageUploadTaskList.size()) {
                        /*Message imageUploadMessage = Message.obtain();
                        imageUploadMessage.what = 6;
                        mHandler.sendMessage(imageUploadMessage);*/
                        imageInfoList.clear();
                        returnFile.clear();
                        Toast.makeText(getApplication(), "已上传" + batchUploadTaskNumberSign + "条任务", Toast.LENGTH_SHORT).show();
                        traverseTask(batchImageUploadTaskList.get(batchUploadTaskNumberSign));
                    }
                    /*
                     * 删除本地文件
                     */
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MyApplication.deletePicture(SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + taskID);
                            MyApplication.deletePicture(SDCardUtils.getSDCardPath() + MyApplication.photosSaveUrl + taskID);
                        }
                    }).start();
                }
            }
        });
    }
}
