package com.keremkulac.okeyscore.util

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.keremkulac.okeyscore.BuildConfig

class InterstitialAdManager private constructor() {

    companion object {
        @Volatile
        private var INSTANCE: InterstitialAdManager? = null

        private const val INTERSTITIAL_AD_UNIT_ID = BuildConfig.INTERSTITIAL_AD_UNIT_ID_RELEASE

        fun getInstance(): InterstitialAdManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: InterstitialAdManager().also { INSTANCE = it }
            }
        }
    }

    private var mInterstitialAd: InterstitialAd? = null
    private var isLoading = false

    interface InterstitialAdCallback {
        fun onAdLoaded() {}
        fun onAdFailedToLoad(error: String) {}
        fun onAdShown() {}
        fun onAdDismissed() {}
        fun onAdFailedToShow(error: String) {}
    }

    private var adCallback: InterstitialAdCallback? = null

    fun loadInterstitialAd(context: Context, callback: InterstitialAdCallback? = null) {
        if (isLoading) {
            Log.d("TAG", "Ad is already loading")
            return
        }

        if (mInterstitialAd != null) {
            Log.d("TAG", "Ad is already loaded")
            callback?.onAdLoaded()
            return
        }

        isLoading = true
        adCallback = callback

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(context, INTERSTITIAL_AD_UNIT_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("TAG", "Failed to load interstitial ad: ${adError.message}")
                isLoading = false
                mInterstitialAd = null
                adCallback?.onAdFailedToLoad(adError.message)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("TAG", "Interstitial ad loaded successfully")
                isLoading = false
                mInterstitialAd = interstitialAd
                setInterstitialAdCallbacks()
                adCallback?.onAdLoaded()
            }
        })
    }

    private fun setInterstitialAdCallbacks() {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                Log.d("TAG", "Interstitial ad clicked")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d("TAG", "Interstitial ad dismissed")
                mInterstitialAd = null
                adCallback?.onAdDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.e("TAG", "Failed to show interstitial ad: ${adError.message}")
                mInterstitialAd = null
                adCallback?.onAdFailedToShow(adError.message)
            }

            override fun onAdImpression() {
                Log.d("TAG", "Interstitial ad impression recorded")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("TAG", "Interstitial ad shown")
                adCallback?.onAdShown()
            }
        }
    }

    fun showInterstitialAd(activity: Activity): Boolean {
        return if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
            true
        } else {
            Log.d("TAG", "Interstitial ad is not ready yet")
            false
        }
    }

    fun showInterstitialAdWithAutoReload(activity: Activity, context: Context): Boolean {
        val shown = showInterstitialAd(activity)
        if (shown) {
            Handler(Looper.getMainLooper()).postDelayed({
                loadInterstitialAd(context)
            }, 1000)
        }
        return shown
    }

    fun isAdReady(): Boolean {
        return mInterstitialAd != null
    }

    fun preloadAd(context: Context) {
        if (!isAdReady() && !isLoading) {
            loadInterstitialAd(context)
        }
    }

    fun destroy() {
        mInterstitialAd = null
        adCallback = null
        isLoading = false
    }
}