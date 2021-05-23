package com.herblabs.herbifyapp.data.source.firebase.model

data class Recipe(
    val _id: Int = 0,
    val imageUrl: String = "",
    val ingredients: List<String> = listOf(),
    val name: String = "",
    val overview: String = "",
    val steps: List<Step> = listOf()
)