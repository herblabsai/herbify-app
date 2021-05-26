package com.herblabs.herbifyapp.view.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import com.herblabs.herbifyapp.data.source.remote.response.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mHerbsRepository: HerbsRepository
) : ViewModel() {

    fun addCapture(mCaptureEntity: CaptureEntity) {
        mHerbsRepository.addCapture(mCaptureEntity)
    }

    fun addPredicted(labelPredicted: List<Data>, captureId: Int) {
        for (i in labelPredicted.indices){
            val predictedId = StringBuilder().apply {
                append(captureId)
                append("_")
                append(i+1)
            }

            Log.d(MainActivity.TAG, predictedId.toString())

            PredictedEntity(
                    "_${i+1}",
                    captureId,
                    labelPredicted[i].plant,
                    labelPredicted[i].confident
            ).apply {
                mHerbsRepository.addPredicted(this)
            }
        }
    }
}