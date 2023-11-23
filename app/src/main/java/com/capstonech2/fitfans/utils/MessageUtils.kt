package com.capstonech2.fitfans.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

object MessageUtils {

    fun showToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showDialog(context: Context, message: String){
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton("Oke") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}