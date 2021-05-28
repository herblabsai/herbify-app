package com.herblabs.herbifyapp.view.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: HerbsRepository
) : ViewModel() {

    fun getCapture(): LiveData<PagedList<CaptureEntity>> {
        return repository.getAllCapture()
    }

}