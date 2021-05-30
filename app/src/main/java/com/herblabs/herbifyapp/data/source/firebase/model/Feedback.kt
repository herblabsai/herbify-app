package com.herblabs.herbifyapp.data.source.firebase.model

import com.herblabs.herbifyapp.data.source.remote.response.Data

data class Feedback(
    val imageUrl: String,
    val email: String,
    val content: String,
    val data: List<Data>
)
