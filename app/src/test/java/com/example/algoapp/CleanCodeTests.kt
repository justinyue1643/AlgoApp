package com.example.algoapp

import com.example.algoapp.util.cleanPythonCode
import org.junit.Test
import org.junit.Assert.*

class CleanCodeTests {
    @Test
    fun testCleanPythonThreeLineBasicExample() {
        val input = mapOf(
            "def printHello():" to arrayOf(Pair(609, 502), Pair(183, 502), Pair(1183, 600), Pair(609, 600)),
            "x=0\ny=0\nfor i in range(len(x)):" to arrayOf(Pair(905, 635), Pair(1801, 623), Pair(1801, 959), Pair(909, 972)),
            "print(i)\nreturn [x,y,z]" to arrayOf(Pair(928, 1042), Pair(1450, 1166), Pair(1450, 1166), Pair(941, 1206))
        )
        val ans = "" +
                "def printHello():\n" +
                "\tx=0\n" +
                "\ty=0\n" +
                "\tfor i in range(len(x)):\n" +
                "\t\tprint(i)\n" +
                "\t\treturn [x,y,z]"
        val output = cleanPythonCode(input)

        assertEquals(ans, output)
    }

    @Test
    fun testCleanPythonOneLine() {
        val input = mapOf(
            "runCode()" to arrayOf(Pair(0,0), Pair(0, 100), Pair(100, 100), Pair(0, 100))
        )
        val ans = "runCode()"
        val output = cleanPythonCode(input)

        assertEquals(ans, output)
    }

    @Test
    fun testCleanPythonEmptyText() {
        val input = mapOf<String, Array<Pair<Int, Int>>>()
        val ans = ""
        val output = cleanPythonCode(input)

        assertEquals(ans, output)
    }

    @Test
    fun testCleanPythonWhereNumOfTabsDecreasesForOneOfTheLines() {
        val input = mapOf(
            "def printHello():" to arrayOf(Pair(0,100)),
            "x=0\ny=0\nif x == y:" to arrayOf(Pair(1,100)),
            "print(\"TRUE\")" to arrayOf(Pair(2,100)),
            "return True" to arrayOf(Pair(1,100))
        )

        val ans = "def printHello():\n" +
                "\tx=0\n" +
                "\ty=0\n" +
                "\tif x == y:\n" +
                "\t\tprint(\"TRUE\")\n" +
                "\treturn True"
        val output = cleanPythonCode(input)

        assertEquals(ans, output)
    }
}