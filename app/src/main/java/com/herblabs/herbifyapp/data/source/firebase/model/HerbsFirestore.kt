package com.herblabs.herbifyapp.data.source.firebase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HerbsFirestore(
    val _id: Int = 0,
    val imageUrl: String = "",
    val latinName: String = "",
    val name: String = "",
    val overview: String = "",
    val recipes: List<Recipe> = listOf()
): Parcelable