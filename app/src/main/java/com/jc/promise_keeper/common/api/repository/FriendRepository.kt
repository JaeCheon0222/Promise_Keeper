package com.jc.promise_keeper.common.api.repository

import com.jc.promise_keeper.common.api.Connect
import com.jc.promise_keeper.data.model.basic_response.BasicResponse
import retrofit2.Response

object FriendRepository {

    suspend fun getRequestFriendList(type: String): Response<BasicResponse> =
        Connect.promiseKeepService.getRequestFriendList(type)

}