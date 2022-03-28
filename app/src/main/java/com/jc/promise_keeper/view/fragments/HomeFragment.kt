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
import com.jc.promise_keeper.databinding.FragmentHomeBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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


    private fun getDate(): HashMap<String, Int> {

        val dayHash = HashMap<String, Int>()

        val today = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyyMMdd HH:mm")

        val getDayTime = sdf.format(today.time)
        val split = getDayTime.split(" ")
        val day = split[0].toInt()
        val time = split[1].replace(":", "").toInt()

        dayHash["day"] = day
        dayHash["time"] = time

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

        if (day == null) {
            return
        }

//        val strTime = String.format("%04d", 800)
//        val tempTime = SimpleDateFormat("HHmm").parse(strTime)
//        val resultTime = SimpleDateFormat("HHmm").format(tempTime).toInt()
//        val addBeforeZero = DecimalFormat("0000")
//        val time = addBeforeZero.format(resultTime)
//        Log.d("TAG", "getWeather b: ${time.toInt()}")

        try {

            val result = WeatherRepository.getRequestWeather(
                numOfRows = 26,
                pageNo = 1,
                base_date = day,
                nx = lat,
                ny = lng
            )

            if (result.isSuccessful) {

                val body = result.body()

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

                when(item[i].category) {

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

        resultList[0].second


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