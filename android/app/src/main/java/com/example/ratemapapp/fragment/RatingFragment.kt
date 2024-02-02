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
import com.example.ratemapapp.components.PlaceMark
import com.example.ratemapapp.service.MarkService
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.Layer
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.math.pow


class RatingFragment(
    private val fragmentActivity: FragmentActivity,
    private val placeMark: PlaceMark,
    private val mapView: MapView,
    private val point: Point,
    private val markService: MarkService,
    private val zoom: Float,
    private val layer: Layer
) : Fragment() {
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

    override fun onStart() {
        super.onStart()
        val callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                close()
            }
            override fun onResponse(call: Call, response: Response) {
                layer.clear()
                close()
            }
        }
        sendButton.setOnClickListener {
            val depth = getDepth(zoom)
            markService.setMark(point.latitude, point.longitude, stars.rating.toDouble(), depth).enqueue(callback)
        }
        closeButton.setOnClickListener {
            close()
        }
        placeMark.place(point)
            .also { moveCamera(it) }
            .also { setHintText(it.latitude, it.longitude) }
            .also { p -> rootLayout.setOnClickListener { moveCamera(p) } }
    }

    override fun onStop() {
        super.onStop()
        placeMark.remove()
    }

    private fun moveCamera(point: Point) {
        mapView.map.move(
            CameraPosition(point, mapView.map.cameraPosition.zoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1F),
            null
        )
    }

    private fun close() {
        fragmentActivity.supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                androidx.transition.R.anim.abc_slide_out_bottom,
                androidx.transition.R.anim.abc_slide_out_bottom
            )
            .remove(this)
            .commit()
    }

    private fun setHintText(lat: Double, lng: Double) {
        textView.text = resources.getString(
            R.string.rate_point_hint_text,
            lat.toString().replace(',','.', true),
            lng.toString().replace(',','.', true)
        )
    }

    private fun getDepth(zoom: Float): Long {
        return (7340855.913171174 / 1.5 / 2.0.pow(zoom - 1.0)).toLong()
    }
}