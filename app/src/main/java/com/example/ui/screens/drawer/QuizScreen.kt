package com.example.ui.screens.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DataRepository
import com.example.data.QuizQuestion
import com.example.sound.SoundHelper
import com.example.ui.components.ConfettiShower
import com.example.ui.components.KidButton
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.GoldStar
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.TextDark

@Composable
fun QuizScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = repository.quizQuestions
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }

    val currentQuestion = questions[currentIndex]
    val progress = (currentIndex + 1) / questions.size.toFloat()

    LaunchedEffect(currentIndex) {
        val q = questions[currentIndex]
        soundHelper.speak("${q.question}")
    }

    fun submitAnswer(index: Int) {
        if (isAnswerSubmitted) return
        selectedOptionIndex = index
        isAnswerSubmitted = true

        if (index == currentQuestion.correctIndex) {
            soundHelper.playSuccess()
            correctAnswersCount++
            soundHelper.speak("Correct! ${currentQuestion.explanation}")
        } else {
            soundHelper.playError()
            soundHelper.speak("Not quite! ${currentQuestion.explanation}")
        }
    }

    fun nextQuestion() {
        if (currentIndex < questions.size - 1) {
            currentIndex++
            selectedOptionIndex = null
            isAnswerSubmitted = false
        } else {
            isQuizFinished = true
            soundHelper.playFanfare()
            soundHelper.speak("Quiz completed! You scored $correctAnswersCount out of ${questions.size}!")
            // Award quiz stars & XP
            val xpEarned = correctAnswersCount * 15
            val currentProfile = repository.profile.value
            repository.updateProfile(
                name = currentProfile.name,
                age = currentProfile.age,
                avatarId = currentProfile.avatarId
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CleanWhite)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            soundHelper.playClick()
                            onBack()
                        },
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = SkyBlue
                        )
                    }
                    Text(
                        text = "❓ Kids Quiz Arena",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { soundHelper.speak(currentQuestion.question) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Read question",
                            tint = SkyBlue
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF3E5F5))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isQuizFinished) {
                    // Question progress
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Question ${currentIndex + 1} of ${questions.size}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF6A1B9A)
                                )
                                Text(
                                    text = "⭐ $correctAnswersCount Correct",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = Color(0xFFE65100)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = Color(0xFFAB47BC),
                                trackColor = Color(0xFFE1BEE7)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Question Box
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp))
                            .border(3.dp, Color(0xFFBA68C8), RoundedCornerShape(28.dp)),
                        color = CleanWhite,
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = currentQuestion.emoji, fontSize = 54.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = currentQuestion.question,
                                style = MaterialTheme.typography.titleLarge,
                                color = TextDark,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Options
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        currentQuestion.options.forEachIndexed { index, option ->
                            val isSelected = selectedOptionIndex == index
                            val isCorrect = index == currentQuestion.correctIndex

                            val backgroundColor = when {
                                !isAnswerSubmitted -> if (isSelected) Color(0xFFEDE7F6) else CleanWhite
                                isCorrect -> Color(0xFFE8F5E9)
                                isSelected -> Color(0xFFFFEBEE)
                                else -> CleanWhite
                            }

                            val borderColor = when {
                                !isAnswerSubmitted -> if (isSelected) Color(0xFFAB47BC) else Color(0xFFE0E0E0)
                                isCorrect -> Color(0xFF43A047)
                                isSelected -> Color(0xFFE53935)
                                else -> Color(0xFFE0E0E0)
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable(enabled = !isAnswerSubmitted) { submitAnswer(index) }
                                    .border(2.5.dp, borderColor, RoundedCornerShape(20.dp)),
                                color = backgroundColor,
                                shadowElevation = 2.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(borderColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = ('A'.code + index).toChar().toString(),
                                            fontWeight = FontWeight.Bold,
                                            color = CleanWhite,
                                            fontSize = 15.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isAnswerSubmitted && isCorrect) {
                                        Text(text = "✅", fontSize = 20.sp)
                                    } else if (isAnswerSubmitted && isSelected) {
                                        Text(text = "❌", fontSize = 20.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Explanation feedback
                    if (isAnswerSubmitted) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedOptionIndex == currentQuestion.correctIndex) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (selectedOptionIndex == currentQuestion.correctIndex) "🎉 Superb Answer!" else "💡 Quick Learning Tip:",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = if (selectedOptionIndex == currentQuestion.correctIndex) Color(0xFF2E7D32) else Color(0xFFE65100)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentQuestion.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextDark
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        KidButton(
                            text = if (currentIndex < questions.size - 1) "Next Question ➡️" else "See My Results! 🏆",
                            onClick = { nextQuestion() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            containerColor = Color(0xFFAB47BC),
                            testTag = "quiz_next_button"
                        )
                    }
                } else {
                    // Result Screen
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🏆", fontSize = 64.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Quiz Champion!",
                                style = MaterialTheme.typography.headlineLarge,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "You got $correctAnswersCount out of ${questions.size} correct!",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF6A1B9A),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "You earned +${correctAnswersCount * 15} XP and valuable life skills wisdom! 🌟",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF2E7D32)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                KidButton(
                                    text = "Retake Quiz 🔄",
                                    onClick = {
                                        currentIndex = 0
                                        correctAnswersCount = 0
                                        selectedOptionIndex = null
                                        isAnswerSubmitted = false
                                        isQuizFinished = false
                                    },
                                    containerColor = Color(0xFF78909C),
                                    modifier = Modifier.weight(1f)
                                )
                                KidButton(
                                    text = "Done 🚀",
                                    onClick = onBack,
                                    containerColor = Color(0xFF43A047),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        ConfettiShower(visible = isQuizFinished)
    }
}
