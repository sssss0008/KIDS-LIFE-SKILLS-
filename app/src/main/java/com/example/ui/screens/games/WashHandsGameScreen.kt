package com.example.ui.screens.games

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
import kotlinx.coroutines.delay

@Composable
fun WashHandsGameScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var secondsLeft by remember { mutableIntStateOf(20) }
    var isRunning by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }
    var poppedBubblesCount by remember { mutableIntStateOf(0) }

    val steps = listOf(
        "1. Wet hands & apply soap 🧼",
        "2. Rub palm to palm 🫧",
        "3. Scrub between fingers 🖐️",
        "4. Scrub thumbs & fingernails 🔁",
        "5. Rinse clean & dry hands! ✨"
    )

    val currentStepIndex = ((20 - secondsLeft) / 4).coerceIn(0, 4)

    // Bubble targets to pop
    val bubbleStates = remember { mutableStateListOf(true, true, true, true, true, true) }

    LaunchedEffect(isRunning, secondsLeft) {
        if (isRunning && secondsLeft > 0) {
            delay(1000)
            secondsLeft--
            if (secondsLeft % 4 == 0) {
                // Reset bubbles for child to pop
                for (i in bubbleStates.indices) bubbleStates[i] = true
            }
        } else if (isRunning && secondsLeft == 0) {
            isRunning = false
            isCompleted = true
            soundHelper.playFanfare()
            soundHelper.speak("Squeaky clean! You washed away all the germs!")
            repository.completeSkill(LifeSkillId.WASH_HANDS, 35, 1)
        }
    }

    LaunchedEffect(Unit) {
        soundHelper.speak("Wash your hands for 20 seconds! Tap the soap bubbles to pop germs!")
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
                            tint = Color(0xFF00897B)
                        )
                    }
                    Text(
                        text = "🧼 Wash Hands Trainer",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { soundHelper.speak("Wash for 20 seconds. Scrub palms, fingers, and thumbs!") }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Read instructions",
                            tint = Color(0xFF00897B)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFE0F2F1))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Timer and Step Tracker
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
                                text = "⏱️ ${secondsLeft}s",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF00796B)
                            )
                            Text(
                                text = "🫧 $poppedBubblesCount Popped!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0288D1)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { (20 - secondsLeft) / 20f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = Color(0xFF00897B),
                            trackColor = Color(0xFFB2DFDB)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = steps[currentStepIndex],
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF004D40)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hand Washing Basin & Interactive Bubble Field
                Surface(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .border(4.dp, Color(0xFF80CBC4), RoundedCornerShape(32.dp)),
                    color = Color(0xFFE0F7FA),
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Hands in center
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isCompleted) "🙌 ✨" else if (isRunning) "🫧 👐 🫧" else "👐",
                                fontSize = 64.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isCompleted) "Germs Defeated!" else if (isRunning) "Pop bubbles to wash!" else "Tap Start to scrub!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF00695C)
                            )
                        }

                        // Floating interactive bubbles
                        if (isRunning) {
                            val positions = listOf(
                                Alignment.TopStart,
                                Alignment.TopEnd,
                                Alignment.CenterStart,
                                Alignment.CenterEnd,
                                Alignment.BottomStart,
                                Alignment.BottomEnd
                            )
                            positions.forEachIndexed { index, align ->
                                if (bubbleStates[index]) {
                                    Box(
                                        modifier = Modifier
                                            .align(align)
                                            .padding(16.dp)
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFB2EBF2))
                                            .border(2.dp, Color(0xFF26C6DA), CircleShape)
                                            .clickable {
                                                soundHelper.playBubblePop()
                                                bubbleStates[index] = false
                                                poppedBubblesCount++
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "🫧", fontSize = 26.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (!isRunning && !isCompleted) {
                    KidButton(
                        text = "Start 20s Wash! 🧼 💧",
                        onClick = {
                            isRunning = true
                            soundHelper.playClick()
                            soundHelper.speak("Start scrubbing palms and fingers!")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        containerColor = Color(0xFF00897B),
                        testTag = "start_wash_button"
                    )
                } else if (isRunning) {
                    KidButton(
                        text = "Pop All Bubbles! 🫧",
                        onClick = {
                            soundHelper.playBubblePop()
                            for (i in bubbleStates.indices) {
                                if (bubbleStates[i]) {
                                    bubbleStates[i] = false
                                    poppedBubblesCount++
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        containerColor = Color(0xFF0288D1)
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
                            Text(text = "🎉 Pure & Clean! 🎉", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You popped $poppedBubblesCount bubbles and cleaned your hands! +35 XP & 1 Star ⭐",
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
                                    text = "Wash Again 🔄",
                                    onClick = {
                                        secondsLeft = 20
                                        isCompleted = false
                                        isRunning = true
                                        poppedBubblesCount = 0
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
