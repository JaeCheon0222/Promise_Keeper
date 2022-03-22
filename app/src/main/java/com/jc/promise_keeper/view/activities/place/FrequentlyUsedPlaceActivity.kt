package com.jc.promise_keeper.view.activities.place

import android.os.Bundle
import android.widget.Toast
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.util.UtilityBase
import com.jc.promise_keeper.databinding.ActivityFrequentlyUsedPlaceBinding


/**
 * 자주 사용하는 장소 등록
 * */
class FrequentlyUsedPlaceActivity : UtilityBase.BaseAppCompatActivity<ActivityFrequentlyUsedPlaceBinding>(R.layout.activity_frequently_used_place) {

    override fun ActivityFrequentlyUsedPlaceBinding.onCreate() {
        setEvents()
    }

    override fun initViews() {
        super.initViews()
        map.setUpMap()
    }

    override fun setEvents() = with(binding) {
        super.setEvents()

        placeAddButton.setOnClickListener {

            val placeName = addPlaceEditText.text.toString()
            val isBasicPlace = addBasicPlaceCheckBox.isChecked

            if (placeName.isEmpty()) {
                Toast.makeText(mContext, "등록할 장소 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            postRequestFrequentlyPlace()

        }

    }


    private fun postRequestFrequentlyPlace() {
//        UserRepository.postRequestFrequentlyPlace()
    }


    // 맵 액티비티 라이프 사이클
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