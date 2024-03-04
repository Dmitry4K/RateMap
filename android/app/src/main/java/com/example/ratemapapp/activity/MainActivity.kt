package com.example.ratemapapp.activity

import android.os.Bundle
import com.example.ratemapapp.R
import com.example.ratemapapp.map.listener.InputListenerImpl
import com.example.ratemapapp.map.listener.MaxZoomCameraListener
import com.example.ratemapapp.map.provider.ImageUrlProviderImpl
import com.example.ratemapapp.map.provider.RateMapUrlProviderImpl
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.geo.Projection
import com.yandex.mapkit.geometry.geo.Projections
import com.yandex.mapkit.layers.Layer
import com.yandex.mapkit.layers.LayerOptions
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener

private const val MAX_ZOOM = 12.0f

class MainActivity : MapKitActivity() {
    private lateinit var l: Layer
    private val imageUrlProvider = ImageUrlProviderImpl()
    private lateinit var inputListener: InputListener
    private val urlProvider = RateMapUrlProviderImpl()
    private lateinit var cameraListener: CameraListener
    private lateinit var projection: Projection
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
        projection = Projections.getWgs84Mercator()
        l = mapView.map.addLayer(
            "mapkit_logo",
            "image/png",
            LayerOptions(),
            urlProvider,
            imageUrlProvider,
            projection
        )
        mapView.map.set2DMode(true)
        inputListener = InputListenerImpl(this@MainActivity, mapView, l)
        cameraListener = MaxZoomCameraListener(this@MainActivity, MAX_ZOOM)
    }
}