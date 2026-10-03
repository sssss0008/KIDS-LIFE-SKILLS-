package com.example.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DataRepository
import com.example.sound.SoundHelper
import com.example.ui.components.AvatarDisplay
import com.example.ui.components.KidButton
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.GoldStar
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SunbeamYellow
import com.example.ui.theme.TextDark

@Composable
fun OnboardingScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(0) }
    var kidName by remember { mutableStateOf("Little Hero") }
    var selectedAge by remember { mutableIntStateOf(6) }
    var selectedAvatarId by remember { mutableStateOf("hero_boy") }

    val quickNames = listOf("Leo", "Mia", "Sam", "Zara", "Noah", "Emma", "Max", "Lily")
    val avatars = repository.availableAvatars

    LaunchedEffect(step) {
        when (step) {
            0 -> soundHelper.speak("Welcome to Kids Life Skills! What is your name?")
            1 -> soundHelper.speak("How old are you, little hero?")
            2 -> soundHelper.speak("Pick your favorite superhero avatar!")
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE1F5FE), Color(0xFFFFF8E1), Color(0xFFF3E5F5))
                )
            )
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Stepper Dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(if (index == step) 28.dp else 12.dp, 12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (index == step) SkyBlue else Color(0xFFCBD5E1))
                    )
                }
            }

            // Step Content
            when (step) {
                0 -> {
                    // Name Step
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "🌟", fontSize = 54.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Welcome Little Hero!",
                            style = MaterialTheme.typography.headlineLarge,
                            color = TextDark,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "What is your awesome name?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = kidName,
                            onValueChange = { kidName = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("onboarding_name_input"),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CleanWhite,
                                unfocusedContainerColor = CleanWhite,
                                focusedBorderColor = SkyBlue,
                                unfocusedBorderColor = Color(0xFFB0BEC5)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Or tap a name:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            items(quickNames) { name ->
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable {
                                            soundHelper.playClick()
                                            kidName = name
                                        },
                                    color = if (kidName == name) SkyBlue else CleanWhite,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.5.dp,
                                        if (kidName == name) SkyBlue else Color(0xFFCFD8DC)
                                    )
                                ) {
                                    Text(
                                        text = name,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = if (kidName == name) CleanWhite else TextDark
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Age Step
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "🎂", fontSize = 54.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "How old are you?",
                            style = MaterialTheme.typography.headlineLarge,
                            color = TextDark,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap your age number:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        val ages = listOf(3, 4, 5, 6, 7, 8, 9, 10)
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(ages) { age ->
                                val isSelected = selectedAge == age
                                Surface(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .clickable {
                                            soundHelper.playBubblePop()
                                            selectedAge = age
                                        }
                                        .testTag("age_option_$age"),
                                    color = if (isSelected) Color(0xFFFF7043) else CleanWhite,
                                    border = androidx.compose.foundation.BorderStroke(
                                        2.dp,
                                        if (isSelected) Color(0xFFD84315) else Color(0xFFCFD8DC)
                                    ),
                                    shadowElevation = if (isSelected) 6.dp else 2.dp
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "$age",
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isSelected) CleanWhite else TextDark
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Awesome! Age $selectedAge is a super age for life skills! 🎉",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = Color(0xFF2E7D32),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                2 -> {
                    // Avatar Step
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "🦸", fontSize = 54.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Choose Your Avatar!",
                            style = MaterialTheme.typography.headlineLarge,
                            color = TextDark,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pick the character you like best:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(avatars) { avatar ->
                                val isSelected = selectedAvatarId == avatar.id
                                Card(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .clickable {
                                            soundHelper.playBubblePop()
                                            selectedAvatarId = avatar.id
                                        }
                                        .testTag("avatar_${avatar.id}"),
                                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                                    border = androidx.compose.foundation.BorderStroke(
                                        if (isSelected) 3.dp else 1.dp,
                                        if (isSelected) SkyBlue else Color(0xFFE2E8F0)
                                    ),
                                    elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = avatar.emoji, fontSize = 34.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = avatar.name.split(" ").first(),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextDark,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step > 0) {
                    KidButton(
                        text = "Back",
                        onClick = {
                            soundHelper.playClick()
                            step--
                        },
                        containerColor = Color(0xFF90A4AE),
                        height = 50.dp,
                        modifier = Modifier.weight(0.8f),
                        testTag = "onboarding_back_button"
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                KidButton(
                    text = if (step < 2) "Next ➡️" else "Start Adventure! 🚀",
                    onClick = {
                        if (step < 2) {
                            soundHelper.playClick()
                            step++
                        } else {
                            soundHelper.playFanfare()
                            repository.updateProfile(
                                name = kidName,
                                age = selectedAge,
                                avatarId = selectedAvatarId
                            )
                            onComplete()
                        }
                    },
                    containerColor = if (step == 2) Color(0xFF43A047) else SkyBlue,
                    height = 54.dp,
                    modifier = Modifier.weight(1.2f),
                    testTag = "onboarding_next_button"
                )
            }
        }
    }
}
