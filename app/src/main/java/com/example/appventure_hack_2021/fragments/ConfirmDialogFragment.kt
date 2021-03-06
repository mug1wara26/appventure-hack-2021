package com.example.appventure_hack_2021.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmDialogFragment(private val message_id: Int) : DialogFragment() {
    var onConfirm: () -> Unit = {}
    var onCancel: () -> Unit = {}
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder(it).apply {
                setMessage(message_id)

                setPositiveButton(android.R.string.ok) { _, _ ->
                    onConfirm()
                }
                setNegativeButton(android.R.string.cancel) { _, _ ->
                    onCancel()
                }
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}