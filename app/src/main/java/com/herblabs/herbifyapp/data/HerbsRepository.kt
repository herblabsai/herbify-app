package com.herblabs.herbifyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.herblabs.herbifyapp.data.source.remote.RemoteDataSource
import com.herblabs.herbifyapp.data.source.remote.model.PredictModel
import com.herblabs.herbifyapp.data.source.remote.response.Data

class HerbsRepository private constructor(private val remoteDataSource: RemoteDataSource):
    HerbsDataSource{

    companion object{
        @Volatile
        private var instance: HerbsRepository? = null
        fun getInstance(remoteDataSource: RemoteDataSource): HerbsRepository =
            instance ?: synchronized(this){
                instance ?: HerbsRepository(remoteDataSource)
            }
    }

    override fun getPredict(predictModel: PredictModel): LiveData<List<Data>> {
        val predict = MutableLiveData<List<Data>>()

        remoteDataSource.getPredict(predictModel, object : RemoteDataSource.LoadPredictCallback{
            override fun onPredictReceive(predictResponse: List<Data>) {
                predict.postValue(predictResponse)
            }
        })

        return predict
    }

}