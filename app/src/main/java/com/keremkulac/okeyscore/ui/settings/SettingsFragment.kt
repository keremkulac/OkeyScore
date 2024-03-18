package com.keremkulac.okeyscore.ui.settings

import  android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import com.keremkulac.okeyscore.util.LanguageDialog
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
            requireActivity().recreate()
        }
    }

    private fun selectLanguage(){
        val dialog = LanguageDialog(requireActivity(),requireContext(),sharedPrefHelper)
        binding.languageTextView.setOnClickListener {
            dialog.setOnDismissListener{
                if(dialog.getLanguage() != "" && dialog.getLanguage() != null){
                    if(dialog.getLanguage() == "İngilizce" || dialog.getLanguage() == "English"){
                        viewModel.setLanguageSharedPreferencesValue(translateEN(dialog.getLanguage().toString()))
                        val locale = Locale("en", "EN")
                       updateResources(requireActivity(),locale)
                    }else{
                        viewModel.setLanguageSharedPreferencesValue(translateTR(dialog.getLanguage().toString()))
                        val defaultLocale = Locale.getDefault()
                        updateResources(requireActivity(),defaultLocale)
                    }
                }
                requireActivity().recreate()
            }
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