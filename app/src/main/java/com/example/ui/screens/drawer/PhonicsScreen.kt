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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.PhonicsItem
import com.example.sound.SoundHelper
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.TextDark

@Composable
fun PhonicsScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val phonicsList = repository.phonicsList
    var activeItem by remember { mutableStateOf(phonicsList.first()) }

    LaunchedEffect(Unit) {
        soundHelper.speak("Welcome to Phonics and Sound World! Tap any letter to hear its sound and life skill word!")
    }

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
                    text = "🔤 Phonics & Sound World",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { soundHelper.speak("${activeItem.letter}! ${activeItem.word}! ${activeItem.sentence}") }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.VolumeUp,
                        contentDescription = "Read sentence",
                        tint = Color(0xFF0288D1)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE1F5FE))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Active Hero Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF9C4))
                            .border(3.dp, Color(0xFFFFD54F), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = activeItem.letter,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE65100)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = activeItem.icon, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = activeItem.word,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 22.sp
                                ),
                                color = TextDark
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = activeItem.sentence,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF0277BD)
                        )
                    }
                    IconButton(
                        onClick = {
                            soundHelper.playSuccess()
                            soundHelper.speak("${activeItem.letter}! ${activeItem.word}! ${activeItem.sentence}")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Speak",
                            tint = SkyBlue,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tap a letter to listen and learn:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Grid of Letters
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(phonicsList) { item ->
                    val isActive = activeItem.letter == item.letter
                    Surface(
                        modifier = Modifier
                            .height(95.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                activeItem = item
                                soundHelper.playBubblePop()
                                soundHelper.speak("${item.letter}! ${item.word}! ${item.sentence}")
                            }
                            .border(
                                width = if (isActive) 3.dp else 1.dp,
                                color = if (isActive) Color(0xFF0288D1) else Color(0xFFB0BEC5),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        color = if (isActive) Color(0xFFB3E5FC) else CleanWhite,
                        shadowElevation = if (isActive) 6.dp else 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = item.letter,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isActive) Color(0xFF01579B) else TextDark
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = item.icon, fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.word,
                                fontSize = 12.sp,
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
