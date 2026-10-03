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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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

data class FoodItem(
    val name: String,
    val icon: String,
    val isHealthy: Boolean,
    val tip: String
)

@Composable
fun HealthyFoodGameScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val foods = remember {
        listOf(
            FoodItem("Apple", "🍎", true, "Crunchy apples keep your heart and teeth strong!"),
            FoodItem("Carrot", "🥕", true, "Carrots have vitamin A for superhero vision!"),
            FoodItem("Broccoli", "🥦", true, "Little green trees packed with vitamins!"),
            FoodItem("Boiled Egg", "🥚", true, "Eggs help grow strong muscles!"),
            FoodItem("Banana", "🍌", true, "Bananas give fast natural energy for running!"),
            FoodItem("Fresh Water", "💧", true, "Water hydrates your brain and body!"),
            FoodItem("Donut", "🍩", false, "Yummy sometimes, but too much sugar causes crashes!"),
            FoodItem("Soda", "🥤", false, "Fizzy sodas have lots of sneaky sugar!"),
            FoodItem("Lollipop", "🍭", false, "Sticky sugar can cause cavities in teeth!")
        )
    }

    val plateItems = remember { mutableStateListOf<FoodItem>() }
    var currentTip by remember { mutableStateOf("Tap healthy foods to build your Superhero Power Plate!") }
    var isCompleted by remember { mutableStateOf(false) }

    val neededHealthyCount = 4
    val healthyCount = plateItems.count { it.isHealthy }
    val progress = (healthyCount / neededHealthyCount.toFloat()).coerceAtMost(1f)

    LaunchedEffect(Unit) {
        soundHelper.speak("Build a Healthy Power Plate! Pick fruits, veggies, and good proteins!")
    }

    fun onFoodTap(food: FoodItem) {
        if (isCompleted) return

        if (food.isHealthy) {
            if (!plateItems.contains(food)) {
                soundHelper.playSuccess()
                plateItems.add(food)
                currentTip = food.tip
                soundHelper.speak(food.tip)

                if (plateItems.count { it.isHealthy } >= neededHealthyCount) {
                    isCompleted = true
                    soundHelper.playFanfare()
                    soundHelper.speak("Power Plate Complete! Your body has super energy!")
                    repository.completeSkill(LifeSkillId.HEALTHY_FOOD, 40, 1)
                }
            } else {
                soundHelper.playClick()
                currentTip = "${food.name} is already on your plate! 🍽️"
            }
        } else {
            soundHelper.playError()
            currentTip = "Warning: ${food.tip}"
            soundHelper.speak(food.tip)
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
                            tint = Color(0xFF43A047)
                        )
                    }
                    Text(
                        text = "🍎 Healthy Food Choice",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { soundHelper.speak("Choose healthy foods like apples, carrots, and eggs for your plate!") }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Read instructions",
                            tint = Color(0xFF43A047)
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
                // Power Meter Card
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
                                text = "⚡ Superhero Energy Meter",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "$healthyCount / $neededHealthyCount Foods",
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
                            color = Color(0xFF43A047),
                            trackColor = Color(0xFFC8E6C9)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentTip,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1B5E20)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // The Big Plate
                Surface(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(CircleShape)
                        .border(6.dp, Color(0xFF81C784), CircleShape),
                    color = CleanWhite,
                    shadowElevation = 6.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (plateItems.isEmpty()) {
                            Text(
                                text = "🍽️ Empty Plate\nTap healthy foods below!",
                                textAlign = TextAlign.Center,
                                color = Color(0xFF757575),
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                plateItems.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .size(52.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE8F5E9)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = item.icon, fontSize = 28.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Food Market Items
                Text(
                    text = "🥗 Food Market (Tap to eat):",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(foods) { food ->
                        val isChosen = plateItems.contains(food)
                        Surface(
                            modifier = Modifier
                                .size(90.dp, 100.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onFoodTap(food) }
                                .border(
                                    width = if (isChosen) 2.5.dp else 1.dp,
                                    color = if (isChosen) Color(0xFF43A047) else Color(0xFFCFD8DC),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            color = if (isChosen) Color(0xFFC8E6C9) else CleanWhite,
                            shadowElevation = if (isChosen) 1.dp else 3.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = food.icon, fontSize = 34.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = food.name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark,
                                    maxLines = 1
                                )
                                if (isChosen) {
                                    Text(
                                        text = "On Plate!",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF2E7D32)
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
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🥗 Nutrition Superhero! 🥗", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You packed your plate with rainbow vitamins! +40 XP & 1 Star ⭐",
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
                                    text = "Remake Plate 🔄",
                                    onClick = {
                                        plateItems.clear()
                                        isCompleted = false
                                        currentTip = "Tap healthy foods to build your Superhero Power Plate!"
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
