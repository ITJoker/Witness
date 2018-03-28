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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.adapter.GridAdapter;
import com.risenb.witness.adapter.GridVideoAdapter;
import com.risenb.witness.beans.*;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.CustomCamera.CameraActivity;
import com.risenb.witness.ui.tasklist.CustomCamera.TakePhotoActivity;
import com.risenb.witness.ui.tasklist.PlayVideo.PlayVideoActivity;
import com.risenb.witness.ui.tasklist.fragment.RecentlyDistanceFragment;
import com.risenb.witness.ui.home.TaskInfoTrainUI;
import com.risenb.witness.utils.FileUtils;
import com.risenb.witness.utils.ImageAddWatermarkUtil;
import com.risenb.witness.utils.NetWorksUtils;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.risenb.witness.utils.NetWorksUtils.NETWORK_NONE;

/**
 * 模板3，纯拍照任务
 */
public class UncertainEvidence extends BaseUI {
    final int SYSTEM_TAKE_PHOTH_CAMERA = 10106;
    final int SYSTEM_IMAGE_CROP_CAMERA = 10107;
    final int SYSTEM_PLAY_VIDEO_CAMERA = 10108;
    final int REQUEST_CODE_ASK_CAMERA_PERMISSIONS = 10109;
    public static final String MODLETHREETASK = "UncertainEvidence";
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
    //任务指南
    @BindView(R.id.operation_guide)
    RelativeLayout mOperationGuide;
    //备注
    @BindView(R.id.text_quzheng)
    TextView text_quzheng;

    private String mImageFileName;
    private String mImageFilePath;
    private Uri cropUri = null;

    private GridAdapter adapter;
    private GridVideoAdapter videoAdapter;
    private String taskid;
    private String marks;
    private String modeltype;
    private int page;
    //选择的图片的临时列表
    public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<>();
    //存放视频信息List
    public ArrayList<VideoInfo> videoInfoList = new ArrayList<>();
    public static int max = 0;
    public static int num = 30;
    //服务器返回的图片路径
    public List<String> returnFile = new ArrayList<>();
    //默认类型为拍照
    private String isType = "1";

    boolean backToExecState = false;
    public static String sort = "5";
    private String netTime;

    /*Handler handler;*/

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

            boolean operationGuide = SharedPreferencesUtil.getBoolean(getApplication(), "OperationGuide", false);
            if (!operationGuide) {
                mOperationGuide.setVisibility(View.VISIBLE);
                SharedPreferencesUtil.saveBoolean(getApplication(), "OperationGuide", true);
            }
            mOperationGuide.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOperationGuide.setVisibility(View.GONE);
                }
            });

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
            marks = bundle.getString("marks");
            //当前页码
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
            taskGuideLinearLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UncertainEvidence.this, TaskInfoTrainUI.class);
                    intent.putExtra("id", taskid);
                    startActivity(intent);
                }
            });

            onNonTaskAccessNetWorkDetail();
            //判断任务状态
            onJudgeTaskReadState("1");
        } catch (OutOfMemoryError e) {
            mUncertainEviNext.setEnabled(false);
            mUncertainEviCamera.setEnabled(false);
            makeText("空间不足，请重新启动应用");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isType.equals("1") && adapter != null) {
            /*
             * tempSelectBitmap不再使用static修饰且从查看照片页面返回时需要更新tempSelectBitmap.size()
             * 需要两次Update,拍照页面返回或者是删除照片返回的时候
             */
            adapter.notifyDataSetChanged();
        } else if (isType.equals("0") && videoAdapter != null) {
            if (videoInfoList != null) {
                videoInfoList.clear();
                showFiles(SDCardUtils.getSDCardPath() + application.videosSaveUrl + taskid);
                videoAdapter = new GridVideoAdapter(videoInfoList, taskid, marks);
                mUncertainAddImage.setAdapter(videoAdapter);
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
            showFiles(SDCardUtils.getSDCardPath() + application.videosSaveUrl + taskid);
            videoAdapter = new GridVideoAdapter(videoInfoList, taskid, marks);
            mUncertainAddImage.setAdapter(videoAdapter);
        } else if (isType.equals("1")) {
            // 说明需求为照片拍摄
            showFiles(SDCardUtils.getSDCardPath() + application.photosCompressSaveUrl + taskid);
            adapter = new GridAdapter(getApplication(), tempSelectBitmap);
            mUncertainAddImage.setAdapter(adapter);
            mUncertainAddImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != tempSelectBitmap && tempSelectBitmap.size() >= 0) {
                        if (tempSelectBitmap.size() != position) {
                            Intent intent = new Intent(UncertainEvidence.this, GalleryActivity.class);
                            intent.putExtra("position", "1");
                            intent.putExtra("ID", position);
                            intent.putExtra("mark", "UncertainEvidence");
                            //intent.putParcelableArrayListExtra("tempSelectBitmap", tempSelectBitmap);
                            startActivity(intent);
                        } else {
                            TaskObtainEvidenceTakePhoto();
                        }
                    }
                }
            });
        }

        /*ImageLoader.getInstance().displayImage(info.getTaskList().get(0).getExampleFile().get(0), mUncertainExampleImage, MyConfig.options);*/
    }

    private void taskLocation() {
        //LocationClient定位器必须在主线程中声明
        LocationClient locationClient = new LocationClient(getApplicationContext());
        BDLocationListener bdLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //获取定位结果
                StringBuffer sb = new StringBuffer(256);
                //获取定位时间
                sb.append("time : ");
                sb.append(bdLocation.getTime());
                //获取类型类型，61说明GPS定位成功
                sb.append("\nerror code : ");
                sb.append(bdLocation.getLocType());
                //获取纬度信息
                sb.append("\nlatitude : ");
                sb.append(bdLocation.getLatitude());
                SharedPreferencesUtil.saveString(getApplication(), taskid + "Latitude", String.valueOf(bdLocation.getLatitude()));
                //获取经度信息
                sb.append("\nlongitude : ");
                sb.append(bdLocation.getLongitude());
                SharedPreferencesUtil.saveString(getApplication(), taskid + "Longitude", String.valueOf(bdLocation.getLongitude()));
                //获取定位精准度
                sb.append("\nradius : ");
                sb.append(bdLocation.getRadius());
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    /*
                     * GPS定位结果
                     */
                    //单位：公里每小时
                    sb.append("\nspeed : ");
                    sb.append(bdLocation.getSpeed());
                    //获取卫星数
                    sb.append("\nsatellite : ");
                    sb.append(bdLocation.getSatelliteNumber());
                    //获取海拔高度信息，单位米
                    sb.append("\nheight : ");
                    sb.append(bdLocation.getAltitude());
                    //获取方向信息，单位度
                    sb.append("\ndirection : ");
                    sb.append(bdLocation.getDirection());
                    //获取地址信息
                    sb.append("\naddr : ");
                    sb.append(bdLocation.getAddrStr());
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                    /*
                     * time : 2017-02-28 20:46:12
                     * error code : 61
                     * latitude : 39.871917
                     * lontitude : 116.468266
                     * radius : 6.0
                     * speed : 7.500944
                     * satellite : 4
                     * height : 81.6
                     * direction : 259.9
                     * addr : 中国北京市朝阳区东三环南路98号
                     * describe : gps定位成功
                     */
                    // makeText("时间:" + bdLocation.getTime() + "\n" + "位置:" + bdLocation.getAddrStr() + "\n" + "纬度:" + bdLocation.getLatitude() + "\n" + "经度:" + bdLocation.getLongitude());
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    /*
                     * 网络定位结果
                     */
                    //获取地址信息
                    sb.append("\naddr : ");
                    sb.append(bdLocation.getAddrStr());
                    //获取运营商信息
                    sb.append("\noperationers : ");
                    sb.append(bdLocation.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {
                    /*
                     * 离线定位结果
                     */
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                //位置语义化信息
                sb.append("\nlocationdescribe : ");
                sb.append(bdLocation.getLocationDescribe());
                /*
                 * POI数据
                 */
                List<Poi> list = bdLocation.getPoiList();
                if (list != null) {
                    sb.append("\npoilist size = : ");
                    sb.append(list.size());
                    for (Poi p : list) {
                        sb.append("\npoi= : ");
                        sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                    }
                }
                Log.i("BaiduLocationApiDem", sb.toString());
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

    @Override
    protected void back() {
        if (tempSelectBitmap.size() > 0 || videoInfoList.size() > 0) {
            if (NetWorksUtils.getNetworkState(getApplication()) == NETWORK_NONE) {
                //无网络连接
                makeText("网络连接不稳定，照片已保存");
                finish();
            } else {
                backToExecState = true;
                // 保存任务状态为执行中，可通过参数控制不跳转下一页
                /*
                 * 联网多线程操作，所以下面的backToExecState = false;会先执行
                 */
                UploadHttpUrl();
            }
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        tempSelectBitmap.clear();
    }

    @Override
    protected void setControlBasis() {
        setTitle("任务详情");
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }

    @OnClick(R.id.uncertain_evi_next)
    public void UploadTaskImage() {
        if ("1".equals(isType)) {
            // 说明拍摄要求为图片
            if (tempSelectBitmap.size() == 0) {
                makeText("请按照任务说明拍摄照片");
                return;
            }
        } else if ("0".equals(isType)) {
            // 说明拍摄需求为视频
            if (videoInfoList.size() == 0) {
                makeText("请按照任务说明拍摄视频");
                return;
            }
        }
        UploadHttpUrl();
    }

    public void UploadHttpUrl() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskfilepath));
        List<UploadFileBean> uploadfileBeanList = new ArrayList<>();
        Map<String, String> params1 = new HashMap<>();
        StringBuilder sbstr = new StringBuilder();
        params1.put("taskid", taskid);
        params1.put("c", MyApplication.getInstance().getC());
        for (int i = 0; i < returnFile.size(); i++) {
            String path = returnFile.get(i);
            if (i != returnFile.size() - 1) {
                sbstr.append(path + ",");
            } else {
                sbstr.append(path);
            }
        }
        UploadFileBean uploadfileBean = new UploadFileBean();
        uploadfileBean.setReturnfile(sbstr.toString());
        if (isType.equals("1")) {
            // 说明文件类型为照片
            uploadfileBean.setType("1");
        } else if (isType.equals("0")) {
            // 说明文件类型为视频
            uploadfileBean.setType("0");
        }
        String latitude = SharedPreferencesUtil.getString(getApplication(), taskid + "Latitude", "");
        String longitude = SharedPreferencesUtil.getString(getApplication(), taskid + "Longitude", "");
        uploadfileBean.setLatitude(latitude);
        uploadfileBean.setLongtitude(longitude);
        uploadfileBean.setSort(UncertainEvidence.sort);
        uploadfileBeanList.add(uploadfileBean);
        BaseBeans<List<UploadFileBean>> baseBens = new BaseBeans<>();
        baseBens.setData(uploadfileBeanList);
        String jsontsr = JSON.toJSONString(baseBens);
        params1.put("taskJson", jsontsr);
        TaskListInfoUI.isClose = true;
        Utils.getUtils().showProgressDialog(getActivity(), "保存中");
        MyOkHttp.get().post(this, url, params1, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
                makeText("网络错误");
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                Utils.getUtils().dismissDialog();
                if ("1".equals(response.getSuccess())) {
                    RecentlyDistanceFragment.isNetRefresh = true;
                    // makeText("取证第一步完成");
                    /*for (int i = 0; i < tempSelectBitmap.size(); i++) {
                         // ★仅在程序运行的内存中做了暂时性照片被上传的标记
                        tempSelectBitmap.get(i).setSelected(false);
                    }*/
                    if (!backToExecState) {
                        Intent intent = new Intent(UncertainEvidence.this, UncertainTaskIssueState.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("taskid", taskid);
                        bundle.putString("modeltype", modeltype);
                        bundle.putInt("page", ++page);
                        bundle.putString("isType", isType);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        --page;
                        UIManager.getInstance().pushActivity(UncertainEvidence.this);
                    } else {
                        // 在点击下一步的时候不提示任务保存的提示
                        makeText("任务已保存至执行中");
                        // TODO 提示刷新列表
                        finish();
                    }
                }
            }
        });
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
                Intent intent = new Intent(this, PlayVideoActivity.class);
                intent.putExtra("taskid", taskid);
                startActivityForResult(intent, SYSTEM_PLAY_VIDEO_CAMERA);

                //定位
                taskLocation();
            } else {
                makeText("视频数量已达上限");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoInfoList.clear();
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

    public boolean getImageSavePath() {
        String mSDCardPath = SDCardUtils.getSDCardPath();
        mImageFilePath = mSDCardPath + MyApplication.photosSaveUrl + taskid + "/";
        boolean exists = FileUtils.createOrExistsDir(mImageFilePath);
        return exists;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SYSTEM_TAKE_PHOTH_CAMERA:
                    /*//TakePhotoActivity
                    if (resultCode == RESULT_OK) {
                        new CompressPhotoTask().execute();
                    }*/
                    Utils.getUtils().showProgressDialog(getActivity(), "保存中");
                    /*new CompressPhotoTask().execute();*/
                    compressPhotoTaskExecute();
                    break;
                case SYSTEM_IMAGE_CROP_CAMERA:
                    onCropImageResult();
                    break;
                case SYSTEM_PLAY_VIDEO_CAMERA:
                    // 说明是从视频拍摄页面返回的请求码
                    break;
            }
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
                }, 1600);
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
            //数值越高，图片像素越低
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
            //把压缩后的数据存放到byteArrayOutputStream中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            String mSDCardPath = SDCardUtils.getSDCardPath();
            String compressPath = mSDCardPath + MyApplication.photosCompressSaveUrl + taskid + "/";
            boolean exists = FileUtils.createOrExistsDir(compressPath);
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
            takePhoto.setImagePath(cropUri.getPath());
            /**
             * 置为true说明照片已经被压缩过了或是未被上传过
             */
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
                            /*
                             * ★★★Parcelable序列化ImageItem
                             */
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

    /**
     * 判断任务状态
     */
    private void onJudgeTaskReadState(String type) {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskIsread));
        Map<String, String> param = new HashMap<>();
        param.put("taskId", taskid);
        param.put("c", MyApplication.getInstance().getC());
        param.put("type", type);
        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                String flag = response.getSuccess();
                if ("1".equals(flag)) {
                    System.out.println("任务已读");
                } else if ("2".equals(flag)) {
                    System.out.println("任务未读");
                }
            }
        });
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
