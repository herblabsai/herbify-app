package com.herblabs.herbifyapp.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.herblabs.herbifyapp.data.source.local.db.HerbifyDao
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.local.entity.CaptureWithPredicted
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val herbifyDao: HerbifyDao){

    fun addCapture(userCaptureEntity: CaptureEntity) {
        herbifyDao.addCapture(userCaptureEntity)
    }

    fun addPredicted(predictedEntity: PredictedEntity) {
        herbifyDao.addPredicted(predictedEntity)
    }

    fun getAllFavorite(): DataSource.Factory<Int, CaptureEntity> {
        return herbifyDao.getCapture()
    }

    fun getCaptureWithPredicted(captureId: Int) : LiveData<CaptureWithPredicted> {
        return herbifyDao.getCourseWithModuleById(captureId)
    }


}