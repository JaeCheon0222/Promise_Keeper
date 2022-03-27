package com.jc.promise_keeper.data.model.odsay


import com.google.gson.annotations.SerializedName

data class SubPath(
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("door")
    val door: String?,
    @SerializedName("endExitNo")
    val endExitNo: String?,
    @SerializedName("endExitX")
    val endExitX: Double?,
    @SerializedName("endExitY")
    val endExitY: Double?,
    @SerializedName("endID")
    val endID: Int?,
    @SerializedName("endName")
    val endName: String?,
    @SerializedName("endX")
    val endX: Double?,
    @SerializedName("endY")
    val endY: Double?,
    @SerializedName("lane")
    val lane: List<Lane>?,
    @SerializedName("passStopList")
    val passStopList: PassStopList?,
    @SerializedName("sectionTime")
    val sectionTime: Int?,
    @SerializedName("startExitNo")
    val startExitNo: String?,
    @SerializedName("startExitX")
    val startExitX: Double?,
    @SerializedName("startExitY")
    val startExitY: Double?,
    @SerializedName("startID")
    val startID: Int?,
    @SerializedName("startName")
    val startName: String?,
    @SerializedName("startX")
    val startX: Double?,
    @SerializedName("startY")
    val startY: Double?,
    @SerializedName("stationCount")
    val stationCount: Int?,
    @SerializedName("trafficType")
    val trafficType: Int?,
    @SerializedName("way")
    val way: String?,
    @SerializedName("wayCode")
    val wayCode: Int?
)