package com.herblabs.herbifyapp.data.source.local.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.local.entity.CaptureWithPredicted
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity

@Dao
interface HerbifyDao {

    @Query("SELECT * FROM capture")
    fun getCapture(): DataSource.Factory<Int, CaptureEntity>

    @Transaction
    @Query("SELECT * FROM capture WHERE captureId = :captureId")
    fun getCourseWithModuleById(captureId: Int): LiveData<CaptureWithPredicted>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCapture(entity: CaptureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPredicted(entity: PredictedEntity)

}