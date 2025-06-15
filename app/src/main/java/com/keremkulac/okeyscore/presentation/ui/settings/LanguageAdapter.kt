package com.keremkulac.okeyscore.presentation.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.util.LanguageDiffCallback
import com.keremkulac.okeyscore.util.TR_CODE
import java.util.Locale


data class Language(
    val code: String,
    val name: String,
    val flagResId: Int,
    var isSelected: Boolean = false
)

class LanguageAdapter(
    private var languages: List<Language>,
    private val onLanguageSelected: (Language) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    private var filteredLanguages = languages.toMutableList()

    class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flagImageView: ImageView = itemView.findViewById(R.id.flagImageView)
        val languageTextView: TextView = itemView.findViewById(R.id.languageTextView)
        val checkImageView: ImageView = itemView.findViewById(R.id.checkImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = filteredLanguages[position]

        holder.flagImageView.setImageResource(language.flagResId)
        holder.languageTextView.text = language.name
        holder.checkImageView.visibility = if (language.isSelected) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            languages.forEach { it.isSelected = false }
            filteredLanguages.forEach { it.isSelected = false }

            language.isSelected = true
            onLanguageSelected(language)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = filteredLanguages.size

    fun filter(query: String) {
        val localeTR = Locale(TR_CODE)

        val newFilteredList = if (query.isEmpty()) {
            languages.toMutableList()
        } else {
            languages.filter {
                it.name.lowercase(localeTR).contains(query.lowercase(localeTR))
            }.toMutableList()
        }

        val diffCallback = LanguageDiffCallback(filteredLanguages, newFilteredList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        filteredLanguages = newFilteredList
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateSelection(selectedLanguageCode: String) {
        val newList = languages.map {
            it.copy(isSelected = it.code == selectedLanguageCode)
        }

        val diffCallback = LanguageDiffCallback(filteredLanguages, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        filteredLanguages = newList.toMutableList()
        languages = newList
        diffResult.dispatchUpdatesTo(this)
    }

}

