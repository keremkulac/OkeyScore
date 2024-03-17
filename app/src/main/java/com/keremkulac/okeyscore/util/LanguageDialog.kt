package com.keremkulac.okeyscore.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.keremkulac.okeyscore.R

class LanguageDialog(activity : Activity,context: Context) : Dialog(context){
    private var language: String? = null

   init {
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.language_dialog, null)
        val spinner = view.findViewById<Spinner>(R.id.spinnerLanguage)
        val languages = arrayOf("Türkçe", "İngilizce")
        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, languages)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                language = languages[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        setContentView(view)
    }


    fun getLanguage() : String?{
        return language
    }

}