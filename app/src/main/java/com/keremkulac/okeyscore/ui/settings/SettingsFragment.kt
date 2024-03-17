package com.keremkulac.okeyscore.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import com.keremkulac.okeyscore.util.LanguageDialog
import com.keremkulac.okeyscore.util.updateResources
import java.util.Locale

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding : FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferences2: SharedPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        sharedPreferences = requireActivity().getSharedPreferences("isNightModeActive", AppCompatActivity.MODE_PRIVATE)
        sharedPreferences2 = requireActivity().getSharedPreferences("selectedLanguage", AppCompatActivity.MODE_PRIVATE)
        checkNightMode()
        checkThemeSwitch()
        selectLanguage()
        goToChooseGameFragment()
    }


    private fun checkNightMode(){
        val isNightModeActive = sharedPreferences.getBoolean("isNightModeActive",false)
        if(!isNightModeActive){
            binding.darkModeSwitch.isChecked = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }else{
            binding.darkModeSwitch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun checkThemeSwitch(){
        binding.darkModeSwitch.setOnCheckedChangeListener{_,isChecked->
            if(!isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putBoolean("isNightModeActive",false).apply()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putBoolean("isNightModeActive",true).apply()
            }
            requireActivity().recreate()
        }
    }


    private fun selectLanguage(){
        val selectedLanguage = sharedPreferences2.getString("selectedLanguage","Türkçe")
        val dialog = LanguageDialog(requireActivity(),requireContext())
        if(dialog.getLanguage() != null){
            binding.languageTextView.text = dialog.getLanguage()
        }else{
            binding.languageTextView.text = selectedLanguage
        }
        binding.languageTextView.setOnClickListener {
            dialog.setOnDismissListener{
                binding.languageTextView.text = dialog.getLanguage()
                sharedPreferences2.edit().putString("selectedLanguage",binding.languageTextView.text.toString()).apply()
                if(dialog.getLanguage() == "İngilizce"){
                    val locale = Locale("en", "EN")
                    updateResources(requireActivity(),locale)
                }else{
                    val defaultLocale = Locale.getDefault()
                    updateResources(requireActivity(),defaultLocale)
                }
                requireActivity().recreate()
            }
            dialog.show()
        }
    }

    private fun goToChooseGameFragment(){
        binding.goToChooseGameFragment.setOnClickListener{
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToChooseGameFragment())
        }
    }

}