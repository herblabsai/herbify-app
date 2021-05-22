package com.herblabs.herbifyapp.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("nama_tumbuhan")
    val namaTumbuhan: String = "",
    @SerializedName("score")
    val score: String = ""
)