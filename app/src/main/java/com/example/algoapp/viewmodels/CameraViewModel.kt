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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CameraViewModel(private val context: Context, private val navHostController: NavHostController): ViewModel() {
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
                setUpCode = cleanText(imageAnalyzer.textBlocks)
                Log.d(TAG, "setup code is ${setUpCode}")
            }
            else {
                runnableCode = cleanText(imageAnalyzer.textBlocks)
                Log.d(TAG, "runnable code is ${runnableCode}")
            }

            incrementNumOfPicturesTaken()
            Log.d(TAG, "navRoute: ${NavRoutes.submissionRoute}/${setUpCode}/${runnableCode}")
            if (numOfPicturesTaken == 2) {
                // navHostController.navigate("${NavRoutes.submissionRoute}/${setUpCode}/${runnableCode}")
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
                if (v[0]!!.y > lastPoint?.get(0)!!.y) {
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

class CameraViewModelFactory(private val context: Context, private val navHostController: NavHostController): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel(context, navHostController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}