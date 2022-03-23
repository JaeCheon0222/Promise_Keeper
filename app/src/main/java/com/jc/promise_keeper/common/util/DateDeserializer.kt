package com.jc.promise_keeper.common.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

// 실제로 String 을 어떻게 Date 로 변환해줄건지 방법 명시 클래스

class DateDeserializer: JsonDeserializer<Date> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date {

        // 실제 String 을 Date 로 변환해주는 구체적인 방법 명시 함수
        val dateStr = json?.asString
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = sdf.parse(dateStr)   // 문구로 들어온 일시를 Date 형태로 변환.

        return date!!

    }

}