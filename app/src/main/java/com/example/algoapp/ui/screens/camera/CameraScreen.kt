package com.example.algoapp.ui.screens.camera

import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.algoapp.viewmodels.CameraViewModel
import com.example.algoapp.viewmodels.CameraViewModelFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

const val TAG = "CameraScreen"

@Preview(showBackground = true)
@Composable
fun PreviewCameraScreen() {
    val context = LocalContext.current
    val language = "Python"
    val f = File("")
    val dummyNav = NavHostController(context)

    CameraScreen(language, f, dummyNav, CameraViewModel(language, context, dummyNav))
}

@Composable
fun CameraScreen(
    language: String,
    outputDir: File,
    navHostController: NavHostController,
    cameraViewModel: CameraViewModel = viewModel(
        factory = CameraViewModelFactory(
            language,
            LocalContext.current,
            navHostController
        )
    ),
) {
    val context = LocalContext.current
    val executor = ContextCompat.getMainExecutor(context)
    var imageCapture = remember { ImageCapture.Builder()
        .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
        .build()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(imageCapture, modifier = Modifier.fillMaxSize())
        ShutterButton(
            onClick = {
                takePicture(outputDir, imageCapture, executor, cameraViewModel::extractText)
            },
            Modifier.align(Alignment.BottomCenter)
        )
        LoadingStatus(cameraViewModel.status)
    }

    DisposableEffect(true) {
        onDispose {
            cameraViewModel.clearState()
        }
    }

}

private fun takePicture(outputDir: File, imageCapture: ImageCapture, executor: Executor, action: (String) -> Unit) {
    val photoFile = File(outputDir, SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
        .format(System.currentTimeMillis()) + ".jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    imageCapture.takePicture(
        outputOptions, executor, object: ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Log.d(TAG, "Image has been saved!")
                val uri = Uri.fromFile(photoFile)
                action(uri.toString())
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e(TAG, "Image was not saved: ${exception.stackTraceToString()}")
            }
        }
    )
    photoFile.delete()
}

@Composable
fun LoadingStatus(status: Boolean) {
    Text(status.toString())
}