package com.jc.promise_keeper.common.api.repository

import com.jc.promise_keeper.common.api.Connect
import com.jc.promise_keeper.data.model.basic_response.BasicResponse
import retrofit2.Response

object PlaceRepository {

    // 기본 장소 등록
    suspend fun postRequestFrequentlyPlace(
        name: String,
        lat: Long,
        lng: Long,
        isPrimary: Boolean
    ): Response<BasicResponse> =
        Connect.promiseKeepService.postRequestFrequentlyPlace(name, lat, lng, isPrimary)

    suspend fun getRequestFrequentlyPlaceList(): Response<BasicResponse> =
        Connect.promiseKeepService.getRequestFrequentlyPlaceList()


}