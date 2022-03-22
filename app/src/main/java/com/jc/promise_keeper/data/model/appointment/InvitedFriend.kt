package com.jc.promise_keeper.data.model.appointment


import com.google.gson.annotations.SerializedName

data class InvitedFriend(
    @SerializedName("app_maker")
    val appMaker: Any?,
    @SerializedName("arrived_at")
    val arrivedAt: Any?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nick_name")
    val nickName: String?,
    @SerializedName("profile_img")
    val profileImg: String?,
    @SerializedName("provider")
    val provider: String?,
    @SerializedName("ready_minute")
    val readyMinute: Int?,
    @SerializedName("uid")
    val uid: Any?,
    @SerializedName("updated_at")
    val updatedAt: String?
)