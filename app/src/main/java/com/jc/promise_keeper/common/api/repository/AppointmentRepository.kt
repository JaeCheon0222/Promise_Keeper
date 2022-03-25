package com.jc.promise_keeper.common.api.repository

import com.jc.promise_keeper.common.api.Connect
import com.jc.promise_keeper.data.model.basic_response.BasicResponse
import retrofit2.Response

object AppointmentRepository {

    suspend fun postRequestAddAppointment(
        title: String,
        dateTime: String,
        startPlace: String,
        startLatitude: Double,
        startLongitude: Double,
        place: String,
        lat: Double,
        lng: Double
    ): Response<BasicResponse> =
        Connect.promiseKeepService.postRequestAddAppointment(
            title,
            dateTime,
            startPlace,
            startLatitude,
            startLongitude,
            place,
            lat,
            lng
        )

    suspend fun deleteRequestAppointment(id: Int): Response<BasicResponse> =
        Connect.promiseKeepService.deleteRequestAppointment(id)

}