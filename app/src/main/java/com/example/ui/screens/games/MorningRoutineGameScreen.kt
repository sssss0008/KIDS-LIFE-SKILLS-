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
import androidx.compose.material.icons.rounded.Check
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

data class RoutineStep(
    val index: Int,
    val title: String,
    val detail: String,
    val icon: String,
    val buttonAction: String
)

@Composable
fun MorningRoutineGameScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = remember {
        listOf(
            RoutineStep(0, "Wake Up & Stretch", "Smile and stretch your arms up high!", "☀️", "Big Morning Stretch! 🥱"),
            RoutineStep(1, "Make My Bed", "Smooth out the blanket and fluff the pillow!", "🛏️", "Tidy Up Blanket! ✨"),
            RoutineStep(2, "Brush Teeth & Wash Face", "Clean away sugar bugs and wake up refreshed!", "🪥", "Brush & Splash Water! 🫧"),
            RoutineStep(3, "Put On Clothes", "Change out of pajamas into awesome clothes!", "👕", "Put On Cool Outfit! 👖"),
            RoutineStep(4, "Healthy Breakfast", "Eat yummy fruits, eggs, or oatmeal!", "🥣", "Eat Super Breakfast! 🍎"),
            RoutineStep(5, "Shoes & Backpack", "Tie your shoes and grab your packed bag!", "🎒", "Ready To Conquer The Day! 🚀")
        )
    }

    var currentStepIndex by remember { mutableIntStateOf(0) }
    val completedSteps = remember { mutableStateListOf<Int>() }
    var isCompleted by remember { mutableStateOf(false) }

    val progress = completedSteps.size / steps.size.toFloat()

    LaunchedEffect(currentStepIndex) {
        if (currentStepIndex < steps.size) {
            val step = steps[currentStepIndex]
            soundHelper.speak("${step.title}! ${step.detail}")
        }
    }

    fun completeCurrentStep() {
        if (isCompleted) return

        soundHelper.playSuccess()
        completedSteps.add(currentStepIndex)

        if (currentStepIndex < steps.size - 1) {
            currentStepIndex++
        } else {
            isCompleted = true
            soundHelper.playFanfare()
            soundHelper.speak("Morning Routine Completed! You are a superstar ready for an awesome day!")
            repository.completeSkill(LifeSkillId.MORNING_ROUTINE, 60, 1)
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
                            tint = Color(0xFFFFA000)
                        )
                    }
                    Text(
                        text = "☀️ Morning Routine",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { soundHelper.speak("Follow your morning steps: stretch, make bed, brush, dress, breakfast, shoes!") }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Read instructions",
                            tint = Color(0xFFFFA000)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFFFF8E1))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Routine Progress
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
                                text = "☀️ Morning Hero Tracker",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "${completedSteps.size} / ${steps.size} Done",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFE65100)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp)
                                .clip(RoundedCornerShape(7.dp)),
                            color = Color(0xFFFFA000),
                            trackColor = Color(0xFFFFECB3)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Current Step Hero Card
                if (!isCompleted) {
                    val active = steps[currentStepIndex]
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .border(3.5.dp, Color(0xFFFFB300), RoundedCornerShape(32.dp)),
                        color = CleanWhite,
                        shadowElevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Step ${currentStepIndex + 1} of ${steps.size}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF57C00))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = active.icon, fontSize = 68.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = active.title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = TextDark,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = active.detail,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            KidButton(
                                text = active.buttonAction,
                                onClick = { completeCurrentStep() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                containerColor = Color(0xFFFFA000),
                                testTag = "routine_action_button"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // All Step Badges
                Text(
                    text = "📋 Morning Sequence Checklist:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    steps.forEach { step ->
                        val isDone = completedSteps.contains(step.index)
                        val isCurrent = currentStepIndex == step.index && !isCompleted
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = if (isCurrent) 2.5.dp else 1.dp,
                                    color = if (isDone) Color(0xFF43A047) else if (isCurrent) Color(0xFFFFA000) else Color(0xFFE0E0E0),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            color = if (isDone) Color(0xFFE8F5E9) else CleanWhite
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = step.icon, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = step.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Bold,
                                    color = if (isDone) Color(0xFF2E7D32) else TextDark,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = "Completed",
                                        tint = Color(0xFF2E7D32)
                                    )
                                } else if (isCurrent) {
                                    Text(
                                        text = "Current 👈",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF57C00)
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
                            Text(text = "👑 Morning Super Champion! 👑", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You conquered your morning routine! You are energized, prepared, and unstoppable! +60 XP & 1 Star ⭐",
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
                                    text = "Restart 🔄",
                                    onClick = {
                                        completedSteps.clear()
                                        currentStepIndex = 0
                                        isCompleted = false
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
