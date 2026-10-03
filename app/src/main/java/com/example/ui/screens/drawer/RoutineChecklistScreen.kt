package com.example.ui.screens.drawer

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DataRepository
import com.example.sound.SoundHelper
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.TextDark

@Composable
fun RoutineChecklistScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tasks by repository.routineTasks.collectAsState()
    val profile by repository.profile.collectAsState()

    val morningTasks = tasks.filter { it.isMorning }
    val eveningTasks = tasks.filter { !it.isMorning }
    val completedCount = tasks.count { it.isCompleted }

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
                    text = "📝 Daily Routine Checklist",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFFFDE7))
                .padding(16.dp)
        ) {
            // Daily Streak & Progress Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "🔥 ${profile.streakDays}-Day Super Streak!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE65100)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$completedCount of ${tasks.size} Daily Habits Done",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037)
                        )
                    }
                    Text(text = "☀️ 🌙", fontSize = 32.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "☀️ Morning Habits:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100)
                    )
                }

                items(morningTasks) { task ->
                    RoutineItemCard(
                        task = task,
                        onToggle = {
                            soundHelper.playSuccess()
                            repository.toggleRoutineTask(task.id)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "🌙 Evening & Bedtime Habits:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A148C)
                    )
                }

                items(eveningTasks) { task ->
                    RoutineItemCard(
                        task = task,
                        onToggle = {
                            soundHelper.playSuccess()
                            repository.toggleRoutineTask(task.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RoutineItemCard(
    task: com.example.data.RoutineTask,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onToggle() }
            .border(
                width = if (task.isCompleted) 2.dp else 1.dp,
                color = if (task.isCompleted) Color(0xFF43A047) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(18.dp)
            ),
        color = if (task.isCompleted) Color(0xFFE8F5E9) else CleanWhite,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = task.icon, fontSize = 26.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (task.isCompleted) FontWeight.Bold else FontWeight.Medium,
                    color = if (task.isCompleted) Color(0xFF2E7D32) else TextDark
                )
                Text(
                    text = if (task.isCompleted) "Done! +${task.xpReward} XP" else "+${task.xpReward} XP on completion",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (task.isCompleted) Color(0xFF388E3C) else Color(0xFF757575)
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (task.isCompleted) Color(0xFF43A047) else Color(0xFFECEFF1))
                    .border(
                        2.dp,
                        if (task.isCompleted) Color(0xFF2E7D32) else Color(0xFFB0BEC5),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Done",
                        tint = CleanWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
