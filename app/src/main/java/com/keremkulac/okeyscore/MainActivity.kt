package com.keremkulac.okeyscore

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.keremkulac.okeyscore.util.SharedPrefHelper
import com.keremkulac.okeyscore.util.updateResources
import com.keremkulac.okeyscore.util.updateTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    @Inject
    lateinit var defaultFragmentFactory: DefaultFragmentFactory
    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navHostFragment : FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = defaultFragmentFactory
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        navHostFragment = findViewById(R.id.nav_host_fragment)
        checkDisplaySize()
        themeListener()
        bottomNavigation()
        checkOnboarding()
        selectLanguage()
    }


    private fun themeListener(){
        val isNightModeActive = sharedPrefHelper.getNightModeSharedPreferencesValue()
        updateTheme(isNightModeActive)
    }

    private fun checkDisplaySize(){
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        if (width == 366 && height == 708) {
            Toast.makeText(this, this.getString(R.string.error_not_work_this_device), Toast.LENGTH_SHORT).show()
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
        val isOnboardingCompleted = sharedPrefHelper.getOnBoardingSharedPreferencesValue()
        sharedPrefHelper.setOnBoardingSharedPreferencesValue(isOnboardingCompleted)
        if(isOnboardingCompleted){
            bottomNavigationView.visibility = View.VISIBLE
        }else{
            bottomNavigationView.visibility = View.GONE
        }
    }

    private fun selectLanguage(){
        val selectedLanguage = sharedPrefHelper.getLanguageSharedPreferencesValue()
        selectedLanguage?.let {
            if(it == "İngilizce" || it== "English"){
                val locale = Locale("en", "EN")
                updateResources(this,locale)
            }else{
                val defaultLocale = Locale.getDefault()
                updateResources(this,defaultLocale)
            }
        }
    }
}
