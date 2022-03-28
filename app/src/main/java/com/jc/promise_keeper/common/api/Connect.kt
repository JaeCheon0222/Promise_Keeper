package com.jc.promise_keeper.common.api

import com.google.gson.GsonBuilder
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import com.jc.promise_keeper.BuildConfig
import com.jc.promise_keeper.common.api.service.PromiseKeepService
import com.jc.promise_keeper.common.api.service.WeatherService
import com.jc.promise_keeper.common.util.DateDeserializer
import com.jc.promise_keeper.common.util.Preferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.*

object Connect {


    interface JsonResponseHandler {
        fun onResponse(jsonObject: JSONObject)
    }

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

        // Date 자료형으로 파싱 => String 을 yyyy-MM-dd HH:mm:ss 으로 파싱해서 저장해야한다. (고정된 양식으로 내려줌)
        val gson = GsonBuilder()
            .setDateFormat("yyy-MM-dd HH:mm:ss")    // 서버가 이런 양식으로 보내주는 String 을
            .registerTypeAdapter(
                Date::class.java,
                DateDeserializer()
            )     // 어떤 형태의 자료형에 적용시킬지, Date 클래스로 파싱
            .create()


        Retrofit.Builder()
            .baseUrl(Url.KEEP_THE_TIME_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(buildOkHttpClient())
            .client(myClient)
            .build()
            .create()
    }

    val weatherService: WeatherService by lazy {

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