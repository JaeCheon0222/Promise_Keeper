package com.jc.promise_keeper.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jc.promise_keeper.R
import com.jc.promise_keeper.data.weather.Item
import com.jc.promise_keeper.data.weather.enum.Grade

class WeatherRecyclerViewAdapter(
    private val mContext: Context,
//    private val weatherList: List<Item>
    private val weatherHash: HashMap<String, String>
) : RecyclerView.Adapter<WeatherRecyclerViewAdapter.MyViewHolder>() {

    private val newWeatherList = HashMap<String, String>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val date = view.findViewById<TextView>(R.id.weatherDateTextView)
        private val time = view.findViewById<TextView>(R.id.weatherTimeTextView)
        private val skyState = view.findViewById<TextView>(R.id.skyStateTextView)
        private val temperatureState = view.findViewById<TextView>(R.id.temperatureStateTextView)
        private val minTemperatureState = view.findViewById<TextView>(R.id.minTemperatureStateTextView)
        private val maxTemperatureState = view.findViewById<TextView>(R.id.maxTemperatureStateTextView)

//        fun bind(data: Item) {
//
//
//
//        }


        fun bind(data: HashMap<String, String>) {

            date.text = data["baseDate"]
            time.text = data["fcstTime"]

            skyState.text = data["sky"]
            temperatureState.text = data["tmp"]
            minTemperatureState.text = data["tmn"]
            maxTemperatureState.text = data["tmx"]

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            LayoutInflater
                .from(mContext)
                .inflate(R.layout.weather_list_item, parent, false)
        )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = weatherHash
        holder.bind(data)
    }

    override fun getItemCount(): Int = weatherHash.size


}