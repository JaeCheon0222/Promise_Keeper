package com.jc.promise_keeper.view.activities.place

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.PlaceRepository
import com.jc.promise_keeper.common.util.Keys
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.data.model.datas.PlaceData
import com.jc.promise_keeper.databinding.ActivityFrequentlyUpdateBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FrequentlyUpdateActivity : BaseAppCompatActivity<ActivityFrequentlyUpdateBinding>(R.layout.activity_frequently_update) {

    private lateinit var placeData: PlaceData
    private lateinit var marker: Marker
    private lateinit var selectLatLng: LatLng

    private val scope = MainScope()

    override fun ActivityFrequentlyUpdateBinding.onCreate() {
        placeData = intent.getSerializableExtra(Keys.FREQUENTLY_UPDATE) as PlaceData

        initViews()
        setEvents()

    }

    override fun initViews() {
        super.initViews()

        with(binding) {

            updatePlaceEditText.setText(placeData.name)

            naverMap.getMapAsync { _naverMap ->

                marker = Marker()


                val coord = LatLng(placeData.latitude, placeData.longitude)

                marker.apply {
                    position = coord
                    icon = OverlayImage.fromResource(R.drawable.start_marker)
                    map = _naverMap
                    width = 150
                    height = 150
                }

                // coord 에 설정한 좌표로 > 네이버지도의 카메라 이동
                val cameraUpdate = CameraUpdate.scrollTo(coord)
                _naverMap.moveCamera(cameraUpdate)

                _naverMap.setOnMapClickListener { pointF, latLng ->

                    marker.map = null

                    marker.apply {
                        position = latLng
                        icon = OverlayImage.fromResource(R.drawable.start_marker)
                        map = _naverMap
                        width = 150
                        height = 150
                    }

                    selectLatLng = LatLng(latLng.latitude, latLng.longitude)

                }

            }

        }

    }

    override fun setEvents() = with(binding) {
        super.setEvents()

        placeUpdateButton.setOnClickListener {

            Log.d("TAG", "setEvents: ${placeData.id}")

            val placeName = updatePlaceEditText.text.toString()

            if (placeName.isEmpty()) {
                showToast("등록할 장소 이름을 입력해주세요.")
                return@setOnClickListener
            }

            if (selectLatLng == null) {
                showToast("장소를 선택해주세요")
                return@setOnClickListener
            }

            patchPlace(placeData.id)

        }

    }

    private fun patchPlace(placeId: Int) = scope.launch {

        val result = PlaceRepository.patchRequestPlace(placeId)

        if (result.isSuccessful) {
            finish()
        } else {
            showToast("수정에 실패했습니다.")
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