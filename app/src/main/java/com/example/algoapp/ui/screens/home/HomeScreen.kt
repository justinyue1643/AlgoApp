package com.example.algoapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.algoapp.NavRoutes
import com.example.algoapp.R
import com.example.algoapp.ui.theme.poppinsFamily
import com.google.accompanist.coil.rememberCoilPainter

sealed class Language(val name: String, val color: Color, val image: Int) {
    object Python: Language("Python", Color(0xFF2196F3), R.drawable.python_icon)
    object Java: Language("Java",  Color(0xFFDBC81A), R.drawable.java_icon)
}

@Preview()
@Composable
fun PreviewHomeScreen() {
    val dummyNav = rememberNavController()
    HomeScreen(dummyNav)
}

@Composable
fun HomeScreen(navHostController: NavHostController) {
    val languageList = listOf(Language.Python, Language.Java)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        languageList.forEach { language ->
            LanguageButton(language, navHostController, modifier = Modifier.height(150.dp))

            if (language != languageList.last()) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun LanguageButton(language: Language, navHostController: NavHostController, modifier: Modifier = Modifier) {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = language.color),
        onClick = { navHostController.navigate("${NavRoutes.cameraRoute}/${language.name}" ) },
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            language.name,
            style = TextStyle(color = Color.White, fontSize = 20.sp),
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = rememberCoilPainter(request = language.image),
            contentDescription = "${language.name} icon"
        )
    }
}