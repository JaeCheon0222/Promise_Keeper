package com.jc.promise_keeper.common.util

import android.app.Application
import com.jc.promise_keeper.BuildConfig
import com.naver.maps.map.NaverMapSdk

class NaverMapApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAP_CLIENT_ID)
    }

}