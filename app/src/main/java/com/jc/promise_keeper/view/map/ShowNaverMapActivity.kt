package com.jc.promise_keeper.view.map

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.map.MapView
import com.naver.maps.map.util.FusedLocationSource

class ShowNaverMapActivity(view: View, resId: Int) : AppCompatActivity() {

    private lateinit var locationSource: FusedLocationSource

    private var mapView: MapView = view.findViewById(resId)

    fun setUpMap() {


//
        mapView.getMapAsync {


        }

    }




}