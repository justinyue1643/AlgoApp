package com.example.algoapp.viewmodels

import android.graphics.Point
import android.util.Log
import androidx.lifecycle.ViewModel

class CameraViewModel: ViewModel() {
    private var textBlocks = mutableMapOf<String, Array<Point?>>()
    private var text = ""

    val extractedText: String
        get() = text

    val textBlockString
        get() = textBlocks.toString()

    fun clearTextBlocks(): Unit {
        textBlocks.clear()
    }

    fun addTextBlock(textBlock: String, points: Array<Point?>) {
        textBlocks[textBlock] = points
        Log.i(TAG, "\n" +  textBlocks.toString())
    }

    fun cleanText(): Unit {
        val firstKey = textBlocks.keys.first()
        var lastPoint = textBlocks.values.first()
        var ret = "${firstKey}\n"

        var numOfTabs = 0

        for ((k,v) in textBlocks) {
            if (k != firstKey) {
                if (v[0]!!.y > lastPoint[0]!!.y) {
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