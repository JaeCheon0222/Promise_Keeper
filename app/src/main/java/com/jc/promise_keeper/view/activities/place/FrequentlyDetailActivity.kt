package com.jc.promise_keeper.view.activities.place

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.PlaceRepository
import com.jc.promise_keeper.common.util.Keys
import com.jc.promise_keeper.common.util.REQ_RES_CODE
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.data.model.datas.PlaceData
import com.jc.promise_keeper.databinding.ActivityFrequentlyDetailBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FrequentlyDetailActivity :
    BaseAppCompatActivity<ActivityFrequentlyDetailBinding>(R.layout.activity_frequently_detail) {

    private val scope = MainScope()
    private lateinit var placeData: PlaceData

    override fun ActivityFrequentlyDetailBinding.onCreate() {

        placeData = intent.getSerializableExtra(Keys.FREQUENTLY) as PlaceData

        initViews()
        setEvents()
    }

    override fun initViews() {
        super.initViews()



        binding.placeName.text = placeData.name

        binding.naverMap.getMapAsync { _naverMap ->

            val marker = Marker()

            // 지도 시작지점 : 내 선택된 출발 지점
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

        }

    }

    override fun setEvents() {
        super.setEvents()

        binding.updatePlace.setOnClickListener {

//            Log.d("TAG", "setEvents: ${data}")

            val intent = Intent(mContext, FrequentlyUpdateActivity::class.java).apply {
                putExtra(Keys.FREQUENTLY_UPDATE, placeData)
            }
            startActivity(intent)

        }


        binding.deletePlace.setOnClickListener {
            deleteDialog(placeData.id)
        }


    }

    private fun deleteDialog(id: Int) {

        AlertDialog.Builder(mContext)
            .setTitle("약속 삭제하기")
            .setMessage("해당 약속을 삭제하시겠습니까?")
            .setPositiveButton("삭제", DialogInterface.OnClickListener { _, _ ->
                deleteRequestAppointment(id)
            })
            .setNegativeButton("취소", null)
            .show()

    }

    private fun deleteRequestAppointment(id: Int) {
        scope.launch {
            val result = PlaceRepository.deleteRequestPlace(id)

            if (result.isSuccessful) {

                val code = result.body()?.code

                if (code == REQ_RES_CODE.SUCCESS) {
                    Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }

            } else {
                Toast.makeText(mContext, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
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
        initViews()
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