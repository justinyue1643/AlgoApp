package com.example.algoapp.ui.screens.camera

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.algoapp.util.ImageAnalyzer
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun CameraPreview(imageCapture: ImageCapture, executor: Executor, ocr: ImageAnalyzer) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
            val executor = ContextCompat.getMainExecutor(context)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                )
            }, executor)
            previewView
        },
        modifier = Modifier.fillMaxHeight()
    )
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun PreviewShutterButton() {
    ShutterButton(onClick = {})
}

@Composable
fun ShutterButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(32.dp)) {
        Button(
            shape = CircleShape,
            onClick = onClick,
            border = BorderStroke(5.dp, Color.White),
            modifier = Modifier.size(64.dp)
        ){}
    }
}