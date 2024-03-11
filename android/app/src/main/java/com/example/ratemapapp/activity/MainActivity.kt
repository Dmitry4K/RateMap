package com.example.ratemapapp.activity

import android.os.Bundle
import android.widget.Button
import com.example.ratemapapp.R
import com.example.ratemapapp.map.extension.setLayer
import com.example.ratemapapp.map.layer.impl.AvgMeterPriceMapLayerImpl
import com.example.ratemapapp.map.layer.impl.MarkRateMapLayerImpl
import com.example.ratemapapp.map.listener.InputListenerImpl
import com.example.ratemapapp.map.listener.MaxZoomCameraListener
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.Layer
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition

private const val MAX_ZOOM = 12.0f

class MainActivity : MapKitActivity() {
    private lateinit var l: Layer
    private lateinit var inputListener: InputListenerImpl
    private lateinit var cameraListener: CameraListener
    private lateinit var refreshLayerButton: Button
    private lateinit var onMarkLayerButton: Button
    private lateinit var onAvgMeterPriceLayerButton: Button
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

        onMarkLayerButton.isClickable = false

        refreshLayerButton.setOnClickListener { l.clear() }
        onMarkLayerButton.setOnClickListener {
            l.remove()
            l = mapView.map.setLayer(rateMapLayers["marks"]!!)
            l.invalidate("0.0.0")
        }
        onAvgMeterPriceLayerButton.setOnClickListener {
            l.remove()
            l = mapView.map.setLayer(rateMapLayers["avgMetersPrice"]!!)
            l.invalidate("0.0.0")
        }
    }
}