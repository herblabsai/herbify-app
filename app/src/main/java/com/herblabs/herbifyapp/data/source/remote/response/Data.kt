package com.herblabs.herbifyapp.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("name")
    val plant: String = "",
    @SerializedName("confident")
    val confident: Double = 0.0
)