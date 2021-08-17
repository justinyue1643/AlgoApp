package com.example.algoapp.util

import android.content.Context
import android.graphics.Point
import android.util.Log
import androidx.core.net.toUri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import java.io.IOException

class ImageAnalyzer(private val context: Context, val status: Boolean, val changeStatus: () -> Unit) {
    private val recognizer = TextRecognition.getClient()
    var textBlocks = mutableMapOf<String, Array<Pair<Int, Int>>>()
    val textBlockString
        get() = textBlocks.toString()

    fun extractTextFromFilePath(filePath: String): Unit {
        lateinit var image: InputImage

        try {
            image = InputImage.fromFilePath(context, filePath.toUri())
        } catch (e: IOException) {
            Log.d(TAG, e.stackTraceToString())
        }

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                analyzeText(visionText)
                Log.d(TAG, "mlkit success")
                // changeStatus()
            }
            .addOnFailureListener { e ->
                Log.d(TAG, e.stackTraceToString())
            }
            .addOnCompleteListener {
                Log.d(TAG, "process is finished")
                changeStatus()
            }
    }

    private fun addTextBlock(blockText: String, points: Array<Point?>) {
        val arrayPairs = Array(4) { i -> Pair(points[i]!!.x, points[i]!!.y) }
        textBlocks[blockText] = arrayPairs
    }

    private fun analyzeText(result: Text): Unit {
        var ret = ""
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