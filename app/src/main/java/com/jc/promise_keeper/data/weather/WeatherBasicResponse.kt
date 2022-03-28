package com.jc.promise_keeper.data.weather


import com.google.gson.annotations.SerializedName

data class WeatherBasicResponse(
    @SerializedName("response")
    val response: Response?
)