package com.herblabs.herbifyapp.data

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import androidx.paging.PagedList
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.local.entity.CaptureWithPredicted
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import com.herblabs.herbifyapp.data.source.remote.model.PredictModel
import com.herblabs.herbifyapp.data.source.remote.response.Data

interface HerbsDataSource {

    /**
     * REMOTE
     **/

    fun getPredict(predictModel: PredictModel): LiveData<List<Data>>


    /**
     * LOCAL
     **/

    fun getAllUserCapture() : LiveData<PagedList<CaptureEntity>>

    fun getCaptureWithPredicted(captureId : Int) : LiveData<CaptureWithPredicted>

    fun addCapture(entity: CaptureEntity)

    fun addPredicted(entity: PredictedEntity)

}