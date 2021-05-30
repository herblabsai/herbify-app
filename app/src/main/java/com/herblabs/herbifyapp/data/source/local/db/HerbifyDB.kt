package com.herblabs.herbifyapp.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity

@Database(entities = [CaptureEntity::class, PredictedEntity::class], version = 1, exportSchema = false)
abstract class HerbifyDB : RoomDatabase() {
    abstract fun herbifyDao(): HerbifyDao
}
