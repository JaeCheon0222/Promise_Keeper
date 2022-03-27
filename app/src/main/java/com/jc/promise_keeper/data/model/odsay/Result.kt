package com.jc.promise_keeper.data.model.odsay


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("busCount")
    val busCount: Int?,
    @SerializedName("endRadius")
    val endRadius: Int?,
    @SerializedName("outTrafficCheck")
    val outTrafficCheck: Int?,
    @SerializedName("path")
    val path: List<Path>?,
    @SerializedName("pointDistance")
    val pointDistance: Int?,
    @SerializedName("searchType")
    val searchType: Int?,
    @SerializedName("startRadius")
    val startRadius: Int?,
    @SerializedName("subwayBusCount")
    val subwayBusCount: Int?,
    @SerializedName("subwayCount")
    val subwayCount: Int?
)