package com.keremkulac.okeyscore.presentation.ui.settings

import  android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.MainActivity
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import com.keremkulac.okeyscore.util.SharedPrefHelper
import com.keremkulac.okeyscore.util.translateEN
import com.keremkulac.okeyscore.util.translateTR
import com.keremkulac.okeyscore.util.updateResources
import com.keremkulac.okeyscore.util.updateTheme
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper
    private val viewModel by viewModels<SettingsViewModel>()
    private lateinit var binding : FragmentSettingsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        checkNightMode()
        checkThemeSwitch()
        selectLanguage()
        goToChooseGameFragment()
        setLanguage()
    }

    private fun checkNightMode(){
        viewModel.isNightModeActive.observe(viewLifecycleOwner){isNightModeActive->
            isNightModeActive?.let {
                updateTheme(it)
                binding.darkModeSwitch.isChecked = it
            }
        }
    }

    private fun checkThemeSwitch(){
        binding.darkModeSwitch.setOnClickListener {
            viewModel.setNightModeSharedPreferencesValue(binding.darkModeSwitch.isChecked)
            recreate(requireActivity() as MainActivity)
        }
    }

    private fun selectLanguage(){
        val builder = AlertDialog.Builder(requireContext())
        binding.languageTextView.setOnClickListener {
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.language_dialog, null)
            val languages = arrayOf(requireContext().getString(R.string.turkish), requireContext().getString(R.string.english))
            val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
            val adapter = ArrayAdapter(requireActivity(),R.layout.dropdown_item, languages)
            autoCompleteTextView.setAdapter(adapter)
            builder.setView(view)
            builder.setPositiveButton("Onayla") { _ , _ ->
                val selectedLanguage = autoCompleteTextView.text.toString()
                if(selectedLanguage == "İngilizce" || selectedLanguage == "English"){
                    viewModel.setLanguageSharedPreferencesValue(translateEN(selectedLanguage))
                    val locale = Locale("en", "EN")
                    updateResources(requireActivity(),locale)
                    recreate(requireActivity() as MainActivity)
                }else{
                    viewModel.setLanguageSharedPreferencesValue(translateTR(selectedLanguage))
                    val defaultLocale = Locale.getDefault()
                    updateResources(requireActivity(),defaultLocale)
                    recreate(requireActivity() as MainActivity)
                }
            }
            builder.setNegativeButton("İptal") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun setLanguage() {
        viewModel.selectedLanguage.observe(viewLifecycleOwner) { selectedLanguage ->
            selectedLanguage?.let {
                binding.languageTextView.text = it
            }
        }
    }

    private fun goToChooseGameFragment(){
        binding.goToChooseGameFragment.setOnClickListener{
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToChooseGameFragment())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getNightModeSharedPreferencesValue()
    }
}