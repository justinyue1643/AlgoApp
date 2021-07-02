package com.example.algoapp.util

import android.content.Context
import android.graphics.Point
import android.util.Log
import androidx.core.net.toUri
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class ImageAnalyzer(val context: Context) {
    private val recognizer = TextRecognition.getClient()
    var textBlocks = mutableMapOf<String, Array<Point?>>()
    val textBlockString
        get() = textBlocks.toString()

    suspend fun extractTextFromFilePath(filePath: String): Task<Text> {
        lateinit var image: InputImage

        try {
            image = InputImage.fromFilePath(context, filePath.toUri())
        } catch (e: IOException) {
            Log.d(TAG, e.stackTraceToString())
        }

        val process = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                analyzeText(visionText)
                Log.d(TAG, "mlkit success")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, e.stackTraceToString())
            }

        withContext(Dispatchers.Default) {
            while(!process.isComplete) {
                Log.d(TAG, "process is not finished")
            }
        }

        Log.d(TAG, "process is finished")

        return process
    }

    private fun addTextBlock(blockText: String, points: Array<Point?>) {
        textBlocks[blockText] = points
    }

    private fun analyzeText(result: Text): Unit {
        var ret = ""
        var count = 0
        for (block in result.textBlocks) {
            val blockText = block.text
            val blockCornerPoints = block.cornerPoints
            val cornerPointsArray = arrayOf(
                blockCornerPoints?.get(0),
                blockCornerPoints?.get(1),
                blockCornerPoints?.get(2),
                blockCornerPoints?.get(3),
            )
            addTextBlock(blockText, cornerPointsArray)
            Log.d(TAG, "analyzeText() $count")
            count += 1

            ret += "$blockText\n"
        }

        Log.i(TAG,ret)
    }

    fun clearState() {
        textBlocks.clear()
    }

    companion object {
        const val TAG = "ImageAnalyzer"
    }
}