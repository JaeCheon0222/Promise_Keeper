package com.jc.promise_keeper.view.activities.map

import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.data.model.datas.Appointment
import com.jc.promise_keeper.databinding.ActivityPromisePlaceMapViewBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.Marker

class PromisePlaceMapViewActivity : BaseAppCompatActivity<ActivityPromisePlaceMapViewBinding>(R.layout.activity_promise_place_map_view) {

    lateinit var mAppointment: Appointment

    override fun ActivityPromisePlaceMapViewBinding.onCreate() {

        mAppointment = intent.getSerializableExtra("appointment") as Appointment

    }

    override fun initViews() {
        super.initViews()

        txtTitle.text = "약속장소 지도 확인"
        showMap()


    }

    private fun showMap() {

        binding.naverMapView.getMapAsync { naverMap ->

            // 도착지
            val destLatLng = LatLng(mAppointment.latitude!!, mAppointment.longitude!!)

            // 도착지 카메라 이동
            val cameraUpdate = CameraUpdate.scrollTo(destLatLng)
            naverMap.moveCamera(cameraUpdate)

            // 마커.
            val marker = Marker()
            marker.position = destLatLng
            marker.map = naverMap

            // 출발지 ~ 도착지 사이의 정거장이 있다면 정거장들을 좌표로 추가

            val stationList = ArrayList<LatLng>()

            // 첫 좌표는 출발 장소
            stationList.add(LatLng(mAppointment.startLatitude!!, mAppointment.startLongitude!!))

        }

    }
}