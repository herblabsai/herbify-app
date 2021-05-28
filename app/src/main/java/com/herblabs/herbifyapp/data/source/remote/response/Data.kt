package com.herblabs.herbifyapp.data.source.remote.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("confident")
    val confident: Double = 0.0
) : Parcelable