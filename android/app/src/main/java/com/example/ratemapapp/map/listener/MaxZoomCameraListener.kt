package com.example.ratemapapp.map.listener

import androidx.fragment.app.FragmentActivity
import com.example.ratemapapp.fragment.RatingFragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map

class MaxZoomCameraListener(
    private val fragmentActivity: FragmentActivity,
    private val maxZoom: Float
) : CameraListener {
    override fun onCameraPositionChanged(p0: Map, p1: CameraPosition, p2: CameraUpdateReason, p3: Boolean) {
        if (p1.zoom < maxZoom) {
            p0.move(
                CameraPosition(p1.target, maxZoom, p1.azimuth, p1.tilt),
                Animation(Animation.Type.LINEAR, 0F),
                null
            )
        }
        //Log.i("Camera", p1.target.let { it.latitude.toString() + ' ' + it.longitude.toString() + ' ' + p1.zoom} )
        fragmentActivity.supportFragmentManager
            .findFragmentByTag(RatingFragment.TAG)
            ?.let { it as RatingFragment }
            ?.apply { zoom = p1.zoom }
    }
}