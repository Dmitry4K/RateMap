package com.example.ratemapapp.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.example.ratemapapp.R
import com.example.ratemapapp.map.extension.setLayer
import com.example.ratemapapp.map.layer.impl.AvgMeterPriceMapLayerImpl
import com.example.ratemapapp.map.layer.impl.MarkRateMapLayerImpl
import com.example.ratemapapp.map.listener.InputListenerImpl
import com.example.ratemapapp.map.listener.MaxZoomCameraListener
import com.example.ratemapapp.service.MapLegendService
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.Layer
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


private const val MAX_ZOOM = 12.0f

class MainActivity : MapKitActivity() {
    private lateinit var l: Layer
    private lateinit var inputListener: InputListenerImpl
    private lateinit var cameraListener: CameraListener
    private lateinit var refreshLayerButton: Button
    private lateinit var onMarkLayerButton: Button
    private lateinit var onAvgMeterPriceLayerButton: Button
    private lateinit var imageView: ImageView
    private val rateMapLayers = listOf(
        MarkRateMapLayerImpl(),
        AvgMeterPriceMapLayerImpl()
    ).associateBy { it.getName() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        mapView.map.move(
            CameraPosition(Point(55.752776, 37.617669), MAX_ZOOM + 2.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
        mapView.map.addCameraListener(cameraListener)
        mapView.map.addInputListener(inputListener)
        l.invalidate("0.0.0")
    }

    private fun init() {
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapView)
        mapView.map.set2DMode(true)
        rateMapLayers.values.forEach { it.init() }
        l = mapView.map.setLayer(rateMapLayers["marks"]!!)
        inputListener = InputListenerImpl(this@MainActivity, mapView)
        cameraListener = MaxZoomCameraListener(MAX_ZOOM)

        refreshLayerButton = findViewById(R.id.layer_refresh_button)
        onMarkLayerButton = findViewById(R.id.mark_layer_on_button)
        onAvgMeterPriceLayerButton = findViewById(R.id.avg_meter_price_layer_on_button)
        imageView = findViewById(R.id.map_legend)
        setLegend("marks")

        onMarkLayerButton.isClickable = false

        refreshLayerButton.setOnClickListener { l.clear() }
        onMarkLayerButton.setOnClickListener {
            l.remove()
            l = mapView.map.setLayer(rateMapLayers["marks"]!!)
            setLegend("marks")
            l.invalidate("0.0.0")
        }
        onAvgMeterPriceLayerButton.setOnClickListener {
            l.remove()
            l = mapView.map.setLayer(rateMapLayers["avgMetersPrice"]!!)
            setLegend("avgMeterPrice")
            l.invalidate("0.0.0")
        }
    }

    private fun setLegend(mapName: String) {
        val callback = object: Callback {
            override fun onFailure(call: Call, e: IOException) = onLegendFailed()
            override fun onResponse(call: Call, response: Response) = setImageViewContent(response)
        }
        MapLegendService.default()
            .getMapLegend(mapName)
            .enqueue(callback)
    }

    private fun setImageViewContent(response: Response) {
        if (response.code == 200) {
            val byteArray = response.body?.byteStream()?.readBytes()!!
            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            imageView.setImageBitmap(Bitmap.createBitmap(bmp))
        } else {
            onLegendFailed()
        }
    }

    private fun onLegendFailed() {
        Log.w("MAP LEGEND", "CANNOT GET LEGEND")
    }
}