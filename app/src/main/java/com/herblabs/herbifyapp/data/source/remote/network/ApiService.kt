package com.herblabs.herbifyapp.data.source.remote.network

import com.herblabs.herbifyapp.data.source.remote.model.PredictModel
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("predict")
    fun getPredict(@Body predictModel: PredictModel): Call<HerbsResponse>
}