package com.risenb.witness.ui.tasklist.PlayVideo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.risenb.witness.R;
import com.risenb.witness.beans.VideoInfo;

import java.util.ArrayList;

public class VideoPlayerActivity extends BaseActivity {
    private static final int UPDATE_SYSTEM_TIME = 1;
    private static final int UPDATE_PLAYED_TIME = 2;
    private static final int HIDE_PANEL = 3;
    private VideoView videoView;
    private TextView tv_videoTitle;
    private ImageButton ib_playPause;
    private ImageView iv_battery;
    private MyBatteryReceiver receiver;
    private TextView tv_system_time;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SYSTEM_TIME:
                    updateSystemTime();
                    break;
                case UPDATE_PLAYED_TIME:
                    updatePlayedTime();
                    break;
                case HIDE_PANEL:
                    hidePanel();
                    break;
            }
        }
    };

    private SeekBar sb_volume;
    private int currentVolume;
    private AudioManager manager;
    private ImageView iv_volume;
    private int lastVolume;
    private float startY;
    private int screenWidth;
    private View alpha_cover;
    private float currentAlpha;
    private TextView tv_playedTime;
    private TextView tv_duration;
    private SeekBar sb_progress;
    private ArrayList<VideoInfo> videoItemList;
    private int currentPosition;
    private ImageButton ib_pre;
    private ImageButton ib_next;
    private int screenHeight;
    private ImageButton ib_sreenSize;
    // 用来记录是否是全屏状态 默认是非全屏
    private boolean isFullScreen = false;
    private int videoHeight;
    private int videoWidth;
    private RelativeLayout rl_top_panel;
    private LinearLayout ll_bottom_panel;
    private int bottomHeight;
    private int topHeight;
    private GestureDetector detector;
    // 用来记录当前的操作面板是出于显示状态还是隐藏状态
    private boolean isShowPanel = false;
    private LinearLayout loading_cover;
    private ProgressBar pb_buffering;
    private ImageButton ib_back;

    private void updateSystemTime() {
        // 获取当前时间设置到textView上
        tv_system_time.setText(StringUtils.formatSystemTime());
        // 通过handler发送一个延迟1s执行的消息 更新系统时间
        handler.sendEmptyMessageDelayed(UPDATE_SYSTEM_TIME, 1000);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_videoplayer);
        videoView = (VideoView) findViewById(R.id.vv_video);
        // 顶部悬浮窗
        tv_videoTitle = (TextView) findViewById(R.id.tv_video_panel_title);
        iv_battery = (ImageView) findViewById(R.id.iv_video_panel_battery);
        tv_system_time = (TextView) findViewById(R.id.tv_video_panel_system_time);
        sb_volume = (SeekBar) findViewById(R.id.sb_volume);
        iv_volume = (ImageView) findViewById(R.id.iv_video_panel_volume);
        alpha_cover = findViewById(R.id.alpha_cover);
        rl_top_panel = (RelativeLayout) findViewById(R.id.rl_top_panel);

        // 底部悬浮窗
        ib_playPause = (ImageButton) findViewById(R.id.ib_playpause);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        tv_playedTime = (TextView) findViewById(R.id.tv_video_current_playedtime);
        tv_duration = (TextView) findViewById(R.id.tv_video_duration);
        sb_progress = (SeekBar) findViewById(R.id.sb_position);
        ib_pre = (ImageButton) findViewById(R.id.ib_pre);
        ib_next = (ImageButton) findViewById(R.id.ib_next);
        ib_sreenSize = (ImageButton) findViewById(R.id.ib_screensize);
        ll_bottom_panel = (LinearLayout) findViewById(R.id.ll_bottom_panel);

        // 加载数据悬浮窗
        loading_cover = (LinearLayout) findViewById(R.id.ll_loading_cover);
        pb_buffering = (ProgressBar) findViewById(R.id.pb_buffering);
    }

    @Override
    protected void initData() {
        Uri data = getIntent().getData();
        if (data != null) {
            // 响应其它应用的调用
            videoView.setVideoURI(data);
            tv_videoTitle.setText(data.toString());
        } else {
            // 如果Uri data为空说明是自己应用调用的当前activity
            // 获取视频数据
            // VideoItem item = (VideoItem) getIntent().getSerializableExtra("videoItem");
            videoItemList = (ArrayList<VideoInfo>) getIntent().getSerializableExtra("videoInfoList");
            // 当前播放的视频在集合中的索引
            currentPosition = getIntent().getIntExtra("position", 0);

            // 给videoView设置数据 这个方法会帮助创建一个MediaPlayer对象
            // 并且会调用MediaPlayer的setDataSource方法 让后开启一个异步的准备 prepareAsync();方法
            // videoView.setVideoPath(item.path);
            videoView.setVideoPath(videoItemList.get(currentPosition).getPath());
            // videoView.setVideoPath("http://video.chinanews.com/flv/2016/1118/quancheng.mp4");

            // 设置标题
            // tv_videoTitle.setText(item.title);
            /*tv_videoTitle.setText(videoItemList.get(currentPosition).getTitle());*/
            tv_videoTitle.setText("");
            // updateSystemTime();
        }

        // 设置音量
        // ①获取最大音量 设置seekBar的最大音量
        manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // getStreamMaxVolume() 这个方法根据传入的类型 来获取当前声音类型的最大音量
        // AudioManager.STREAM_MUSIC; 音乐
        // AudioManager.STREAM_VOICE_CALL; 通话音量
        // AudioManager.STREAM_NOTIFICATION;通知的音量
        // AudioManager.STREAM_SYSTEM; 系统音量
        // AudioManager.STREAM_ALARM;闹铃音量
        int maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sb_volume.setMax(maxVolume);

        // ②获取系统当前音量 设置seekBar当前的进度
        currentVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sb_volume.setProgress(currentVolume);

        // 获取屏幕宽度 高度
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        // 隐藏顶部和底部面板
        // 测量顶部面板和底部面板的高度
        ll_bottom_panel.measure(0, 0);
        rl_top_panel.measure(0, 0);

        // 获取测量的高度
        bottomHeight = ll_bottom_panel.getMeasuredHeight();
        topHeight = rl_top_panel.getMeasuredHeight();

        // 在y方向移动 面板高度的距离 隐藏起来
        ll_bottom_panel.setTranslationY(bottomHeight);
        rl_top_panel.setTranslationY(-topHeight);
    }

    @Override
    public void initListener() {
        ib_back.setOnClickListener(this);
        ib_playPause.setOnClickListener(this);
        iv_volume.setOnClickListener(this);
        ib_pre.setOnClickListener(this);
        ib_next.setOnClickListener(this);
        ib_sreenSize.setOnClickListener(this);
        // 设置准备好的监听
        videoView.setOnPreparedListener(new MyOnPreparedListener());
        // 设置一个播放完成的监听器
        videoView.setOnCompletionListener(new MyOnCompletionListener());

        // 动态注册电量的广播接收者
        receiver = new MyBatteryReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        MyOnSeekBarChangeListener myOnSeekBarChangeListener = new MyOnSeekBarChangeListener();
        sb_volume.setOnSeekBarChangeListener(myOnSeekBarChangeListener);
        sb_progress.setOnSeekBarChangeListener(myOnSeekBarChangeListener);

        /*
         * 手势监听器
         */
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                // 只要点击了就会走onSingleTapUp
                Log.e("videoplayer", "onSingleTapUp");
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // 双击事件
                Log.e("videoplayer", "onDoubleTap");
                // 双击修改视频的大小
                changeVideoSize();
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // 单击事件
                Log.e("videoplayer", "onSingleTapConfirmed");
                if (isShowPanel) {
                    hidePanel();
                    // 面板已经隐藏了 可以移除隐藏面板的消息
                    handler.removeMessages(HIDE_PANEL);
                } else {
                    showPanel();
                }
                return super.onSingleTapConfirmed(e);
            }
        });

    }

    /**
     * 显示面板的方法
     */
    private void showPanel() {
        // 由于面板在界面中 setContentView加载之后是处于显示状态的
        // 之后通过代码setTranslationY 隐藏起来 现在想恢复显示的状态 那么就传坐标0就可以了
        // 恢复到初始状态
        ll_bottom_panel.setTranslationY(0);
        rl_top_panel.setTranslationY(0);

        // 延迟5秒隐藏控制面板
        handler.sendEmptyMessageDelayed(HIDE_PANEL, 5000);

        isShowPanel = true;
    }

    /**
     * 隐藏面板的方法
     */
    private void hidePanel() {
        ll_bottom_panel.setTranslationY(bottomHeight);
        rl_top_panel.setTranslationY(-topHeight);
        // 面板隐藏之后 修改标志位的值
        isShowPanel = false;
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.ib_playpause:
                // 如果视频正在播放
                if (videoView.isPlaying()) {
                    // 把视频暂停
                    videoView.pause();
                } else {
                    // 如果处于暂停的状态 让视频播放起来
                    videoView.start();
                }
                // 根据当前状态更新播放暂停的图标
                updatePlayPauseIcon();
                break;
            case R.id.iv_video_panel_volume:
                if (currentVolume > 0) {
                    sb_volume.setProgress(0);
                } else {
                    sb_volume.setProgress(lastVolume);
                }
                break;
            case R.id.ib_next:
                playNext();
                break;
            case R.id.ib_pre:
                playPre();
                break;
            case R.id.ib_screensize:
                changeVideoSize();
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }

    /**
     * 修改视频的尺寸
     */
    private void changeVideoSize() {
        if (isFullScreen) {
            // 是全屏状态 恢复到初始的状态
            videoView.getLayoutParams().width = videoWidth * screenHeight / videoHeight;
            videoView.getLayoutParams().height = screenHeight;
            // 修改图标的状态
            ib_sreenSize.setImageResource(R.drawable.video_fullscreen_selector);
        } else {
            // 非全屏状态 修改到全屏的状态
            videoView.getLayoutParams().width = screenWidth;
            videoView.getLayoutParams().height = screenHeight;
            // 修改图标状态
            ib_sreenSize.setImageResource(R.drawable.video_defaultsize_selector);
        }
        // 修改记录当前状态的变量
        isFullScreen = !isFullScreen;
        // 重新绘制videoView
        videoView.requestLayout();
    }

    /**
     * 处理上一首的方法
     */
    private void playPre() {
        if (currentPosition > 0) {
            currentPosition--;
        }
        startPlay();
    }

    private void startPlay() {
        // 切换音乐 先停止之前的播放
        videoView.stopPlayback();
        // 根据当前的媒体文件索引 到集合中找到对应的数据 播放新的视频
        videoView.setVideoPath(videoItemList.get(currentPosition).getPath());
    }

    private void playNext() {
        if (currentPosition < videoItemList.size() - 1) {
            currentPosition++;
        }
        startPlay();
    }

    private void updatePlayPauseIcon() {
        if (videoView.isPlaying()) {
            ib_playPause.setImageResource(R.drawable.video_pause_selector);
            updatePlayedTime();
        } else {
            ib_playPause.setImageResource(R.drawable.video_play_selector);
            handler.removeMessages(UPDATE_PLAYED_TIME);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开始更新系统时间
        updateSystemTime();
        if (videoView.isPlaying()) {
            updatePlayedTime();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除更新系统时间的消息
        // handler.removeMessages(UPDATE_SYSTEM_TIME);
        // 移除所有的消息
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
        // 注销广播接收者
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录按下的位置
                startY = event.getY();
                // 获取当前 覆盖在整个View上面的 覆盖层的alpha初始值
                currentAlpha = alpha_cover.getAlpha();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                // 判断当前的x坐标
                if (x < screenWidth / 2) {
                    // 如果是左半边屏幕 处理音量变化
                    int temp = (int) (currentVolume + (startY - y) / 150);
                    if (temp < 0) {
                        currentVolume = 0;
                    } else if (temp > sb_volume.getMax()) {
                        currentVolume = sb_volume.getMax();
                    } else {
                        currentVolume = temp;
                    }
                    sb_volume.setProgress(currentVolume);
                } else {
                    // 右半边屏幕处理亮度变化
                    float temp = (float) (currentAlpha + (y - startY) / 150 * 0.1);
                    if (temp < 0) {
                        currentAlpha = 0;
                    } else if (temp > 0.8) {
                        currentAlpha = 0.8f;
                    } else {
                        currentAlpha = temp;
                    }
                    alpha_cover.setAlpha(currentAlpha);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 准备好的监听器
     */
    private class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            // 开始播放 隐藏盖在上面的加载层
            loading_cover.setVisibility(View.GONE);
            // 开始播放视频
            videoView.start();
            // 设置一个监听消息的监听器
            mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            // 开始缓冲
                            pb_buffering.setVisibility(View.VISIBLE);
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            // 缓冲结束
                            pb_buffering.setVisibility(View.GONE);
                            break;
                    }
                    // 返回true说明消息已经处理 返回false 没处理
                    return false;
                }
            });
            // 设置一个缓冲的监听
            mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    // 通过第二个参数 percent 可以知道当前下载了多少数据
                    // 通过seekBar的第二进度展示当前已经下载的数据
                    sb_progress.setSecondaryProgress(mp.getDuration() * percent / 100);
                }
            });
            // 设置一个出错的监听
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 如果播放出错了 会走出错的监听 通过what可以获取对应的错误信息
                    // 返回值 如果是true 说明当前错误已经处理了 如果返回false或者没设置OnErrorListener
                    // 会调用onCompletionListener
                    return false;
                }
            });
            // 更新播放图标
            updatePlayPauseIcon();
            // 更新视频的总时长
            sb_progress.setMax(videoView.getDuration());
            tv_duration.setText(StringUtils.formatMediaTime(videoView.getDuration()));
            // 开始更新当前播放的进度
            updatePlayedTime();

            if (videoItemList == null || videoItemList.size() == 0) {
                // 说明列表数据不存在
                // 上一首/下一首设置为灰色图标 并且不可以点击
                ib_pre.setImageResource(R.drawable.btn_pre_gray);
                ib_pre.setClickable(false);

                ib_next.setImageResource(R.drawable.btn_next_gray);
                ib_next.setClickable(false);
            } else {
                // 更新上一首/下一首图标的状态
                if (currentPosition == 0) {
                    // 第一首
                    ib_pre.setImageResource(R.drawable.btn_pre_gray);
                    ib_pre.setClickable(false);
                } else {
                    ib_pre.setImageResource(R.drawable.video_pre_selector);
                    ib_pre.setClickable(true);
                }

                if (currentPosition == videoItemList.size() - 1) {
                    // 最后一首
                    ib_next.setImageResource(R.drawable.btn_next_gray);
                    ib_next.setClickable(false);
                } else {
                    ib_next.setImageResource(R.drawable.video_next_selector);
                    ib_next.setClickable(true);
                }
            }
            // 通过mediaPlayer获取视频的宽度和高度
            videoHeight = mp.getVideoHeight();
            videoWidth = mp.getVideoWidth();
        }
    }

    private class MyBatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 系统的电量是通过广播,以一个int类型的参数进行传递 对应的key level 数据在intent中进行携带
            int level = intent.getIntExtra("level", 100);
            if (level > 90) {
                iv_battery.setImageResource(R.drawable.ic_battery_100);
            } else if (level > 80) {
                iv_battery.setImageResource(R.drawable.ic_battery_80);
            } else if (level > 60) {
                iv_battery.setImageResource(R.drawable.ic_battery_60);
            } else if (level > 40) {
                iv_battery.setImageResource(R.drawable.ic_battery_40);
            } else if (level > 20) {
                iv_battery.setImageResource(R.drawable.ic_battery_20);
            } else if (level > 10) {
                iv_battery.setImageResource(R.drawable.ic_battery_10);
            } else {
                iv_battery.setImageResource(R.drawable.ic_battery_0);
            }
        }
    }

    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // 当进度条进度发生改变的时候 就会走这个方法
            switch (seekBar.getId()) {
                case R.id.sb_volume:
                    // 处理音量相关的进度条
                    // 先记录当前的音量
                    lastVolume = currentVolume;
                    // 用更新的音量修改当前音量
                    currentVolume = progress;
                    // 第一个参数要调整的音量类型
                    // 第二个参数 音量要调整的大小
                    // 第三个参数  调整时候的效果
                    // AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE 没声不震动
                    // AudioManager.FLAG_VIBRATE  震动  震动需要一个震动权限
                    // udioManager.FLAG_PLAY_SOUND 调整过程中会发生 嘟嘟嘟嘟
                    // AudioManager.FLAG_SHOW_UI  以吐司的形式展示系统调整音量界面
                    manager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                    break;
                case R.id.sb_position:
                    // 控制播放进度的进度条
                    // videoView.seekTo 跳到某一个位置继续播放
                    if (fromUser) {
                        // 如果当前onProgressChanged 方法是由用户操作导致的 fromUser为true
                        // 如果调用了 seekbar的 setProgress方法 也会走onProgressChanged 这个时候 fromUser为false
                        // 只处理用户手动拖动进度条 导致的进度改变
                        videoView.seekTo(progress);
                    }
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // 当用户开始操作进度条的时候 会走这个方法
            Log.e("video", "开始操作面板");
            // 移除隐藏控制面板的消息
            handler.removeMessages(HIDE_PANEL);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 当用户停止操作进度条的时候 会走这个方法
            Log.e("video", "停止操作面板");
            // 发送延迟隐藏控制面板的消息
            handler.sendEmptyMessageDelayed(HIDE_PANEL, 3000);
        }
    }

    private class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            // 播放结束 移除更新播放时间的消息
            handler.removeMessages(UPDATE_PLAYED_TIME);
            // 把当前播放的时间设置为总时长
            tv_playedTime.setText(StringUtils.formatMediaTime(videoView.getDuration()));
            // 把播放的进度条设置为最大进度
            sb_progress.setProgress(sb_progress.getMax());
            // 修改播放暂停的图标为三角图标状态
            ib_playPause.setImageResource(R.drawable.video_play_selector);
        }
    }

    /**
     * 更新当前播放的时间,通过textView和seekBar展示进度
     */
    private void updatePlayedTime() {
        // videoView.getCurrentPosition() 获取当前播放的进度，返回毫秒值
        Log.e("videoplayer", "updatePlayedTime");
        tv_playedTime.setText(StringUtils.formatMediaTime(videoView.getCurrentPosition()));
        // 更新进度条的进度
        sb_progress.setProgress(videoView.getCurrentPosition());
        handler.sendEmptyMessageDelayed(UPDATE_PLAYED_TIME, 500);
    }

}
