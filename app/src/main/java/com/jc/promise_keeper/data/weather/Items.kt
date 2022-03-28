package com.jc.promise_keeper.data.weather


import com.google.gson.annotations.SerializedName

data class Items(
    @SerializedName("item")
    val item: List<Item>?
)