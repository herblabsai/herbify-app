package com.herblabs.herbifyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.local.entity.CaptureWithPredicted
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import com.herblabs.herbifyapp.data.source.remote.response.Data
import com.herblabs.herbifyapp.vo.Resource
import okhttp3.MultipartBody

interface HerbsDataSource {

    /**
     * REMOTE
     **/

    fun getPredict(predictModel: MultipartBody.Part): LiveData<Resource<List<Data>>>


    /**
     * LOCAL
     **/

    fun getAllUserCapture() : LiveData<PagedList<CaptureEntity>>

    fun getCaptureWithPredicted(captureId : Int) : LiveData<CaptureWithPredicted>

    fun addCapture(entity: CaptureEntity)

    fun addPredicted(entity: PredictedEntity)

}