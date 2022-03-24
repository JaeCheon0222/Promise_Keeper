package com.jc.promise_keeper.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jc.promise_keeper.R
import com.jc.promise_keeper.data.model.datas.Appointment
import com.jc.promise_keeper.view.activities.map.PromisePlaceMapViewActivity
import java.text.SimpleDateFormat

class AppointmentRecyclerAdapter(
    val mContext: Context,
    val mList: List<Appointment>
) : RecyclerView.Adapter<AppointmentRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtTitle = view.findViewById<TextView>(R.id.titleTextView)
        val txtDateTime = view.findViewById<TextView>(R.id.dateTimeTextView)
        val txtPlaceName = view.findViewById<TextView>(R.id.placeNameTextView)
        val imgViewMap = view.findViewById<ImageView>(R.id.imgViewMap)

        fun bind(data: Appointment) {

            txtTitle.text = data.title!!
            txtPlaceName.text = data.place!!

            // 서버가 주는 datetime (String - 2022-03-15 10:57:23 양식)
            // 이제 서버가 주는 datetime (Date 형태로 내려온다)

            // 출력하고 싶은 datetime (String - 22년 3월 5일 오후 10:57 양식) - format 활용
            val sdf = SimpleDateFormat("yy년 M월 d일 a h시 m분")
            txtDateTime.text = sdf.format(data.datetime!!)

            imgViewMap.setOnClickListener {
                val myIntent = Intent(mContext, PromisePlaceMapViewActivity::class.java)
                myIntent.putExtra("appointment", data)
                mContext.startActivity(myIntent)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            LayoutInflater
                .from(mContext)
                .inflate(R.layout.appointment_list_item, parent, false)
        )


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        Log.d("holder", "onBindViewHolder: $data")
        holder.bind(data)

    }

    override fun getItemCount(): Int = mList.size

}