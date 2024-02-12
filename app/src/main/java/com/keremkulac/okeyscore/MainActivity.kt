package com.keremkulac.okeyscore

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    @Inject
    lateinit var defaultFragmentFactory: DefaultFragmentFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = defaultFragmentFactory
        setContentView(R.layout.activity_main)
        themeListener()
        checkDisplaySize()
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

}
