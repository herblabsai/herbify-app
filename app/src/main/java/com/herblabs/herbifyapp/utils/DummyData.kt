package com.herblabs.herbifyapp.utils

import com.google.gson.Gson
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import com.herblabs.herbifyapp.data.source.remote.response.Data
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse

object DummyData {

    fun getLabelPredicted(): List<Data> {
        val json = "{\n" +
                "  \"data\" : [\n" +
                "    {\n" +
                "      \"plant\" :\"Kumis Kucing\",\n" +
                "      \"score\" : 98.5\n" +
                "    },\n" +
                "    {\n" +
                "      \"plant\" :\"Valerian\",\n" +
                "      \"score\" : 70\n" +
                "    },\n" +
                "    {\n" +
                "      \"plant\" : \"Meniran\",\n" +
                "      \"score\" : 60\n" +
                "    }\n" +
                "  ]\n" +
                "}"
        val response =  Gson().fromJson(json, HerbsResponse::class.java)
        return response.data
    }
}