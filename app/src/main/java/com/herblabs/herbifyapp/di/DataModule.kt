package com.herblabs.herbifyapp.di

import android.app.Application
import androidx.room.Room
import com.herblabs.herbifyapp.data.HerbsDataSource
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.local.LocalDataSource
import com.herblabs.herbifyapp.data.source.local.db.HerbifyDB
import com.herblabs.herbifyapp.data.source.local.db.HerbifyDao
import com.herblabs.herbifyapp.data.source.remote.RemoteDataSource
import com.herblabs.herbifyapp.data.source.remote.network.ApiConfig
import com.herblabs.herbifyapp.data.source.remote.network.ApiService
import com.herblabs.herbifyapp.utils.AppExecutors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideApiService() : ApiService {
        return ApiConfig.provideApiService()
    }


    private const val  DATABASE_NAME = "herbify.db"

    @Provides
    @Singleton
    fun providedeAppDatabase(app : Application) : HerbifyDB {
        return Room.databaseBuilder(
            app,
            HerbifyDB::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserFavoriteDao(appDatabase: HerbifyDB) : HerbifyDao {
        return appDatabase.herbifyDao()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        herbifyDao: HerbifyDao
    ) : LocalDataSource {
        return LocalDataSource(herbifyDao)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        herbifyDao: HerbifyDao
    ) : RemoteDataSource {
        return RemoteDataSource(provideApiService())
    }

    @Provides
    @Singleton
    fun provideExecutorService() : AppExecutors = AppExecutors()

    @Provides
    @Singleton
    fun provideHerbsRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        appExecutors: AppExecutors
    ) : HerbsDataSource {
        return HerbsRepository(remoteDataSource, localDataSource, appExecutors)
    }


}