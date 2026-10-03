package com.example.ui.screens.games

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.rounded.Replay
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DataRepository
import com.example.data.LifeSkillId
import com.example.sound.SoundHelper
import com.example.ui.components.ConfettiShower
import com.example.ui.components.KidButton
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.GoldStar
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.TextDark
import kotlinx.coroutines.delay

@Composable
fun BrushTeethGameScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Quadrants: 0: Top Left, 1: Top Right, 2: Bottom Left, 3: Bottom Right
    val quadrantNames = listOf("Top Left Teeth", "Top Right Teeth", "Bottom Left Teeth", "Bottom Right Teeth")
    val quadrantProgress = remember { mutableStateListOf(0, 0, 0, 0) }
    var activeQuadrant by remember { mutableIntStateOf(0) }
    var isCompleted by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isTimerRunning by remember { mutableStateOf(false) }

    val totalProgress = quadrantProgress.sum() / 400f
    val animatedProgress by animateFloatAsState(targetValue = totalProgress, label = "progress")

    // Timer effect
    LaunchedEffect(isTimerRunning, secondsLeft) {
        if (isTimerRunning && secondsLeft > 0) {
            delay(1000)
            secondsLeft--
            if (secondsLeft % 5 == 0) {
                soundHelper.playClick()
            }
        } else if (isTimerRunning && secondsLeft == 0) {
            isTimerRunning = false
            // Mark all complete
            for (i in 0..3) quadrantProgress[i] = 100
            isCompleted = true
            soundHelper.playFanfare()
            repository.completeSkill(LifeSkillId.BRUSH_TEETH, 40, 1)
        }
    }

    LaunchedEffect(Unit) {
        soundHelper.speak("Tap and brush all four areas of your teeth to wash away the sugar bugs!")
    }

    fun brushCurrentQuadrant() {
        if (isCompleted) return
        if (!isTimerRunning) isTimerRunning = true

        soundHelper.playBubblePop()
        val cur = quadrantProgress[activeQuadrant]
        if (cur < 100) {
            quadrantProgress[activeQuadrant] = (cur + 25).coerceAtMost(100)
        }

        // Auto advance quadrant if filled
        if (quadrantProgress[activeQuadrant] >= 100) {
            val nextIncomplete = quadrantProgress.indexOfFirst { it < 100 }
            if (nextIncomplete != -1) {
                activeQuadrant = nextIncomplete
                soundHelper.playSuccess()
            } else {
                isCompleted = true
                isTimerRunning = false
                soundHelper.playFanfare()
                soundHelper.speak("Woohoo! Sparkling white clean teeth! You're a toothbrush champion!")
                repository.completeSkill(LifeSkillId.BRUSH_TEETH, 40, 1)
            }
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
                        text = "🪥 Brush Teeth Game",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            soundHelper.speak("Brush each quadrant until sparkling clean!")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Read instructions",
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
                    .background(Color(0xFFF0F9FF))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Timer and Progress
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⏱️", fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "$secondsLeft s",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (secondsLeft <= 5) Color(0xFFE53935) else SkyBlue
                                )
                            }
                            Text(
                                text = "${(totalProgress * 100).toInt()}% Clean ✨",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF2E7D32)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = Color(0xFF00C853),
                            trackColor = Color(0xFFE0E0E0)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Interactive Mouth & Teeth Arena
                Surface(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .border(4.dp, Color(0xFF81D4FA), RoundedCornerShape(32.dp)),
                    color = Color(0xFFFFEBEE),
                    shadowElevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Teeth Row (Top Left & Top Right)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            QuadrantCard(
                                name = "Top Left",
                                progress = quadrantProgress[0],
                                isSelected = activeQuadrant == 0,
                                onClick = {
                                    activeQuadrant = 0
                                    brushCurrentQuadrant()
                                }
                            )
                            QuadrantCard(
                                name = "Top Right",
                                progress = quadrantProgress[1],
                                isSelected = activeQuadrant == 1,
                                onClick = {
                                    activeQuadrant = 1
                                    brushCurrentQuadrant()
                                }
                            )
                        }

                        // Tongue / Mouth Center
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isCompleted) "😁 Sparkling Clean!" else "👅 Tap teeth to brush!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFFD81B60)
                            )
                        }

                        // Bottom Teeth Row (Bottom Left & Bottom Right)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            QuadrantCard(
                                name = "Bottom Left",
                                progress = quadrantProgress[2],
                                isSelected = activeQuadrant == 2,
                                onClick = {
                                    activeQuadrant = 2
                                    brushCurrentQuadrant()
                                }
                            )
                            QuadrantCard(
                                name = "Bottom Right",
                                progress = quadrantProgress[3],
                                isSelected = activeQuadrant == 3,
                                onClick = {
                                    activeQuadrant = 3
                                    brushCurrentQuadrant()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Brush Action Button
                if (!isCompleted) {
                    KidButton(
                        text = "Scrub & Foam! 🫧 🪥",
                        onClick = { brushCurrentQuadrant() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        containerColor = SkyBlue,
                        testTag = "brush_action_button"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Currently brushing: ${quadrantNames[activeQuadrant]}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0277BD)
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🎉 Super Job! 🎉", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "All sugar germs are washed away! You earned +40 XP & 1 Star ⭐",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF1B5E20)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                KidButton(
                                    text = "Play Again 🔄",
                                    onClick = {
                                        for (i in 0..3) quadrantProgress[i] = 0
                                        activeQuadrant = 0
                                        secondsLeft = 30
                                        isCompleted = false
                                        isTimerRunning = false
                                    },
                                    containerColor = Color(0xFF78909C),
                                    modifier = Modifier.weight(1f)
                                )
                                KidButton(
                                    text = "Finish 🏆",
                                    onClick = onBack,
                                    containerColor = Color(0xFF2E7D32),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        ConfettiShower(visible = isCompleted)
    }
}

@Composable
private fun QuadrantCard(
    name: String,
    progress: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(105.dp, 80.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 3.dp else 1.5.dp,
                color = if (isSelected) SkyBlue else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(18.dp)
            ),
        color = if (progress >= 100) Color(0xFFE8F5E9) else CleanWhite,
        shadowElevation = if (isSelected) 4.dp else 1.dp
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (progress >= 100) "✨🦷✨" else if (progress > 50) "🫧🦷🫧" else "👾🦷👾",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                maxLines = 1
            )
            Text(
                text = "$progress%",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (progress >= 100) Color(0xFF2E7D32) else Color(0xFF757575)
            )
        }
    }
}
