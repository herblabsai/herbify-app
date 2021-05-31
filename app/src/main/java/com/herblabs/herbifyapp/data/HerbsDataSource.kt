package com.herblabs.herbifyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.local.entity.CaptureWithPredicted
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import com.herblabs.herbifyapp.vo.Resource
import okhttp3.MultipartBody

interface HerbsDataSource {

    /**
     * REMOTE
     **/

    fun getPredict(part: MultipartBody.Part): LiveData<Resource<HerbsResponse>>


    /**
     * LOCAL
     **/

    fun getAllCapture() : LiveData<PagedList<CaptureEntity>>

    fun getCaptureWithPredicted(captureId : Int) : LiveData<CaptureWithPredicted>

    fun addCapture(entity: CaptureEntity)

    fun addPredicted(entity: PredictedEntity)

    fun getLastedCapture(): LiveData<List<CaptureEntity>>

    fun getPredictedByCaptureId(captureId: Int) : LiveData<List<PredictedEntity>>


    /**
     * FIRESTORE
     **/

    fun getHerbs():  LiveData<Resource<List<HerbsFirestore>>>

    fun getRecipes():  LiveData<Resource<List<Recipe>>>

    fun getHerbByName(name: String):  LiveData<Resource<List<HerbsFirestore>>>

    fun getRecipeByDocumentID(id: String):  LiveData<List<Recipe>>

    fun getRecipeByListID(listID: List<Int>):  MutableLiveData<Resource<List<Recipe>>>

<<<<<<< Updated upstream

=======
    fun searchHerbs(keyword: String):  MutableLiveData<Resource<List<HerbsFirestore>>>
>>>>>>> Stashed changes
}