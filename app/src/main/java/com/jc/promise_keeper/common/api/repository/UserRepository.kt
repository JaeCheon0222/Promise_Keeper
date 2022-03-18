package com.jc.promise_keeper.common.api.repository

import com.jc.promise_keeper.common.api.Connect
import com.jc.promise_keeper.data.model.BasicResponse
import retrofit2.Response

object UserRepository {

    suspend fun postRequestUserSignUp(email: String, pw: String): Response<BasicResponse> =
        Connect.keepTheTimeService.postRequestUserSignUp(email, pw)

}