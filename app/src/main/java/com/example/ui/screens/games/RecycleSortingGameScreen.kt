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

enum class RecycleBinType(val binName: String, val icon: String, val colorHex: Long) {
    PAPER("Blue: Paper", "🟦", 0xFF1976D2),
    PLASTIC("Yellow: Plastic", "🟨", 0xFFFBC02D),
    COMPOST("Green: Compost", "🟩", 0xFF388E3C),
    GLASS("Grey: Glass", "⬜", 0xFF78909C)
}

data class WasteItem(
    val id: String,
    val name: String,
    val icon: String,
    val correctBin: RecycleBinType
)

@Composable
fun RecycleSortingGameScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initialItems = remember {
        listOf(
            WasteItem("1", "Cardboard Box", "📦", RecycleBinType.PAPER),
            WasteItem("2", "Plastic Bottle", "🧴", RecycleBinType.PLASTIC),
            WasteItem("3", "Banana Peel", "🍌", RecycleBinType.COMPOST),
            WasteItem("4", "Newspaper", "📰", RecycleBinType.PAPER),
            WasteItem("5", "Apple Core", "🍏", RecycleBinType.COMPOST),
            WasteItem("6", "Glass Jar", "🫙", RecycleBinType.GLASS)
        )
    }

    val unmanagedItems = remember { mutableStateListOf<WasteItem>().apply { addAll(initialItems) } }
    var selectedItem by remember { mutableStateOf<WasteItem?>(null) }
    var tipMessage by remember { mutableStateOf("Tap an item, then drop it into the right colored bin!") }
    var isCompleted by remember { mutableStateOf(false) }

    val sortedCount = initialItems.size - unmanagedItems.size
    val progress = sortedCount / initialItems.size.toFloat()

    LaunchedEffect(Unit) {
        soundHelper.speak("Recycle Sorting! Put paper in blue, plastic in yellow, food in green, and glass in grey!")
    }

    fun handleBinClick(bin: RecycleBinType) {
        val current = selectedItem
        if (current == null) {
            soundHelper.playError()
            tipMessage = "First choose an item to recycle below! 👇"
            return
        }

        if (current.correctBin == bin) {
            soundHelper.playSuccess()
            tipMessage = "Great! ${current.name} belongs in the ${bin.binName}! 🌍💚"
            soundHelper.speak("Sorted into ${bin.binName}!")
            unmanagedItems.remove(current)
            selectedItem = null

            if (unmanagedItems.isEmpty()) {
                isCompleted = true
                soundHelper.playFanfare()
                soundHelper.speak("Super Planet Earth Hero! You recycled everything correctly!")
                repository.completeSkill(LifeSkillId.RECYCLE_SORT, 45, 1)
            }
        } else {
            soundHelper.playError()
            tipMessage = "Oops! ${current.name} doesn't go there. Check the bin material! 🤔"
            soundHelper.speak("Try another bin!")
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
                            tint = Color(0xFF2E7D32)
                        )
                    }
                    Text(
                        text = "♻️ Recycle Sorting",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { soundHelper.speak("Sort blue for paper, yellow for plastic, green for food, grey for glass!") }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Read instructions",
                            tint = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFE8F5E9))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Eco Progress Card
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
                                text = "🌍 Eco Planet Score",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "$sortedCount / ${initialItems.size} Sorted",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = Color(0xFF2E7D32),
                            trackColor = Color(0xFFC8E6C9)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tipMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1B5E20)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // The 4 Recycling Bins
                Text(
                    text = "🗂️ Recycling Bins (Tap bin to drop item):",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RecycleBinType.entries.forEach { bin ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(115.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { handleBinClick(bin) }
                                .border(2.5.dp, Color(bin.colorHex), RoundedCornerShape(20.dp)),
                            color = Color(bin.colorHex).copy(alpha = 0.15f),
                            shadowElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = bin.icon, fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = bin.binName.split(": ").last(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(bin.colorHex),
                                    maxLines = 1
                                )
                                Text(
                                    text = "BIN ♻️",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(bin.colorHex)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Items on Table
                Text(
                    text = if (unmanagedItems.isNotEmpty()) "📦 Items to Sort (Tap an item):" else "🌟 Planet Earth says Thank You!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                if (unmanagedItems.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        unmanagedItems.take(3).forEach { item ->
                            val isSelected = selectedItem?.id == item.id
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(95.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable {
                                        soundHelper.playClick()
                                        selectedItem = item
                                        tipMessage = "Selected ${item.name}! Drop it in a bin! 👆"
                                    }
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color(0xFF2E7D32) else Color(0xFFCFD8DC),
                                        shape = RoundedCornerShape(18.dp)
                                    ),
                                color = if (isSelected) Color(0xFFC8E6C9) else CleanWhite,
                                shadowElevation = if (isSelected) 6.dp else 2.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = item.icon, fontSize = 32.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
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

                    if (unmanagedItems.size > 3) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            unmanagedItems.drop(3).forEach { item ->
                                val isSelected = selectedItem?.id == item.id
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(95.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .clickable {
                                        soundHelper.playClick()
                                        selectedItem = item
                                        tipMessage = "Selected ${item.name}! Drop it in a bin! 👆"
                                    }
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color(0xFF2E7D32) else Color(0xFFCFD8DC),
                                        shape = RoundedCornerShape(18.dp)
                                    ),
                                color = if (isSelected) Color(0xFFC8E6C9) else CleanWhite,
                                shadowElevation = if (isSelected) 6.dp else 2.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = item.icon, fontSize = 32.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
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
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🌱 Eco Hero Champion! 🌱", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You protected trees and oceans by recycling like a master! +45 XP & 1 Star ⭐",
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
                                    text = "Recycle Again 🔄",
                                    onClick = {
                                        unmanagedItems.clear()
                                        unmanagedItems.addAll(initialItems)
                                        selectedItem = null
                                        isCompleted = false
                                        tipMessage = "Tap an item, then drop it into the right colored bin!"
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
