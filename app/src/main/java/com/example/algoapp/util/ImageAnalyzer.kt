package com.example.algoapp.util

import android.content.Context
import android.graphics.Point
import android.util.Log
import androidx.core.net.toUri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import java.io.IOException

class ImageAnalyzer(val context: Context, val updateTextBlock: (String, Array<Point?>) -> Unit) {
    val recognizer = TextRecognition.getClient()

    fun extractTextFromFilePath(filePath: String) {
        lateinit var image: InputImage

        try {
            image = InputImage.fromFilePath(context, filePath.toUri())
        }
        catch (e: IOException) {
            Log.d(TAG, e.stackTraceToString())
        }

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val ocrText = analyzeText(visionText)

                Log.d(TAG, "mlkit success: $ocrText")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, e.stackTraceToString())
            }
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
            updateTextBlock(blockText, cornerPointsArray)

            ret += "$blockText\n"
        }

        Log.i(TAG,ret)
    }

    companion object {
        const val TAG = "ImageAnalyzer"
    }
}