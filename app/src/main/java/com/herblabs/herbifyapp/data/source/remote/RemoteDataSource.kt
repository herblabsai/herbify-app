package com.herblabs.herbifyapp.data.source.remote

import android.util.Log
import com.herblabs.herbifyapp.data.source.remote.model.PredictModel
import com.herblabs.herbifyapp.data.source.remote.network.ApiService
import com.herblabs.herbifyapp.data.source.remote.response.Data
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource private constructor(private val apiService: ApiService) {
    companion object{
        const val TAG = "RemoteDataSource"

        @Volatile
        private var instance: RemoteDataSource? = null
        @InternalCoroutinesApi
        fun getInstance(helper: ApiService): RemoteDataSource =
            instance ?: kotlinx.coroutines.internal.synchronized(this) {
                instance ?: RemoteDataSource(helper)
            }
    }

    fun getPredict(predictModel: PredictModel, callback: LoadPredictCallback){
        apiService.getPredict(predictModel).enqueue(object : Callback<HerbsResponse>{
            override fun onResponse(call: Call<HerbsResponse>, response: Response<HerbsResponse>) {
                if(response.isSuccessful){
                    val predict = response.body()?.data
                    if(predict != null){
                        callback.onPredictReceive(predict)
                    }
                }
            }

            override fun onFailure(call: Call<HerbsResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    interface LoadPredictCallback {
        fun onPredictReceive(predictResponse: List<Data>)
    }
}