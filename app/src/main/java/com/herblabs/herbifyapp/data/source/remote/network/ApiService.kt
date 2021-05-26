package com.herblabs.herbifyapp.data.source.remote.network

import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

//
//    @POST("predict")
//    fun getPredict(@Body predictModel: PredictModel): Call<HerbsResponse>

    @Multipart
    @POST("predict")
    fun getPredict(@Part data : MultipartBody.Part ): Call<HerbsResponse>

//    @Multipart
//    @POST("predict")
//    fun getPredict(@Body body : PredictModel ): Call<HerbsResponse>

}