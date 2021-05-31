 package com.herblabs.herbifyapp.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.herblabs.herbifyapp.data.source.remote.network.ApiService
import com.herblabs.herbifyapp.data.source.remote.response.Data
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import com.herblabs.herbifyapp.vo.Resource
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.log

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    companion object{
        const val TAG = "RemoteDataSource"
    }

    fun getPredict(part: MultipartBody.Part) : LiveData<Resource<HerbsResponse>> {

        val predictedResponse = MutableLiveData<Resource<HerbsResponse>>()

        predictedResponse.value = Resource.loading( null )
        apiService.getPredict(part).enqueue(object : Callback<HerbsResponse>{
            override fun onResponse(call: Call<HerbsResponse>, response: Response<HerbsResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.data != null){

                        val listData = ArrayList<Data>()
                        val getList : List<Data> = listData

                        response.body()?.data?.forEach {
                            if (it.confident > 20 ){
                                Log.d(TAG, "STATUS SUCCESS 2: $it")
                                listData.add(it)
                            }
                        }
                        predictedResponse.value = Resource.success( HerbsResponse(data=
                            getList.let { data ->
                                data.sortedByDescending { it.confident }
                                    .filterIndexed { index, _ ->
                                        (index <= 2 )
                                    }
                            }, imageUploaded = response.body()?.imageUploaded
                        ))
                    } else {
                        predictedResponse.value = Resource.empty( response.message(), null)
                    }
                }else{
                    predictedResponse.value = Resource.error( response.message(), null )
                }
            }
            override fun onFailure(call: Call<HerbsResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message.toString()}")
                predictedResponse.value = Resource.error( t.message, null )
            }

        })

        return predictedResponse
    }

}