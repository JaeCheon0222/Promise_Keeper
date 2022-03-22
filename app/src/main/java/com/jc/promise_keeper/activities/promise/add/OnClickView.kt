package com.jc.promise_keeper.activities.promise.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import com.jc.promise_keeper.databinding.ActivityAddPromiseBinding
import java.text.SimpleDateFormat
import java.util.*

class OnClickView(val binding: ActivityAddPromiseBinding) {

    // 약속 시간 일/시 를 저장해줄 Calendar (월 값이 0 ~ 11 로 움직이게 맞춰져있다.)
    private val selectedAppointmentDateTime = Calendar.getInstance() // 기본 값: 현재 일시

    fun getPromiseDate() = with(binding) {

        // 날짜 선택 텍스트뷰 클릭 이벤트 - DatePickerDialog
        promiseDateTextView.setOnClickListener {
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
                binding.root.context,
                dsl,
                selectedAppointmentDateTime.get(Calendar.YEAR),
                selectedAppointmentDateTime.get(Calendar.MONTH),
                selectedAppointmentDateTime.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    fun getPromiseTime() = with(binding) {

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
                binding.root.context,
                tsl,
                18,
                0,
                false
            ).show()

        }

    }

}