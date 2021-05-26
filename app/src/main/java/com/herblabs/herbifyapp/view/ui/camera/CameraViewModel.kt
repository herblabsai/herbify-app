package com.herblabs.herbifyapp.view.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import com.herblabs.herbifyapp.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
        private val repository: HerbsRepository
): ViewModel() {

    fun addCapture(mCaptureEntity: CaptureEntity){
        repository.addCapture(mCaptureEntity)
    }

    fun uploadPredict(file: File) : LiveData<Resource<HerbsResponse>>{
        val requestBody : RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part : MultipartBody.Part = MultipartBody.Part.createFormData("img", file.name, requestBody)

        return repository.getPredict(part)
    }
}