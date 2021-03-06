package com.herblabs.herbifyapp.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.data.HerbsDataSource
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.firebase.FirebaseDataSource
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
import dagger.hilt.components.SingletonComponent
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
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiService: ApiService
    ) : RemoteDataSource {
        return RemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideFirestoreDataStore(
        firebaseFirestore: FirebaseFirestore
    ) : FirebaseDataSource {
        return FirebaseDataSource(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideExecutorService() : AppExecutors = AppExecutors()

    @Provides
    @Singleton
    fun provideHerbsRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        firebaseDataSource: FirebaseDataSource,
        appExecutors: AppExecutors
    ) : HerbsDataSource {
        return HerbsRepository(remoteDataSource, localDataSource, firebaseDataSource, appExecutors)
    }



}