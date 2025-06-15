package com.keremkulac.okeyscore.util

import androidx.recyclerview.widget.DiffUtil
import com.keremkulac.okeyscore.presentation.ui.settings.Language

class LanguageDiffCallback(
    private val oldList: List<Language>,
    private val newList: List<Language>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].code == newList[newItemPosition].code
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
