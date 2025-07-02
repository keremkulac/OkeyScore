package com.keremkulac.okeyscore.util

import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

object BannerAdManager {
    private const val TAG = "BannerAdManager"

    fun loadBannerAd(
        adView: AdView,
        onLoaded: (() -> Unit)? = null,
        onFailed: (() -> Unit)? = null
    ) {
        val adRequest = AdRequest.Builder().build()
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d(TAG, "Banner ad loaded")
                onLoaded?.invoke()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e(TAG, "Banner ad failed to load: ${adError.message}")
                onFailed?.invoke()
            }

            override fun onAdOpened() {
                Log.d(TAG, "Banner ad opened")
            }

            override fun onAdClosed() {
                Log.d(TAG, "Banner ad closed")
            }

            override fun onAdClicked() {
                Log.d(TAG, "Banner ad clicked")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Banner ad impression recorded")
            }
        }

        adView.loadAd(adRequest)
    }
}