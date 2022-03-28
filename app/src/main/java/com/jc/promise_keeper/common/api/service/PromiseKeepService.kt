package com.jc.promise_keeper.common.api.service

import com.jc.promise_keeper.data.model.basic_response.BasicResponse
import com.jc.promise_keeper.data.model.datas.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface PromiseKeepService {

    //region * User
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

    @GET("/user")
    suspend fun getMyInfo(): Response<BasicResponse>

    @FormUrlEncoded
    @POST("/user/social")
    suspend fun postSocialLogin(
        @Field("provider") provider: String,
        @Field("uid") uid: String,
        @Field("nick_name") nickname: String
    ): Response<BasicResponse>

    //endregion

    //region * place
    @FormUrlEncoded
    @POST("/user/place")
    suspend fun postRequestFrequentlyPlace(
//        @Header("X-Http-Token") token: String,
        @Field("name") name: String,
        @Field("latitude") lat: Long,
        @Field("longitude") lng: Long,
        @Field("is_primary") isPrimary: Boolean
    ): Response<BasicResponse>

    @GET("/user/place")
    suspend fun getRequestFrequentlyPlaceList(): Response<BasicResponse>

    @FormUrlEncoded
    @POST("/user/place")
    suspend fun postRequestAddMyPlace(
        @Field("name") name: String,
        @Field("latitude") lat: Double,
        @Field("longitude") lng: Double,
        @Field("is_primary") isPrimary: Boolean
    ): Response<BasicResponse>

    @GET("/user/place")
    suspend fun getRequestMyPlaceList(): Response<BasicResponse>

    @DELETE("/user/place")
    suspend fun deleteRequestPlace(
        @Query("place_id") id: Int
    ): Response<BasicResponse>

    //endregion


    //region * appointment
    @FormUrlEncoded
    @POST("/appointment")
    suspend fun postRequestAddAppointment(
        @Field("title") title: String,
        @Field("datetime") dateTime: String,
        @Field("start_place") startPlace: String,
        @Field("start_latitude") startLatitude: Double,
        @Field("start_longitude") startLongitude: Double,
        @Field("place") place: String,
        @Field("latitude") lat: Double,
        @Field("longitude") lng: Double
    ): Response<BasicResponse>

    @GET("/appointment")
    fun getRequestAppointmentList(): Call<BasicResponse>

    @DELETE("/appointment")
    suspend fun deleteRequestAppointment(
        @Query("appointment_id") id: Int
    ): Response<BasicResponse>
    //endregion

    //region * friend
    @GET("/user/friend")
    suspend fun getRequestFriendList(
        @Query("type") type: String
    ): Response<BasicResponse>


    @FormUrlEncoded
    @PUT("/user/friend")
    suspend fun putRequestAcceptOrDenyFriend(
        @Field("user_id") userId: Int,
        @Field("type") type: String
    ): Response<BasicResponse>

    @GET("/search/user")
    suspend fun getRequestSearchUser(
        @Query("nickname") nickname: String
    ): Response<BasicResponse>

    @FormUrlEncoded
    @POST("/user/friend")
    suspend fun postRequestAddFriend(
        @Field("user_id") userId: Int
    ): Response<BasicResponse>

    @Multipart // 파일을 관장하는 애노테이션
    @PUT("/user/image")
    suspend fun putRequestSetProfile(
        @Part image: MultipartBody.Part
    ): Response<BasicResponse>


    @FormUrlEncoded
    @PATCH("/user/password")
    suspend fun patchRequestChangePassword(
        @Field("current_password") currentPw: String,
        @Field("new_password") newPw: String
    ): Response<BasicResponse>

    //endregion

}