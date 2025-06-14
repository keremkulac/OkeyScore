package com.keremkulac.okeyscore.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.presentation.ui.settings.Language
import com.keremkulac.okeyscore.presentation.ui.settings.LanguageAdapter

class LanguageSelectionDialog(
    context: Context,
    private val currentLanguageCode: String,
    private val onLanguageSelected: (Language) -> Unit
) : Dialog(context) {

    private lateinit var etSearch: EditText
    private lateinit var rvLanguages: RecyclerView
    private lateinit var languageAdapter: LanguageAdapter

    private val languages = listOf(
        Language(TR_CODE, context.getString(R.string.turkish), R.drawable.ic_turkey_flag),
        Language(EN_CODE, context.getString(R.string.english), R.drawable.ic_uk_flag)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_language_selection)

        window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.9).toInt(),
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        initViews()
        setupRecyclerView()
        setupSearch()
        languageAdapter.updateSelection(currentLanguageCode)
    }

    private fun initViews() {
        etSearch = findViewById(R.id.etSearch)
        rvLanguages = findViewById(R.id.rvLanguages)
    }

    private fun setupRecyclerView() {
        languageAdapter = LanguageAdapter(languages) { selectedLanguage ->
            onLanguageSelected(selectedLanguage)
            dismiss()
        }

        rvLanguages.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = languageAdapter
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                languageAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}

