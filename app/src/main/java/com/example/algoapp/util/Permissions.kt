package com.example.algoapp.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

fun checkCameraPermissions(context: Context): Boolean {
     return ContextCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}