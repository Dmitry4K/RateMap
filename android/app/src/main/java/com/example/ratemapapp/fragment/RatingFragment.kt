package com.example.ratemapapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.ratemapapp.R
import com.example.ratemapapp.components.PlaceMark
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView


class RatingFragment(
    private val fragmentActivity: FragmentActivity,
    private val placeMark: PlaceMark,
    private val mapView: MapView,
    private val point: Point
) : Fragment() {
    private lateinit var view: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        view = inflater.inflate(R.layout.rate_fragment, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        view.findViewById<Button>(R.id.rateButton).setOnClickListener {
            close()
        }
        view.findViewById<ImageButton>(R.id.rateFragmentCloseButton).setOnClickListener {
            close()
        }
        val p = placeMark.place(point).also { moveCamera(it) }
        view.findViewById<TextView>(R.id.rateFragmentTextViewPointHint).text = resources.getString(
            R.string.rate_point_hint_text,
            p.latitude.toString().replace(',','.', true),
            p.longitude.toString().replace(',','.', true)
        )
        view.findViewById<ViewGroup>(R.id.rateFragmentRootLayout).setOnClickListener { moveCamera(p) }
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
}