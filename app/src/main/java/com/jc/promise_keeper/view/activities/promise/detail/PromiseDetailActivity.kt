package com.jc.promise_keeper.view.activities.promise.detail

import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.AppointmentRepository
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.data.model.datas.Appointment
import com.jc.promise_keeper.databinding.ActivityPromiseDetailBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.Marker
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat

class PromiseDetailActivity :
    BaseAppCompatActivity<ActivityPromiseDetailBinding>(R.layout.activity_promise_detail) {

    lateinit var mAppointment: Appointment
    private val scope = MainScope()

    // 출발지 ~ 도착지 사이의 정거장이 있다면 정거장들을 좌표로 추가
    private val stationList = ArrayList<LatLng>()
    lateinit var jsonObjectData: JSONObject


    override fun ActivityPromiseDetailBinding.onCreate() {
        mAppointment = intent.getSerializableExtra("appointment") as Appointment
        jsonObjectData = JSONObject()
        initViews()
        setEvents()
    }


    override fun initViews() {
        super.initViews()
        txtTitle.text = "약속장소 확인"
        showMap()

    }

    override fun setEvents() {
        super.setEvents()

        binding.deleteAppointment.setOnClickListener {
            deleteDialog(mAppointment.id!!)
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


    private fun deleteRequestAppointment(id: Int) = scope.launch {

        val result = AppointmentRepository.deleteRequestAppointment(id)

        if (result.isSuccessful) {

            val code = result.body()?.code

            if (code == 200) {
                Toast.makeText(mContext, "삭제했습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }


        } else {
            Toast.makeText(mContext, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showMap() {

        binding.naverMapView.getMapAsync { naverMap ->

            // 도착지 좌표
            val destLatLng = LatLng(mAppointment.latitude!!, mAppointment.longitude!!)
            val appointmentTitle = mAppointment.title
            val appointmentDate = mAppointment.datetime
            val appointmentPlace = mAppointment.place
            val appointmentStartPlace = mAppointment.startPlace

            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
            val formatDate = sdf.format(appointmentDate)

            val splitTime = formatDate.split(" ")
            val date = splitTime[0]
            val time = splitTime[1]

            with(binding) {

                promiseTitleTextView.text = appointmentTitle
                promiseDateTextView.text = date
                promiseTimeTextView.text = time
                promisePlaceTextView.text = appointmentPlace
                promiseStartPlaceTextView.text = appointmentStartPlace

                updateAppointment.setOnClickListener {

                }



            }



//            Log.d("TAG", "showMap: ${mAppointment}")
            Log.d("TAG", "showMap: ${formatDate}")
            Log.d("TAG", "showMap: ${splitTime}")
            Log.d("TAG", "showMap: ${date}")
            Log.d("TAG", "showMap: ${time}")

            // 도착지 카메라 이동
            val cameraUpdate = CameraUpdate.scrollTo(destLatLng)
            naverMap.moveCamera(cameraUpdate)

            // 마커.
            val marker = Marker()
            marker.position = destLatLng
            marker.map = naverMap
//
//            // 첫 좌표는 출발 장소
//            stationList.add(LatLng(mAppointment.startLatitude!!, mAppointment.startLongitude!!))
//
//
//            val obSayService = ODsayService.init(mContext, BuildConfig.ODSAY_API_KEY)
//
//            obSayService.requestSearchPubTransPath(
//                mAppointment.startLongitude.toString(),
//                mAppointment.startLatitude.toString(),
//                mAppointment.longitude.toString(),
//                mAppointment.latitude.toString(),
//                null,
//                null,
//                null, object : OnResultCallbackListener {
//                    override fun onSuccess(data: ODsayData?, p1: API?) {
//
//                        val jsonObject = data!!.json!!
//
//                        val resultObj = jsonObject.getJSONObject("result")
//                        val pathArr = resultObj.getJSONArray("path")
//                        val firstPathObj = pathArr.getJSONObject(0)
//                        val subPathObj = firstPathObj.getJSONObject("subPath")
//
//
//
//                    }
//
//
//                    override fun onError(p0: Int, p1: String?, p2: API?) {
//                        if (p2 == API.SEARCH_PUB_TRANS_PATH) {
//                        }
//                    }
//
//                })
//
        }


    }

}