package com.example.algoapp.util

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions
import kotlinx.coroutines.coroutineScope
import java.io.IOException

class ImageAnalyzer(val context: Context, val text: String, val setText: (String) -> Unit) {
    val recognizer = TextRecognition.getClient()
    // var text: Text? = null

    fun extractText(filePath: String): Text? {
        lateinit var image: InputImage
        var randoText: Text? = null

        try {
            image = InputImage.fromFilePath(context, filePath.toUri())
        }
        catch (e: IOException) {
            Log.d(TAG, e.stackTraceToString())
        }

        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // randoText = visionText
                // text = visionText
                val ocrText = analyzeText(visionText)
                setText(ocrText)


                Log.d(TAG, "mlkit success: $text")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, e.stackTraceToString())
            }

        return randoText
    }

    fun analyzeText(result: Text): String {
        var ret = ""
        val resultText = result.text
        for (block in result.textBlocks) {
            val blockText = block.text
            ret += "$blockText\n"
        }
        Log.d(TAG, ret)

        return ret
    }

    companion object {
        const val TAG = "ImageAnalyzer"
    }
}