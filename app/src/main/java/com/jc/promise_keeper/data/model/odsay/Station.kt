package com.jc.promise_keeper.data.model.odsay


import com.google.gson.annotations.SerializedName

data class Station(
    @SerializedName("index")
    val index: Int?,
    @SerializedName("isNonStop")
    val isNonStop: String?,
    @SerializedName("stationID")
    val stationID: Int?,
    @SerializedName("stationName")
    val stationName: String?,
    @SerializedName("x")
    val x: String?,
    @SerializedName("y")
    val y: String?
)