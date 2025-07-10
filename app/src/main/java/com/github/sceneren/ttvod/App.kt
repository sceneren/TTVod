package com.github.sceneren.ttvod

import android.app.Application


class App: Application() {
    override fun onCreate() {
        super.onCreate()
//        init(this)
    }

//    private fun init(context: Context) {
//
//
//        // 1. 设置 Logcat & Asserts 开关
//        // 开启日志方便排查问题，release 版本一定要关闭
//        L.ENABLE_LOG = BuildConfig.DEBUG
//
//        // 开启断言在内部状态出错时会抛 crash，便于及时发现问题。release 版本一定要关闭。
//        Asserts.DEBUG = BuildConfig.DEBUG
//
//
//        // 2. 初始化配置
//        VolcPlayerInit.config(
//            VolcPlayerInitConfig.Builder()
//                .setContext(context)
//                .setAppInfo(
//                    VolcPlayerInitConfig.AppInfo.Builder()
//                        .setAppId("781559") // 应用 ID，可在视频点播控制台应用管理页面
//                        .setAppName("TTVOD Test") // 应用英文名，可在视频点播控制台应用管理页面获取
//                        .setAppChannel("your app channel") // 渠道号。由您自定义，如小米应用商店 (xiaomi)、华为应用市场 (huawei) 等
//                        .setAppVersion(BuildConfig.VERSION_NAME) // App 版本号。合法版本号应包含大于或等于 2 个 . 分隔符，如 "1.3.2"
//                        .setLicenseUri("assets:///license/vod.lic") // License 文件路径。假设您的 License 文件路径为 /VEVodDemo-android/app/src/main/assets/vod.lic，则 LICENSE_URI 应为 assets:///vod.lic
//                        .build()
//                )
//                .build()
//        )
//
//
//        // 3. 调用初始化方法
//        VolcPlayerInit.initSync()
//    }
}