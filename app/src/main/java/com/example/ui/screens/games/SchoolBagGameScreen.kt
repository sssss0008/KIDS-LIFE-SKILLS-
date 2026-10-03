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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
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

data class BagItem(
    val id: String,
    val name: String,
    val icon: String,
    val isSchoolItem: Boolean,
    val reason: String
)

@Composable
fun SchoolBagGameScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allItems = remember {
        listOf(
            BagItem("1", "Notebook", "📓", true, "Needed for lessons!"),
            BagItem("2", "Pencil Case", "✏️", true, "For writing and drawing!"),
            BagItem("3", "Water Bottle", "💧", true, "Stay hydrated during recess!"),
            BagItem("4", "Healthy Lunch", "🥪", true, "Fuel for your brain!"),
            BagItem("5", "Homework Folder", "📂", true, "To show your teacher!"),
            BagItem("6", "TV Remote", "📺", false, "TV remotes belong at home in the living room!"),
            BagItem("7", "Video Game", "🎮", false, "Video games stay home for playtime!"),
            BagItem("8", "Ice Cream", "🍦", false, "Ice cream will melt all over your bag!"),
            BagItem("9", "Bowling Ball", "🎳", false, "Too heavy! A heavy backpack hurts your back!")
        )
    }

    val packedItemIds = remember { mutableStateListOf<String>() }
    var hintMessage by remember { mutableStateOf("Tap the items that belong in your school backpack!") }
    var isCompleted by remember { mutableStateOf(false) }

    val neededCount = allItems.count { it.isSchoolItem }
    val packedCount = packedItemIds.size
    val progress = packedCount / neededCount.toFloat()

    LaunchedEffect(Unit) {
        soundHelper.speak("School Bag Packing! Pick the right things for school and leave the toys at home!")
    }

    fun onItemClick(item: BagItem) {
        if (isCompleted) return

        if (packedItemIds.contains(item.id)) {
            soundHelper.playClick()
            hintMessage = "${item.name} is already packed! 🎒"
            return
        }

        if (item.isSchoolItem) {
            soundHelper.playSuccess()
            packedItemIds.add(item.id)
            hintMessage = "Packed ${item.name}! ${item.reason} 🎒✨"

            if (packedItemIds.size == neededCount) {
                isCompleted = true
                soundHelper.playFanfare()
                soundHelper.speak("Awesome! Your school bag is packed and ready for tomorrow!")
                repository.completeSkill(LifeSkillId.SCHOOL_BAG, 45, 1)
            }
        } else {
            soundHelper.playError()
            hintMessage = "Nope! ${item.reason} ❌"
            soundHelper.speak(item.reason)
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
                            tint = Color(0xFFAB47BC)
                        )
                    }
                    Text(
                        text = "🎒 School Bag Packing",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { soundHelper.speak("Pack notebooks, pencils, water bottle, and lunch. Leave toys!") }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Read instructions",
                            tint = Color(0xFFAB47BC)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF3E5F5))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Backpack Packing Status
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
                                text = "🎒 Backpack Status",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "$packedCount / $neededCount Packed",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF6A1B9A)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = Color(0xFFAB47BC),
                            trackColor = Color(0xFFE1BEE7)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = hintMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF4A148C)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Inside the Backpack Preview
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .border(3.dp, Color(0xFFCE93D8), RoundedCornerShape(24.dp)),
                    color = Color(0xFFEDE7F6)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Inside Backpack: ${if (packedItemIds.isEmpty()) "Empty (Tap items to pack!)" else ""}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF4A148C)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            allItems.filter { it.isSchoolItem }.forEach { item ->
                                val isPacked = packedItemIds.contains(item.id)
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(if (isPacked) Color(0xFFC8E6C9) else Color(0xFFE0E0E0))
                                        .border(
                                            2.dp,
                                            if (isPacked) Color(0xFF43A047) else Color(0xFFBDBDBD),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isPacked) item.icon else "❓",
                                        fontSize = 24.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Item Grid Selection
                Text(
                    text = "Items on your desk:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(allItems) { item ->
                        val isPacked = packedItemIds.contains(item.id)
                        Surface(
                            modifier = Modifier
                                .height(85.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .clickable { onItemClick(item) }
                                .border(
                                    width = if (isPacked) 2.5.dp else 1.dp,
                                    color = if (isPacked) Color(0xFF43A047) else Color(0xFFCFD8DC),
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            color = if (isPacked) Color(0xFFE8F5E9) else CleanWhite,
                            shadowElevation = if (isPacked) 1.dp else 3.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = item.icon, fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark,
                                    maxLines = 1
                                )
                                if (isPacked) {
                                    Text(
                                        text = "✓ In Bag",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                            Text(text = "🎒 Ready for School! 🎒", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Your bag is neat, light, and packed with all essentials! +45 XP & 1 Star ⭐",
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
                                    text = "Pack Again 🔄",
                                    onClick = {
                                        packedItemIds.clear()
                                        isCompleted = false
                                        hintMessage = "Tap the items that belong in your school backpack!"
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
