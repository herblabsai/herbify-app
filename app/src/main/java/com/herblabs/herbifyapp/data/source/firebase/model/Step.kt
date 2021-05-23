package com.herblabs.herbifyapp.data.source.firebase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Step(
    val caption: String = "",
    val imageUrl: String = "",
    val number: Int = 0
): Parcelable