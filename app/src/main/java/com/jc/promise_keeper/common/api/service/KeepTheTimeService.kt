package com.jc.promise_keeper.common.api.service

import com.jc.promise_keeper.data.model.BasicResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface KeepTheTimeService {

    @FormUrlEncoded
    @POST("/user")
    suspend fun postRequestUserSignUp(
        @Field("email") email: String,
        @Field("password") pw: String
    ): Response<BasicResponse>

}