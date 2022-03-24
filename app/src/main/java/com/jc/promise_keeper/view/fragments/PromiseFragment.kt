package com.jc.promise_keeper.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.jc.promise_keeper.R
import com.jc.promise_keeper.adapter.AppointmentRecyclerAdapter
import com.jc.promise_keeper.common.api.Connect
import com.jc.promise_keeper.common.api.repository.PlaceRepository
import com.jc.promise_keeper.common.util.base_view.BaseFragment
import com.jc.promise_keeper.data.model.basic_response.BasicResponse
import com.jc.promise_keeper.data.model.datas.Appointment
import com.jc.promise_keeper.databinding.FragmentPromiseBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromiseFragment: BaseFragment<FragmentPromiseBinding>(R.layout.fragment_promise) {

    lateinit var appointmentRecyclerAdapter: AppointmentRecyclerAdapter
    private val appointmentList = ArrayList<Appointment>()

    private val scope = MainScope()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()

    }

    override fun initViews() {
        super.initViews()
        Log.d("appointmentList", "initViews: ${appointmentList}")
        appointmentRecyclerAdapter = AppointmentRecyclerAdapter(mContext, appointmentList)
        binding.appointmentRecyclerView.adapter = appointmentRecyclerAdapter
        binding.appointmentRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    private fun getMyAppointmentList(){


        Connect.promiseKeepService.getRequestAppointmentList().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    // 기존의 약속목록을 비우고 나서 추가
                    appointmentList.clear()
                    val br = response.body()!!
                    appointmentList.addAll(br.data?.appointments!!)
                    appointmentRecyclerAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                call.cancel()
            }

        })

    }

    // invoke Error 추후 찾아봐야됨.
//    private fun getMyAppointmentList() = scope.launch {
//
//        val result = PlaceRepository.getRequestMyPlaceList()
//
//        if (result.isSuccessful) {
//
//            appointmentList.clear()
//            val body = result.body()!!
//            appointmentList.addAll(body.data?.appointments!!)
////            appointmentRecyclerAdapter.notifyDataSetChanged()
//
//        }
//
//    }


    override fun onResume() {
        super.onResume()
        getMyAppointmentList()
    }


}