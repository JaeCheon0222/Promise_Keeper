package com.jc.promise_keeper.data.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    val id: Int,
    val provider: String,
    val uid: String,
    val email: String,
    @SerializedName("ready_minute")
    val readyMinute: String,
    @SerializedName("nick_name")
    val nickname: String,
    @SerializedName("app_maker")
    val appMaker: String,
    @SerializedName("profile_img")
    val profileImg: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String

)
