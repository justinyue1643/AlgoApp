package com.example.algoapp.ui.screens.submission

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.algoapp.ui.theme.AlgoAppTheme

@Preview(showBackground = true)
@Composable
fun CodeEditorPreview() {
    var text by remember { mutableStateOf("") }
    AlgoAppTheme() {
        CodeEditor(text) { value -> text = value }
    }
}

@Composable
fun CodeEditor(code: String, onCodeChange: (String) -> Unit) {
    TextField(
        code,
        {value -> onCodeChange(value)},
        maxLines = 5,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun RunButtonPreview() {
    AlgoAppTheme() {
        RunButton({})
    }
}

@Composable()
fun RunButton(action: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Button(
            onClick = action,
        ) {
            Text("Run!")
        }
    }
}