package com.jc.promise_keeper.data.model.basic_response

import com.google.gson.annotations.SerializedName
import com.jc.promise_keeper.data.model.datas.Data

data class BasicResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val data: Data?,
    @SerializedName("message")
    val message: String?
)
