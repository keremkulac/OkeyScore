package com.keremkulac.okeyscore.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.keremkulac.okeyscore.R

class LanguageDialog(activity : Activity,context: Context,sharedPrefHelper: SharedPrefHelper) : Dialog(context){
    private var language: String? = null

   init {
       val selectedLanguage = sharedPrefHelper.getLanguageSharedPreferencesValue()
       val inflater = activity.layoutInflater
       val view = inflater.inflate(R.layout.language_dialog, null)
       val languages = arrayOf(context.getString(R.string.turkish), context.getString(R.string.english))
       val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
       val adapter = ArrayAdapter(activity,R.layout.dropdown_item, languages)
       autoCompleteTextView.setAdapter(adapter)
       autoCompleteTextView.showDropDown()
       autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
           if(languages[i] != selectedLanguage){
               language = languages[i]
           }
       }
       setContentView(view)
   }

    fun getLanguage() : String?{
        return language
    }
}