package com.keremkulac.okeyscore.util

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object InterstitialAdManager {

    private var interstitialAd: InterstitialAd? = null
    private const val TAG = "InterstitialAdManager"

    fun loadAd(activity: Activity,interstitialAdId : String, onLoaded: (() -> Unit)? = null, onFailed: (() -> Unit)? = null) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            activity,
            interstitialAdId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    Log.d(TAG, "Ad loaded")
                    onLoaded?.invoke()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    Log.e(TAG, "Ad failed to load: ${error.message}")
                    onFailed?.invoke()
                }
            }
        )
    }

    fun showAd(activity: Activity, onDismissed: (() -> Unit)? = null, onFailedToShow: (() -> Unit)? = null) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad dismissed")
                    interstitialAd = null
                    onDismissed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                    Log.e(TAG, "Ad failed to show: ${adError.message}")
                    interstitialAd = null
                    onFailedToShow?.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed")
                }
            }
            interstitialAd?.show(activity)
        } else {
            Log.d(TAG, "Ad is not ready")
            onFailedToShow?.invoke()
        }
    }

    fun isAdLoaded(): Boolean {
        return interstitialAd != null
    }

    fun clear() {
        interstitialAd = null
    }
}
