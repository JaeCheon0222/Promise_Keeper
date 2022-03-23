package com.jc.promise_keeper.common.api

import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import com.jc.promise_keeper.BuildConfig
import com.jc.promise_keeper.common.api.service.PromiseKeepService
import com.jc.promise_keeper.common.util.Preferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Connect {

//    private var retrofit: Retrofit? = null

    //region * retrofit & httpClient
//    val promiseKeepService: PromiseKeepService by lazy { getRetrofit().create(PromiseKeepService::class.java) }

//    val keepTheTimeService: PromiseKeepService by lazy {
//        Retrofit.Builder()
//            .baseUrl(Url.KEEP_THE_TIME_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(buildOkHttpClient())
//            .build()
//            .create()
//    }

    val promiseKeepService: PromiseKeepService by lazy {

        val interceptor = Interceptor {
            with(it) {
                // 기존의 request 에, 헤더를 추가해주자.
                val newRequest = request()
                    .newBuilder()
                    .addHeader("X-Http-Token", Preferences.getUserToken(context))
                    .build()

                // 다시 하려던 일을 이어가도록한다.
                proceed(newRequest)
            }
        }

        // 만들어낸 인터셉터를 활용하도록 세팅.
        // 레트로핏이 사용하는 클라이언트 객체를 수정한다.
        val myClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()



        Retrofit.Builder()
            .baseUrl(Url.KEEP_THE_TIME_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildOkHttpClient())
            .client(myClient)
            .build()
            .create()



    }

    private fun buildOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    //endregion


//    val keepTheTimeService: PromiseKeepService by lazy {
//        Retrofit.Builder()
//            .baseUrl(Url.KEEP_THE_TIME_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(buildHttpClient())
//            .build()
//            .create()
//    }


}