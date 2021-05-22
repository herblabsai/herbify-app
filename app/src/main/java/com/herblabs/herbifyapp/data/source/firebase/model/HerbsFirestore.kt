package com.herblabs.herbifyapp.data.source.firebase.model

data class HerbsFirestore(
    val _id: Int = 0,
    val imageUrl: String = "",
    val latinName: String = "",
    val name: String = "",
    val overview: String = "",
    val recipes: List<Recipe> = listOf()
)