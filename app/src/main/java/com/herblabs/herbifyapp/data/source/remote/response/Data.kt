package com.herblabs.herbifyapp.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("plant")
    val plant: String = "",
    @SerializedName("score")
    val score: Double = 0.0
)