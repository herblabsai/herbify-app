package com.herblabs.herbifyapp.data.source.remote.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HerbsResponse(
    @SerializedName("data")
    val data: List<Data> = listOf()
) : Parcelable