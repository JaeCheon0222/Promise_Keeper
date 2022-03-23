package com.jc.promise_keeper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jc.promise_keeper.R
import com.jc.promise_keeper.data.model.datas.PlaceData

class PlaceRecyclerAdapter(
    val mContext: Context,
    val mList: List<PlaceData>
) : RecyclerView.Adapter<PlaceRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val placeTitleTextView = view.findViewById<TextView>(R.id.placeTitleTextView)
        private val isPrimaryTextview = view.findViewById<TextView>(R.id.isPrimaryTextview)

        // 실제 데이터 반영 기능이 있는 함수
        fun bind(data: PlaceData) {
            placeTitleTextView.text = data.name

            if (data.isPrimary.not()) {
                isPrimaryTextview.text = "기본설정을하지 않음"
            } else {
                isPrimaryTextview.text = "기본설정"
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        // xml 을 inflate 해와서 => 이를 가지고, MyViewHolder 객체로 성생. 리턴
        // 재사용성을 알아서 보존해준다.
        val row =
            LayoutInflater.from(mContext).inflate(R.layout.frequenlty_place_list_item, parent, false)
        return MyViewHolder(row)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 실제 데이터 반영 함수
        val data = mList[position]

        // 이 함수에서 직접 코딩하면 => holder.UI 변수로 매번 holder 단어를 사용해야한다.
        // holder 변수의 멤버 변수들을 사용할 수 있는 것처럼, 함수도 사용할 수 있다.
        holder.bind(data)
    }

    // 몇 개의 아이템을 보여줄 것인지 => 데이터 목록의 갯 수 만큼 보여준다.
    override fun getItemCount(): Int = mList.size

}