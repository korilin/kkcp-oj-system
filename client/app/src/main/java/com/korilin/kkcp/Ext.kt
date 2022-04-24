package com.korilin.kkcp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.startActivity(clazz: Class<out AppCompatActivity>) {
    startActivity(Intent(this, clazz))
    finish()
}

fun logError(text: String) {
    Log.e("KKCP_ERROR_LOG", text)
}