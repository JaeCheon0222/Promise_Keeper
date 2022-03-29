package com.jc.promise_keeper.view.activities.promise.detail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.forEachIndexed
import androidx.core.view.iterator
import androidx.core.view.size
import com.jc.promise_keeper.R
import com.jc.promise_keeper.adapter.StartPlaceSpinnerAdapter
import com.jc.promise_keeper.adapter.UpdatePlaceSpinnerAdapter
import com.jc.promise_keeper.common.api.repository.AppointmentRepository
import com.jc.promise_keeper.common.api.repository.PlaceRepository
import com.jc.promise_keeper.common.util.Keys
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.data.model.datas.Appointment
import com.jc.promise_keeper.data.model.datas.PlaceData
import com.jc.promise_keeper.databinding.ActivityPromiseUpdateBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PromiseUpdateActivity :
    BaseAppCompatActivity<ActivityPromiseUpdateBinding>(R.layout.activity_promise_update) {

    private lateinit var appointment: Appointment

    private lateinit var startPlaceSpinnerAdapter: StartPlaceSpinnerAdapter
//    private lateinit var startPlaceSpinnerAdapter: UpdatePlaceSpinnerAdapter
    private val selectedAppointmentDateTime = Calendar.getInstance()

    private var selectedStartPlace: PlaceData? = null
    private val startPlaceList = ArrayList<PlaceData>()

    private val scope = MainScope()

    private val startMarker = Marker()
    private val destinationMarker = Marker()

    private lateinit var startPlaceLatLng: LatLng
    private lateinit var cameraPosition: LatLng

    private lateinit var naverMap: NaverMap

    private var selectedLatLng: LatLng? = null

    private lateinit var cameraUpdate: CameraUpdate

    private var selectedStartPlaceName: String? = null

    override fun ActivityPromiseUpdateBinding.onCreate() {

        appointment = intent.getSerializableExtra(Keys.APPOINTMENT_UPDATE) as Appointment

        initViews()
        touchScroll()
        setEvents()

    }

    @SuppressLint("NewApi")
    override fun initViews() {
        super.initViews()

        txtTitle.text = "약속장소 수정"


        with(binding) {

            startPlaceSpinnerAdapter = StartPlaceSpinnerAdapter(
                mContext,
                R.layout.start_place_spinner_item,
                startPlaceList
            )
            startPlaceSpinner.adapter = startPlaceSpinnerAdapter


            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
            val date = sdf.format(appointment.datetime)
            val time = date.split(" ")

            promiseTitleEditText.setText(appointment.title)
            promiseDateTextView.text = time.first()
            promiseTimeTextView.text = time.last()
            promisePlaceEditText.setText(appointment.place)


            startPlaceLatLng = LatLng(appointment.startLatitude!!, appointment.startLongitude!!)
            setMap()

        }

    }


    override fun setEvents() {
        super.setEvents()
        spinnerEvent()
        getPromiseDate()
        getPromiseTime()
        getMyStartPlaceList()

        binding.promiseUpdateButton.setOnClickListener {
            updatePromise()
        }

    }

    private fun setMap(isStartPlace: Boolean = false) {

        binding.naverMapView.getMapAsync { _naverMap ->

            naverMap = _naverMap
            Log.d("TAG", "setMap: $startPlaceLatLng")


            startMarker.apply {

                position = startPlaceLatLng
                icon = OverlayImage.fromResource(R.drawable.start_marker)
                width = 150
                height = 150
                map = naverMap

            }

            destinationMarker.apply {

                position = LatLng(appointment.latitude!!, appointment.longitude!!)
                icon = OverlayImage.fromResource(R.drawable.destination_marker)
                width = 150
                height = 150
                map = naverMap

            }

            if (isStartPlace.not()) {
                cameraUpdate = CameraUpdate.scrollTo(destinationMarker.position)
                naverMap.moveCamera(cameraUpdate)
            } else {
                cameraUpdate = CameraUpdate.scrollTo(cameraPosition)
                naverMap.moveCamera(cameraUpdate)
            }

            naverMap.setOnMapClickListener { pointF, latLng ->

                destinationMarker.apply {

                    position = LatLng(latLng.latitude, latLng.longitude)
                    icon = OverlayImage.fromResource(R.drawable.destination_marker)
                    width = 150
                    height = 150
                    map = _naverMap

                }

                cameraUpdate = CameraUpdate.scrollTo(destinationMarker.position)
                naverMap.moveCamera(cameraUpdate)
            }




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
                    selectedStartPlace = startPlaceList[position]
//                    selectedStartPlaceName = startPlaceList[position].name

                    val startPlaceLat =
                        startPlaceList[binding.startPlaceSpinner.selectedItemPosition].latitude
                    val startPlaceLng =
                        startPlaceList[binding.startPlaceSpinner.selectedItemPosition].longitude

                    startPlaceLatLng = LatLng(startPlaceLat, startPlaceLng)
                    cameraPosition = LatLng(startPlaceLat, startPlaceLng)


                    setMap(true)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

    }

    private fun getPromiseDate() {

        // 날짜 선택 텍스트뷰 클릭 이벤트 - DatePickerDialog
        binding.promiseDateTextView.setOnClickListener {
            val dsl = object : DatePickerDialog.OnDateSetListener {

                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    // 연/월/일은, JAVA / Kotlin 언어의 기준 (0 ~ 11월)으로 월 값을 준다. (사람은 1 ~ 12월)
                    // 주는 그대로 Calendar 에 set 하게 되면, 올바른 월로 세팅된다.
                    selectedAppointmentDateTime.set(year, month, dayOfMonth)   // 연월일 한번에 세팅하는 함수

                    // 약속 일자의 문구를 22/03/08 등의 형식으로 바꿔서 보여주자.
                    // SimpleDateFormat 을 활용 => 월 값도 알아서 보정
                    val sdf = SimpleDateFormat("yy/MM/dd")

                    binding.promiseDateTextView.text = sdf.format(selectedAppointmentDateTime.time)

                }
            }

            val dpd = DatePickerDialog(
                mContext,
                dsl,
                selectedAppointmentDateTime.get(Calendar.YEAR),
                selectedAppointmentDateTime.get(Calendar.MONTH),
                selectedAppointmentDateTime.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun getPromiseTime() {

        // 시간 선택 텍스트뷰 클릭 이벤트 - TimePickerDialog
        binding.promiseTimeTextView.setOnClickListener {
            val tsl = object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
                    // 약속 일시의 시간으로 설정.
                    selectedAppointmentDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedAppointmentDateTime.set(Calendar.MINUTE, minute)

                    val sdf = SimpleDateFormat("a h시 m분")
                    binding.promiseTimeTextView.text = sdf.format(selectedAppointmentDateTime.time)
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

    private fun updatePromise() {

        val now = Calendar.getInstance()

        if (selectedAppointmentDateTime.timeInMillis < now.timeInMillis) {
            Toast.makeText(mContext, "현재 이후의 시간으로 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd : HH:mm")

        val id = appointment.id!!
        val title = binding.promiseTitleEditText.text.toString()
        val date = sdf.format(selectedAppointmentDateTime.time)
        val startLatLng = startMarker.position
        val name = selectedStartPlace?.name!!
        val placeName = binding.promisePlaceEditText.text.toString()
        val destinationLatLng = destinationMarker.position


        Log.d("TAG", "updatePromise: " +
                "${id}" +
                "${title}" +
                "${date}" +
                "${startLatLng}" +
                "${name}" +
                "${placeName}" +
                "${destinationLatLng}"
        )

        putRequestAppointment(
            id,
            title,
            date.toString(),
            name,
            startLatLng.latitude,
            startLatLng.latitude,
            placeName,
            destinationLatLng.latitude,
            destinationLatLng.longitude
        )



    }

    private fun getMyStartPlaceList() = scope.launch {

        val result = PlaceRepository.getRequestMyPlaceList()


        if (result.isSuccessful) {

            val body = result.body()!!

            startPlaceList.clear()
            startPlaceList.addAll(body.data?.places!!)
            startPlaceSpinnerAdapter.notifyDataSetChanged()

        }

    }

    private fun putRequestAppointment(
        id: Int,
        inputTitle: String,
        format: String,
        name: String,
        latitude: Double,
        longitude: Double,
        inputPlaceName: String,
        lat: Double,
        lng: Double) = scope.launch {

        val success = AppointmentRepository.putRequestUpdateAppointment(
            id,
            inputTitle,
            format,
            name,
            latitude,
            longitude,
            inputPlaceName,
            lat,
            lng
        )

        if (success) {
            showToast("수정되었습니다.")
            finish()
        } else {
            showToast("수정에 실패했습니다.")
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

}