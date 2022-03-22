package com.jc.promise_keeper.view.activities.promise.add

import android.annotation.SuppressLint
import android.os.Bundle
import com.jc.promise_keeper.R
import com.jc.promise_keeper.adapter.StartPlaceSpinnerAdapter
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.databinding.ActivityAddPromiseBinding

class AddPromiseActivity : BaseAppCompatActivity<ActivityAddPromiseBinding>(R.layout.activity_add_promise) {

    private lateinit var startPlaceSpinnerAdapter: StartPlaceSpinnerAdapter

    override fun ActivityAddPromiseBinding.onCreate() {

        initViews()
        setEvents()

    }

    override fun initViews() {
        super.initViews()

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



}