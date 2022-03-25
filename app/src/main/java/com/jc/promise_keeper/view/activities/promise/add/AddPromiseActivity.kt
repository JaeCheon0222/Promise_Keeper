package com.jc.promise_keeper.view.activities.promise.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.jc.promise_keeper.R
import com.jc.promise_keeper.adapter.StartPlaceSpinnerAdapter
import com.jc.promise_keeper.common.api.repository.AppointmentRepository
import com.jc.promise_keeper.common.api.repository.PlaceRepository
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.data.model.datas.PlaceData
import com.jc.promise_keeper.databinding.ActivityAddPromiseBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log


class AddPromiseActivity :
    BaseAppCompatActivity<ActivityAddPromiseBinding>(R.layout.activity_add_promise) {

    private val scope = MainScope()

    private lateinit var startPlaceSpinnerAdapter: StartPlaceSpinnerAdapter

    // 약속 시간 일/시 를 저장해줄 Calendar (월 값이 0 ~ 11 로 움직이게 맞춰져있다.)
    private val mSelectedAppointmentDateTime = Calendar.getInstance() // 기본 값: 현재 일시

    // 약속 장소 관련 멤버변수
    var startMarker: Marker? = null
    var marker: Marker? = null                   // 지도에 표시도리 하나의 마커, 처음에는 찍지 않은 상태
    var path: PathOverlay? = null
    var mSelectedLatLng: LatLng? = null         // 약속 장소 위/경도도 처음에는 설정하지 않은 상태
    var coord: LatLng? = null


    var mSelectedStartPlace: PlaceData? = null
    var naverMap: NaverMap? = null

    val mStartPlaceList = ArrayList<PlaceData>()

    override fun ActivityAddPromiseBinding.onCreate() {

        initViews()
        setEvents()

    }

    @SuppressLint("MissingPermission")
    override fun initViews() {
        super.initViews()

        binding.naverMapView.getMapAsync {

            naverMap = it
            setNaverMap()
        }

        getMyStartPlaceList()


        startPlaceSpinnerAdapter = StartPlaceSpinnerAdapter(
            mContext,
            R.layout.start_place_spinner_item,
            mStartPlaceList
        )
        binding.startPlaceSpinner.adapter = startPlaceSpinnerAdapter

    }


    override fun setEvents() {
        super.setEvents()
        touchScroll()
        spinnerEvent()

        getPromiseDate()
        getPromiseTime()

        binding.promiseAddButton.setOnClickListener {
            addAppointmentButton()
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

    private fun spinnerEvent() {

        binding.startPlaceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {

                    // 몇 번째 아이템이 선택 되었는지, p2 or position 이 알려줌
                    mSelectedStartPlace = mStartPlaceList[position]

                    // 네이버 지도보다 로딩이 느릴 수 있다.
                    // 출발 장소도 로딩이 끝나면, 다시 지도 세팅 진행.
                    setNaverMap()

                    // 선택한 출발지 ~ 지도에서 클릭한 도착까지의 이동 경로 / 교통 정보 표현.
//                    findWay()

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

    }

    private fun setNaverMap() {

        if (mSelectedStartPlace == null) {
            return      // 우선 함수 강제 종료.
        }

        if (naverMap == null) {
            return // 이상황도 함수 강제 종료
        }

        // 지도 시작지점 : 내 선택된 출발 지점
        coord = LatLng(mSelectedStartPlace!!.latitude, mSelectedStartPlace!!.longitude)

        if (startMarker == null) {
            startMarker = Marker()
            Log.d("TAG", "setNaverMap: ${startMarker!!.position}")
        }


        startMarker!!.apply {
            position = coord!!
            icon = OverlayImage.fromResource(R.drawable.start_marker)
            map = naverMap
            width = 150
            height = 150
        }

        // coord 에 설정한 좌표로 > 네이버지도의 카메라 이동
        val cameraUpdate = CameraUpdate.scrollTo(coord!!)
        naverMap!!.moveCamera(cameraUpdate)


        Log.d("TAG", "setNaverMap: ${startMarker!!.position}")

        // 지도 클릭 이벤트
        naverMap!!.setOnMapClickListener { pointF, latLng ->
//                Log.d("클릭된 위/경도", "위도 : ${latLng.latitude}, 경도 : ${latLng.longitude}")

            // (찍혀있는 마커가 없다면) 마커를 새로 추가
            if (marker == null) {
                marker = Marker()
            }

            // 그 마커의 위치 / 지도 적용
            marker!!.apply {
                position = latLng
                icon = OverlayImage.fromResource(R.drawable.destination_marker)
                map = naverMap
                width = 150
                height = 150
            }


            // 약속 장소도 새 좌표로 설정
            mSelectedLatLng = latLng

            // coord ~ 선택한 latlng 까지 대중교통 경로를 그려보자 (PathOverlay 기능 활용) + ODSay 라이브러리 활용


//            if (path == null) {
//                path = PathOverlay()
//            }


            // ArrayList 를 만들어서, 출발지와 도착지를 추가
            val coordList = ArrayList<LatLng>()

            coordList.add(coord!!)    // 출발지를 임시로 학원으로
            coordList.add(latLng)   // 클릭 된 좌표 추가

//            path!!.coords = coordList

//            path!!.map = naverMap


        }

    }

    private fun addAppointmentButton() {

        // 입력 값들이 제대로 되어있는지 확인하고, 잘못되었다면 막아주자. (input validation)
        val inputTitle = binding.promiseTitleTextView.text.toString()

        // 입력하지 않았다면 거부 (예시)
        if (inputTitle.isEmpty()) {
            Toast.makeText(mContext, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 장소를 선택했는지 확인, 하지 않았다면 등록 거부
        if (mSelectedLatLng == null) {
            Toast.makeText(mContext, "약속 장소를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 장소 이름도 반드시 입력하게
        val inputPlaceName = binding.promisePlaceTextView.text.toString()
        if (inputPlaceName.isEmpty()) {
            Toast.makeText(mContext, "장소 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        // 시간을 선택하지 않았다면 막자
        // 기준 textData, txtTime 두 개의 문구중 하나라도 처음 문구 그대로면 입력하지 않았다고 간주
        if (binding.promiseDateTextView.text == "약속 일자") {
            Toast.makeText(mContext, "일자를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.promiseTimeTextView.text == "약속 시간") {
            Toast.makeText(mContext, "시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 선택한 일시가, 지금보다 이전의 일시라면 "현재 이후의 시간으로 선택해주세요." 토스트
        val now = Calendar.getInstance()        // 저장 버튼을 누른 현재 시간

        Log.d("선택시간", "위도 : ${mSelectedAppointmentDateTime.timeInMillis}")
        Log.d("선택시간", "위도 : ${mSelectedAppointmentDateTime}")
        Log.d("현재시간", "위도 : ${now.timeInMillis}")

        if (mSelectedAppointmentDateTime.timeInMillis < now.timeInMillis) {
            Toast.makeText(mContext, "현재 이후의 시간으로 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }


        Log.d("선택한 약속 장소 - 위도", "위도 : ${mSelectedLatLng!!.latitude}")
        Log.d("선택한 약속 장소 - 경도", "경도 : ${mSelectedLatLng!!.longitude}")

        // binding.startPlaceSpinner.selectedItemPosition // 지금 선택되어 있는 아이템의 몇 번째 아이템인지
        val selectStartPlace = mStartPlaceList[binding.startPlaceSpinner.selectedItemPosition]

        // 약속 일시 - yyyy-MM-dd HH:mm 의 양식을 서버가 지정해서 요청
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")

        addAppointment(
            inputTitle,
            sdf.format(mSelectedAppointmentDateTime.time),
            selectStartPlace.name,
            selectStartPlace.latitude,
            selectStartPlace.longitude,
            inputPlaceName,
            mSelectedLatLng!!.latitude,
            mSelectedLatLng!!.longitude
        )


    }

    private fun getPromiseDate()  {

        // 날짜 선택 텍스트뷰 클릭 이벤트 - DatePickerDialog
        binding.promiseDateTextView.setOnClickListener {
            val dsl = object : DatePickerDialog.OnDateSetListener {

                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    // 연/월/일은, JAVA / Kotlin 언어의 기준 (0 ~ 11월)으로 월 값을 준다. (사람은 1 ~ 12월)
                    // 주는 그대로 Calendar 에 set 하게 되면, 올바른 월로 세팅된다.
                    mSelectedAppointmentDateTime.set(year, month, dayOfMonth)   // 연월일 한번에 세팅하는 함수

                    // 약속 일자의 문구를 22/03/08 등의 형식으로 바꿔서 보여주자.
                    // SimpleDateFormat 을 활용 => 월 값도 알아서 보정
                    val sdf = SimpleDateFormat("yy/MM/dd")

                    binding.promiseDateTextView.text = sdf.format(mSelectedAppointmentDateTime.time)

                }
            }

            val dpd = DatePickerDialog(
                mContext,
                dsl,
                mSelectedAppointmentDateTime.get(Calendar.YEAR),
                mSelectedAppointmentDateTime.get(Calendar.MONTH),
                mSelectedAppointmentDateTime.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun getPromiseTime()  {

        // 시간 선택 텍스트뷰 클릭 이벤트 - TimePickerDialog
        binding.promiseTimeTextView.setOnClickListener {
            val tsl = object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
                    // 약속 일시의 시간으로 설정.
                    mSelectedAppointmentDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    mSelectedAppointmentDateTime.set(Calendar.MINUTE, minute)

                    val sdf = SimpleDateFormat("a h시 m분")
                    binding.promiseTimeTextView.text = sdf.format(mSelectedAppointmentDateTime.time)
                }

            }

            val tpd = TimePickerDialog(
                mContext,
                tsl,
                18,
                0,
                false
            ).show()

        }

    }

    private fun getMyStartPlaceList() = scope.launch {

        val result = PlaceRepository.getRequestMyPlaceList()


        if (result.isSuccessful) {

            val body = result.body()!!

            mStartPlaceList.clear()
            mStartPlaceList.addAll(body.data?.places!!)
            startPlaceSpinnerAdapter.notifyDataSetChanged()

        }


    }

    private fun addAppointment(
        inputTitle: String,
        format: String,
        name: String,
        latitude: Double,
        longitude: Double,
        inputPlaceName: String,
        lat: Double,
        lng: Double
    ) = scope.launch {

        val result = AppointmentRepository.postRequestAddAppointment(
            inputTitle, format, name, latitude, longitude, inputPlaceName, lat, lng
        )

        if (result.isSuccessful) {
            Toast.makeText(mContext, "약속이 등록되었습니다.", Toast.LENGTH_SHORT).show()
            finish()

        } else {
            Toast.makeText(mContext, "등록에 실패했습니다..", Toast.LENGTH_SHORT).show()
        }

    }


    //region * Android LifeCycle
    override fun onStart() {
        super.onStart()
        binding.naverMapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.naverMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.naverMapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.naverMapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.naverMapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.naverMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.naverMapView.onLowMemory()
    }
    //endregion

}