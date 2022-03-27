package com.jc.promise_keeper.data.model.odsay


import com.google.gson.annotations.SerializedName

data class Path(
    @SerializedName("info")
    val info: Info?,
    @SerializedName("pathType")
    val pathType: Int?,
    @SerializedName("subPath")
    val subPath: List<SubPath>?
)