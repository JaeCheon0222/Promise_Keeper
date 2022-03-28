package com.jc.promise_keeper.common.util

import android.app.Application
import com.jc.promise_keeper.BuildConfig
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "${BuildConfig.KAKAO_API_KEY}")
    }
}