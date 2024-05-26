package com.example.ratemapapp.service.impl

import com.example.ratemapapp.dto.Consts.RATEMAP_API_URL
import com.example.ratemapapp.dto.GeoPoint
import com.example.ratemapapp.dto.PostMarkRequestBody
import com.example.ratemapapp.service.MarkService
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Call
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.time.Duration
import java.time.temporal.ChronoUnit


private const val BASE_URL = "$RATEMAP_API_URL/v1/mark"

class MarkServiceImpl(
    private val objectMapper: ObjectMapper
) : MarkService {
    override fun setMark(mark: Double, polygon: List<GeoPoint>): Call {
        val postMarkRequestBody = PostMarkRequestBody(polygon, mark)
        val json = objectMapper.writeValueAsBytes(postMarkRequestBody)
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, json)
        val url = BASE_URL.toHttpUrl()
            .newBuilder()
            .build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .put(requestBody)
            .build()
        val client = OkHttpClient.Builder()
            .callTimeout(Duration.of(1, ChronoUnit.SECONDS))
            .build()
        return client.newCall(request)
    }
}