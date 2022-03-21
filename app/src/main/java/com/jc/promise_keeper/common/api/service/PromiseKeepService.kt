package com.jc.promise_keeper.common.api.service

import com.jc.promise_keeper.data.model.BasicResponse
import retrofit2.Response
import retrofit2.http.*

interface PromiseKeepService {

    @FormUrlEncoded
    @POST("/user")
    suspend fun postRequestUserSignIn(
        @Field("email") email: String,
        @Field("password") pw: String
    ): Response<BasicResponse>

    @GET("/user/check")
    suspend fun getRequestDuplicateEmailAndNickname(
        @Query("type") type: String,
        @Query("value") value: String
    ): Response<BasicResponse>

    @FormUrlEncoded
    @PUT("/user")
    suspend fun putRequestUserSignUp(
        @Field("email") email: String,
        @Field("password") pw: String,
        @Field("nick_name") nickname: String
    ): Response<BasicResponse>



}