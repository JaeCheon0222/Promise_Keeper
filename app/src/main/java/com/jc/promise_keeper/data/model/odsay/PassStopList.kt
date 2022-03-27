package com.jc.promise_keeper.data.model.odsay


import com.google.gson.annotations.SerializedName

data class PassStopList(
    @SerializedName("stations")
    val stations: List<Station>?
)