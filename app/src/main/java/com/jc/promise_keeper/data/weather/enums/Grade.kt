package com.jc.promise_keeper.data.weather.enums

enum class Grade(
    val label: String,
    val emoji: String
) {

    GOOD("맑음", "☀"),
    CLODY("구름 많음", "⛅"),
    BLUR("흐림", "☁"),
    UNKNOWN("미측정", "😵");

    override fun toString(): String {
        return "$label $emoji"
    }

}