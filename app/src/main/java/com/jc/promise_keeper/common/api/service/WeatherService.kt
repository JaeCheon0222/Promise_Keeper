package com.jc.promise_keeper.common.api.service

import com.jc.promise_keeper.BuildConfig
import com.jc.promise_keeper.common.api.Url
import com.jc.promise_keeper.data.weather.WeatherBasicResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    // base_time=0800
//    http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=i4414TNCRf4NYpgkU0dKkxuvQiLpaYhUKW6ZeC28QjvGwcBpj0RojnhhVQgbSv215XJgSSuASQ4LpqMuMEalJw==&numOfRows=26&pageNo=1&dataType=JSON&base_date=20220329&base_time=2000&nx=55&ny=127
    @GET("${Url.WEATHER_API_BASE_URL}?serviceKey=${BuildConfig.WEATHER_API_KEY}&dataType=JSON")
    suspend fun getRequestWeather(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("base_date") baseDate: Int,
        @Query("base_time") baseTime: Int,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): Response<WeatherBasicResponse>

}