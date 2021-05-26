package com.herblabs.herbifyapp.view.ui.camera

import androidx.lifecycle.*
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.remote.response.Data
import com.herblabs.herbifyapp.vo.Resource
import okhttp3.RequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
        private val repository: HerbsRepository
): ViewModel() {

    fun uploadPredict(file: File) : LiveData<Resource<List<Data>>>{
        val requestBody : RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part : MultipartBody.Part = MultipartBody.Part.createFormData("img", file.name, requestBody)

        return repository.getPredict(part)

// MultipartBody.Part is used to send also the actual file name
//        val part : MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//        val fullName = RequestBody.create(MediaType.parse("multipart/form-data"), "img")
//        val predictModel = PredictModel(file)
    }
}