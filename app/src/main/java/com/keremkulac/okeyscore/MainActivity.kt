package com.keremkulac.okeyscore

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    @Inject
    lateinit var defaultFragmentFactory: DefaultFragmentFactory
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navHostFragment : FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = defaultFragmentFactory
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        navHostFragment = findViewById(R.id.nav_host_fragment)
        themeListener()
        checkDisplaySize()
        bottomNavigation()
    }

   private fun themeListener(){
        addOnConfigurationChangedListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            this.recreate()
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
}
