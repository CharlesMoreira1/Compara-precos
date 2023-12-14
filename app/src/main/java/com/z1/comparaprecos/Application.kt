package com.z1.comparaprecos

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.z1.comparaprecos.admob.AppOpenAdManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        val testDevicesId = listOf(BuildConfig.DEVICE_TEST_AD_ID)
        val configuration = RequestConfiguration
            .Builder()
            .setTestDeviceIds(testDevicesId)
            .build()
        MobileAds.setRequestConfiguration(configuration)
        AppOpenAdManager(this, BuildConfig.ADMOB_OPEN_APP_ID)
    }
}

