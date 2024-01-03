package com.example.ratemapapp.map.listener

import androidx.fragment.app.FragmentActivity
import com.example.ratemapapp.R
import com.example.ratemapapp.components.impl.PlaceMarkImpl
import com.example.ratemapapp.fragment.RatingFragment
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView

class InputListenerImpl(
    private val fragmentActivity: FragmentActivity,
    private val mapView: MapView
) : InputListener {
    override fun onMapTap(map: Map, point: Point) {
        val ratingFragment = RatingFragment(
            fragmentActivity,
            PlaceMarkImpl(map),
            mapView,
            point
        )
        fragmentActivity.supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(androidx.appcompat.R.anim.abc_slide_in_bottom, androidx.appcompat.R.anim.abc_slide_out_bottom)
            .replace(R.id.fragmentContainer, ratingFragment)
            .commit()
    }

    override fun onMapLongTap(map: Map, point: Point) {
    }
}