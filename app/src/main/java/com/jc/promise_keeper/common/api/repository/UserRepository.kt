package com.jc.promise_keeper.common.api.repository

import com.jc.promise_keeper.common.api.Connect
import com.jc.promise_keeper.data.model.basic_response.BasicResponse
import retrofit2.Response

object UserRepository {

    suspend fun postRequestUserSignIn(email: String, pw: String): Response<BasicResponse> =
        Connect.promiseKeepService.postRequestUserSignIn(email, pw)

    suspend fun getRequestDuplicateEmailAndNickname(type: String, value: String): Boolean {
        val result = Connect.promiseKeepService.getRequestDuplicateEmailAndNickname(type, value)
        return result.isSuccessful
    }


    suspend fun putRequestUserSignUp(email: String, pw: String, nickname: String): Response<BasicResponse> =
        Connect.promiseKeepService.putRequestUserSignUp(email, pw, nickname)

}