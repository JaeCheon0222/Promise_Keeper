package com.jc.promise_keeper.view.activities.promise.add

import android.annotation.SuppressLint
import android.os.Bundle
import com.jc.promise_keeper.R
import com.jc.promise_keeper.adapter.StartPlaceSpinnerAdapter
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.data.model.appointment.PlaceData
import com.jc.promise_keeper.databinding.ActivityAddPromiseBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import java.util.*

class AddPromiseActivity : BaseAppCompatActivity<ActivityAddPromiseBinding>(R.layout.activity_add_promise) {

    private lateinit var startPlaceSpinnerAdapter: StartPlaceSpinnerAdapter

    // 약속 시간 일/시 를 저장해줄 Calendar (월 값이 0 ~ 11 로 움직이게 맞춰져있다.)
    val mSelectedAppointmentDateTime = Calendar.getInstance() // 기본 값: 현재 일시

    // 약속 장소 관련 멤버변수
    var marker: Marker? = null                   // 지도에 표시도리 하나의 마커, 처음에는 찍지 않은 상태
    var mSelectedLatLng: LatLng? = null         // 약속 장소 위/경도도 처음에는 설정하지 않은 상태

    var mSelectedStartPlace: PlaceData? = null
    var naverMap: MapView? = null

    override fun ActivityAddPromiseBinding.onCreate() {


        initViews()
        setEvents()

    }

    @SuppressLint("MissingPermission")
    override fun initViews() {
        super.initViews()
        setNaverMapView(getNaverMapView())
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? ->
//                Log.d("TAG", "initViews: ${location?.latitude}, ${location?.longitude}")
//
//                scope.launch {
//
//                    LocationRepository.getNearByMonitoringStation(location?.latitude!!, location?.longitude!!)
//
//
//                }
//
//
//
//            }


//        startPlaceSpinnerAdapter = StartPlaceSpinnerAdapter(
//            mContext,
//            R.layout.start_place_spinner_item,
//            null //mStartPlaceList
//        )
//        binding.startPlaceSpinner.adapter = startPlaceSpinnerAdapter

    }


    override fun setEvents() {
        super.setEvents()
        touchScroll()
        OnClickView(binding).run {
            getPromiseDate()
            getPromiseTime()
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun touchScroll() {
        // 스크롤보조용 텍스트뷰를 손이 닿으면 => 스크롤뷰의 이벤트 일시 정지 (지도만 움직이게)
        binding.txtScrollHelp.setOnTouchListener { view, motionEvent ->

            // 스크롤뷰의 이벤트 정지
            // Disallow -> 허가 하지 않겠다.
            binding.scrollView.requestDisallowInterceptTouchEvent(true)

            // 텍스트뷰의 터치 이벤트만 실행 할 것인지 => 뒤에 가려져있는 지도도 터치를 허용해줘야한다.
            return@setOnTouchListener false

        }
    }

    private fun getNaverMapView(): MapView {
        if (naverMap == null) {
            naverMap = binding.naverMap
        }
        return naverMap!!
    }

    private fun setNaverMapView(_naverMap: MapView) {

        _naverMap.getMapAsync { naverMap ->

            val coord = LatLng(37.5849048944, 127.094181789)

            val cameraUpdate = CameraUpdate.scrollTo(coord)
            naverMap.moveCamera(cameraUpdate)
            marker = Marker()
            marker?.run {
                position = coord
                map = naverMap
            }

        }

    }





    //region * Android LifeCycle
    override fun onStart() {
        super.onStart()
        binding.naverMap.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.naverMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.naverMap.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.naverMap.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.naverMap.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.naverMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.naverMap.onLowMemory()
    }
    //endregion


}