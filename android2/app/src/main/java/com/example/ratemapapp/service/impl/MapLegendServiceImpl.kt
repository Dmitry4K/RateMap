package com.example.ratemapapp.service.impl

import com.example.ratemapapp.dto.Consts
import com.example.ratemapapp.service.MapLegendService
import okhttp3.Call
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Duration
import java.time.temporal.ChronoUnit

private const val BASE_URL = "${Consts.RATEMAP_API_URL}/v1/layer/%s/legend/png?width=140&height=600&fontSize=40"

class MapLegendServiceImpl: MapLegendService {
    override fun getMapLegend(mapName: String): Call {
        val url = String.format(BASE_URL, mapName).toHttpUrl()
            .newBuilder()
            .build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .get()
            .build()
        val client = OkHttpClient.Builder()
            .callTimeout(Duration.of(1, ChronoUnit.SECONDS))
            .build()
        return client.newCall(request)
    }
}
