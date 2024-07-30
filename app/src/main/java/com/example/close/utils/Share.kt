package com.example.close.utils

import android.content.Context
import android.content.Intent


fun Context.shareMessage(sender: String, message: String){
    val sendIntent = Intent(
        Intent.ACTION_SEND
    ).apply {
        putExtra(Intent.EXTRA_TEXT, "${sender}: $message")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(
        sendIntent, "Share Message from $sender"
    )
    startActivity(shareIntent)
}