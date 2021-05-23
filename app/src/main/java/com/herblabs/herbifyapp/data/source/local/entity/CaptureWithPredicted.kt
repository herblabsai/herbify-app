package com.herblabs.herbifyapp.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CaptureWithPredicted (
    @Embedded
    var mCapture: CaptureEntity,

    @Relation(parentColumn = "captureId", entityColumn = "predictedId")
    var mPredicted: List<PredictedEntity>

)