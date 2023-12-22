package com.capstonech2.fitfans.utils

import android.content.Context
import android.widget.Toast
import com.capstonech2.fitfans.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun showDialog(context: Context, message: String){
    MaterialAlertDialogBuilder(context)
        .setTitle(context.getString(R.string.error))
        .setMessage(message)
        .setPositiveButton(context.getString(R.string.oke)){ dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

fun showDialogWithAction(context: Context, title: String, message: String, positiveAction: () -> Unit){
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(context.getString(R.string.oke)) { _, _ ->
            positiveAction.invoke()
        }
        .show()
}

fun dialogDeleteAction(context: Context, title: String, message: String, positiveAction: () -> Unit){
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(context.getString(R.string.no)){ dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton(context.getString(R.string.yes)) { _, _ ->
            positiveAction.invoke()
        }.show()
}