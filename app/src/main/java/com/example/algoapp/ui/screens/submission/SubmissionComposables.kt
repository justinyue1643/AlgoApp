package com.example.algoapp.ui.screens.submission

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CodeEditor(code: String, onCodeChange: (String) -> Unit) {
    TextField(
        code,
        onCodeChange,
        maxLines = 5,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable()
fun RunButton(modifier: Modifier) {

    Box(
        modifier = modifier
    ) {
        Button(
            onClick = {},
        ) {
            Text("Run!")
        }
    }
}