package com.example.ratemapapp.service

import com.example.ratemapapp.dto.GeoPoint
import com.example.ratemapapp.service.impl.MarkServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Call

interface MarkService {
    fun setMark(mark: Double, polygon: List<GeoPoint>): Call
    companion object {
        fun default(): MarkService {
            return MarkServiceImpl(ObjectMapper())
        }
    }
}