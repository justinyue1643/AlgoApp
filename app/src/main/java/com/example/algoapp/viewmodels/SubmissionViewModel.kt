package com.example.algoapp.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.algoapp.network.PythonServerApi
import com.example.algoapp.util.cleanPythonCodeRequest
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException

class SubmissionViewModel(sCode: String, rCode: String): ViewModel() {
    var setupCode by mutableStateOf("def hello():\n\treturn \"hello\"")
    var runnableCode by mutableStateOf("hello()")
    var result by mutableStateOf("")

    fun mutateSetupCode(value: String) {
        setupCode = value
    }

    fun mutateRunnableCode(value: String) {
        runnableCode = value
    }

    fun getPythonResult() {
        viewModelScope.launch {
            try {
                val response = PythonServerApi.retrofitService.getResult(cleanPythonCodeRequest(setupCode), runnableCode)
                Log.d(TAG, "response- $response")
                result = response.output ?: "Error"

                Log.d(TAG, "result: $result")
            } catch (e: Exception) {
                Log.d(TAG, "error: $e")
            }

        }
    }

    companion object {
        const val TAG = "SubmissionViewModel"
    }
}

class SubmissionViewModelFactory(private val sCode: String, private val rCode: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubmissionViewModel::class.java)) {
            return SubmissionViewModel(sCode, rCode) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}