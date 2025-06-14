package com.keremkulac.okeyscore.presentation.ui.settings

import  android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.MainActivity
import com.keremkulac.okeyscore.databinding.FragmentSettingsBinding
import com.keremkulac.okeyscore.util.BaseFragment
import com.keremkulac.okeyscore.util.LanguageSelectionDialog
import dagger.hilt.android.AndroidEntryPoint
import com.keremkulac.okeyscore.util.SharedPrefHelper
import com.keremkulac.okeyscore.util.TR_CODE
import com.keremkulac.okeyscore.util.translateEN
import com.keremkulac.okeyscore.util.translateTR
import com.keremkulac.okeyscore.util.updateResources
import com.keremkulac.okeyscore.util.updateTheme
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper
    private val viewModel: SettingsViewModel by viewModels()
    private var currentLanguageCode = ""
    private var currentLanguageName = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkNightMode()
        checkThemeSwitch()
        getCurrentLanguage()
        selectLanguage()
        goToChooseGameFragment()
        setLanguageText()
    }

    private fun checkNightMode() {
        viewModel.isNightModeActive.observe(viewLifecycleOwner) { isNightModeActive ->
            isNightModeActive?.let {
                updateTheme(it)
                binding.darkModeSwitch.isChecked = it
            }
        }
    }

    private fun checkThemeSwitch() {
        binding.darkModeSwitch.setOnClickListener {
            viewModel.setNightModeSharedPreferencesValue(binding.darkModeSwitch.isChecked)
            recreate(requireActivity() as MainActivity)
        }
    }

    private fun selectLanguage() {
        binding.languageTextView.setOnClickListener {
            val dialog =
                LanguageSelectionDialog(requireContext(), currentLanguageCode) { selectedLanguage ->
                    currentLanguageCode = selectedLanguage.code
                    currentLanguageName = selectedLanguage.name
                    binding.languageTextView.text = getTranslatedCurrentLanguage()
                    viewModel.setLanguageCodeSharedPreferencesValue(selectedLanguage.code)
                    viewModel.setLanguageNameSharedPreferencesValue(getTranslatedCurrentLanguage())
                    updateResources(requireActivity(), Locale(selectedLanguage.code))
                    recreate(requireActivity() as MainActivity)
                }

            dialog.show()
        }
    }

    private fun setLanguageText() {
        viewModel.selectedLanguageName.observe(viewLifecycleOwner) { selectedLanguageName ->
            binding.languageTextView.text = selectedLanguageName
        }
    }

    private fun getCurrentLanguage(){
        viewModel.selectedLanguageCode.observe(viewLifecycleOwner){currentLanguageCode->
            this.currentLanguageCode = currentLanguageCode!!
        }
    }

    private fun getTranslatedCurrentLanguage() : String{
        if (currentLanguageCode == TR_CODE){
            return translateTR(currentLanguageName)
        }
        return translateEN(currentLanguageName)
    }

    private fun goToChooseGameFragment() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToChooseGameFragment())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getNightModeSharedPreferencesValue()
    }
}