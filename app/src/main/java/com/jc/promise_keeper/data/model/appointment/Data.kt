package com.jc.promise_keeper.data.model.appointment


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("appointment")
    val appointment: Appointment?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("token")
    val token: String,
    @SerializedName("places")
    val places: List<PlaceData>
)