package com.herblabs.herbifyapp.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.*

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
    @ColumnInfo(name = "label")
    var label : String = "",

    @NonNull
    @ColumnInfo(name = "confident")
    var confident : Int = 0
)