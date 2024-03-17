package com.keremkulac.okeyscore

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.keremkulac.okeyscore.util.updateResources
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    @Inject
    lateinit var defaultFragmentFactory: DefaultFragmentFactory
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navHostFragment : FragmentContainerView
    private lateinit var onboardingSharedPreferences: SharedPreferences
    private lateinit var nightModeSharedPreferences: SharedPreferences
    private lateinit var languageSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = defaultFragmentFactory
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        navHostFragment = findViewById(R.id.nav_host_fragment)
        onboardingSharedPreferences = getSharedPreferences("isOnboardingCompleted", MODE_PRIVATE)
        nightModeSharedPreferences = getSharedPreferences("isNightModeActive", MODE_PRIVATE)
        languageSharedPreferences = getSharedPreferences("selectedLanguage", MODE_PRIVATE)
        checkDisplaySize()
        themeListener()
        bottomNavigation()
        checkOnboarding()
        selectLanguage()
    }


   private fun themeListener(){
       val isNightModeActive = nightModeSharedPreferences.getBoolean("isNightModeActive",false)
        if(isNightModeActive){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun checkDisplaySize(){
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        if (width == 366 && height == 708) {
            Toast.makeText(this, "Uygulama bu cihazda çalışmıyor.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun bottomNavigation(){
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_new_game -> {
                    navHostFragment.findNavController().navigate(MainActivityDirections.actionMainActivityToChooseGameFragment())
                }
                R.id.menu_history -> {
                    navHostFragment.findNavController().navigate(MainActivityDirections.actionMainActivityToFinishedGameViewFragment("single"))
                }
            }
            true
        }
    }

    private fun checkOnboarding(){
        val isOnboardingCompleted = onboardingSharedPreferences.getBoolean("isOnboardingCompleted",false)
        onboardingSharedPreferences.edit().putBoolean("isOnboardingCompleted",isOnboardingCompleted ).apply()
            if(isOnboardingCompleted){
                bottomNavigationView.visibility = View.VISIBLE
            }else{
                bottomNavigationView.visibility = View.GONE
            }
    }

   private fun selectLanguage(){
       val selectedLanguage = languageSharedPreferences.getString("selectedLanguage","Türkçe")
        if(selectedLanguage == "İngilizce"){
            val locale = Locale("en", "EN")
            updateResources(this,locale)
        }else{
            val defaultLocale = Locale.getDefault()
            updateResources(this,defaultLocale)
        }
    }

}
