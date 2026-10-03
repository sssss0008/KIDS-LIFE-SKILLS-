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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.LifeSkillId
import com.example.sound.SoundHelper
import com.example.ui.components.ConfettiShower
import com.example.ui.components.KidButton
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.TextDark

@Composable
fun CrossRoadGameScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Look checks: Left, Right, Left again
    var lookedLeft1 by remember { mutableStateOf(false) }
    var lookedRight by remember { mutableStateOf(false) }
    var lookedLeft2 by remember { mutableStateOf(false) }

    // Traffic light: "RED", "YELLOW", "GREEN"
    var lightState by remember { mutableStateOf("RED") }
    var kidCrossStep by remember { mutableIntStateOf(0) } // 0 at sidewalk, 1 mid, 2 crossed!
    var statusText by remember { mutableStateOf("Red light! Cars are zooming. Stop on the curb!") }
    var isCompleted by remember { mutableStateOf(false) }

    val allLooked = lookedLeft1 && lookedRight && lookedLeft2
    val canCross = allLooked && lightState == "GREEN"

    LaunchedEffect(Unit) {
        soundHelper.speak("Cross the road safely! Check left, right, and left again, and wait for the green light!")
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
                            tint = Color(0xFFE53935)
                        )
                    }
                    Text(
                        text = "🚦 Cross The Road Safety",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { soundHelper.speak("Always stop at curb. Look left, right, left again. Walk on green!") }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Read instructions",
                            tint = Color(0xFFE53935)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFFFEBEE))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Safety Rules Card
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
                                text = "🚦 Traffic Signal",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            // Traffic light display
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF212121))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(if (lightState == "RED") Color(0xFFFF1744) else Color(0xFF550000))
                                )
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(if (lightState == "YELLOW") Color(0xFFFFEA00) else Color(0xFF555500))
                                )
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(if (lightState == "GREEN") Color(0xFF00E676) else Color(0xFF004400))
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (lightState == "GREEN") Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Zebra Crossing Road Scene
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(3.dp, Color(0xFF78909C), RoundedCornerShape(24.dp)),
                    color = Color(0xFF37474F)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Zebra crossing stripes
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 12.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            repeat(5) {
                                Box(
                                    modifier = Modifier
                                        .width(180.dp)
                                        .height(18.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.White)
                                )
                            }
                        }

                        // Park destination on the right
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(12.dp)
                        ) {
                            Text(text = "🏡 🌳", fontSize = 34.sp)
                        }

                        // Kid crossing position
                        val xOffset = when (kidCrossStep) {
                            0 -> 24.dp
                            1 -> 130.dp
                            else -> 240.dp
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .offset(x = xOffset)
                        ) {
                            Text(
                                text = if (kidCrossStep == 2) "🎉 🦸" else "🚶‍♂️",
                                fontSize = 42.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Step 1: Look Left - Right - Left Buttons
                Text(
                    text = "👀 1. Look Both Ways Before Crossing:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LookButton(
                        text = "1. Look Left 👈",
                        isLooked = lookedLeft1,
                        onClick = {
                            soundHelper.playBubblePop()
                            lookedLeft1 = true
                            statusText = "Looked Left! Now look Right! 👉"
                            soundHelper.speak("Clear on the left!")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    LookButton(
                        text = "2. Look Right 👉",
                        isLooked = lookedRight,
                        enabled = lookedLeft1,
                        onClick = {
                            soundHelper.playBubblePop()
                            lookedRight = true
                            statusText = "Looked Right! Now look Left again! 👈"
                            soundHelper.speak("Clear on the right!")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    LookButton(
                        text = "3. Look Left 👈",
                        isLooked = lookedLeft2,
                        enabled = lookedRight,
                        onClick = {
                            soundHelper.playSuccess()
                            lookedLeft2 = true
                            lightState = "GREEN"
                            statusText = "Both sides clear! Green light is ON! 🚶‍♂️ You can cross now!"
                            soundHelper.speak("The road is clear and green light is on! Walk safely!")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Step 2: Cross Walk Button
                if (!isCompleted) {
                    KidButton(
                        text = if (kidCrossStep == 0) "Step onto Zebra Stripes! 🦓" else "Walk Safely Across! 🚶‍♂️",
                        onClick = {
                            if (!canCross) {
                                soundHelper.playError()
                                soundHelper.speak("Wait! Look left, right, and wait for green light!")
                            } else {
                                soundHelper.playBubblePop()
                                kidCrossStep++
                                if (kidCrossStep >= 2) {
                                    isCompleted = true
                                    soundHelper.playFanfare()
                                    soundHelper.speak("Super job! You crossed the road safely!")
                                    repository.completeSkill(LifeSkillId.CROSS_ROAD, 45, 1)
                                }
                            }
                        },
                        enabled = canCross,
                        containerColor = if (canCross) Color(0xFF2E7D32) else Color(0xFF90A4AE),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        testTag = "cross_road_button"
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
                            Text(text = "🚦 Street Smart Hero! 🚦", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You stopped, checked both ways, and crossed safely! +45 XP & 1 Star ⭐",
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
                                    text = "Cross Again 🔄",
                                    onClick = {
                                        lookedLeft1 = false
                                        lookedRight = false
                                        lookedLeft2 = false
                                        lightState = "RED"
                                        kidCrossStep = 0
                                        isCompleted = false
                                        statusText = "Red light! Cars are zooming. Stop on the curb!"
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
private fun LookButton(
    text: String,
    isLooked: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(72.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = enabled && !isLooked) { onClick() }
            .border(
                width = 2.dp,
                color = if (isLooked) Color(0xFF43A047) else if (enabled) Color(0xFF0288D1) else Color(0xFFB0BEC5),
                shape = RoundedCornerShape(16.dp)
            ),
        color = if (isLooked) Color(0xFFE8F5E9) else if (enabled) CleanWhite else Color(0xFFECEFF1)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(4.dp)) {
            Text(
                text = if (isLooked) "✓ Done!" else text,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isLooked) Color(0xFF2E7D32) else if (enabled) TextDark else Color(0xFF90A4AE),
                textAlign = TextAlign.Center
            )
        }
    }
}
