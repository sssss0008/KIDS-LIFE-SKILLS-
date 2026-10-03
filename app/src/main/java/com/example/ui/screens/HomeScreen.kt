package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DataRepository
import com.example.data.LifeSkill
import com.example.data.LifeSkillId
import com.example.sound.SoundHelper
import com.example.ui.components.AvatarDisplay
import com.example.ui.components.KidButton
import com.example.ui.components.KidSectionHeader
import com.example.ui.components.StarBadge
import com.example.ui.components.XPChip
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.GoldStar
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SunbeamYellow
import com.example.ui.theme.TextDark

@Composable
fun HomeScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onNavigateToGame: (LifeSkillId) -> Unit,
    onNavigateToLearn: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToRoutine: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by repository.profile.collectAsState()
    val completedSkills by repository.completedSkillIds.collectAsState()
    val routineTasks by repository.routineTasks.collectAsState()
    val lifeSkills = repository.lifeSkills

    val currentAvatar = repository.availableAvatars.firstOrNull { it.id == profile.avatarId }
    val pendingRoutineCount = routineTasks.count { !it.isCompleted }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Greeting Banner with Avatar & Badges
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .border(2.dp, Color(0xFFE2E8F0), RoundedCornerShape(28.dp)),
            color = CleanWhite,
            shadowElevation = 3.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarDisplay(
                        emoji = currentAvatar?.emoji ?: "🦸‍♂️",
                        bgHex = currentAvatar?.bgHex ?: 0xFFFFF8E1,
                        size = 64.dp,
                        fontSize = 34
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hello, ${profile.name}! 👋",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 21.sp
                            ),
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = profile.superTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF57C00)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StarBadge(stars = profile.totalStars, modifier = Modifier.weight(1f))
                    XPChip(xp = profile.totalXp, modifier = Modifier.weight(1.2f))
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color(0xFFFFCC80), RoundedCornerShape(16.dp)),
                        color = Color(0xFFFFF3E0)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "🔥", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${profile.streakDays}d Streak",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = Color(0xFFE65100)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Mission Card: Morning Routine or Brush Teeth
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .clickable {
                    soundHelper.playClick()
                    onNavigateToGame(LifeSkillId.BRUSH_TEETH)
                }
                .testTag("featured_mission_card"),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(CleanWhite),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🪥", fontSize = 34.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Today's Hero Mission 🌟",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0288D1)
                    )
                    Text(
                        text = "Brush Teeth Game!",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark
                    )
                    Text(
                        text = "Defeat sugar bugs & keep teeth sparkling!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF01579B)
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = "Play",
                    tint = SkyBlue,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Routine Checklist Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .clickable {
                    soundHelper.playClick()
                    onNavigateToRoutine()
                }
                .border(1.5.dp, Color(0xFFFFD54F), RoundedCornerShape(22.dp)),
            color = Color(0xFFFFFDE7)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "☀️", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Daily Routine Checklist",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextDark
                        )
                        Text(
                            text = if (pendingRoutineCount == 0) "All habits done today! 🎉" else "$pendingRoutineCount habits left today",
                            fontSize = 12.sp,
                            color = Color(0xFFE65100),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    text = "View ➡️",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = Color(0xFFF57C00)
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Featured Life Skills Carousel
        KidSectionHeader(
            title = "Popular Life Skills Games",
            icon = "🎮",
            subtitle = "Tap to play interactive challenges!"
        )
        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(lifeSkills) { skill ->
                val isDone = completedSkills.contains(skill.id.key)
                Surface(
                    modifier = Modifier
                        .size(140.dp, 165.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .clickable {
                            soundHelper.playClick()
                            onNavigateToGame(skill.id)
                        }
                        .border(
                            width = 2.dp,
                            color = Color(skill.colorHex),
                            shape = RoundedCornerShape(22.dp)
                        ),
                    color = CleanWhite,
                    shadowElevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(skill.colorHex).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = skill.icon, fontSize = 26.sp)
                        }
                        Text(
                            text = skill.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            maxLines = 2
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "+${skill.xpReward} XP",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(skill.colorHex)
                            )
                            if (isDone) {
                                Text(text = "⭐", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quiz Arena Banner
        KidButton(
            text = "Play Kids Quiz Arena! ❓ 🏆",
            onClick = {
                soundHelper.playClick()
                onNavigateToQuiz()
            },
            containerColor = Color(0xFF7E57C2),
            modifier = Modifier.fillMaxWidth(),
            testTag = "home_quiz_button"
        )
    }
}
