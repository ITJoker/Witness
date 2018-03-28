package com.risenb.witness.utils;

import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyConfig {
    /**
     * 圆形图片
     */
    public static DisplayImageOptions optionsRound = new DisplayImageOptions.Builder()
            // 设置图片下载期间显示的图片
            .showStubImage(R.drawable.default_image)
            // 设置图片Uri为空或是错误的时候显示的图片
            .showImageForEmptyUri(R.drawable.default_image)
            // 设置图片加载或解码过程中发生错误显示的图片
            .showImageOnFail(R.drawable.default_image)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new CircleBitmapDisplayer())
            .build();

    /**
     * 圆角图片
     */
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            // 设置图片下载期间显示的图片
            .showStubImage(R.drawable.default_image)
            // 设置图片Uri为空或是错误的时候显示的图片
            .showImageForEmptyUri(R.drawable.default_image)
            // 设置图片加载或解码过程中发生错误显示的图片
            .showImageOnFail(R.drawable.default_image)
            // 设置下载的图片是否缓存在内存中
            .cacheInMemory(true)
            // 设置下载的图片是否缓存在SD卡中
            .cacheOnDisc(true)
            // 设置成圆角图片
            .displayer(new RoundedBitmapDisplayer(Utils.getUtils().getDimen(R.dimen.dm006)))
            // 创建配置过得DisplayImageOption对象
            .build();

    /**
     * 圆角图片
     */
    public static DisplayImageOptions optionss = new DisplayImageOptions.Builder()
            // 设置图片下载期间显示的图片
            .showStubImage(R.drawable.default_user)
            // 设置图片Uri为空或是错误的时候显示的图片
            .showImageForEmptyUri(R.drawable.default_user)
            // 设置图片加载或解码过程中发生错误显示的图片
            .showImageOnFail(R.drawable.default_user)
            // 设置下载的图片是否缓存在内存中
            .cacheInMemory(true)
            // 设置下载的图片是否缓存在SD卡中
            .cacheOnDisc(true)
            // 设置成圆角图片
            .displayer(new RoundedBitmapDisplayer(Utils.getUtils().getDimen(R.dimen.dm040)))
            // 创建配置过得DisplayImageOption对象
            .build();

    public static final String SIGN = "308202c1308201a9a00302010202047f360ae1300d06092a864886f70d01010b"
            + "05003011310f300d06035504031306626561757479301e170d3136303130373032343535395a170d34303132"
            + "33313032343535395a3011310f300d0603550403130662656175747930820122300d06092a864886f70d0101"
            + "0105000382010f003082010a02820101008b2128bde89cdb8ede9c081ede9fd30a1115e6fc2552a710ac15f4"
            + "00548e09664dfa29878a4f4c798de6fba873d3436178670b3035dc148d3340b1fd650dfa9a33acff8754df8a"
            + "237be1516b1d1a506260498fff94b009cb90da0f11be29fd81e9d7bcddd5961bd6717949141e9d168c6be01c"
            + "952b57de12cb798849b10860c8e53d17f898e5b7f81057015daecaaea257f5d441efd75c17bd63fed0ac2a5e"
            + "ea8e6c0b8fad2d58725ad58e1bee160a2cf0134425cc57a45f68d2fd2eb64a8067f5ea4ce90a50f73d3503fd"
            + "85878d22fc9c2e6a0cdf0586e4afe05149dfde4e4a570f34c7da06299a785413a778a4fba9feea6ff1675016"
            + "20c19b711950ebb9ab0203010001a321301f301d0603551d0e0416041477c8930b02291c2fbf2610997a9d20"
            + "6fe32fb9b5300d06092a864886f70d01010b0500038201010043f3b10c41a9c8a7499f7692257dc6365e9c5b"
            + "e645b545afd77c025246cffe91ddce503a775b818acbc6fce87e20b2dd883e4ce77cb9726e71025cf055c0f7"
            + "13d56b142d6731a1fa6b3a9140ade68ff7dfed0ee777e90aada7306bbac4486e6f9a0f70cd4c8fce78f86bbb"
            + "7feb9aa239f9031c279705382dc68f0dcc33140e4e75263c95df2f0b1ada08f5e159a390c3be63ff633a389f"
            + "628ae2d41e087bcc5daa032d35eb147ac9684d2908877904ce2097b98aaab9d902a68133b9e5393d8afb38c0"
            + "af3eecec1a7126802ab7d3c17db85223d8a14bca203f9dfedd9dc7ba29b175b727d58a2263ca3311c2bd15f9" + "dfa23481b4dc09cd41a2a6713562602c58";
}
