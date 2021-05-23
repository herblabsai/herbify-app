package com.herblabs.herbifyapp.data

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.data.source.remote.model.PredictModel
import com.herblabs.herbifyapp.data.source.remote.response.Data

interface HerbsDataSource {

    fun getPredict(predictModel: PredictModel): LiveData<List<Data>>

}