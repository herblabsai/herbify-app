package com.herblabs.herbifyapp.data

import androidx.lifecycle.LiveData
import com.herblabs.herbifyapp.data.source.remote.model.PredictModel
import com.herblabs.herbifyapp.data.source.remote.response.Data

interface HerbsDataSource {

    fun getPredict(predictModel: PredictModel): LiveData<List<Data>>
}