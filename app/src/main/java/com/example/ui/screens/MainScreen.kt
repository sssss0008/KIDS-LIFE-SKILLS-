package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.LifeSkillId
import com.example.sound.SoundHelper
import com.example.ui.components.AvatarDisplay
import com.example.ui.screens.drawer.BackupRestoreScreen
import com.example.ui.screens.drawer.BadgesScreen
import com.example.ui.screens.drawer.LeaderboardScreen
import com.example.ui.screens.drawer.PhonicsScreen
import com.example.ui.screens.drawer.ProfileEditScreen
import com.example.ui.screens.drawer.QuizScreen
import com.example.ui.screens.drawer.RoutineChecklistScreen
import com.example.ui.screens.games.BrushTeethGameScreen
import com.example.ui.screens.games.CleanRoomGameScreen
import com.example.ui.screens.games.CrossRoadGameScreen
import com.example.ui.screens.games.HealthyFoodGameScreen
import com.example.ui.screens.games.MorningRoutineGameScreen
import com.example.ui.screens.games.PlantGrowingGameScreen
import com.example.ui.screens.games.RecycleSortingGameScreen
import com.example.ui.screens.games.SchoolBagGameScreen
import com.example.ui.screens.games.WashHandsGameScreen
import com.example.ui.screens.games.WaterSavingGameScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.GoldStar
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.TextDark
import kotlinx.coroutines.launch

enum class MainTab(val title: String, val icon: @Composable () -> Unit) {
    HOME("Home", { Icon(Icons.Rounded.Home, contentDescription = "Home") }),
    LEARN("Learn", { Icon(Icons.Rounded.MenuBook, contentDescription = "Learn") }),
    PRACTICE("Practice", { Icon(Icons.Rounded.SportsEsports, contentDescription = "Practice") }),
    ABOUT_US("About Us", { Icon(Icons.Default.Info, contentDescription = "About Us") })
}

sealed class ActiveScreen {
    object TabView : ActiveScreen()
    data class Game(val skillId: LifeSkillId) : ActiveScreen()
    object Quiz : ActiveScreen()
    object Phonics : ActiveScreen()
    object Leaderboard : ActiveScreen()
    object Badges : ActiveScreen()
    object ProfileEdit : ActiveScreen()
    object RoutineChecklist : ActiveScreen()
    object BackupRestore : ActiveScreen()
}

@Composable
fun MainScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    modifier: Modifier = Modifier
) {
    val profile by repository.profile.collectAsState()
    var currentTab by remember { mutableStateOf(MainTab.HOME) }
    var activeSubScreen by remember { mutableStateOf<ActiveScreen>(ActiveScreen.TabView) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Show onboarding if not onboarded
    if (!profile.isOnboarded) {
        OnboardingScreen(
            repository = repository,
            soundHelper = soundHelper,
            onComplete = {
                // profile isOnboarded becomes true
            }
        )
        return
    }

    // Secondary screen back handler
    BackHandler(enabled = activeSubScreen !is ActiveScreen.TabView) {
        activeSubScreen = ActiveScreen.TabView
    }

    // Render Sub-Screens
    when (val sub = activeSubScreen) {
        is ActiveScreen.Game -> {
            when (sub.skillId) {
                LifeSkillId.BRUSH_TEETH -> BrushTeethGameScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
                LifeSkillId.WASH_HANDS -> WashHandsGameScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
                LifeSkillId.CLEAN_ROOM -> CleanRoomGameScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
                LifeSkillId.SCHOOL_BAG -> SchoolBagGameScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
                LifeSkillId.HEALTHY_FOOD -> HealthyFoodGameScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
                LifeSkillId.CROSS_ROAD -> CrossRoadGameScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
                LifeSkillId.RECYCLE_SORT -> RecycleSortingGameScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
                LifeSkillId.WATER_SAVING -> WaterSavingGameScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
                LifeSkillId.PLANT_GROWING -> PlantGrowingGameScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
                LifeSkillId.MORNING_ROUTINE -> MorningRoutineGameScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
            }
            return
        }
        is ActiveScreen.Quiz -> {
            QuizScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
            return
        }
        is ActiveScreen.Phonics -> {
            PhonicsScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
            return
        }
        is ActiveScreen.Leaderboard -> {
            LeaderboardScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
            return
        }
        is ActiveScreen.Badges -> {
            BadgesScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
            return
        }
        is ActiveScreen.ProfileEdit -> {
            ProfileEditScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
            return
        }
        is ActiveScreen.RoutineChecklist -> {
            RoutineChecklistScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
            return
        }
        is ActiveScreen.BackupRestore -> {
            BackupRestoreScreen(repository, soundHelper, onBack = { activeSubScreen = ActiveScreen.TabView })
            return
        }
        ActiveScreen.TabView -> {
            // Display standard 4-tab scaffold with drawer
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                drawerContainerColor = CleanWhite
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Drawer Header with Profile
                    val currentAvatar = repository.availableAvatars.firstOrNull { it.id == profile.avatarId }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                scope.launch { drawerState.close() }
                                activeSubScreen = ActiveScreen.ProfileEdit
                            },
                        color = Color(0xFFF1F5F9)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AvatarDisplay(
                                emoji = currentAvatar?.emoji ?: "🦸‍♂️",
                                bgHex = currentAvatar?.bgHex ?: 0xFFFFF8E1,
                                size = 52.dp,
                                fontSize = 28
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = profile.name,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    color = TextDark
                                )
                                Text(
                                    text = profile.superTitle,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0288D1)
                                )
                            }
                            Text(text = "✏️", fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "EXPLORE & TOOLS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    // Drawer Items
                    NavigationDrawerItem(
                        label = { Text("Daily Routine Checklist", fontWeight = FontWeight.SemiBold) },
                        icon = { Text("📝", fontSize = 20.sp) },
                        selected = false,
                        onClick = {
                            soundHelper.playClick()
                            scope.launch { drawerState.close() }
                            activeSubScreen = ActiveScreen.RoutineChecklist
                        },
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = CleanWhite)
                    )

                    NavigationDrawerItem(
                        label = { Text("Kids Quiz Arena", fontWeight = FontWeight.SemiBold) },
                        icon = { Text("❓", fontSize = 20.sp) },
                        selected = false,
                        onClick = {
                            soundHelper.playClick()
                            scope.launch { drawerState.close() }
                            activeSubScreen = ActiveScreen.Quiz
                        },
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = CleanWhite)
                    )

                    NavigationDrawerItem(
                        label = { Text("Phonics & Sound World", fontWeight = FontWeight.SemiBold) },
                        icon = { Text("🔤", fontSize = 20.sp) },
                        selected = false,
                        onClick = {
                            soundHelper.playClick()
                            scope.launch { drawerState.close() }
                            activeSubScreen = ActiveScreen.Phonics
                        },
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = CleanWhite)
                    )

                    NavigationDrawerItem(
                        label = { Text("Hero Rankings & Leaderboard", fontWeight = FontWeight.SemiBold) },
                        icon = { Text("🏆", fontSize = 20.sp) },
                        selected = false,
                        onClick = {
                            soundHelper.playClick()
                            scope.launch { drawerState.close() }
                            activeSubScreen = ActiveScreen.Leaderboard
                        },
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = CleanWhite)
                    )

                    NavigationDrawerItem(
                        label = { Text("Sticker Album & Badges", fontWeight = FontWeight.SemiBold) },
                        icon = { Text("🎖️", fontSize = 20.sp) },
                        selected = false,
                        onClick = {
                            soundHelper.playClick()
                            scope.launch { drawerState.close() }
                            activeSubScreen = ActiveScreen.Badges
                        },
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = CleanWhite)
                    )

                    NavigationDrawerItem(
                        label = { Text("Backup & Restore Data", fontWeight = FontWeight.SemiBold) },
                        icon = { Text("💾", fontSize = 20.sp) },
                        selected = false,
                        onClick = {
                            soundHelper.playClick()
                            scope.launch { drawerState.close() }
                            activeSubScreen = ActiveScreen.BackupRestore
                        },
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = CleanWhite)
                    )

                    NavigationDrawerItem(
                        label = { Text("Edit Kid Profile", fontWeight = FontWeight.SemiBold) },
                        icon = { Text("✏️", fontSize = 20.sp) },
                        selected = false,
                        onClick = {
                            soundHelper.playClick()
                            scope.launch { drawerState.close() }
                            activeSubScreen = ActiveScreen.ProfileEdit
                        },
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = CleanWhite)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "SETTINGS & AUDIO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    // Sound Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = if (profile.soundEnabled) "🔊" else "🔇", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Sound Effects", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                        Switch(
                            checked = profile.soundEnabled,
                            onCheckedChange = { repository.setSoundEnabled(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = SkyBlue)
                        )
                    }

                    // Speech Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = if (profile.speechEnabled) "🗣️" else "🤫", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Voice Read-Aloud", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                        Switch(
                            checked = profile.speechEnabled,
                            onCheckedChange = { repository.setSpeechEnabled(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = SkyBlue)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CleanWhite)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            soundHelper.playClick()
                            scope.launch { drawerState.open() }
                        },
                        modifier = Modifier.testTag("menu_drawer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = SkyBlue,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = "Kids Life Skills",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        ),
                        color = SkyBlue,
                        modifier = Modifier.weight(1f)
                    )

                    // Quick Star & XP indicator
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                soundHelper.playBubblePop()
                                activeSubScreen = ActiveScreen.Leaderboard
                            },
                        color = Color(0xFFFFF9C4)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⭐", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${profile.totalStars}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = Color(0xFFE65100)
                            )
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = CleanWhite,
                    tonalElevation = 8.dp,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    MainTab.entries.forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                soundHelper.playClick()
                                currentTab = tab
                            },
                            icon = tab.icon,
                            label = {
                                Text(
                                    text = tab.title,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SkyBlue,
                                selectedTextColor = SkyBlue,
                                indicatorColor = Color(0xFFE1F5FE),
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF64748B)
                            ),
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    MainTab.HOME -> HomeScreen(
                        repository = repository,
                        soundHelper = soundHelper,
                        onNavigateToGame = { skillId -> activeSubScreen = ActiveScreen.Game(skillId) },
                        onNavigateToLearn = { currentTab = MainTab.LEARN },
                        onNavigateToQuiz = { activeSubScreen = ActiveScreen.Quiz },
                        onNavigateToRoutine = { activeSubScreen = ActiveScreen.RoutineChecklist }
                    )
                    MainTab.LEARN -> LearnScreen(
                        repository = repository,
                        soundHelper = soundHelper,
                        onNavigateToGame = { skillId -> activeSubScreen = ActiveScreen.Game(skillId) }
                    )
                    MainTab.PRACTICE -> PracticeScreen(
                        repository = repository,
                        soundHelper = soundHelper,
                        onNavigateToGame = { skillId -> activeSubScreen = ActiveScreen.Game(skillId) }
                    )
                    MainTab.ABOUT_US -> AboutScreen(
                        soundHelper = soundHelper,
                        onNavigateToBackup = { activeSubScreen = ActiveScreen.BackupRestore }
                    )
                }
            }
        }
    }
}
