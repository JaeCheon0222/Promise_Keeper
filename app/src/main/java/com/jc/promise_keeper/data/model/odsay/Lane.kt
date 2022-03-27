package com.jc.promise_keeper.data.model.odsay


import com.google.gson.annotations.SerializedName

data class Lane(
    @SerializedName("busID")
    val busID: Int?,
    @SerializedName("busNo")
    val busNo: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("subwayCityCode")
    val subwayCityCode: Int?,
    @SerializedName("subwayCode")
    val subwayCode: Int?,
    @SerializedName("type")
    val type: Int?
)