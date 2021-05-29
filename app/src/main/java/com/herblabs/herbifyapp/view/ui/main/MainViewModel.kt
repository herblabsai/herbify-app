package com.herblabs.herbifyapp.view.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mHerbsRepository: HerbsRepository
) : ViewModel() {

    fun getLastedCapture(): LiveData<List<CaptureEntity>> {
        return mHerbsRepository.getLastedCapture()
    }

    fun addPredicted(response: HerbsResponse, captureEntity: CaptureEntity)  {

        val captureId = captureEntity.captureId

        val labelPredicted = response.data
        for (i in labelPredicted.indices){
            val predictedId = StringBuilder().apply {
                append(captureId)
                append("_")
                append(i+1)
            }.toString()

            Log.d(MainActivity.TAG, predictedId)
            Log.d(MainActivity.TAG, predictedId)

            PredictedEntity(
                    predictedId,
                    captureId,
                    labelPredicted[i].name,
                    labelPredicted[i].confident
            ).let {
                mHerbsRepository.addPredicted(it)
            }
        }
    }
}