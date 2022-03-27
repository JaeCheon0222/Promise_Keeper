package com.jc.promise_keeper.view.activities.promise.detail

import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.jc.promise_keeper.BuildConfig
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.AppointmentRepository
import com.jc.promise_keeper.common.util.LogLongContentPrint
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.data.model.datas.Appointment
import com.jc.promise_keeper.databinding.ActivityPromiseDetailBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
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
    private var path: PathOverlay? = null                // 출발지 ~ 도착지 까지 보여줄 경로선. 처음에는 보이지 않는 상태
    private var destinationMarker: Marker? = null
    private var startMarker: Marker? = null


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

            // 출발지 좌표
            stationList.add(LatLng(mAppointment.startLatitude!!, mAppointment.startLongitude!!))
            // 도착지 좌표
            stationList.add(LatLng(mAppointment.latitude!!, mAppointment.longitude!!))

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


            // 도착지 카메라 이동
            val cameraUpdate = CameraUpdate.scrollTo(stationList.last())
            naverMap.moveCamera(cameraUpdate)

            // 마커.
            if (startMarker == null) {
                startMarker = Marker()
            }

            if (destinationMarker == null) {
                destinationMarker = Marker()
            }

            if (path == null) {
                path = PathOverlay()
            }

            startMarker?.apply {

                position = stationList.first()
                icon = OverlayImage.fromResource(R.drawable.start_marker)
                width = 150
                height = 150
                map = naverMap

            }


            destinationMarker?.apply {

                position = stationList.last()
                icon = OverlayImage.fromResource(R.drawable.destination_marker)
                width = 150
                height = 150
                map = naverMap

            }

//            val coordList = ArrayList<LatLng>()
//
//            coordList.add(stationList.first())    // 출발지
//            coordList.add(stationList.last())   // 클릭 된 좌표 추가
//
//            path!!.coords = coordList
//
//            path!!.map = naverMap


            pathAway(naverMap)

        }

    }

    private fun pathAway(naverMap: NaverMap) {


        val obSayService = ODsayService.init(mContext, BuildConfig.ODSAY_API_KEY)

        obSayService.requestSearchPubTransPath(
            mAppointment.startLongitude.toString(),
            mAppointment.startLatitude.toString(),
            mAppointment.longitude.toString(),
            mAppointment.latitude.toString(),
            null,
            null,
            null, object : OnResultCallbackListener {
                override fun onSuccess(data: ODsayData?, p1: API?) {

                    val jsonObject = data!!.json!!

                    val resultObj = jsonObject.getJSONObject("result")

//                    LogLongContentPrint.logLineBreak(resultObj.toString())

                    val pathArr = resultObj.getJSONArray("path")
                    val firstPathObj = pathArr.getJSONObject(0)

                    val startToDestinationPath = ArrayList<LatLng>()

                    startToDestinationPath.add(
                        LatLng(
                            stationList.first().latitude,
                            stationList.first().longitude
                        )
                    )

                    Log.d("TAG", "start: ${startToDestinationPath}")

                    val subPathArr = firstPathObj.getJSONArray("subPath")

                    for (i in 0 until subPathArr.length()) {

                        val subPathObj = subPathArr.getJSONObject(i)

                        // 둘러보려는 경로가, 정거장 목록을 내려준다면 (지하철 or 버스) => 내부 파싱
                        if (!subPathObj.isNull("passStopList")) {

                            val passStopListObj = subPathObj.getJSONObject("passStopList")
                            val stationsArr = passStopListObj.getJSONArray("stations")

                            // 실제 정거장 목록 파싱 => 각 정거장의 위도/경도 추출 가능 => ArrayList 에 담아서, 경로선의 좌표로 활용.
                            for (j in 0 until stationsArr.length()) {

                                val stationObj = stationsArr.getJSONObject(j)

                                // 위도 (y 좌표), 경도 (x 좌표)
                                val lat = stationObj.getString("y").toDouble()
                                val lng = stationObj.getString("x").toDouble()
                                Log.d("TAG", "x, y : ${lat}, $lng")
                                // 네이버 지도의 좌표로 만들어서 > ArrayList 에 담기
                                startToDestinationPath.add(LatLng(lat, lng))

                            }

                        }

                        Log.d("TAG", "result: ${startToDestinationPath}")

                        // (첫 번째 추천 경로의) 정보 항목도 파싱.
                        // 예상 소요 시간 파싱 => 임시로 토스트 출력.

                        val infoObj = firstPathObj.getJSONObject("info")

                        val totalTime = infoObj.getInt("totalTime")     // 소요 분

                        val payment = infoObj.getInt("payment")         // 소요 비용


                        // 네이버 지도 라이브러리의 InfoWindow 기능 활용.
                        val infoWindow = InfoWindow()
                        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(mContext) {
                            override fun getText(p0: InfoWindow): CharSequence {
                                return "이동시간 : ${totalTime}분, 비용 : ${payment}원"
                            }

                        }
                        infoWindow.open(destinationMarker!!)

                        destinationMarker!!.setOnClickListener {

                            if (destinationMarker!!.infoWindow == null) {
                                infoWindow.open(destinationMarker!!)
                            } else {
                                infoWindow.close()
                            }

                            return@setOnClickListener true
                        }


                    }

                    // 완성된 정거장 경로들을 => path 의 경로로 재설정. 지도에 새로 반영
//                    naverMap.maxZoom = 18.0
//                    naverMap.extent = LatLngBounds(startToDestinationPath.first(), startToDestinationPath.last())
                    path!!.coords = startToDestinationPath
//                    path!!.patternImage = OverlayImage.fromResource(R.drawable.path_pattern)
                    path!!.patternInterval = 10             // 패턴이 0일 경우 그려지지 않음
                    path!!.map = naverMap


                }

                override fun onError(p0: Int, p1: String?, p2: API?) {
                    if (p2 == API.SEARCH_PUB_TRANS_PATH) {
                    }
                }

            })

    }


}