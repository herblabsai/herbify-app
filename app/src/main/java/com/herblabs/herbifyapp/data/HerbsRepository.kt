package com.herblabs.herbifyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
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

    override fun getAllUserCapture(): LiveData<PagedList<CaptureEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(3)
            .setPageSize(3)
            .build()

        return LivePagedListBuilder(localDataSource.getAllFavorite(), config).build()
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

}