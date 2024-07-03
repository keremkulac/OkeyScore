package com.keremkulac.okeyscore

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
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
    private lateinit var appUpdateManager: AppUpdateManager
    private val requestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUpdate()
        themeListener()
        supportFragmentManager.fragmentFactory = defaultFragmentFactory
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        navHostFragment = findViewById(R.id.nav_host_fragment)
        checkDisplaySize()
        selectLanguage()
        bottomNavigation()
        checkOnboarding()
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
        for (item in bottomNavigationView.menu.children) {
            if (item.itemId == R.id.menu_new_game){
                item.setTitle(getString(R.string.new_game_record))
            }
            if (item.itemId == R.id.menu_history){
                item.setTitle(getString(R.string.history))
            }
        }
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
                val locale = Locale("tr", "TR")
                updateResources(this,locale)
            }
        }
    }

    private fun checkUpdate(){
        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    requestCode
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCode) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, this.resources.getString(R.string.update_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
