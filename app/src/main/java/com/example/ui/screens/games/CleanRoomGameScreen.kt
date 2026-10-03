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

enum class RoomTarget(val title: String, val icon: String, val colorHex: Long) {
    TOY_BOX("Toy Box", "📦", 0xFFFFE0B2),
    HAMPER("Hamper", "🧺", 0xFFE1BEE7),
    BOOKSHELF("Bookshelf", "📚", 0xFFC8E6C9),
    TRASH("Waste Bin", "🗑️", 0xFFCFD8DC)
}

data class RoomItem(
    val id: String,
    val name: String,
    val icon: String,
    val correctTarget: RoomTarget
)

@Composable
fun CleanRoomGameScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initialItems = remember {
        listOf(
            RoomItem("1", "Teddy Bear", "🧸", RoomTarget.TOY_BOX),
            RoomItem("2", "Race Car", "🏎️", RoomTarget.TOY_BOX),
            RoomItem("3", "Dirty Sock", "🧦", RoomTarget.HAMPER),
            RoomItem("4", "Striped Shirt", "👕", RoomTarget.HAMPER),
            RoomItem("5", "Adventure Book", "📖", RoomTarget.BOOKSHELF),
            RoomItem("6", "Used Paper", "📄", RoomTarget.TRASH)
        )
    }

    val uncleanedItems = remember { mutableStateListOf<RoomItem>().apply { addAll(initialItems) } }
    var selectedItem by remember { mutableStateOf<RoomItem?>(null) }
    var feedbackMessage by remember { mutableStateOf("Tap an item, then tap where it belongs!") }
    var isCompleted by remember { mutableStateOf(false) }

    val tidyPercentage = ((initialItems.size - uncleanedItems.size) / initialItems.size.toFloat())

    LaunchedEffect(Unit) {
        soundHelper.speak("Clean your room! Tap an item on the floor, then tap where it belongs!")
    }

    fun handleTargetClick(target: RoomTarget) {
        val current = selectedItem
        if (current == null) {
            soundHelper.playError()
            feedbackMessage = "First tap an item on the messy floor below! 👇"
            return
        }

        if (current.correctTarget == target) {
            soundHelper.playSuccess()
            feedbackMessage = "Great job! ${current.name} goes into ${target.title}! ✨"
            uncleanedItems.remove(current)
            selectedItem = null

            if (uncleanedItems.isEmpty()) {
                isCompleted = true
                soundHelper.playFanfare()
                soundHelper.speak("Amazing! Your room is sparkling clean and tidy!")
                repository.completeSkill(LifeSkillId.CLEAN_ROOM, 50, 1)
            }
        } else {
            soundHelper.playError()
            feedbackMessage = "Oops! ${current.name} doesn't go there. Try again! 🤔"
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
                            tint = Color(0xFFFF7043)
                        )
                    }
                    Text(
                        text = "🧹 Clean Your Room",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { soundHelper.speak("Put toys in toy box, clothes in laundry, and books on shelf!") }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Read instructions",
                            tint = Color(0xFFFF7043)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFFFF3E0))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tidy Progress
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
                                text = "✨ Tidy Room Meter",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "${(tidyPercentage * 100).toInt()}% Done",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFE65100)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { tidyPercentage },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = Color(0xFFFF7043),
                            trackColor = Color(0xFFFFE0B2)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = feedbackMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF5D4037)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Sorting Stations / Containers
                Text(
                    text = "📦 Sort Stations (Tap where item goes):",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RoomTarget.entries.forEach { target ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { handleTargetClick(target) }
                                .border(2.dp, Color(target.colorHex), RoundedCornerShape(16.dp)),
                            color = Color(target.colorHex),
                            shadowElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = target.icon, fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = target.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextDark,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Messy Floor Arena
                Text(
                    text = if (uncleanedItems.isNotEmpty()) "🧹 Messy Floor (Tap an item):" else "🌟 Your room is sparkling clean!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                if (uncleanedItems.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uncleanedItems.take(3).forEach { item ->
                            val isSelected = selectedItem?.id == item.id
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(90.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable {
                                        soundHelper.playClick()
                                        selectedItem = item
                                        feedbackMessage = "Selected ${item.name}! Now tap where it belongs! 👆"
                                    }
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color(0xFFFF7043) else Color(0xFFCFD8DC),
                                        shape = RoundedCornerShape(18.dp)
                                    ),
                                color = if (isSelected) Color(0xFFFFE0B2) else CleanWhite,
                                shadowElevation = if (isSelected) 6.dp else 2.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = item.icon, fontSize = 32.sp)
                                    Text(
                                        text = item.name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    if (uncleanedItems.size > 3) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uncleanedItems.drop(3).forEach { item ->
                                val isSelected = selectedItem?.id == item.id
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(90.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .clickable {
                                            soundHelper.playClick()
                                            selectedItem = item
                                            feedbackMessage = "Selected ${item.name}! Now tap where it belongs! 👆"
                                        }
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) Color(0xFFFF7043) else Color(0xFFCFD8DC),
                                            shape = RoundedCornerShape(18.dp)
                                        ),
                                    color = if (isSelected) Color(0xFFFFE0B2) else CleanWhite,
                                    shadowElevation = if (isSelected) 6.dp else 2.dp
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(text = item.icon, fontSize = 32.sp)
                                        Text(
                                            text = item.name,
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
                            Text(text = "👑 Tidy Room Champion! 👑", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Every toy, book, and clothes item is in its happy home! +50 XP & 1 Star ⭐",
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
                                        uncleanedItems.clear()
                                        uncleanedItems.addAll(initialItems)
                                        selectedItem = null
                                        isCompleted = false
                                        feedbackMessage = "Tap an item, then tap where it belongs!"
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
