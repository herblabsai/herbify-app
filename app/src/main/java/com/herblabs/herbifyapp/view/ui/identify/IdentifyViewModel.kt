package com.herblabs.herbifyapp.view.ui.identify

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.local.entity.CaptureWithPredicted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IdentifyViewModel @Inject constructor(
        private val repository: HerbsRepository
) : ViewModel() {

    fun getCaptureWithPredicted(captureId : Int): LiveData<CaptureWithPredicted> {
        return repository.getCaptureWithPredicted(captureId)
    }
}