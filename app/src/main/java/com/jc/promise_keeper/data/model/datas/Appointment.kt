package com.jc.promise_keeper.data.model.datas


import com.google.gson.annotations.SerializedName

data class Appointment(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("datetime")
    val datetime: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("invited_friends")
    val invitedFriends: List<InvitedFriend>?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("place")
    val place: String?,
    @SerializedName("start_latitude")
    val startLatitude: Double?,
    @SerializedName("start_longitude")
    val startLongitude: Double?,
    @SerializedName("start_place")
    val startPlace: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("user_id")
    val userId: Int?
)