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

data class WaterLeak(
    val id: String,
    val title: String,
    val room: String,
    val icon: String,
    val litersSaved: Int,
    val actionText: String
)

@Composable
fun WaterSavingGameScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val leaks = remember {
        listOf(
            WaterLeak("1", "Dripping Sink Tap", "Bathroom", "🚰", 12, "Twist faucet tight shut!"),
            WaterLeak("2", "Running Shower", "Shower", "🚿", 15, "Turn off water while lathering!"),
            WaterLeak("3", "Tap Running During Brushing", "Basin", "🪥", 10, "Turn off tap while scrubbing!"),
            WaterLeak("4", "Leaking Garden Hose", "Backyard", "🪴", 13, "Use watering can for plants!")
        )
    }

    val fixedLeakIds = remember { mutableStateListOf<String>() }
    var currentInfo by remember { mutableStateOf("Find and fix the 4 running water spots in the house!") }
    var isCompleted by remember { mutableStateOf(false) }

    val totalTargetLiters = 50
    val savedLiters = leaks.filter { fixedLeakIds.contains(it.id) }.sumOf { it.litersSaved }
    val progress = (savedLiters / totalTargetLiters.toFloat()).coerceAtMost(1f)

    LaunchedEffect(Unit) {
        soundHelper.speak("Water Saving Challenge! Tap the dripping taps and running showers to save 50 liters of clean water!")
    }

    fun fixLeak(leak: WaterLeak) {
        if (fixedLeakIds.contains(leak.id)) return

        soundHelper.playSuccess()
        fixedLeakIds.add(leak.id)
        currentInfo = "Fixed! Saved +${leak.litersSaved} Liters of water! 💧✨"
        soundHelper.speak("Saved ${leak.litersSaved} liters!")

        if (fixedLeakIds.size == leaks.size) {
            isCompleted = true
            soundHelper.playFanfare()
            soundHelper.speak("Super Water Guardian! You saved 50 liters of precious water!")
            repository.completeSkill(LifeSkillId.WATER_SAVING, 40, 1)
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
                            tint = Color(0xFF0288D1)
                        )
                    }
                    Text(
                        text = "💧 Water Saving Challenge",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { soundHelper.speak("Turn off faucets while brushing and fix dripping taps!") }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Read instructions",
                            tint = Color(0xFF0288D1)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFE1F5FE))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Water Saved Progress Meter
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
                                text = "💧 Water Meter",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "$savedLiters / $totalTargetLiters Liters Saved!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0288D1)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp)
                                .clip(RoundedCornerShape(7.dp)),
                            color = Color(0xFF0288D1),
                            trackColor = Color(0xFFB3E5FC)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentInfo,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF01579B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // The 4 Home Water Leaks
                Text(
                    text = "🏠 Tap running spots to shut them off:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    leaks.forEach { leak ->
                        val isFixed = fixedLeakIds.contains(leak.id)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .clickable(enabled = !isFixed) { fixLeak(leak) }
                                .border(
                                    width = if (isFixed) 2.dp else 1.5.dp,
                                    color = if (isFixed) Color(0xFF43A047) else Color(0xFF0288D1),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            color = if (isFixed) Color(0xFFE8F5E9) else CleanWhite,
                            shadowElevation = if (isFixed) 1.dp else 4.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(14.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(if (isFixed) Color(0xFFC8E6C9) else Color(0xFFE1F5FE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = leak.icon, fontSize = 28.sp)
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = leak.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                    Text(
                                        text = if (isFixed) "Saved ${leak.litersSaved}L! Faucet Off ✨" else "${leak.actionText} (+${leak.litersSaved}L)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isFixed) Color(0xFF2E7D32) else Color(0xFF0288D1),
                                        fontWeight = if (isFixed) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                                if (isFixed) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = "Fixed",
                                        tint = Color(0xFF43A047),
                                        modifier = Modifier.size(28.dp)
                                    )
                                } else {
                                    Text(
                                        text = "💧 Drip!",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFFE53935)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isCompleted) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "💧 Water Saver Hero! 💧", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You saved 50 Liters of water! Every drop counts for our planet! +40 XP & 1 Star ⭐",
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
                                        fixedLeakIds.clear()
                                        isCompleted = false
                                        currentInfo = "Find and fix the 4 running water spots in the house!"
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
