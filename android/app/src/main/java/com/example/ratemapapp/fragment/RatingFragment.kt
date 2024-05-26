package com.example.ratemapapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.ratemapapp.R
import com.example.ratemapapp.dto.GeoPoint
import com.example.ratemapapp.service.MarkService
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class RatingFragment(
    private val activity: FragmentActivity,
    private val mapView: MapView,
    private val initPoint: Point
) : Fragment() {
    private val ratingPolygon: RatingPolygon = RatingPolygon(mapView.map.mapObjects)
    private lateinit var view: View
    private lateinit var stars: RatingBar
    private lateinit var sendButton: Button
    private lateinit var closeButton: ImageButton
    private lateinit var textView: TextView
    private lateinit var rootLayout: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        view = inflater.inflate(R.layout.rate_fragment, container, false)
        stars = view.findViewById(R.id.ratingBar)
        sendButton = view.findViewById(R.id.rateButton)
        closeButton = view.findViewById(R.id.rateFragmentCloseButton)
        textView = view.findViewById(R.id.rateFragmentTextViewPointHint)
        rootLayout = view.findViewById(R.id.rateFragmentRootLayout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addPolygonPoint(initPoint)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        sendButton.setOnClickListener { enqueueCallback() }
        closeButton.setOnClickListener { enqueueCallback() }
        rootLayout.setOnClickListener { moveToCenter() }
    }

    override fun onDestroy() {
        ratingPolygon.removePolygon()
        super.onDestroy()
    }

    fun addPolygonPoint(point: Point) {
        ratingPolygon.addPoint(point)
        setHintText(point)
    }

    private fun setHintText(point: Point) {
        textView.text = resources.getString(
            R.string.rate_point_hint_text,
            point.latitude.toString().replace(',','.', true),
            point.longitude.toString().replace(',','.', true)
        )
    }

    private fun moveToCenter() {
        val point = ratingPolygon.getAveragePoint()
        mapView.map.move(
            CameraPosition(point, mapView.map.cameraPosition.zoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 2F),
            null
        )
    }

    private fun enqueueCallback() {
        val callback = object: Callback {
            override fun onFailure(call: Call, e: IOException) = closeFragment()
            override fun onResponse(call: Call, response: Response) = closeFragment()
        }
        val polygon = ratingPolygon.getPoints().map { GeoPoint(lat = it.latitude, lng = it.longitude) }
        MarkService.default().setMark(stars.rating.toDouble(), polygon).enqueue(callback)
    }

    private fun closeFragment() {
        activity.supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(androidx.transition.R.anim.abc_slide_out_bottom, androidx.transition.R.anim.abc_slide_out_bottom)
            .remove(this@RatingFragment)
            .commit()
        activity.runOnUiThread { ratingPolygon.removePolygon() }
    }

    companion object {
        const val TAG = "RATING_FRAGMENT"
    }
}