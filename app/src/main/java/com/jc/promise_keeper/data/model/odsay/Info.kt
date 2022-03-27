package com.jc.promise_keeper.data.model.odsay


import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("busStationCount")
    val busStationCount: Int?,
    @SerializedName("busTransitCount")
    val busTransitCount: Int?,
    @SerializedName("firstStartStation")
    val firstStartStation: String?,
    @SerializedName("lastEndStation")
    val lastEndStation: String?,
    @SerializedName("mapObj")
    val mapObj: String?,
    @SerializedName("payment")
    val payment: Int?,
    @SerializedName("subwayStationCount")
    val subwayStationCount: Int?,
    @SerializedName("subwayTransitCount")
    val subwayTransitCount: Int?,
    @SerializedName("totalDistance")
    val totalDistance: Int?,
    @SerializedName("totalStationCount")
    val totalStationCount: Int?,
    @SerializedName("totalTime")
    val totalTime: Int?,
    @SerializedName("totalWalk")
    val totalWalk: Int?,
    @SerializedName("totalWalkTime")
    val totalWalkTime: Int?,
    @SerializedName("trafficDistance")
    val trafficDistance: Int?
)