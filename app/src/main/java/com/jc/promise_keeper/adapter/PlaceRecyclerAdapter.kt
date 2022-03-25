package com.jc.promise_keeper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jc.promise_keeper.R
import com.jc.promise_keeper.data.model.datas.PlaceData

class PlaceRecyclerAdapter(
    private val mContext: Context,
    private val mList: List<PlaceData>
) : RecyclerView.Adapter<PlaceRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val placeTitleTextView = view.findViewById<TextView>(R.id.placeTitleTextView)
        private val isPrimaryTextview = view.findViewById<TextView>(R.id.isPrimaryTextview)
        private val placeImageView = view.findViewById<ImageView>(R.id.placeImageView)
        private val deleteButton = view.findViewById<ImageView>(R.id.deleteButton)


        fun bind(data: PlaceData) {
            placeTitleTextView.text = data.name

            if (data.isPrimary.not()) {
                isPrimaryTextview.text = "기본설정을하지 않음"
            } else {
                isPrimaryTextview.text = "기본설정"
            }

            placeImageView.setOnClickListener {
                // todo 기본장소 상세 화면
            }

            deleteButton.setOnClickListener {



            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row =
            LayoutInflater.from(mContext).inflate(R.layout.frequenlty_place_list_item, parent, false)
        return MyViewHolder(row)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = mList.size

}