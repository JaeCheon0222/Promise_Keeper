package com.jc.promise_keeper.data.model

data class BasicResponse(
    val code: Int,
    val message: String,
    val data: UserModel,
    val token: String
)
