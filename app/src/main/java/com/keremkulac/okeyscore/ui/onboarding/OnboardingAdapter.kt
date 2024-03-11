package com.keremkulac.okeyscore.ui.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.keremkulac.okeyscore.R
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator


class OnboardingAdapter(private val context: Context, private val images: List<Int>, private val viewPager: ViewPager) :
    PagerAdapter() {

    var clickListener: (() -> Unit)? = null

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.onboarding_item, container, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val indicator = view.findViewById<SpringDotsIndicator>(R.id.indicator)
        val skip = view.findViewById<TextView>(R.id.skip)
        skip.setOnClickListener {
            clickListener!!.invoke()
        }
        indicator.attachTo(viewPager)
        imageView.setImageResource(images[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}