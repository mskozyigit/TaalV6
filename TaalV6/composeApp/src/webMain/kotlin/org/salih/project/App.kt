package org.salih.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import taalv6.composeapp.generated.resources.Res
import taalv6.composeapp.generated.resources.compose_multiplatform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon

// Custom Colors
val DuolingoGreen = Color(0xFF58CC02)
val DuolingoLightGreen = Color(0xFF8AFF33)
val DuolingoDarkGreen = Color(0xFF4CAF50)
val DuolingoShadowGreen = Color(0xFF5B9F00)

sealed class Screen {
    object Loading : Screen()
    object LevelSelection : Screen()
    data class SubLevelSelection(val level: String) : Screen()
    data class LessonSelection(val level: String, val subLevel: String) : Screen()
    data class QuestionScreen(val level: String, val subLevel: String, val lesson: String) : Screen()
}

@Composable
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Loading) }

        when (currentScreen) {
            Screen.Loading -> LoadingScreen { currentScreen = Screen.LevelSelection }
            Screen.LevelSelection -> LevelSelectionScreen { level ->
                currentScreen = Screen.SubLevelSelection(level)
            }
            is Screen.SubLevelSelection -> {
                val (level) = currentScreen as Screen.SubLevelSelection
                SubLevelSelectionScreen(
                    level = level,
                    onBack = { currentScreen = Screen.LevelSelection },
                    onSelectSubLevel = { subLevel ->
                        currentScreen = Screen.LessonSelection(level, subLevel)
                    }
                )
            }
            is Screen.LessonSelection -> {
                val (level, subLevel) = currentScreen as Screen.LessonSelection
                LessonSelectionScreen(
                    level = level,
                    subLevel = subLevel,
                    onBack = { currentScreen = Screen.SubLevelSelection(level) },
                    onSelectLesson = { lesson ->
                        currentScreen = Screen.QuestionScreen(level, subLevel, lesson)
                    }
                )
            }
            is Screen.QuestionScreen -> {
                val (level, subLevel, lesson) = currentScreen as Screen.QuestionScreen
                QuestionScreen(
                    level = level,
                    subLevel = subLevel,
                    lesson = lesson,
                    onBack = { currentScreen = Screen.LessonSelection(level, subLevel) }
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(onLoadingComplete: () -> Unit) {
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (progress < 1f) {
            progress += 0.1f
            delay(300) // Simulate loading
        }
        onLoadingComplete()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(DuolingoGreen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Laden...",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.width(400.dp).height(20.dp).clip(RoundedCornerShape(10.dp)),
            color = DuolingoLightGreen,
            trackColor = DuolingoDarkGreen
        )
    }
}

@Composable
fun LevelSelectionScreen(onSelectLevel: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(DuolingoGreen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kies een niveau",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 40.dp)
        )
        DuolingoButton(text = "Beginner (A1/A2)") { onSelectLevel("Beginner") }
        Spacer(Modifier.height(20.dp))
        DuolingoButton(text = "Gemiddeld (B1/B2)") { onSelectLevel("Gemiddeld") }
        Spacer(Modifier.height(20.dp))
        DuolingoButton(text = "Gevorderd (B2 en hoger)") { onSelectLevel("Gevorderd") }
    }
}

@Composable
fun SubLevelSelectionScreen(level: String, onBack: () -> Unit, onSelectSubLevel: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(DuolingoGreen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(onBack = onBack, onHome = { /* Ana sayfaya dönme mantığı */ })
        Text(
            text = "$level: Kies een subniveau",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 30.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            repeat(5) { index ->
                DuolingoButton(text = "Subniveau ${index + 1}") { onSelectSubLevel("Subniveau ${index + 1}") }
            }
        }
    }
}

@Composable
fun LessonSelectionScreen(level: String, subLevel: String, onBack: () -> Unit, onSelectLesson: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(DuolingoGreen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(onBack = onBack, onHome = { /* Ana sayfaya dönme mantığı */ })
        Text(
            text = "$level - $subLevel: Kies een les",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 30.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            repeat(4) { index ->
                DuolingoButton(text = "Les ${index + 1}") { onSelectLesson("Les ${index + 1}") }
            }
        }
    }
}

@Composable
fun QuestionScreen(level: String, subLevel: String, lesson: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(DuolingoGreen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(onBack = onBack, onHome = { /* Ana sayfaya dönme mantığı */ })
        Text(
            text = "$level - $subLevel - $lesson: Vragen",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 30.dp)
        )
        Text(
            text = "Soru ve cevap mekanizması buraya gelecek.",
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Composable
fun DuolingoButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(500.dp) // Buton genişliğini iki katına çıkardık
            .height(100.dp) // Buton yüksekliğini iki katına çıkardık
            .shadow(8.dp, RoundedCornerShape(16.dp), ambientColor = DuolingoShadowGreen, spotColor = DuolingoShadowGreen)
            .graphicsLayer {
                translationY = -8.dp.toPx() // 3D efekti için yukarı kaydırma
            },
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = DuolingoLightGreen,
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            fontSize = 24.sp, // Yazı boyutunu büyüttük
            fontWeight = FontWeight.ExtraBold, // Yazıyı kalınlaştırdık
            textAlign = TextAlign.Center,
            maxLines = 1 // Tek satırda yazıya izin ver
        )
    }
}

@Composable
fun Header(onBack: () -> Unit, onHome: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Geri butonu
        Button(
            onClick = onBack,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(50))
                .shadow(4.dp, RoundedCornerShape(50), ambientColor = DuolingoShadowGreen, spotColor = DuolingoShadowGreen),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = DuolingoLightGreen,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Geri",
                modifier = Modifier.size(30.dp)
            )
        }

        // Ev butonu
        Button(
            onClick = onHome,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = DuolingoShadowGreen, spotColor = DuolingoShadowGreen),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = DuolingoLightGreen,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Ana Sayfa",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}