package com.herblabs.herbifyapp.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class HerbsResponse(
    @SerializedName("data")
    val data: List<Data> = listOf()
)