package com.herblabs.herbifyapp.view.ui.identify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.local.entity.CaptureWithPredicted
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IdentifyViewModel @Inject constructor(
        private val repository: HerbsRepository
) : ViewModel() {

    fun getCaptureWithPredicted(captureId : Int): LiveData<CaptureWithPredicted> {
        return repository.getCaptureWithPredicted(captureId)
    }

    fun getHerb(captureWithPredicted: CaptureWithPredicted) {

    }
}