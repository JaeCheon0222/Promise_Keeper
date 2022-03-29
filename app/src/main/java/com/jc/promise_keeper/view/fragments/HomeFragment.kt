package com.jc.promise_keeper.view.fragments

import GpsTransfer
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.jc.promise_keeper.R
import com.jc.promise_keeper.adapter.WeatherRecyclerViewAdapter
import com.jc.promise_keeper.common.api.repository.WeatherRepository
import com.jc.promise_keeper.common.util.base_view.BaseFragment
import com.jc.promise_keeper.data.weather.WeatherBasicResponse
import com.jc.promise_keeper.data.weather.enums.Grade
import com.jc.promise_keeper.databinding.FragmentHomeBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val scope = MainScope()

    // 현재 위치 정보
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var cancellationTokenSource: CancellationTokenSource? = null

    private lateinit var weatherRecyclerAdapter: WeatherRecyclerViewAdapter

//    private val weatherList = ArrayList<Item>()

    private val weatherDataHash = HashMap<String, String>()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        getLocation()
//        getDate()
//        setAdapter()
        getDate()
    }

    override fun initViews() {
        super.initViews()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)

    }

    private fun setAdapter() {
//        weatherRecyclerAdapter = WeatherRecyclerViewAdapter(mContext, weatherDataHash)
////        weatherRecyclerAdapter = WeatherRecyclerViewAdapter(mContext, weatherList)
//        binding.weatherRecyclerView.adapter = weatherRecyclerAdapter
//        // 리사이클러뷰 가로 설정
//        binding.weatherRecyclerView.layoutManager =
//            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setDay(nowTime: Date): String {
        val cal = Calendar.getInstance()
        val timeSdf = SimpleDateFormat("HH")
        val nowTime = timeSdf.format(nowTime.time)
        val timeHash = hashMapOf<String, Date>(
            "two" to timeSdf.parse("02"),
            "five" to timeSdf.parse("05"),
            "eight" to timeSdf.parse("08"),
            "eleven" to timeSdf.parse("11"),
            "fourteen" to timeSdf.parse("14"),
            "seventeen" to timeSdf.parse("17"),
            "twenty" to timeSdf.parse("20"),
            "twentyThree" to timeSdf.parse("23"),
        )

        val two = timeSdf.format(timeHash["two"])
        val five = timeSdf.format(timeHash["five"])
        val eight = timeSdf.format(timeHash["eight"])
        val eleven = timeSdf.format(timeHash["eleven"])
        val fourteen = timeSdf.format(timeHash["fourteen"])
        val seventeen = timeSdf.format(timeHash["seventeen"])
        val twenty = timeSdf.format(timeHash["twenty"])
        val twentyThree = timeSdf.format(timeHash["twentyThree"])

        var tempTime = nowTime.toInt()
        var currentTime = ""

        // 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 (1일, 8회)
        if (two.toInt() <= tempTime && tempTime < five.toInt()) {
            currentTime = "02"
        } else if (five.toInt() <= tempTime && tempTime < eight.toInt()) {
            currentTime = "05"
        } else if (eight.toInt() <= tempTime && tempTime < eleven.toInt()) {
            currentTime = "08"
        } else if (eleven.toInt() <= tempTime && tempTime < fourteen.toInt()) {
            currentTime = "11"
        } else if (fourteen.toInt() <= tempTime && tempTime < seventeen.toInt()) {
            currentTime = "14"
        } else if (seventeen.toInt() <= tempTime && tempTime < twenty.toInt()) {
            currentTime = "17"
        } else if (twenty.toInt() <= tempTime && tempTime < twentyThree.toInt()) {
            currentTime = "20"
        } else if (tempTime >= twentyThree.toInt()) {
            currentTime = "23"
        }

        val changeDate = timeSdf.parse(currentTime)

        return timeSdf.format(changeDate!!)

    }


    private fun getDate(): HashMap<String, Int> {

        val dayHash = HashMap<String, Int>()

        val today = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyyMMdd HH:mm")

        val hour = setDay(today.time)
        val getDayTime = sdf.format(today.time)
        Log.d("TAG", "getDate: ${sdf.format(today.time)}")



        val split = getDayTime.split(" ")
        val day = split[0].toInt()
        val time = split[1].replace(":", "").toInt()

        dayHash["day"] = day
        dayHash["time"] = hour.toInt()

        return dayHash

    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {

        cancellationTokenSource = CancellationTokenSource()

        // 현재 위치 가져오기
        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource!!.token
        ).addOnSuccessListener { location ->

            scope.launch {
                getWeather(location)
            }

        }

    }


    private suspend fun getWeather(location: Location) {

        // 위경도 -> 격자 변경
        val gps = GpsTransfer(location.latitude, location.longitude).apply {
            transfer(this, 0)
        }

        val lat = gps.getxLat().toInt()
        val lng = gps.getyLon().toInt()

        val resultDate = getDate()

        val day = resultDate["day"]
        val time = resultDate["time"]


//        val resultTime = sdf.format(time)

        val strTime = time.toString() + "00"

        Log.d("TAG", "getWeather: $day")
        Log.d("TAG", "getWeather: $time")



        try {

            val result = WeatherRepository.getRequestWeather(
                numOfRows = 26,
                pageNo = 1,
                base_date = day!!.toInt(),
                base_time = strTime.toInt(),
                nx = lat,
                ny = lng
            )

            if (result.isSuccessful) {

                val body = result.body()

                Log.d("TAG", "getWeather: $body")

                if (body == null) {
                    return
                }
                weatherDataHash.clear()
                successGetWeatherData(body)

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun successGetWeatherData(result: WeatherBasicResponse) {

        val body = result.response?.body
        val item = body?.items?.item

        Log.d("TAG", "successGetWeatherData: $body")

        for (i in item?.indices!!) {


            if (item[i].category == "SKY" ||
                item[i].category == "TMP" ||
                item[i].category == "POP" ||
                item[i].category == "TMN" ||
                item[i].category == "TMX"
            ) {

                weatherDataHash["baseDate"] = item[i].baseDate!!
                weatherDataHash["baseTime"] = item[i].baseTime!!
                weatherDataHash["fcstTime"] = item[i].fcstTime!!

                when (item[i].category) {

                    "SKY" -> {
                        weatherDataHash["sky"] = item[i].fcstValue!!

                    }
                    "TMP" -> {
                        weatherDataHash["tmp"] = item[i].fcstValue!!
                    }
                    "TMN" -> {
                        weatherDataHash["tmn"] = item[i].fcstValue!!
                    }
                    "TMX" -> {
                        weatherDataHash["tmx"] = item[i].fcstValue!!
                    }

                }


            } else {
                continue
            }
        }

        val resultList: List<Pair<String, String>> = weatherDataHash.toList()
        resultData(resultList)

    }

    private fun resultData(resultList: List<Pair<String, String>>) {

        Log.d("TAG", "resultData: ${resultList}")
        Log.d("TAG", "resultData: ${resultList[0].second}")

        val resultSky = resultList[0].second
        val resultBaseDate = resultList[1].second
        val resultFcstTime = resultList[2].second
        val resultTmp = resultList[3].second
        val resultBaseTime = resultList[4].second
//
//        val date = "$resultBaseDate $resultFcstTime"
        val strToDate = SimpleDateFormat("yyyyMMdd")
        val sdf = SimpleDateFormat("yyyy년MM월dd일")

        val strToTime = SimpleDateFormat("HHmm")
        val sdfTime = SimpleDateFormat("HH:mm")

        val day = strToDate.parse(resultBaseDate)
        val resultDay = sdf.format(day.time)

        val time = strToTime.parse(resultBaseTime)
        val resultTime = sdfTime.format(time)


        Log.d("TAG", "resultData: $day")
        Log.d("TAG", "resultData: $resultDay")

        when (resultSky) {
            "1" -> {
                binding.skyStateEmoji.text = Grade.GOOD.emoji
                binding.skyStateTextView.text = Grade.GOOD.label
            }
            "3" -> {
                binding.skyStateTextView.text = Grade.CLODY.emoji
            }
            "4" -> {
                binding.skyStateTextView.text = Grade.BLUR.emoji
            }
        }

//        binding.skyStateTextView.text = resultSky
        binding.weatherDateTextView.text = resultDay.toString()
        binding.weatherTimeTextView.text = resultTime.toString()
        binding.temperatureStateTextView.text = "$resultTmp °C"


    }


//    override fun onResume() {
//        super.onResume()
//        getLocation()
//    }

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource?.cancel()
        scope.cancel()
    }

}