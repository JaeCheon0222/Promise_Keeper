package com.jc.promise_keeper.data.model.datas

import com.google.gson.annotations.SerializedName

data class PlaceData(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("is_primary")
    val isPrimary: Boolean
)
