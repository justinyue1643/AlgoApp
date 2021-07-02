package com.example.algoapp.ui.screens.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.request.Disposable
import com.example.algoapp.NavRoutes
import com.example.algoapp.util.ImageAnalyzer
import com.example.algoapp.util.checkCameraPermissions
import com.example.algoapp.viewmodels.CameraViewModel
import com.example.algoapp.viewmodels.CameraViewModelFactory
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "CameraScreen"

@Preview(showBackground = true)
@Composable
fun PreviewCameraScreen() {
    val f = File("")
    val dummyNav = NavHostController(LocalContext.current)
    CameraScreen("", f, dummyNav, CameraViewModel("", LocalContext.current, dummyNav))
}

@Composable
fun CameraScreen(
    language: String,
    outputDir: File,
    // launcher: ManagedActivityResultLauncher<String, Boolean>,
    // permissionStatus: Boolean,
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
        .build() }

    /*LaunchedEffect(permissionStatus) {
        if (permissionStatus != true) {
            launcher.launch(Manifest.permission.CAMERA)
        }

        if (!checkCameraPermissions(context)) {
            navHostController.popBackStack()
        }
    }*/

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(imageCapture, executor)
        ShutterButton(
            onClick = {
                val photoFile = File(outputDir, SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                    .format(System.currentTimeMillis()) + ".jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                imageCapture.takePicture(
                    outputOptions, executor, object: ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            Log.d(TAG, "Image has been saved!")
                            val uri = Uri.fromFile(photoFile)
                            cameraViewModel.extractText(uri.toString())
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e(TAG, "Image was not saved: ${exception.stackTraceToString()}")
                        }
                    }
                )
                photoFile.delete()
            },
            Modifier.align(Alignment.BottomCenter)
        )
    }

    DisposableEffect(true) {
        onDispose {
            cameraViewModel.clearState()
        }
    }
}