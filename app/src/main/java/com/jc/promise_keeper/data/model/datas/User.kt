package com.jc.promise_keeper.data.model.datas


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("app_maker")
    val appMaker: String?,
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
    val uid: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
) : Serializable