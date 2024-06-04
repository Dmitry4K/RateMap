package com.example.ratemapapp.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import java.io.File

private const val API_KEY = "60e03fd4-fbfe-4b18-9a54-42407c8b609d"


abstract class MapKitActivity: AppCompatActivity() {
    protected lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
        super.onCreate(savedInstanceState)
        Log.i("App", "OnCreate executed")
        if (savedInstanceState?.getBoolean("init") != true) {
            MapKitFactory.setApiKey(API_KEY)
        }
        MapKitFactory.initialize(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("init", true)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}