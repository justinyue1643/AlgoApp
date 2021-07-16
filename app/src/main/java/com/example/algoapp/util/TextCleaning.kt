package com.example.algoapp.util

import android.util.Log

const val TAG = "CleaningTextUtils"

fun cleanPythonCode(textBlocks: Map<String, Array<Pair<Int, Int>>>): String {
    Log.d(TAG, "Pretty Print Points:\n${prettyLogPoints(textBlocks)}")
    val firstKey = textBlocks.keys.firstOrNull()
    var lastPoint = textBlocks.values.firstOrNull()
    var ret = "${firstKey}\n"

    if (firstKey == null && lastPoint == null) {
        return ""
    }

    var numOfTabs = 0

    for ((k,v) in textBlocks) {
        if (k != firstKey) {
            val x = v[0].first
            val y = lastPoint?.get(0)?.first!!
            if (x > y) {
                numOfTabs += 1
            }
            else {
                numOfTabs -= 1

                if (numOfTabs < 0) {
                    numOfTabs = 0
                }
            }
            ret += "${formatLineWithTabs(k, numOfTabs)}"
        }
        lastPoint = v
    }

    ret = ret.trim()
    Log.i(TAG, ret)

    return ret
}

private fun formatLineWithTabs(line: String, numOfTabs: Int): String {
    var output = ""
    line.split("\n").forEach {
        output += "\t".repeat(numOfTabs) + it + "\n"
    }

    return output
}

private fun prettyLogPoints(textBlocks: Map<String, Array<Pair<Int, Int>>>): String {
    var output = ""
    for (i in textBlocks.keys) {
        output += "$i\n"
        output += prettyLogArrayPoints(textBlocks[i]!!)
    }
    output += "\n"

    return output
}

private fun prettyLogArrayPoints(arr: Array<Pair<Int, Int>>): String {
    var output = ""
    for (i in arr) {
        output += "\t${i.first} ${i.second}"
    }

    return output
}