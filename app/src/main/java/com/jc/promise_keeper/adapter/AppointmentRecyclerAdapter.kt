package com.jc.promise_keeper.adapter

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.AppointmentRepository
import com.jc.promise_keeper.data.model.datas.Appointment
import com.jc.promise_keeper.view.activities.promise.detail.PromiseDetailActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class AppointmentRecyclerAdapter(
    private val mContext: Context,
    private val mList: List<Appointment>,
) : RecyclerView.Adapter<AppointmentRecyclerAdapter.MyViewHolder>() {

    val scope = MainScope()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtTitle = view.findViewById<TextView>(R.id.titleTextView)
        private val txtDateTime = view.findViewById<TextView>(R.id.dateTimeTextView)
        private val txtPlaceName = view.findViewById<TextView>(R.id.placeNameTextView)
        private val imgViewMap = view.findViewById<TextView>(R.id.imgViewMap)


        fun bind(data: Appointment) {

            txtTitle.text = data.title!!
            txtPlaceName.text = data.place!!

            // 서버가 주는 datetime (String - 2022-03-15 10:57:23 양식)
            // 이제 서버가 주는 datetime (Date 형태로 내려온다)

            // 출력하고 싶은 datetime (String - 22년 3월 5일 오후 10:57 양식) - format 활용
            val sdf = SimpleDateFormat("yy년 M월 d일 a h시 m분")
            txtDateTime.text = sdf.format(data.datetime!!)

            imgViewMap.setOnClickListener {
                val myIntent = Intent(mContext, PromiseDetailActivity::class.java)
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
        holder.bind(data)

    }

    override fun getItemCount(): Int = mList.size

    private fun deleteRequestAppointment(id: Int) = scope.launch {

        val result = AppointmentRepository.deleteRequestAppointment(id)

        if (result.isSuccessful) {

            val code = result.body()?.code

            if (code == 200) {
                Toast.makeText(mContext, "삭제했습니다.", Toast.LENGTH_SHORT).show()
                refreshAdapter()
            }


        } else {
            Toast.makeText(mContext, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun refreshAdapter() {

//        if (mContext is Fragment) {
//
//            Log.d("TAG", "Fragment")
//
//        } else if (mContext is Activity) {
//            Log.d("TAG", "Activity")
//        } else {
//            Log.d("TAG", "refreshAdapter: ")
//        }


//        val fragmentManager = fragmentManager?.beginTransaction()
//        fragmentManager?.detach(fragment)?.attach(fragment)?.commit()


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
                deleteRequestAppointment(id)
            })
            .setNegativeButton("취소", null)
            .show()
    }




}