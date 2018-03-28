package com.risenb.witness.ui.tasklist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.adapter.GridAdapter;
import com.risenb.witness.adapter.GridVideoAdapter;
import com.risenb.witness.beans.ImageItem;
import com.risenb.witness.beans.VideoInfo;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.CustomCamera.CameraActivity;
import com.risenb.witness.ui.tasklist.CustomCamera.TakePhotoActivity;
import com.risenb.witness.ui.tasklist.PlayVideo.PlayVideoActivity;
import com.risenb.witness.ui.home.TaskInfoTrainUI;
import com.risenb.witness.utils.FileUtils;
import com.risenb.witness.utils.ImageAddWatermarkUtil;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.newViews.MyGridView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UncertainExecEvidence extends BaseUI {
    final int SYSTEM_TAKE_PHOTH_CAMERA = 10106;
    final int SYSTEM_IMAGE_CROP_CAMERA = 10107;
    final int SYSTEM_PLAY_VIDEO_CAMERA = 10108;
    final int REQUEST_CODE_ASK_CAMERA_PERMISSIONS = 10109;
    //地址
    @BindView(R.id.uncertain_evi_adrress)
    TextView mUncertainEviAdrress;
    @BindView(R.id.price_and_countdown_ll)
    LinearLayout price_and_countdown_ll;
    //价格
    @BindView(R.id.uncertain_price_iv)
    ImageView uncertain_price_iv;
    @BindView(R.id.uncertain_price)
    TextView mUncertainPrice;
    //例图
    @BindView(R.id.uncertain_example_image_tv)
    TextView uncertain_example_image_tv;
    @BindView(R.id.uncertain_example_image)
    ImageView mUncertainExampleImage;
    //拍照
    @BindView(R.id.uncertain_evi_camera)
    Button mUncertainEviCamera;
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
    //备注
    @BindView(R.id.text_quzheng)
    TextView text_quzheng;

    private String mImageFileName;
    private String mImageFilePath;
    private Uri cropUri;

    private GridVideoAdapter videoAdapter;
    private GridAdapter adapter;
    private String taskid;
    private String marks;
    private String modeltype;
    private int page;
    //选择的图片的临时列表
    public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<>();
    //存放视频信息List
    public ArrayList<VideoInfo> videoInfoList;
    public static int max = 0;
    public static int num = 30;
    //服务器返回的图片路径
    public List<String> returnfile = new ArrayList<>();

    //默认类型为拍照
    private String isType = "1";
    public static String sort = "5";
    private String netTime;
    /*private TaskListBean.TaskList taskInfo;*/

    private String waterMarkID = "0";
    private String waterMarkNT = "0";
    private String waterMarkXY = "0";
    private String waterMarkST = "0";
    private String address;
    private String price;

    @Override
    protected void onCreate() {
        try {
            super.onCreate();
            setContentView(R.layout.uncertainevidence);
            ButterKnife.bind(this);

            Bundle bundle = getIntent().getExtras();
            taskid = bundle.getString("taskid");
            address = bundle.getString("address");
            String note = bundle.getString("note");
            if (!TextUtils.isEmpty(note)) {
                text_quzheng.setText("备注：".concat(note));
            } else {
                text_quzheng.setText("备注：无");
            }
            price = bundle.getString("price");
            if (!TextUtils.isEmpty(bundle.getString("isType")))
                isType = bundle.getString("isType");
            modeltype = bundle.getString("modeltype");
            marks = bundle.getString("mark");
            /*taskInfo = bundle.getParcelable("taskInfo");*/
            //当前第几页
            page = bundle.getInt("currentpage");
            if (!TextUtils.isEmpty(bundle.getString("waterMarkID"))) {
                waterMarkID = bundle.getString("waterMarkID");
            }
            if (!TextUtils.isEmpty(bundle.getString("waterMarkNT"))) {
                waterMarkNT = bundle.getString("waterMarkNT");
            }
            if (!TextUtils.isEmpty(bundle.getString("waterMarkXY"))) {
                waterMarkXY = bundle.getString("waterMarkXY");
            }
            if (!TextUtils.isEmpty(bundle.getString("waterMarkST"))) {
                waterMarkST = bundle.getString("waterMarkST");
            }

            if (videoInfoList == null) {
                videoInfoList = new ArrayList<>();
            } else if (tempSelectBitmap == null) {
                tempSelectBitmap = new ArrayList<>();
            }

            /*uncertain_example_image_tv.setVisibility(View.GONE);
            mUncertainExampleImage.setVisibility(View.GONE);*/

            mUncertainCountDownLayout.setVisibility(View.GONE);

            taskGuideLinearLayout.setVisibility(View.VISIBLE);
            taskGuideLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UncertainExecEvidence.this, TaskInfoTrainUI.class);
                    intent.putExtra("id", taskid);
                    startActivity(intent);
                }
            });

            onNonTaskAccessNetWorkDetail();
        } catch (OutOfMemoryError e) {
            mUncertainEviNext.setEnabled(false);
            mUncertainEviCamera.setEnabled(false);
            makeText("空间不足，请重新启动应用");
            System.gc();
        }
    }

    protected void onRestart() {
        super.onRestart();
        if (isType.equals("1") && adapter != null) {
            adapter.notifyDataSetChanged();
        } else if (isType.equals("0") && videoAdapter != null) {
            if (videoInfoList != null) {
                videoInfoList.clear();
                showFiles(SDCardUtils.getSDCardPath() + MyApplication.videosSaveUrl + taskid);
                videoAdapter = new GridVideoAdapter(videoInfoList, taskid, marks);
                mUncertainAddImage.setAdapter(videoAdapter);
                // videoAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onNonTaskAccessNetWorkDetail() {
        mUncertainEviAdrress.setText(address);

        if ("3".equals(getIntent().getStringExtra("execution"))) {
            //执行方式(1.专业人员、2.客户检测员、3.奖池)
            price_and_countdown_ll.setVisibility(View.VISIBLE);
            mUncertainPrice.setText("￥".concat(price));
        } else {
            price_and_countdown_ll.setVisibility(View.GONE);
        }

        if (isType.equals("0")) {
            // 说明需求为视频拍摄
            mUncertainEviCamera.setBackgroundResource(R.drawable.task_takerecordvideo);
            showFiles(SDCardUtils.getSDCardPath() + MyApplication.videosSaveUrl + taskid);
            videoAdapter = new GridVideoAdapter(videoInfoList, taskid, marks);
            mUncertainAddImage.setAdapter(videoAdapter);
        } else if (isType.equals("1")) {
            // 说明需求为照片拍摄
            showFiles(SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + taskid);
            adapter = new GridAdapter(getApplication(), tempSelectBitmap);
            mUncertainAddImage.setAdapter(adapter);
            mUncertainAddImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != tempSelectBitmap && tempSelectBitmap.size() >= 0) {
                        if (tempSelectBitmap.size() != position) {
                            Intent intent = new Intent(getApplication(), ExecGalleryActivity.class);
                            intent.putExtra("position", "1");
                            intent.putExtra("ID", position);
                            intent.putExtra("mark", "UncertainExecEvidence");
                            startActivity(intent);
                        } else {
                            TaskObtainEvidenceTakePhoto();
                        }
                    }
                }
            });
        }

        // daojishi(String.valueOf(info.getRemainTime() * 1000));
        /*ImageLoader.getInstance().displayImage(info.getTaskList().get(0).getExampleFile().get(0), mUncertainExampleImage, MyConfig.options);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoInfoList.clear();
    }

    @Override
    protected void back() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        tempSelectBitmap.clear();
    }

    @Override
    protected void setControlBasis() {

    }

    @Override
    protected void prepareData() {
        String taskID = getIntent().getStringExtra("taskid");
        if (SharedPreferencesUtil.getBoolean(getApplication(), taskID + "Reject", false)) {
            setTitle("更正任务");
        } else {
            setTitle("执行中");
        }
    }

    @Override
    public void onLoadOver() {

    }

    @OnClick(R.id.uncertain_evi_next)
    public void UploadTaskImage() {
        if (isType.equals("1")) {
            // 说明拍摄要求为图片
            if (tempSelectBitmap.size() == 0) {
                makeText("请按照任务说明拍摄照片");
                return;
            }
        } else if (isType.equals("0")) {
            // 说明拍摄需求为视频
            if (videoInfoList.size() == 0) {
                makeText("请按照任务说明拍摄视频");
                return;
            }
        }
        UploadHttpUrl();
    }

    public void UploadHttpUrl() {
        Intent intent = new Intent(UncertainExecEvidence.this, UncertainExecTaskIssueState.class);
        Bundle bundle = new Bundle();
        bundle.putString("taskid", taskid);
        bundle.putString("modeltype", modeltype);
        bundle.putInt("page", ++page);
        bundle.putString("isType", isType);
        intent.putExtras(bundle);
        startActivity(intent);
        --page;
        UIManager.getInstance().pushActivity(UncertainExecEvidence.this);
    }

    @OnClick(R.id.uncertain_evi_camera)
    public void OnClickCameraTakePhoto() {
        if (isType.equals("1")) {
            if (tempSelectBitmap.size() < 30) {
                TaskObtainEvidenceTakePhoto();
            } else {
                makeText("照片数量已达上限");
            }
        } else if (isType.equals("0")) {
            if (videoInfoList.size() < 3) {
                shootVideo();
            } else {
                makeText("视频数量已达上限");
            }
        }
    }

    private void taskLocation() {
        //LocationClient定位器必须在主线程中声明
        LocationClient locationClient = new LocationClient(getApplicationContext());
        BDLocationListener bdLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //获取纬度信息
                SharedPreferencesUtil.saveString(getApplication(), taskid + "Latitude", String.valueOf(bdLocation.getLatitude()));
                //获取经度信息
                SharedPreferencesUtil.saveString(getApplication(), taskid + "Longitude", String.valueOf(bdLocation.getLongitude()));
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

    public void TaskObtainEvidenceTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CAMERA_PERMISSIONS);
            return;
        }
        boolean mPathFlag = getImageSavePath();
        if (mPathFlag) {
            mImageFileName = String.valueOf(System.currentTimeMillis()) + ".jpg";

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= 24) {
                File imageDir = new File(Environment.getExternalStorageDirectory(), "ObtainEvidenceImage/");
                if (!imageDir.exists()) {
                    imageDir.mkdirs();
                }
                File imageFilePath = new File(imageDir + "/" + taskid + "/", mImageFileName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), "com.risenb.witness.fileprovider", imageFilePath));
                // 授予目录临时共享权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                File file = new File(mImageFilePath + mImageFileName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
            startActivityForResult(intent, SYSTEM_TAKE_PHOTH_CAMERA);
            makeText("请横屏拍摄");

            /*Intent intent = new Intent(this, TakePhotoActivity.class);
            intent.putExtra("photoSavePath", mImageFilePath + mImageFileName);
            startActivityForResult(intent, SYSTEM_TAKE_PHOTH_CAMERA);*/

            /*Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra("photoSavePath", mImageFilePath + mImageFileName);
            startActivityForResult(intent, SYSTEM_TAKE_PHOTH_CAMERA);*/

            //定位
            taskLocation();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //取得资源对象
                        URL url = new URL("http://www.baidu.com");
                        //生成连接对象
                        URLConnection uc = url.openConnection();
                        //发出连接
                        uc.connect();
                        //取得网站日期时间
                        long ld = uc.getDate();
                        //转换为标准时间对象
                        Date date = new Date(ld);
                        //分别取得时间中的小时，分钟和秒
                        String year = String.valueOf(date.getYear() + 1900);
                        String month = String.valueOf(date.getMonth() + 1);
                        if (date.getMonth() + 1 < 10) {
                            month = "0".concat(month);
                        }
                        String day = String.valueOf(date.getDate());
                        if (date.getDate() < 10) {
                            day = "0".concat(day);
                        }
                        String hours = String.valueOf(date.getHours());
                        if (date.getHours() < 10) {
                            hours = "0".concat(hours);
                        }
                        String minutes = String.valueOf(date.getMinutes());
                        if (date.getMinutes() < 10) {
                            minutes = "0".concat(minutes);
                        }
                        String seconds = String.valueOf(date.getSeconds());
                        if (date.getSeconds() < 10) {
                            seconds = "0".concat(seconds);
                        }
                        netTime = year.concat("-").concat(month).concat("-").concat(day).concat(" ").concat(hours).concat(":").concat(minutes).concat(":").concat(seconds);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void shootVideo() {
        Intent intent = new Intent(this, PlayVideoActivity.class);
        intent.putExtra("taskid", taskid);
        intent.putExtra("marks", marks);
        startActivityForResult(intent, SYSTEM_PLAY_VIDEO_CAMERA);

        //定位
        taskLocation();
    }

    public boolean getImageSavePath() {
        String mSDCardPath = SDCardUtils.getSDCardPath();
        mImageFilePath = mSDCardPath + MyApplication.photosSaveUrl + taskid + "/";
        return FileUtils.createOrExistsDir(mImageFilePath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SYSTEM_TAKE_PHOTH_CAMERA:
                /*//TakePhotoActivity
                if (resultCode == RESULT_OK) {
                    new CompressPhotoTask().execute();
                }*/
                Utils.getUtils().showProgressDialog(getActivity(), "保存中");
                compressPhotoTaskExecute();
                break;
            case SYSTEM_IMAGE_CROP_CAMERA:
                onCropImageResult();
                break;
            case SYSTEM_PLAY_VIDEO_CAMERA:
                break;
        }
    }

    private void compressPhotoTaskExecute() {
        CompressPhotoTask compressPhotoTask = new CompressPhotoTask();
        compressPhotoTask.setCompressPhotoTaskCompletedListener(new CompressPhotoTaskCompletedListener() {
            @Override
            public void onPreExecuteListenerMethod() {
                // 未上传标识，控制任务详情页是否关闭
                SharedPreferencesUtil.saveBoolean(getApplication(), taskid + "Upload", false);
                //获取当前时间
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String photoTime = simpleDateFormat.format(curDate);
                SharedPreferencesUtil.saveString(getApplication(), taskid.concat("PhotoTime"), photoTime);
            }

            @Override
            public void doInBackgroundListenerMethod() {
                takePhotoResult();
            }

            @Override
            public void onPostExecuteListenerMethod() {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                MyApplication.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utils.getUtils().dismissDialog();
                    }
                }, 1800);
            }
        });
        compressPhotoTask.execute();
    }

    public void takePhotoResult() {
        File corpFile = new File(mImageFilePath + mImageFileName);
        File path = compressBitmap(corpFile.getPath());
        if (path == null) {
            makeText("无效路径");
            return;
        }
        cropUri = Uri.fromFile(path);
        onCropImageResult();
    }

    public File compressBitmap(String filePath) throws OutOfMemoryError {
        try {
            // 数值越高，图片像素越低
            int inSampleSize = 4;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outHeight = 720;
            options.outWidth = 1280;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            //采样率
            options.inSampleSize = inSampleSize;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            bitmap = setWaterMarkBitmap(bitmap);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // 把压缩后的数据存放到byteArrayOutputStream中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            String mSDCardPath = SDCardUtils.getSDCardPath();
            String compressPath = mSDCardPath + MyApplication.photosCompressSaveUrl + taskid + "/";
            FileUtils.createOrExistsDir(compressPath);
            String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
            if (!TextUtils.isEmpty(mImageFileName)) {
                fileName = mImageFileName;
            }
            File filepath = new File(compressPath + fileName);
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(byteArrayOutputStream.toByteArray());
            fos.flush();
            fos.close();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return filepath;
        } catch (Exception e) {
            return null;
        }
    }

    private Bitmap setWaterMarkBitmap(Bitmap bitmap) {
        String waterMark = "";

        if ("1".equals(waterMarkNT)) {
            if (!TextUtils.isEmpty(netTime)) {
                waterMark = "NT:".concat(netTime).concat("  ");
            } else {
                waterMark = "NT:".concat("Null").concat("  ");
            }
        }

        if ("1".equals(waterMarkXY)) {
            //获取经度信息
            String longitude = SharedPreferencesUtil.getString(getApplication(), taskid + "Longitude", "Null");
            waterMark = waterMark.concat("Lng:".concat(longitude).concat("  "));
            //获取纬度信息
            String latitude = SharedPreferencesUtil.getString(getApplication(), taskid + "Latitude", "Null");
            waterMark = waterMark.concat("Lat:".concat(latitude).concat("  "));
        }

        if ("1".equals(waterMarkST)) {
            String photoTime = SharedPreferencesUtil.getString(getApplication(), taskid.concat("PhotoTime"), "NoTime");
            waterMark = waterMark.concat("ST:".concat(photoTime));
        }

        bitmap = ImageAddWatermarkUtil.drawTextToLeftBottom(getApplication(), bitmap, waterMark, 8, Color.rgb(232, 16, 16), 2, 2);

        if ("1".equals(waterMarkID)) {
            bitmap = ImageAddWatermarkUtil.drawTextToLeftBottom(getApplication(), bitmap, "ID:".concat(taskid), 8, Color.rgb(232, 16, 16), 2, 10);
        }
        return bitmap;
    }

    public void onCropImageResult() {
        if (cropUri != null) {
            ImageItem takePhoto = new ImageItem();
            /*Bitmap bm = BitmapFactory.decodeFile(cropUri.getPath());
            takePhoto.setBitmap(bm);*/
            takePhoto.setImagePath(cropUri.getPath());
            takePhoto.setSelected(true);
            tempSelectBitmap.add(takePhoto);
        }
    }

    /**
     * 遍历存放文件的taskID目录
     */
    public void showFiles(String taskIDPath) {
        int fileNumber = 0;
        while (true) {
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
                                return;
                            }
                            ImageItem takePhoto = new ImageItem();
                            cropUri = Uri.fromFile(subFile);
                            takePhoto.setImagePath(cropUri.getPath());
                            takePhoto.setSelected(true);
                            tempSelectBitmap.add(takePhoto);
                            fileNumber++;
                        }
                    } else if (isType.equals("0")) {
                        // 需要展示视频文件
                        if (subFile.isFile()) {
                            if (fileNumber > 3) {
                                return;
                            }
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(subFile.getPath());
                            String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            int videoDuration = Integer.valueOf(duration) / 1000;

                            VideoInfo videoInfo = new VideoInfo();
                            cropUri = Uri.fromFile(subFile);
                            videoInfo.setPath(cropUri.getPath());
                            videoInfo.setTaskid(taskid);
                            videoInfo.setTitle(subFile.getName());
                            videoInfo.setDuration(videoDuration);
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
            return;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CAMERA_PERMISSIONS:
                if (permissions[0].equals(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限获取成功
                    TaskObtainEvidenceTakePhoto();
                }
                break;
        }
    }
}
