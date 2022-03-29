package com.jc.promise_keeper.common.api.repository

import android.util.Log
import android.widget.Toast
import com.jc.promise_keeper.common.api.Connect
import com.jc.promise_keeper.data.model.basic_response.BasicResponse
import retrofit2.Response
import retrofit2.http.Field

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

    suspend fun putRequestUpdateAppointment(
        appointmentId: Int,
        title: String,
        dateTime: String,
        startPlace: String,
        startLatitude: Double,
        startLongitude: Double,
        place: String,
        lat: Double,
        lng: Double
    ): Boolean {

        val result = Connect.promiseKeepService.putRequestUpdateAppointment(
            appointmentId,
            title,
            dateTime,
            startPlace,
            startLatitude,
            startLongitude,
            place,
            lat,
            lng
        )

        Log.d("TAG", "putRequestUpdateAppointment: ${result.body()?.data?.appointment?.id}")


        return result.isSuccessful

    }

    suspend fun deleteRequestAppointment(id: Int): Response<BasicResponse> =
        Connect.promiseKeepService.deleteRequestAppointment(id)

}