package com.keremkulac.okeyscore

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.keremkulac.okeyscore.util.GAME_TYPE_SINGLE
import com.keremkulac.okeyscore.util.SharedPrefHelper
import com.keremkulac.okeyscore.util.UPDATE_REQUEST_CODE
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
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkForUpdate()
        themeListener()
        supportFragmentManager.fragmentFactory = defaultFragmentFactory
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        checkDisplaySize()
        selectLanguage()
        bottomNavigation()
        checkOnboarding()
        setBottomNavigationVisibility()
    }

    private fun themeListener() {
        val isNightModeActive = sharedPrefHelper.getNightModeSharedPreferencesValue()
        updateTheme(isNightModeActive)
    }

    private fun checkDisplaySize() {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        if (width == 366 && height == 708) {
            Toast.makeText(
                this,
                this.getString(R.string.error_not_work_this_device),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun setBottomNavigationVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.finishedSingleGameDetailFragment -> {
                    hideBottomNav()
                }

                R.id.finishedPartnerGameDetailFragment -> {
                    hideBottomNav()
                }

                R.id.splashFragment -> {
                    hideBottomNav()
                }

                R.id.savePartnerGameFragment -> hideBottomNav()
                R.id.saveSingleGameFragment -> hideBottomNav()
                R.id.onboardingFragment -> hideBottomNav()
                else -> {
                    showBottomNav()
                }
            }
        }
    }

    private fun bottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph)

        for (item in bottomNavigationView.menu.children) {
            if (item.itemId == R.id.menu_new_game) {
                item.setTitle(getString(R.string.home))
            }
            if (item.itemId == R.id.menu_past_games) {
                item.setTitle(getString(R.string.past_games))
            }
            if (item.itemId == R.id.menu_settings) {
                item.setTitle(getString(R.string.settings))
            }
        }
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_new_game -> {
                    navHostFragment.navController.navigate(MainActivityDirections.actionMainActivityToChooseGameFragment())
                }

                R.id.menu_past_games -> {
                    navHostFragment.navController.navigate(
                        MainActivityDirections.actionMainActivityToFinishedGameViewFragment(
                            GAME_TYPE_SINGLE
                        )
                    )
                }

                R.id.menu_settings -> {
                    navHostFragment.navController.navigate(MainActivityDirections.actionMainActivityToSettingsFragment())
                }
            }
            true
        }
    }

    private fun checkOnboarding() {
        val isOnboardingCompleted = sharedPrefHelper.getOnBoardingSharedPreferencesValue()
        sharedPrefHelper.setOnBoardingSharedPreferencesValue(isOnboardingCompleted)
        if (isOnboardingCompleted) {
            bottomNavigationView.visibility = View.VISIBLE
        } else {
            bottomNavigationView.visibility = View.GONE
        }
    }

    private fun selectLanguage() {
        val selectedLanguage = sharedPrefHelper.getLanguageCodeSharedPreferencesValue()
        updateResources(this, Locale(selectedLanguage))
    }

    private fun checkForUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    UPDATE_REQUEST_CODE.toInt()
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UPDATE_REQUEST_CODE.toInt()) {
            if (resultCode != RESULT_OK) {
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showBottomNav() {
        bottomNavigationView.visibility = View.VISIBLE
    }

    fun hideBottomNav() {
        bottomNavigationView.visibility = View.GONE
    }
}
