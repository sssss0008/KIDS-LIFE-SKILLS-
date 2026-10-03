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
fun PlantGrowingGameScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Plant stages: 0=Seed, 1=Sprout, 2=Tall Stem, 3=Big Blooming Flower!
    var plantStage by remember { mutableIntStateOf(0) }
    var waterCount by remember { mutableIntStateOf(0) }
    var sunCount by remember { mutableIntStateOf(0) }
    var gardenStatus by remember { mutableStateOf("Give your little seed a drink of water and warm sunshine!") }
    var isCompleted by remember { mutableStateOf(false) }

    val plantEmojis = listOf(
        "🟤 🌱 (Seed in Soil)",
        "🌿 (Tiny Green Sprout)",
        "🪴 (Growing Tall)",
        "🌻 🦋 🐝 (Bloomed Sunflower!)"
    )

    LaunchedEffect(Unit) {
        soundHelper.speak("Plant Growing Game! Give your seed gentle water and warm sunshine to watch it bloom!")
    }

    fun checkGrowth() {
        val totalActions = waterCount + sunCount
        val newStage = when {
            totalActions >= 6 -> 3
            totalActions >= 4 -> 2
            totalActions >= 2 -> 1
            else -> 0
        }

        if (newStage > plantStage) {
            plantStage = newStage
            soundHelper.playSuccess()

            if (plantStage == 3) {
                isCompleted = true
                soundHelper.playFanfare()
                soundHelper.speak("Hooray! Your sunflower is in full golden bloom! You're a Green Thumb Hero!")
                repository.completeSkill(LifeSkillId.PLANT_GROWING, 50, 1)
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
                            tint = Color(0xFF689F38)
                        )
                    }
                    Text(
                        text = "🌱 Plant Growing Game",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { soundHelper.speak("Plants need water, warm sunshine, and love to grow!") }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Read instructions",
                            tint = Color(0xFF689F38)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF1F8E9))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Growth Meter Card
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
                                text = "🌻 Growth Stage: ${plantStage + 1} / 4",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = if (plantStage == 3) "Bloomed! 🌸" else "Growing... 🌱",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF33691E)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { ((plantStage + 1) / 4f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp)
                                .clip(RoundedCornerShape(7.dp)),
                            color = Color(0xFF689F38),
                            trackColor = Color(0xFFDCEDC8)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = gardenStatus,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF33691E)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Virtual Flower Pot
                Surface(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .border(4.dp, Color(0xFFAED581), RoundedCornerShape(32.dp)),
                    color = CleanWhite,
                    shadowElevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Sky & Sun in pot
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "☁️", fontSize = 30.sp)
                            Text(text = if (sunCount > 0) "☀️ ✨" else "⛅", fontSize = 34.sp)
                        }

                        // Center Plant Visualization
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = when (plantStage) {
                                    0 -> "🌱"
                                    1 -> "🌿"
                                    2 -> "🪴"
                                    else -> "🌻"
                                },
                                fontSize = if (plantStage == 3) 72.sp else 58.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = plantEmojis[plantStage],
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF2E7D32)
                            )
                        }

                        // Soil Base
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF5D4037)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🟫 Rich Potting Soil 🟫",
                                color = Color(0xFFD7CCC8),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Care Action Buttons: Water & Sun
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(84.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .clickable(enabled = !isCompleted) {
                                soundHelper.playBubblePop()
                                waterCount++
                                gardenStatus = "Splish splash! The soil loves the water! 💧"
                                checkGrowth()
                            }
                            .border(2.5.dp, Color(0xFF0288D1), RoundedCornerShape(22.dp)),
                        color = Color(0xFFE1F5FE),
                        shadowElevation = 3.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "💧 🪣", fontSize = 26.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Water Plant",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF01579B)
                            )
                            Text(
                                text = "Watered: $waterCount",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0288D1)
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(84.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .clickable(enabled = !isCompleted) {
                                soundHelper.playBubblePop()
                                sunCount++
                                gardenStatus = "Warm golden sunshine is powering photosynthesis! ☀️"
                                checkGrowth()
                            }
                            .border(2.5.dp, Color(0xFFFFA000), RoundedCornerShape(22.dp)),
                        color = Color(0xFFFFF8E1),
                        shadowElevation = 3.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "☀️ 🌈", fontSize = 26.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Give Sunshine",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFE65100)
                            )
                            Text(
                                text = "Sunshine: $sunCount",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFA000)
                            )
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
                            Text(text = "🌸 Green Thumb Champion! 🌸", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Your plant is blooming with joy! Plants give our planet oxygen and beauty! +50 XP & 1 Star ⭐",
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
                                    text = "Grow Another 🔄",
                                    onClick = {
                                        plantStage = 0
                                        waterCount = 0
                                        sunCount = 0
                                        isCompleted = false
                                        gardenStatus = "Give your little seed a drink of water and warm sunshine!"
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
