package com.example.ratemapapp.map.listener

import android.util.Log
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map

class LoggingCameraListenerImpl : CameraListener {
    override fun onCameraPositionChanged(p0: Map, p1: CameraPosition, p2: CameraUpdateReason, p3: Boolean) {
        //Log.i("Camera", p1.target.let { it.latitude.toString() + ' ' + it.longitude.toString()} )
    }
}