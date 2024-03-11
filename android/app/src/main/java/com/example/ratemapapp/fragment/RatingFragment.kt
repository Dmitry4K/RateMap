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
import com.example.ratemapapp.R
import com.yandex.mapkit.geometry.Point


class RatingFragment(
    private val onClickSendButton: View.OnClickListener,
    private val onClickCloseButton: View.OnClickListener,
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
        sendButton.setOnClickListener(onClickSendButton)
        closeButton.setOnClickListener(onClickCloseButton)
    }

    fun setHintText(point: Point) {
        textView.text = resources.getString(
            R.string.rate_point_hint_text,
            point.latitude.toString().replace(',','.', true),
            point.longitude.toString().replace(',','.', true)
        )
    }

    companion object {
        const val TAG = "RATING_FRAGMENT"
    }
}