package com.example.algoapp.viewmodels

import android.content.Context
import android.graphics.Point
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.algoapp.NavRoutes
import com.example.algoapp.util.ImageAnalyzer
import com.example.algoapp.util.cleanPythonCode
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

class CameraViewModel(private val language: String, private val context: Context, private val navHostController: NavHostController): ViewModel() {
    var setUpCode = ""
        private set
    var runnableCode = ""
        private set
    var numOfPicturesTaken = 0
        private set
    var status by mutableStateOf(true)
    private val imageAnalyzer = ImageAnalyzer(context, status) { status = true }

    val textBlockString: String
        get() = imageAnalyzer.textBlockString

    fun clearState(): Unit {
        numOfPicturesTaken = 0
        setUpCode = ""
        runnableCode = ""
    }

    fun extractText(filePath: String): Unit {
        status = false
        viewModelScope.launch {
            imageAnalyzer.extractTextFromFilePath(filePath)
            withContext(Dispatchers.Default) {
                while (!status) {
                    Log.d(TAG, "Task isn't finished")
                }
            }

            if (numOfPicturesTaken == 0) {
                setUpCode = cleanPythonCode(imageAnalyzer.textBlocks)
                Log.d(TAG, "setup code is $setUpCode")
            }
            else {
                runnableCode = cleanPythonCode(imageAnalyzer.textBlocks)
                Log.d(TAG, "runnable code is $runnableCode")
            }
            imageAnalyzer.clearState()

            incrementNumOfPicturesTaken()
            Log.d(TAG, "num of pics taken: $numOfPicturesTaken")
            Log.d(TAG, "navRoute: ${NavRoutes.submissionRoute}/${setUpCode}/${runnableCode}")
            if (numOfPicturesTaken == 2) {
                navHostController.navigate("${NavRoutes.submissionRoute}/${language}/${setUpCode}/${runnableCode}")
            }
        }

        Log.d(TAG, "extractText() is finished")
    }

    private fun incrementNumOfPicturesTaken() {
        numOfPicturesTaken += 1
    }

    companion object {
        const val TAG = "CameraViewModel"
    }
}

class CameraViewModelFactory(private val language: String, private val context: Context, private val navHostController: NavHostController): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel(language, context, navHostController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}