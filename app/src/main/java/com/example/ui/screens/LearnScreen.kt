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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DataRepository
import com.example.data.LifeSkill
import com.example.data.LifeSkillId
import com.example.data.SkillCategory
import com.example.sound.SoundHelper
import com.example.ui.components.KidButton
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.TextDark

@Composable
fun LearnScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onNavigateToGame: (LifeSkillId) -> Unit,
    modifier: Modifier = Modifier
) {
    val lifeSkills = repository.lifeSkills
    var selectedCategory by remember { mutableStateOf<SkillCategory?>(null) }

    val filteredSkills = if (selectedCategory == null) {
        lifeSkills
    } else {
        lifeSkills.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "📖 Learn Life Skills",
            style = MaterialTheme.typography.headlineMedium,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Easy illustrated guides with voice read-aloud!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            item {
                CategoryChip(
                    title = "All Skills ✨",
                    isSelected = selectedCategory == null,
                    onClick = {
                        soundHelper.playClick()
                        selectedCategory = null
                    }
                )
            }
            items(SkillCategory.entries.toTypedArray()) { cat ->
                CategoryChip(
                    title = "${cat.icon} ${cat.title}",
                    isSelected = selectedCategory == cat,
                    onClick = {
                        soundHelper.playClick()
                        selectedCategory = cat
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(filteredSkills) { skill ->
                LearnSkillCard(
                    skill = skill,
                    soundHelper = soundHelper,
                    onPlayGame = { onNavigateToGame(skill.id) }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .border(
                1.5.dp,
                if (isSelected) SkyBlue else Color(0xFFCFD8DC),
                RoundedCornerShape(16.dp)
            ),
        color = if (isSelected) SkyBlue else CleanWhite
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = if (isSelected) CleanWhite else TextDark
        )
    }
}

@Composable
private fun LearnSkillCard(
    skill: LifeSkill,
    soundHelper: SoundHelper,
    onPlayGame: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(skill.colorHex).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = skill.icon, fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = skill.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark
                    )
                    Text(
                        text = skill.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(skill.colorHex),
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = {
                        val speechText = "${skill.title}! ${skill.description}. Step one: ${skill.steps.firstOrNull() ?: ""}"
                        soundHelper.speak(speechText)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.VolumeUp,
                        contentDescription = "Read Aloud",
                        tint = Color(skill.colorHex)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = skill.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF475569)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Steps
            Text(
                text = "Steps to Master:",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            skill.steps.forEachIndexed { index, step ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "${index + 1}.",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(skill.colorHex),
                        fontSize = 12.sp,
                        modifier = Modifier.width(20.dp)
                    )
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Play Game Button
            KidButton(
                text = "Practice in Game! 🎮",
                onClick = onPlayGame,
                containerColor = Color(skill.colorHex),
                height = 48.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
