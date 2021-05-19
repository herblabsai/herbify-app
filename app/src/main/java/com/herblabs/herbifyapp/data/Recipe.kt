package com.herblabs.herbifyapp.data

data class Recipe(
    val _id: Int = 0,
    val ingredients: List<String> = listOf(),
    val name: String = "",
    val overview: String = "",
    val steps: List<Step> = listOf()
)