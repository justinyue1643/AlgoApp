package com.example.algoapp.viewmodels

import android.content.Context
import android.graphics.Point
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.algoapp.util.ImageAnalyzer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CameraViewModel(val context: Context): ViewModel() {
    private val imageAnalyzer = ImageAnalyzer(context)
    private var text = ""
    val textBlockString: String
        get() = imageAnalyzer.textBlockString


    val extractedText: String
        get() = text

    fun clearState(): Unit {
        text = ""
    }

    fun extractText(filePath: String): String {
        var text = ""
        viewModelScope.launch {
            val job = viewModelScope.async {
                imageAnalyzer.extractTextFromFilePath(filePath)
                text = "hmmm"
            }
            job.await()
            cleanText(imageAnalyzer.textBlocks)
        }

        Log.d(TAG, "this is the map: ${imageAnalyzer.textBlockString}")
        Log.d(TAG, "lol $text")

        return text
    }

    fun cleanText(textBlocks: MutableMap<String, Array<Point?>>): Unit {
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
                }
                ret += "\t".repeat(numOfTabs) + "$k\n"
            }
        }

        ret.trim()
        Log.i(TAG, ret)
    }

    companion object {
        val TAG = "CameraViewModel"
    }
}

class CameraViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}