package com.example.ratemapapp.service

import com.example.ratemapapp.service.impl.MapLegendServiceImpl
import okhttp3.Call

interface MapLegendService {
    fun getMapLegend(mapName: String): Call

    companion object {
        fun default(): MapLegendService {
            return MapLegendServiceImpl()
        }
    }
}