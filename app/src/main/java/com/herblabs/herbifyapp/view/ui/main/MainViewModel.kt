package com.herblabs.herbifyapp.view.ui.main

import androidx.lifecycle.ViewModel
import com.herblabs.herbifyapp.data.HerbsRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val mHerbsRepository: HerbsRepository
) : ViewModel() {

    fun addCapture() {
        mHerbsRepository
    }

}