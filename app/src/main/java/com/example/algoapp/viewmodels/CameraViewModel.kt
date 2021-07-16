package com.example.algoapp.viewmodels

import android.content.Context
import android.graphics.Point
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.algoapp.NavRoutes
import com.example.algoapp.util.ImageAnalyzer
import com.example.algoapp.util.cleanPythonCode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CameraViewModel(private val language: String, private val context: Context, private val navHostController: NavHostController): ViewModel() {
    private val imageAnalyzer = ImageAnalyzer(context)
    var setUpCode = ""
        private set
    var runnableCode = ""
        private set
    var numOfPicturesTaken = 0
        private set

    val textBlockString: String
        get() = imageAnalyzer.textBlockString

    fun clearState(): Unit {
        numOfPicturesTaken = 0
        setUpCode = ""
        runnableCode = ""
    }

    fun extractText(filePath: String): Unit {
        var status = false
        viewModelScope.launch {
            val job = viewModelScope.async {
                imageAnalyzer.extractTextFromFilePath(filePath)
                status = true
            }
            job.await()
            if (numOfPicturesTaken == 0) {
                setUpCode = cleanPythonCode(imageAnalyzer.textBlocks)
                imageAnalyzer.clearState()
                Log.d(TAG, "setup code is ${setUpCode}")
            }
            else {
                runnableCode = cleanPythonCode(imageAnalyzer.textBlocks)
                imageAnalyzer.clearState()
                Log.d(TAG, "runnable code is ${runnableCode}")
            }

            incrementNumOfPicturesTaken()
            Log.d(TAG, "navRoute: ${NavRoutes.submissionRoute}/${setUpCode}/${runnableCode}")
            if (numOfPicturesTaken == 2) {
                navHostController.navigate("${NavRoutes.submissionRoute}/${language}/${setUpCode}/${runnableCode}")
            }
        }

        Log.d(TAG, "extractText() is finished")
    }

    private fun cleanText(textBlocks: MutableMap<String, Array<Point?>>): String {
        Log.d(TAG, "map looks like this\n${textBlockString}")
        val firstKey = textBlocks.keys.firstOrNull()
        var lastPoint = textBlocks.values.firstOrNull()
        var ret = "${firstKey}\n"

        /*if (firstKey == null && lastPoint == null) {
            return
        }*/

        var numOfTabs = 0

        for ((k,v) in textBlocks) {
            if (k != firstKey) {
                if (v[0]!!.x > lastPoint?.get(0)!!.x) {
                    numOfTabs += 1
                }
                else {
                    numOfTabs -= 1

                    if (numOfTabs < 0) {
                        numOfTabs = 0
                    }
                }
                ret += "\t".repeat(numOfTabs) + "$k\n"
            }
        }

        ret.trim()
        Log.i(TAG, ret)

        return ret
    }

    private fun incrementNumOfPicturesTaken() {
        numOfPicturesTaken += 1
    }

    companion object {
        val TAG = "CameraViewModel"
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