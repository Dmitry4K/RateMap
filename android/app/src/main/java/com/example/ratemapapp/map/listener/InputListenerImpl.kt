package com.example.ratemapapp.map.listener

import androidx.fragment.app.FragmentActivity
import com.example.ratemapapp.R
import com.example.ratemapapp.fragment.RatingFragment
import com.example.ratemapapp.service.MarkService
import com.yandex.mapkit.geometry.LinearRing
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PolygonMapObject
import com.yandex.mapkit.mapview.MapView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class InputListenerImpl(
    private val fragmentActivity: FragmentActivity,
    private val mapView: MapView
) : InputListener {

    private val points: MutableList<Point> = mutableListOf()
    private var polygon: PolygonMapObject? = null
    override fun onMapTap(map: Map, point: Point) {
        val fragment = findFragment()
        points.add(point)
        if (fragment == null) {
            val ratingFragment = RatingFragment(
                { enqueueCallback() },
                { closeFragment() }
            )
            fragmentActivity.supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(androidx.appcompat.R.anim.abc_slide_in_bottom, androidx.appcompat.R.anim.abc_slide_out_bottom)
                .replace(R.id.fragmentContainer, ratingFragment, RatingFragment.TAG)
                .commit()
        } else {
            removePolygon()
            createPolygon()
        }
        updateHintText()
    }

    override fun onMapLongTap(map: Map, point: Point) {}

    private fun closeFragment() {
        val fragment = findFragment() ?: return
        fragmentActivity.supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                androidx.transition.R.anim.abc_slide_out_bottom,
                androidx.transition.R.anim.abc_slide_out_bottom
            )
            .remove(fragment)
            .commit()
        removePolygon()
        points.clear()
    }

    private fun findFragment() = fragmentActivity.supportFragmentManager.findFragmentByTag(RatingFragment.TAG) as RatingFragment?

    private fun enqueueCallback() {
        val callback = object: Callback {
            override fun onFailure(call: Call, e: IOException) = closeFragment()
            override fun onResponse(call: Call, response: Response) = closeFragment()
        }
        MarkService.default().setMark(0.0, 0.0, 0.0, 0).enqueue(callback)
    }

    private fun removePolygon() {
        if (polygon != null) {
            mapView.map.mapObjects.remove(polygon!!)
            polygon = null
        }
    }

    private fun createPolygon() { polygon = mapView.map.mapObjects.addPolygon(Polygon(LinearRing(points), listOf())) }

    private fun getAveragePoint() = Point(points.sumOf { it.latitude } / points.size, points.sumOf { it.longitude } / points.size)

    private fun updateHintText() { findFragment()?.setHintText(getAveragePoint()) }
}
