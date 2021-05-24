package com.herblabs.herbifyapp.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "capture")
data class CaptureEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "captureId")
    var captureId : Int = 0,

    @NonNull
    @ColumnInfo(name = "imageUri")
    var imageUri : String? = ""
)