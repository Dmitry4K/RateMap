package com.example.ratemapapp.service.impl

import com.example.ratemapapp.dto.PostMarkRequestBody
import com.example.ratemapapp.service.MarkService
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Call
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


private val BASE_URL = "http://10.0.2.2:8080/api/mark"

class MarkServiceImpl(
    private val objectMapper: ObjectMapper
) : MarkService {
    override fun setMark(lat: Double, lng: Double, mark: Double, depth: Long): Call {
        val json = objectMapper.writeValueAsBytes(PostMarkRequestBody(
            lat, lng, mark, depth
        ))
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull();
        val requestBody = RequestBody.create(mediaType, json)
        val url = BASE_URL.toHttpUrl()
            .newBuilder()
            .build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .put(requestBody)
            .build()
        val client = OkHttpClient.Builder().build()
        return client.newCall(request)
    }
}