package com.example.algoapp.ui.screens.submission

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.algoapp.ui.theme.AlgoAppTheme
import com.example.algoapp.ui.theme.poppinsFamily
import com.example.algoapp.viewmodels.SubmissionViewModel
import com.example.algoapp.viewmodels.SubmissionViewModelFactory

@Preview(showBackground = true)
@Composable
fun PreviewSubmissionScreen() {
    val dummy = ""
    AlgoAppTheme {
        SubmissionScreen("Python",dummy, dummy, SubmissionViewModel(dummy, dummy))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSubmissionScreenDarkMode() {
    val dummy = ""
    AlgoAppTheme(
        darkTheme = true
    ) {
        SubmissionScreen("Python","", "", SubmissionViewModel(dummy, dummy))
    }
}

@Composable
fun SubmissionScreen(
    language: String,
    setup: String,
    runnable: String,
    submissionViewModel: SubmissionViewModel = viewModel(
        factory = SubmissionViewModelFactory(setup, runnable)
    )) {
//    var setUpCode by remember { mutableStateOf("${setup}") }
//    var runnableCode by remember { mutableStateOf("${runnable}") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                language,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        CodeEditor(submissionViewModel.setupCode, submissionViewModel::mutateSetupCode)
        Spacer(modifier = Modifier.height(16.dp))
        CodeEditor(submissionViewModel.runnableCode, submissionViewModel::mutateRunnableCode)
        Spacer(modifier = Modifier.height(16.dp))
        RunButton(
            submissionViewModel::getPythonResult,
            Modifier.align(Alignment.CenterHorizontally),
        )
        Text(submissionViewModel.result)
    }
}