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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DataRepository
import com.example.sound.SoundHelper
import com.example.ui.components.KidButton
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.TextDark

@Composable
fun ProfileEditScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentProfile by repository.profile.collectAsState()
    var name by remember { mutableStateOf(currentProfile.name) }
    var age by remember { mutableIntStateOf(currentProfile.age) }
    var selectedAvatarId by remember { mutableStateOf(currentProfile.avatarId) }
    var superTitle by remember { mutableStateOf(currentProfile.superTitle) }

    val avatars = repository.availableAvatars
    val availableTitles = listOf(
        "Life Skills Explorer",
        "Toothbrush Ninja",
        "Eco Planet Hero",
        "Sunshine Champion",
        "Super Helper"
    )

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
                    text = "✏️ Edit Kid Profile",
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
                .background(Color(0xFFF0F4F8))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Current avatar display
            val currentAvatar = avatars.firstOrNull { it.id == selectedAvatarId }
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(currentAvatar?.bgHex ?: 0xFFFFF8E1))
                    .border(3.dp, SkyBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = currentAvatar?.emoji ?: "🦸‍♂️", fontSize = 48.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Kid's Name Field
            Text(
                text = "Child's Name:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_name_input"),
                shape = RoundedCornerShape(18.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CleanWhite,
                    unfocusedContainerColor = CleanWhite,
                    focusedBorderColor = SkyBlue,
                    unfocusedBorderColor = Color(0xFFB0BEC5)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Age Selector
            Text(
                text = "Child's Age ($age years old):",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (3..10).take(4).forEach { a ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                soundHelper.playBubblePop()
                                age = a
                            }
                            .border(
                                2.dp,
                                if (age == a) SkyBlue else Color(0xFFCFD8DC),
                                RoundedCornerShape(14.dp)
                            ),
                        color = if (age == a) SkyBlue else CleanWhite
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "$a",
                                fontWeight = FontWeight.Bold,
                                color = if (age == a) CleanWhite else TextDark,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (7..10).forEach { a ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                soundHelper.playBubblePop()
                                age = a
                            }
                            .border(
                                2.dp,
                                if (age == a) SkyBlue else Color(0xFFCFD8DC),
                                RoundedCornerShape(14.dp)
                            ),
                        color = if (age == a) SkyBlue else CleanWhite
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "$a",
                                fontWeight = FontWeight.Bold,
                                color = if (age == a) CleanWhite else TextDark,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Pick Avatar
            Text(
                text = "Choose Avatar Character:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(avatars) { avatar ->
                    val isSelected = selectedAvatarId == avatar.id
                    Surface(
                        modifier = Modifier
                            .height(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                soundHelper.playBubblePop()
                                selectedAvatarId = avatar.id
                            }
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) SkyBlue else Color(0xFFCFD8DC),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        color = if (isSelected) Color(0xFFE1F5FE) else CleanWhite
                    ) {
                        Column(
                            modifier = Modifier.padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = avatar.emoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = avatar.name.split(" ").first(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Superhero Title Chips
            Text(
                text = "Hero Title Badge:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                availableTitles.forEach { title ->
                    val isSelected = superTitle == title
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                soundHelper.playClick()
                                superTitle = title
                            }
                            .border(
                                2.dp,
                                if (isSelected) Color(0xFFFFA000) else Color(0xFFE0E0E0),
                                RoundedCornerShape(16.dp)
                            ),
                        color = if (isSelected) Color(0xFFFFF8E1) else CleanWhite
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = if (isSelected) "⭐" else "🌟", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color(0xFFE65100) else TextDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            KidButton(
                text = "Save Profile Changes! 💾",
                onClick = {
                    soundHelper.playSuccess()
                    soundHelper.speak("Profile updated! You're ready, $name!")
                    repository.updateProfile(
                        name = name,
                        age = age,
                        avatarId = selectedAvatarId,
                        title = superTitle
                    )
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                containerColor = Color(0xFF43A047),
                testTag = "save_profile_button"
            )
        }
    }
}
