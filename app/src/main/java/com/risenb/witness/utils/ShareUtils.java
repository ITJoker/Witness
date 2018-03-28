package com.risenb.witness.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.risenb.witness.R;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class ShareUtils {
    /**
     * 微信key
     */
    private static final String WX_ID = "wx3cd103750f6b14f9";

    /**
     * 微信secret
     */
    private static final String WX_SECRET = "db7614ae2602ee777e8728156a65a117";

    /**
     * 新浪key
     */
    private static final String SINA_KEY = "2103535502";

    /**
     * 新浪secret
     */
    private static final String SINA_SECRET = "ae4896d24696b3a822ae73be1d75eaa4";

    /**
     * 腾讯appid
     */
    private static final String QQ_ID = "1105716783";

    /**
     * 腾讯secret
     */
    private static final String QQ_KEY = "4Gbm9CN9QHE2pA9y";

    private static final String FOLLOW = "人人目击";

    private static ShareUtils shareUtils;

    private ShareUtils() {
        super();
    }

    public static ShareUtils getInstance() {
        if (shareUtils == null) {
            shareUtils = new ShareUtils();
        }
        return shareUtils;
    }

    /**
     * 初始化
     */
    public void init() {
        PlatformConfig.setWeixin(WX_ID, WX_SECRET);
        // 微信 appid appsecret
        PlatformConfig.setSinaWeibo(SINA_KEY, SINA_SECRET);
        // 新浪微博 appkey appsecret
        PlatformConfig.setQQZone(QQ_ID, QQ_KEY);
        // 腾讯扣扣
    }

    public void share(Context context, SHARE_MEDIA platform, String title, String text, String imageUrl, String url, final ShareResult result) {
        UMImage image;
        if (TextUtils.isEmpty(imageUrl)) {
            image = new UMImage(context, R.drawable.ic_launcher);
        } else {
            image = new UMImage(context, imageUrl);
        }
        new ShareAction((Activity) context).setPlatform(platform).setCallback(new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA platform) {
                // 成功
                if (result != null)
                    result.success(platform);
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                // 失败
                if (result != null)
                    result.fail(platform);
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                // 取消
                if (result != null)
                    result.cancel(platform);
            }
        }).withText(text).withTitle(title).withFollow(FOLLOW).withTargetUrl(url).withMedia(image).share();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, Context context) {
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
    }

    public interface ShareResult {
        void success(SHARE_MEDIA platform);

        void fail(SHARE_MEDIA platform);

        void cancel(SHARE_MEDIA platform);
    }
}
