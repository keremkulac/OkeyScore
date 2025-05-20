package com.keremkulac.okeyscore.util

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.keremkulac.okeyscore.R

object CustomDialog {
    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        onNegativeClick: (() -> Unit)? = null,
        onPositiveClick: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_alert_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val titleTextView: TextView = dialog.findViewById(R.id.dialogTitle)
        val messageTextView: TextView = dialog.findViewById(R.id.dialogMessage)
        val positiveButton: TextView = dialog.findViewById(R.id.positiveButton)
        val negativeButton: TextView = dialog.findViewById(R.id.negativeButton)

        titleTextView.text = title
        messageTextView.text = message
        positiveButton.text = positiveButtonText
        negativeButton.text = negativeButtonText

        positiveButton.setOnClickListener {
            onPositiveClick()
            dialog.dismiss()
        }
        negativeButton.setOnClickListener {
            onNegativeClick?.let { negativeClick -> negativeClick() }
            dialog.dismiss()
        }

        dialog.show()
    }
}