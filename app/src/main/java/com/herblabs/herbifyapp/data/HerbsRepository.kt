package com.herblabs.herbifyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.herblabs.herbifyapp.data.source.firebase.FirestoreDataStore
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.data.source.local.LocalDataSource
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.local.entity.CaptureWithPredicted
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import com.herblabs.herbifyapp.data.source.remote.RemoteDataSource
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import com.herblabs.herbifyapp.utils.AppExecutors
import com.herblabs.herbifyapp.vo.Resource
import okhttp3.MultipartBody
import javax.inject.Inject

class HerbsRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val firestoreDataStore: FirestoreDataStore,
    private val appExecutors: AppExecutors
): HerbsDataSource{
    /**
     * REMOTE
     **/

    override fun getPredict(part: MultipartBody.Part): LiveData<Resource<HerbsResponse>> {
        return remoteDataSource.getPredict(part)
    }


    /**
     * LOCAL
     **/

    override fun getAllCapture(): LiveData<PagedList<CaptureEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(3)
            .setPageSize(3)
            .build()

        return LivePagedListBuilder(localDataSource.getAllCapture(), config).build()
    }
    override fun getCaptureWithPredicted(captureId : Int): LiveData<CaptureWithPredicted> {
        return localDataSource.getCaptureWithPredicted(captureId)
    }

    override fun addCapture(entity: CaptureEntity) {
        appExecutors.diskIO().execute{ localDataSource.addCapture(entity)}
    }

    override fun addPredicted(entity: PredictedEntity) {
        appExecutors.diskIO().execute{ localDataSource.addPredicted(entity)}
    }

    override fun getLastedCapture(): LiveData<List<CaptureEntity>> {
        return localDataSource.getLastedCapture()
    }

    override fun getPredictedByCaptureId(captureId: Int): LiveData<List<PredictedEntity>> {
        return localDataSource.getPredictedByCaptureId(captureId)
    }

    /**
     * FIREBASE
     **/


    override fun getHerbs(): LiveData<Resource<List<HerbsFirestore>>> {
        return firestoreDataStore.getHerbs()
    }

    override fun getRecipes(): LiveData<Resource<List<Recipe>>> {
        return firestoreDataStore.getRecipes()
    }

    override fun getHerbByName(name: String): LiveData<Resource<List<HerbsFirestore>>> {
         return firestoreDataStore.getHerbsByName(name)
    }

    override fun getRecipeByDocumentID(id: String): LiveData<List<Recipe>> {
        return firestoreDataStore.getRecipeByDocumentID(id)
    }

    override fun getRecipeByListID(listID: List<Int>): MutableLiveData<Resource<List<Recipe>>> {
        Log.d("DetailViewModel", "SUCCESS : $listID")

        return firestoreDataStore.getRecipesByListID(listID)
    }

    override fun searchHerbs(keyword: String): MutableLiveData<Resource<List<HerbsFirestore>>> {
        return firestoreDataStore.searchHerbs(keyword)
    }


}