package com.jc.promise_keeper.common.api.repository

import com.jc.promise_keeper.common.api.Connect
import com.jc.promise_keeper.data.weather.WeatherBasicResponse
import retrofit2.Response

object WeatherRepository {

    suspend fun getRequestWeather(
        numOfRows: Int,
        pageNo: Int,
        base_date: Int,
//        base_time: Int,
        nx: Int,
        ny: Int
    ): Response<WeatherBasicResponse> =
        Connect.weatherService.getRequestWeather(numOfRows,pageNo, base_date, nx, ny)
//        Connect.weatherService.getRequestWeather(numOfRows,pageNo, base_date, base_time, nx, ny)



}