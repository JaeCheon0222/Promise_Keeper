package com.jc.promise_keeper.adapter

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.PlaceRepository
import com.jc.promise_keeper.common.util.REQ_RES_CODE
import com.jc.promise_keeper.data.model.datas.PlaceData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class PlaceRecyclerAdapter(
    private val mContext: Context,
    private val mList: List<PlaceData>,
) : RecyclerView.Adapter<PlaceRecyclerAdapter.MyViewHolder>() {

    private val scope = MainScope()
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val placeTitleTextView = view.findViewById<TextView>(R.id.placeTitleTextView)
        private val isPrimaryTextview = view.findViewById<TextView>(R.id.isPrimaryTextview)
        private val placeImageView = view.findViewById<ImageView>(R.id.placeImageView)
        private val deletePlaceButton = view.findViewById<ImageView>(R.id.deletePlaceButton)


        fun bind(data: PlaceData) {
            placeTitleTextView.text = data.name

            if (data.isPrimary.not()) {
                isPrimaryTextview.text = "기본설정을하지 않음"
            } else {
                isPrimaryTextview.text = "기본설정"
            }

            placeImageView.setOnClickListener {

            }

            deletePlaceButton.setOnClickListener {

                Log.d("id", "bind: ${data.id}")

                showDialog(data.id)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row =
            LayoutInflater.from(mContext)
                .inflate(R.layout.frequenlty_place_list_item, parent, false)
        return MyViewHolder(row)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = mList[position]
        holder.bind(data)

    }

    override fun getItemCount(): Int = mList.size

    private fun deleteRequestPlace(id: Int) {

        scope.launch {
            val result = PlaceRepository.deleteRequestPlace(id)

            if (result.isSuccessful) {

                val code = result.body()?.code

                if (code == REQ_RES_CODE.SUCCESS) {
                    Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    refreshAdapter()
                }

            } else {
                Toast.makeText(mContext, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun refreshAdapter() {
        // 화면 갱신하기.
        val intent = (mContext as Activity).intent
        mContext.finish() //현재 액티비티 종료 실시
        mContext.overridePendingTransition(0, 0) //효과 없애기
        mContext.startActivity(intent) //현재 액티비티 재실행 실시
        mContext.overridePendingTransition(0, 0) //효과 없애기

    }



    private fun showDialog(id: Int) {

        AlertDialog.Builder(mContext)
            .setTitle("약속 삭제하기")
            .setMessage("해당 약속을 삭제하시겠습니까?")
            .setPositiveButton("삭제", DialogInterface.OnClickListener { _, _ ->
                deleteRequestPlace(id)
            })
            .setNegativeButton("취소", null)
            .show()
    }

}