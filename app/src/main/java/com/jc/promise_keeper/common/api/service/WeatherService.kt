package com.jc.promise_keeper.common.api.service

import com.jc.promise_keeper.BuildConfig
import com.jc.promise_keeper.common.api.Url
import com.jc.promise_keeper.data.weather.WeatherBasicResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("${Url.WEATHER_API_BASE_URL}?serviceKey=${BuildConfig.WEATHER_API_KEY}&dataType=JSON&base_time=0800")
    suspend fun getRequestWeather(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("base_date") base_date: Int,
//        @Query("base_time") base_time: Int,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): Response<WeatherBasicResponse>

}