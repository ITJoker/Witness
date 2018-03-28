package com.risenb.witness.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.adapter.SettledTaskExecutionShowAdapter;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.ImageItem;
import com.risenb.witness.beans.ResultBean;
import com.risenb.witness.beans.SettledTaskDetailsBean;
import com.risenb.witness.beans.SettledTaskPictureReturnParams;
import com.risenb.witness.beans.UploadFileBean;
import com.risenb.witness.beans.VideoInfo;
import com.risenb.witness.beans.VideoOrPhotoItemSign;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.FileUtils;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.newViews.MyGridView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 模板3，纯拍照任务
 */
public class SettledTaskExecutionUI extends BaseUI {
    public static final int SYSTEM_TAKE_PHOTO_CAMERA = 10106;
    public static final int SYSTEM_IMAGE_CROP_CAMERA = 10107;
    public static final int SYSTEM_PLAY_VIDEO_CAMERA = 10108;
    //地址
    @BindView(R.id.uncertain_evi_adrress)
    TextView mUncertainEviAddress;
    //价格
    @BindView(R.id.uncertain_price)
    TextView mUncertainPrice;
    //例图
    @BindView(R.id.uncertain_example_image)
    ImageView mUncertainExampleImage;
    //拍摄要求
    @BindView(R.id.execute_request_tv)
    TextView mExecuteRequestText;
    //添加图片
    @BindView(R.id.uncertain_add_image)
    MyGridView mUncertainAddImage;
    //下一步
    @BindView(R.id.uncertain_evi_next)
    Button mUncertainEviNext;
    //倒计时Layout
    @BindView(R.id.uncertain_countdown_layout)
    LinearLayout mUncertainCountDownLayout;
    //任务指南
    @BindView(R.id.ll_task_guide)
    LinearLayout taskGuideLinearLayout;
    //任务指南图片提示
    @BindView(R.id.operation_guide)
    RelativeLayout mOperationGuide;
    //备注
    @BindView(R.id.settled_task_remark_et)
    EditText mSettledTaskRemark;
    //拍照需隐藏
    @BindView(R.id.uncertain_evi_camera)
    Button mTakePhotos;

    private Uri cropUri = null;
    private SettledTaskExecutionShowAdapter adapter;
    private String fixedID;
    private String isDemo;
    //选择的图片的临时列表
    private ArrayList<VideoOrPhotoItemSign> tempSelectBitmap = new ArrayList<>();
    //服务器返回的图片路径
    public List<String> returnFile = new ArrayList<>();
    static int allLoadFinish = 0;
    static int videoUploadedNumber = 0;

    static Handler handler;

    @Override
    protected void onCreate() {
        super.onCreate();
        setContentView(R.layout.uncertainevidence);
        ButterKnife.bind(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what <= tempSelectBitmap.size()) {
                    makeText("正在上传" + msg.what + "/" + tempSelectBitmap.size());
                }
            }
        };

        mUncertainEviNext.setText("上传任务");
        mSettledTaskRemark.setVisibility(View.VISIBLE);
        mTakePhotos.setVisibility(View.GONE);
        mUncertainCountDownLayout.setVisibility(View.GONE);

        boolean operationGuide = SharedPreferencesUtil.getBoolean(getApplication(), "OperationGuide", false);
        if (!operationGuide) {
            mOperationGuide.setVisibility(View.VISIBLE);
            SharedPreferencesUtil.saveBoolean(getApplication(), "OperationGuide", true);
        }
        mOperationGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOperationGuide.setVisibility(View.GONE);
            }
        });

        Bundle bundle = getIntent().getExtras();
        fixedID = bundle.getString("fixedID");
        isDemo = bundle.getString("isDemo");

        taskGuideLinearLayout.setVisibility(View.VISIBLE);
        taskGuideLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettledTaskExecutionUI.this, TaskInfoTrainUI.class);
                intent.putExtra("fixedID", fixedID);
                startActivity(intent);
            }
        });
        onNonTaskAccessNetWorkDetail();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        /*
         * 判断当前照片或视频是否为第1个,若是定位并保存,否则不采取操作
         */
        if (tempSelectBitmap.size() == 1) {
            //说明为第1张照片,进行定位并保存
            taskLocation();
        }
    }

    private void taskLocation() {
        //LocationClient定位器必须在主线程中声明
        LocationClient locationClient = new LocationClient(getApplicationContext());
        BDLocationListener bdLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //保存纬度信息
                SharedPreferencesUtil.saveString(getApplication(), fixedID + "SettledTask" + "Latitude", String.valueOf(bdLocation.getLatitude()));
                //保存经度信息
                SharedPreferencesUtil.saveString(getApplication(), fixedID + "SettledTask" + "Longitude", String.valueOf(bdLocation.getLongitude()));
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    /*
                     * GPS定位结果
                     */
                    //保存地址信息
                    SharedPreferencesUtil.saveString(getApplication(), fixedID + "SettledTask" + "Address", String.valueOf(bdLocation.getAddrStr()));
                }
            }
        };
        //注册监听函数
        locationClient.registerLocationListener(bdLocationListener);
        //设置百度地图SDK的定位方式
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        int span = 1000;
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        //为定位器设置定位参数
        locationClient.setLocOption(option);
        //开始定位
        locationClient.start();
    }

    public void onNonTaskAccessNetWorkDetail() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.fixedTaskDetails));
        Map<String, String> params = new HashMap<>();
        params.put("fixedid", fixedID);
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(getApplication(), url, params, new GsonResponseHandler<SettledTaskDetailsBean>() {
            @Override
            public void onSuccess(int statusCode, SettledTaskDetailsBean response) {
                SettledTaskDetailsBean.DataBean data = response.getData();
                Utils.getUtils().dismissDialog();
                if (null != data) {
                    // 说明需求为照片拍摄
                    adapter = new SettledTaskExecutionShowAdapter(getActivity(), tempSelectBitmap, fixedID);
                    adapter.setPhotoNumbers(10);
                    mUncertainAddImage.setAdapter(adapter);
                    mUncertainEviAddress.setText(data.getFixedname());
                    mUncertainPrice.setText("￥".concat(data.getPrice()));
                    mExecuteRequestText.setText("");
                    ImageLoader.getInstance().displayImage(getResources().getString(R.string.service_host_address).concat(data.getExample()), mUncertainExampleImage, MyConfig.options);
                    mSettledTaskRemark.setHint("备注：" + data.getRemark());
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    @Override
    protected void back() {
        if (tempSelectBitmap.size() > 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.ImageloadingDialogStyle);
            dialog.setTitle("人人提示");
            dialog.setMessage("此任务为即时上传任务，如果退出当前页面，数据不会保存，确定退出?");
            dialog.setIcon(R.drawable.ic_launcher);
            dialog.setCancelable(false);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
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
            finish();
        }
    }

    @Override
    protected void setControlBasis() {
        setTitle("随手拍");
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }

    @OnClick(R.id.uncertain_evi_next)
    public void UploadTask() {
        // 说明拍摄要求为图片
        if (tempSelectBitmap.size() == 0) {
            makeText("请按照任务说明拍摄照片");
            return;
        }
        if (!TextUtils.isEmpty(isDemo) && "true".equals(isDemo) && TextUtils.isEmpty(MyApplication.getInstance().getC())) {
            finish();
            makeText("上传成功");
            return;
        }
        allLoadFinish = 0;
        videoUploadedNumber = 0;
        Map<String, String> params = new HashMap<>();
        params.put("fixedid", fixedID);
        Map<String, File> files = new HashMap<>();
        for (int i = 0; i < tempSelectBitmap.size(); i++) {
            // 只有经过压缩的照片才可以被上传
            if ("photo".equals(tempSelectBitmap.get(i).sign)) {
                String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.pictureFixed));
                ImageItem imageItem = (ImageItem) tempSelectBitmap.get(i);
                if (imageItem.isSelected()) {
                    try {
                        File file = new File(imageItem.getImagePath());
                        files.put("file", file);
                        UploadImage(url, params, files);
                    } catch (Exception e) {
                        e.printStackTrace();
                        makeText("上传文件不存在");
                    }
                }
            } else if ("video".equals(tempSelectBitmap.get(i).sign)) {
                /*RequestParams requestParams = new RequestParams("http://www.adexmall.net/Task/Uploadvideo/");*/
                String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.videoFixed));
                // 加到url里的参数, http://xxxx/s?wd=xUtils.
                RequestParams requestParams = new RequestParams(url);
                VideoInfo videoInfo = (VideoInfo) tempSelectBitmap.get(i);
                // 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
                // requestParams.addQueryStringParameter("fixedid", fixedID);
                requestParams.addParameter("fixedid", fixedID);
                // 使用multipart表单上传文件.
                requestParams.setMultipart(true);
                // 如果文件没有扩展名, 最好设置contentType参数.
                try {
                    File file = new File(videoInfo.getPath());
                    requestParams.addBodyParameter("file", file, "video/mkv", file.getName());
                    /*try {
                        // 测试中文文件名
                        // InputStream参数获取不到文件名, 最好设置, 除非服务端不关心这个参数.
                        requestParams.addBodyParameter("file2", new FileInputStream(new File(videoInfo.getPath())), "video/mkv", "Thor Ragnarok Teaser Trailer.mkv");
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }*/
                    UploadVideo(requestParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    makeText("上传文件不存在");
                }
            }
        }
    }

    public synchronized void UploadVideo(final RequestParams requestParams) {
        Utils.getUtils().showProgressDialog(this, "上传中...");
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SettledTaskPictureReturnParams returnParams = new Gson().fromJson(result, SettledTaskPictureReturnParams.class);
                if (returnParams != null && returnParams.getSuccess() == 1) {
                    returnFile.add(returnParams.getData().getFile_url());
                    ++allLoadFinish;

                    Message message = Message.obtain();
                    message.what = allLoadFinish;
                    handler.sendMessage(message);

                    if (allLoadFinish == tempSelectBitmap.size()) {
                        // 说明视频/任务已经全部上传
                        UploadHttpUrlAndSort();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                makeText("请检查网络状况");
                // Utils.getUtils().dismissDialog();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                makeText("上传中断");
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onFinished() {
                videoUploadedNumber++;
                makeText("已上传" + videoUploadedNumber + "个视频");
            }
        });
    }

    public synchronized void UploadImage(String url, Map<String, String> params, final Map<String, File> files) {
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().upload(this, url, params, files, new GsonResponseHandler<SettledTaskPictureReturnParams>() {
            @Override
            public void onSuccess(int statusCode, SettledTaskPictureReturnParams response) {
                returnFile.add(response.getData().getFile_url());
                ++allLoadFinish;

                Message message = Message.obtain();
                message.what = allLoadFinish;
                handler.sendMessage(message);

                if (allLoadFinish == tempSelectBitmap.size()) {
                    // 说明图片/任务已经全部上传
                    UploadHttpUrlAndSort();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                makeText("网络中断，请重新上传");
                Utils.getUtils().dismissDialog();
            }
        });
    }

    public void UploadHttpUrlAndSort() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.saveFixedTask));
        List<UploadFileBean> uploadFileBeanList = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        params.put("fixedid", fixedID);
        params.put("c", MyApplication.getInstance().getC());
        params.put("remark", mSettledTaskRemark.getText().toString().trim());
        for (int i = 0; i < returnFile.size(); i++) {
            String path = returnFile.get(i);
            if (i != returnFile.size() - 1) {
                path = path + ",";
                stringBuilder.append(path);
            } else {
                stringBuilder.append(path);
            }
        }
        UploadFileBean uploadFileBean = new UploadFileBean();
        uploadFileBean.setReturnfile(stringBuilder.toString());

        String latitude = SharedPreferencesUtil.getString(getApplication(), fixedID + "SettledTask" + "Latitude", "");
        String longitude = SharedPreferencesUtil.getString(getApplication(), fixedID + "SettledTask" + "Longitude", "");

        uploadFileBean.setLatitude(latitude);
        uploadFileBean.setLongtitude(longitude);

        uploadFileBeanList.add(uploadFileBean);
        BaseBeans<List<UploadFileBean>> baseBeans = new BaseBeans<>();
        baseBeans.setData(uploadFileBeanList);
        String json = JSON.toJSONString(baseBeans);
        params.put("taskJson", json);
        SaveFilePathTaskAccessNetWork(url, params);
    }

    private void SaveFilePathTaskAccessNetWork(String url, Map<String, String> params) {
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<ResultBean>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                makeText("服务器忙");
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onSuccess(int statusCode, ResultBean response) {
                if (response.getSuccess() == 1) {
                    Utils.getUtils().dismissDialog();
                    makeText(response.getErrorMsg());
                    SettledTaskFragment.settledTaskSign = true;
                    allLoadFinish = 0;
                    finish();
                }
            }
        });
    }

    public File compressBitmap(String filePath) {
        try {
            // 数值越高，图片像素越低
            int inSampleSize = 4;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outHeight = 1280;
            options.outWidth = 1040;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //采样率
            options.inSampleSize = inSampleSize;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            String mSDCardPath = SDCardUtils.getSDCardPath();
            String compressPath = mSDCardPath + MyApplication.settledTaskPhotosCompressSaveUrl + fixedID + "/";
            FileUtils.createOrExistsDir(compressPath);
            String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
            File compressFile = new File(compressPath + fileName);
            FileOutputStream fos = new FileOutputStream(compressFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return compressFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void takePhotoResult() {
        File corpFile = new File(adapter.photoFilePath + adapter.photoFileName);
        File path = compressBitmap(corpFile.getPath());
        if (path == null) {
            makeText("存储路径无效");
            return;
        }

        cropUri = Uri.fromFile(path);

        onCropImageResult();
    }

    public void onCropImageResult() {
        if (cropUri != null) {
            ImageItem takePhoto = new ImageItem();
            takePhoto.setImagePath(cropUri.getPath());
            /*
             * 置为true说明照片已经被压缩过了或是未被上传过
             */
            takePhoto.setSelected(true);
            takePhoto.sign = "photo";
            tempSelectBitmap.add(takePhoto);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SYSTEM_TAKE_PHOTO_CAMERA:
                    takePhotoResult();
                    break;
                case SYSTEM_IMAGE_CROP_CAMERA:
                    onCropImageResult();
                    break;
                case SYSTEM_PLAY_VIDEO_CAMERA:
                    String videoFilePath = data.getStringExtra("videoFilePath");
                    if (videoFilePath != null) {
                        playVideoResult(videoFilePath);
                    }
                    break;
            }
        }
    }

    private void playVideoResult(String videoFilePath) {
        File file = new File(videoFilePath);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getPath());
        String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int videoDuration = Integer.valueOf(duration) / 1000;

        VideoInfo videoInfo = new VideoInfo();
        cropUri = Uri.fromFile(file);
        videoInfo.setPath(cropUri.getPath());
        videoInfo.setTaskid(fixedID);
        videoInfo.setTitle(file.getName());
        videoInfo.setDuration(videoDuration);
        videoInfo.sign = "video";
        tempSelectBitmap.add(videoInfo);
    }

    @Override
    public void finish() {
        super.finish();
        tempSelectBitmap.clear();
        MyApplication.deletePicture(SDCardUtils.getSDCardPath() + MyApplication.settledTaskPhotosCompressSaveUrl + fixedID);
        MyApplication.deletePicture(SDCardUtils.getSDCardPath() + MyApplication.settledTaskPhotosSaveUrl + fixedID);
    }
}
