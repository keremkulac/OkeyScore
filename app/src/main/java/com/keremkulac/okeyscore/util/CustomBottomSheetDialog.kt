package com.keremkulac.okeyscore.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.keremkulac.okeyscore.R

object CustomBottomSheetDialog {

    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        onNegativeClick: (() -> Unit)? = null,
        onPositiveClick: () -> Unit
    ) {
        val dialog = BottomSheetDialog(context, R.style.CustomBottomSheetDialog)
        val view = LayoutInflater.from(context).inflate(R.layout.custom_bottom_sheet_dialog, null)
        dialog.setOnShowListener {
            dialog.hideSystemBars(dialog)
        }
        val titleTextView: TextView = view.findViewById(R.id.dialogTitle)
        val messageTextView: TextView = view.findViewById(R.id.dialogMessage)
        val positiveButton: TextView = view.findViewById(R.id.positiveButton)
        val negativeButton: TextView = view.findViewById(R.id.negativeButton)
        titleTextView.text = title
        messageTextView.text = message
        positiveButton.text = positiveButtonText
        negativeButton.text = negativeButtonText

        positiveButton.setOnClickListener {
            onPositiveClick()
            dialog.dismiss()
        }

        negativeButton.setOnClickListener {
            onNegativeClick?.invoke()
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.setBackgroundResource(android.R.color.transparent)

        dialog.setCancelable(true)

        dialog.show()
    }

}
