package com.jc.promise_keeper.common.api

import com.jc.promise_keeper.BuildConfig
import com.jc.promise_keeper.common.api.service.PromiseKeepService
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
        Retrofit.Builder()
            .baseUrl(Url.KEEP_THE_TIME_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildOkHttpClient())
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