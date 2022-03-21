package com.jc.promise_keeper.activities.promise.add

import android.annotation.SuppressLint
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.util.UtilityBase
import com.jc.promise_keeper.databinding.ActivityAddPromiseBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate

class AddPromiseActivity : UtilityBase.BaseAppCompatActivity<ActivityAddPromiseBinding>(R.layout.activity_add_promise) {

    override fun ActivityAddPromiseBinding.onCreate() {

        initViews()
        setEvents()

    }

    override fun initViews() {
        super.initViews()
        initMap()
    }


    override fun setEvents() {
        super.setEvents()
        touchScroll()
    }


    private fun initMap() {

        binding.naverMap.getMapAsync { naverMap ->

            val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.5666102, 126.9783881))
            naverMap.moveCamera(cameraUpdate)

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

}