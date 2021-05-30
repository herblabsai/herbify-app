package com.herblabs.herbifyapp.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "predicted",
    primaryKeys = ["predictedId", "captureId"],
    foreignKeys = [ForeignKey(
        entity = CaptureEntity::class,
        parentColumns = ["captureId"],
        childColumns = ["captureId"])
    ],
    indices = [Index(
        value = ["predictedId"]),
        Index(value = ["captureId"])
    ]
)
data class PredictedEntity (
    @NonNull
    @ColumnInfo(name = "predictedId")
    var predictedId : String = "",

    @NonNull
    @ColumnInfo(name = "captureId")
    var captureId : Int = 0,

    @NonNull
    @ColumnInfo(name = "name")
    var name : String = "",

    @NonNull
    @ColumnInfo(name = "confident")
    var confident : Double = 0.0,

    @NonNull
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String = "",
) : Parcelable