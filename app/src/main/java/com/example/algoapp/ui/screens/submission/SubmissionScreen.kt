package com.example.algoapp.ui.screens.submission

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.algoapp.ui.screens.submission.CodeEditor
import com.example.algoapp.ui.theme.AlgoAppTheme
import com.example.algoapp.viewmodels.SubmissionViewModel

@Preview
@Composable
fun PreviewSubmissionScreen() {
    AlgoAppTheme {
        Surface(
            color = Color.White
        ) {
            SubmissionScreen("", "")
        }
    }
}

@Composable
fun SubmissionScreen(setup: String, runnable: String, submissionViewModel: SubmissionViewModel = viewModel()) {
    var setUpCode by remember { mutableStateOf("${setup}") }
    var runnableCode by remember { mutableStateOf("${runnable}") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CodeEditor(setUpCode) { value -> setUpCode = value }
        Spacer(modifier = Modifier.height(16.dp))
        CodeEditor(runnableCode) { value -> runnableCode = value }
        Spacer(modifier = Modifier.height(16.dp))
        RunButton(Modifier.align(Alignment.CenterHorizontally))
    }
}