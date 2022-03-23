package com.jc.promise_keeper.common.api.repository

import com.jc.promise_keeper.common.api.Connect
import com.jc.promise_keeper.data.model.basic_response.BasicResponse
import retrofit2.Response

object FriendRepository {

    suspend fun getRequestFriendList(type: String): Response<BasicResponse> =
        Connect.promiseKeepService.getRequestFriendList(type)

    suspend fun putRequestAcceptOrDenyFriend(userId: Int, type: String): Response<BasicResponse> =
        Connect.promiseKeepService.putRequestAcceptOrDenyFriend(userId, type)

    suspend fun getRequestSearchUser(nickname: String): Response<BasicResponse> =
        Connect.promiseKeepService.getRequestSearchUser(nickname)

    suspend fun postRequestAddFriend(userId: Int): Response<BasicResponse> =
        Connect.promiseKeepService.postRequestAddFriend(userId)

}