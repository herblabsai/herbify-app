package com.herblabs.herbifyapp.utils

import com.google.gson.Gson
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse

object DummyData {

    fun getHerbResponse(): HerbsResponse {
        val json = "{\n" +
                "  \"data\" : [\n" +
                "    {\n" +
                "      \"name\" :\"Kumis Kucing\",\n" +
                "      \"confident\" : 98.5\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\" :\"Valerian\",\n" +
                "      \"confident\" : 70\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\" : \"Meniran\",\n" +
                "      \"confident\" : 60\n" +
                "    }\n" +
                "  ]\n" +
                "}"
        return Gson().fromJson(json, HerbsResponse::class.java)
    }

}