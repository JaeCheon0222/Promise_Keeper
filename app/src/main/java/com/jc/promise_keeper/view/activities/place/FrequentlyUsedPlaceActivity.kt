package com.jc.promise_keeper.view.activities.place

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.PlaceRepository
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.databinding.ActivityFrequentlyUsedPlaceBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


/**
 * 자주 사용하는 장소 등록
 * */
class FrequentlyUsedPlaceActivity :
    BaseAppCompatActivity<ActivityFrequentlyUsedPlaceBinding>(R.layout.activity_frequently_used_place) {

    var mSelectedPoint: LatLng? = null
    var marker: Marker? = null
    val scope = MainScope()

    override fun ActivityFrequentlyUsedPlaceBinding.onCreate() {

        initViews()
        setEvents()
    }

    override fun initViews() {
        super.initViews()
        addPlace()
    }

    override fun setEvents() = with(binding) {
        super.setEvents()

        placeAddButton.setOnClickListener {

            Log.d("TAG", "setEvents: ")


            val placeName = addPlaceEditText.text.toString()
            val isBasicPlace = addBasicPlaceCheckBox.isChecked

            if (placeName.isEmpty()) {
                Toast.makeText(mContext, "등록할 장소 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            postRequestFrequentlyPlace(
                placeName,
                mSelectedPoint!!.latitude,
                mSelectedPoint!!.longitude,
                isBasicPlace
            )

        }

    }

    private fun addPlace() {

        binding.naverMap.getMapAsync { naverMap ->

            naverMap.setOnMapClickListener { pointF, latLng ->

                if (mSelectedPoint == null) {
                    // 처음으로 지도를 클릭 한 상황
                    // 마커를 새로 만든다. => 위치 정보: latLng 변수가 대입을 하게 된다. 새로 만들 필요는 없다.
                    marker = Marker()
//                    marker!!.icon = OverlayImage.fromResource(R.drawable.red_marker_icon)
                }

                mSelectedPoint = latLng
                marker!!.position = latLng
                marker!!.map = naverMap

            }
        }
    }


    private fun postRequestFrequentlyPlace(
        name: String,
        lat: Double,
        lng: Double,
        isPrimary: Boolean
    ) = scope.launch {
//        UserRepository.postRequestFrequentlyPlace()

        val result = PlaceRepository.postRequestAddMyPlace(name, lat, lng, isPrimary)

        if (result.isSuccessful) {
            Toast.makeText(mContext, "장소가 등록되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

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