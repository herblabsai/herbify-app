package com.herblabs.herbifyapp.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "capture")
@Parcelize
data class CaptureEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "captureId")
    var captureId : Int = 0,

    @NonNull
    @ColumnInfo(name = "imageUri")
    var imageUri : String? = ""
) : Parcelable