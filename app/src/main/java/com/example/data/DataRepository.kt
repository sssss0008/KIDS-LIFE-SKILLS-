package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class DataRepository(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("kids_life_skills_prefs", Context.MODE_PRIVATE)

    private val _profile = MutableStateFlow(loadProfile())
    val profile: StateFlow<KidProfile> = _profile.asStateFlow()

    private val _completedSkillIds = MutableStateFlow(loadCompletedSkills())
    val completedSkillIds: StateFlow<Set<String>> = _completedSkillIds.asStateFlow()

    private val _routineTasks = MutableStateFlow(loadRoutineTasks())
    val routineTasks: StateFlow<List<RoutineTask>> = _routineTasks.asStateFlow()

    // 10 Comprehensive Life Skills
    val lifeSkills: List<LifeSkill> = listOf(
        LifeSkill(
            id = LifeSkillId.BRUSH_TEETH,
            title = "Brush Teeth Game",
            subtitle = "Keep your smile sparkling white!",
            icon = "🪥",
            category = SkillCategory.HYGIENE,
            colorHex = 0xFF42A5F5,
            xpReward = 40,
            description = "Brush twice a day for 2 minutes to defeat sugar bugs and keep your teeth strong!",
            steps = listOf(
                "Squeeze pea-sized toothpaste onto your brush",
                "Brush top teeth gently in round circles",
                "Brush bottom teeth and back molars",
                "Gently brush your tongue and rinse with water!"
            ),
            funFacts = listOf(
                "Enamel on teeth is the hardest substance in your whole body!",
                "Brushing for 2 minutes cleans away sneaky sugar bugs."
            )
        ),
        LifeSkill(
            id = LifeSkillId.WASH_HANDS,
            title = "Wash Hands Trainer",
            subtitle = "Wash away germs with super soap!",
            icon = "🧼",
            category = SkillCategory.HYGIENE,
            colorHex = 0xFF26A69A,
            xpReward = 35,
            description = "Master the 20-second handwash technique with soap bubbles to stay germ-free!",
            steps = listOf(
                "Wet your hands with clean water",
                "Rub soap until foamy bubbles appear",
                "Wash between fingers and the backs of hands",
                "Scrub thumbs and fingernails for 20 seconds, then rinse!"
            ),
            funFacts = listOf(
                "Washing hands for 20 seconds is like singing Happy Birthday twice!",
                "Soap bubbles lift away 99% of invisible germs."
            )
        ),
        LifeSkill(
            id = LifeSkillId.CLEAN_ROOM,
            title = "Clean Your Room",
            subtitle = "Sort toys, clothes, and books!",
            icon = "🧹",
            category = SkillCategory.INDEPENDENCE,
            colorHex = 0xFFFF7043,
            xpReward = 50,
            description = "Put toys in the toy box, clothes in the hamper, and books on the shelf for a neat room!",
            steps = listOf(
                "Pick up toys from the floor and put them in the toy box",
                "Fold or place dirty clothes into the laundry hamper",
                "Stack storybooks neatly on the bookshelf",
                "Toss scrap papers into the recycling bin!"
            ),
            funFacts = listOf(
                "A clean bedroom helps your brain relax and sleep better!",
                "Cleaning up right after playing saves tons of time later."
            )
        ),
        LifeSkill(
            id = LifeSkillId.SCHOOL_BAG,
            title = "School Bag Packing",
            subtitle = "Pack what you need for tomorrow!",
            icon = "🎒",
            category = SkillCategory.INDEPENDENCE,
            colorHex = 0xFFAB47BC,
            xpReward = 45,
            description = "Choose the right school supplies and leave behind messy toys so your bag is light!",
            steps = listOf(
                "Check your school schedule for tomorrow's classes",
                "Pack your notebooks, pencils, and eraser",
                "Fill your clean water bottle and pack lunchbox",
                "Zip up the bag and place it near the door!"
            ),
            funFacts = listOf(
                "Packing your backpack the night before makes mornings relaxed!",
                "A heavy bag can hurt your back, only pack what is needed."
            )
        ),
        LifeSkill(
            id = LifeSkillId.HEALTHY_FOOD,
            title = "Healthy Food Choice",
            subtitle = "Build a colorful rainbow plate!",
            icon = "🍎",
            category = SkillCategory.HYGIENE,
            colorHex = 0xFF66BB6A,
            xpReward = 40,
            description = "Power up your body with crispy fruits, green veggies, and crunchy proteins!",
            steps = listOf(
                "Fill half your plate with colorful fruits and veggies",
                "Add healthy protein like eggs, beans, or chicken",
                "Choose whole grains like brown bread or oatmeal",
                "Drink refreshing fresh water instead of sugary sodas!"
            ),
            funFacts = listOf(
                "Eating different colored foods gives you different superpowers!",
                "Carrots help eyesight and berries boost your memory."
            )
        ),
        LifeSkill(
            id = LifeSkillId.CROSS_ROAD,
            title = "Cross The Road Safety",
            subtitle = "Look left, right, and left again!",
            icon = "🚦",
            category = SkillCategory.SAFETY_ECO,
            colorHex = 0xFFEF5350,
            xpReward = 45,
            description = "Learn zebra crossing rules, traffic signals, and how to hold a grown-up's hand!",
            steps = listOf(
                "Stop before the curb, never run into the street",
                "Look Left, look Right, and look Left again",
                "Wait for the green pedestrian walking light",
                "Walk calmly across the zebra stripes while listening!"
            ),
            funFacts = listOf(
                "Zebra crossings are painted with white stripes so drivers see you!",
                "Bright clothes make you easier to spot in cloudy weather."
            )
        ),
        LifeSkill(
            id = LifeSkillId.RECYCLE_SORT,
            title = "Recycle Sorting",
            subtitle = "Help our Planet Earth stay green!",
            icon = "♻️",
            category = SkillCategory.SAFETY_ECO,
            colorHex = 0xFF2E7D32,
            xpReward = 45,
            description = "Sort waste into Paper, Plastic, Organic Compost, and Glass bins like an eco hero!",
            steps = listOf(
                "Rinse plastic bottles before putting them in yellow bin",
                "Flatten cardboard boxes for the blue paper bin",
                "Put banana peels and apple cores in the green compost bin",
                "Remind family members to use reusable shopping bags!"
            ),
            funFacts = listOf(
                "Recycling 1 plastic bottle saves enough energy to power a lightbulb for 3 hours!",
                "Compost turns old food scraps into nutrient soil for gardens."
            )
        ),
        LifeSkill(
            id = LifeSkillId.WATER_SAVING,
            title = "Water Saving Challenge",
            subtitle = "Every single drop counts!",
            icon = "💧",
            category = SkillCategory.SAFETY_ECO,
            colorHex = 0xFF29B6F6,
            xpReward = 40,
            description = "Turn off running faucets while brushing and take quick showers to protect water!",
            steps = listOf(
                "Turn off the tap while scrubbing teeth and soaping hands",
                "Tell a grown-up if you spot a dripping faucet",
                "Take 5-minute showers instead of long deep baths",
                "Use leftover drinking water to feed house plants!"
            ),
            funFacts = listOf(
                "Leaving the tap running wastes up to 6 liters of water a minute!",
                "Less than 1% of all water on Earth is fresh drinking water."
            )
        ),
        LifeSkill(
            id = LifeSkillId.PLANT_GROWING,
            title = "Plant Growing Game",
            subtitle = "Care for your green garden buddy!",
            icon = "🌱",
            category = SkillCategory.SAFETY_ECO,
            colorHex = 0xFF8BC34A,
            xpReward = 50,
            description = "Plant a tiny seed, give it warm sunshine, and water it gently to watch it bloom!",
            steps = listOf(
                "Place a seed into rich potting soil",
                "Water it gently with a small watering can",
                "Place the pot near warm window sunshine",
                "Watch the tiny green sprout grow into a flowering buddy!"
            ),
            funFacts = listOf(
                "Plants breathe in carbon dioxide and breathe out fresh oxygen for us!",
                "Sunflowers turn their faces to follow the sun across the sky."
            )
        ),
        LifeSkill(
            id = LifeSkillId.MORNING_ROUTINE,
            title = "Morning Routine",
            subtitle = "Start every day like a super champion!",
            icon = "☀️",
            category = SkillCategory.DAILY_ROUTINE,
            colorHex = 0xFFFFA000,
            xpReward = 60,
            description = "Wake up happy, make your bed, brush teeth, dress up, and conquer the day!",
            steps = listOf(
                "Wake up with a big morning stretch and smile",
                "Pull up your blanket and make your bed neat",
                "Brush teeth and wash your face with cool water",
                "Put on clean school clothes and eat yummy breakfast!"
            ),
            funFacts = listOf(
                "Stretching in the morning wakes up your muscles and brain!",
                "A good morning routine gives you superpowers for the whole day."
            )
        )
    )

    // Avatars available for kids
    val availableAvatars: List<AvatarInfo> = listOf(
        AvatarInfo("hero_boy", "🦸‍♂️", "Super Leo", "The Hero", 0xFFE3F2FD),
        AvatarInfo("hero_girl", "🦸‍♀️", "Super Mia", "The Champion", 0xFFFCE4EC),
        AvatarInfo("panda", "🐼", "Pip the Panda", "The Gentle", 0xFFEDE7F6),
        AvatarInfo("lion", "🦁", "Leo the Brave", "The Strong", 0xFFFFF8E1),
        AvatarInfo("astronaut", "🚀", "Nova Kid", "The Explorer", 0xFFE0F7FA),
        AvatarInfo("bunny", "🐰", "Bouncy Bunny", "The Quick", 0xFFF3E5F5),
        AvatarInfo("robot", "🤖", "Beep Bot", "The Smart", 0xFFE8F5E9),
        AvatarInfo("unicorn", "🦄", "Sparkle Horn", "The Magical", 0xFFFFF0F5)
    )

    // Achievement Badges
    val badges: List<BadgeInfo> = listOf(
        BadgeInfo("badge_first_step", "First Step Hero", "Completed first skill game!", "🌟", 40),
        BadgeInfo("badge_smile", "Sparkling Smile", "Toothbrush champion!", "🪥", 80),
        BadgeInfo("badge_clean_hands", "Germ Buster", "Mastered 20-second hand washing!", "🧼", 120),
        BadgeInfo("badge_room_master", "Tidy Room Master", "Sorted toys and clothes neatly!", "🧹", 160),
        BadgeInfo("badge_school_ready", "School Ready Kid", "Packed school bag like a pro!", "🎒", 200),
        BadgeInfo("badge_eco_guard", "Eco Guardian", "Recycled waste and saved water!", "🌱", 250),
        BadgeInfo("badge_safety_first", "Street Smart Kid", "Crossed road with safe signals!", "🚦", 300),
        BadgeInfo("badge_green_thumb", "Green Thumb", "Grew a beautiful blooming plant!", "🌸", 350),
        BadgeInfo("badge_morning_star", "Morning Champion", "Completed the morning routine streak!", "☀️", 400),
        BadgeInfo("badge_grand_master", "Grand Life Skills Hero", "Reached over 500 XP!", "👑", 500)
    )

    // Kid Quiz Questions
    val quizQuestions: List<QuizQuestion> = listOf(
        QuizQuestion(
            id = 1,
            question = "How long should you brush your teeth every morning and night?",
            emoji = "🪥",
            options = listOf("10 seconds", "2 minutes", "1 whole hour"),
            correctIndex = 1,
            explanation = "Brushing for 2 minutes cleans every tooth and defeats sneaky sugar bugs!",
            category = "Hygiene"
        ),
        QuizQuestion(
            id = 2,
            question = "What should you use with water to wash away dirty germs from hands?",
            emoji = "🧼",
            options = listOf("Soap", "Juice", "Sand"),
            correctIndex = 0,
            explanation = "Soap creates foamy bubbles that trap and wash away 99% of germs!",
            category = "Hygiene"
        ),
        QuizQuestion(
            id = 3,
            question = "Where should dirty clothes go when taking them off?",
            emoji = "👕",
            options = listOf("Under the bed", "Laundry hamper", "Out the window"),
            correctIndex = 1,
            explanation = "Putting dirty clothes in the laundry hamper keeps your room clean and fresh!",
            category = "Room Care"
        ),
        QuizQuestion(
            id = 4,
            question = "What should you do before crossing the street?",
            emoji = "🚦",
            options = listOf("Close your eyes", "Run as fast as you can", "Stop, look left, right, and left again"),
            correctIndex = 2,
            explanation = "Always stop at the curb, hold a grown-up's hand, and look left-right-left!",
            category = "Safety"
        ),
        QuizQuestion(
            id = 5,
            question = "Which bin does an empty plastic juice bottle go into?",
            emoji = "♻️",
            options = listOf("Yellow Plastic Recycling", "Green Organic Food", "Floor"),
            correctIndex = 0,
            explanation = "Plastic bottles go into the yellow recycling bin so they can be made into new things!",
            category = "Eco"
        ),
        QuizQuestion(
            id = 6,
            question = "What is a super smart way to save water at home?",
            emoji = "💧",
            options = listOf("Turn off the tap while brushing", "Leave hose running all day", "Throw water away"),
            correctIndex = 0,
            explanation = "Turning off the faucet while brushing teeth saves up to 6 liters of clean water every time!",
            category = "Eco"
        ),
        QuizQuestion(
            id = 7,
            question = "What two important things does a little plant seed need to grow?",
            emoji = "🌱",
            options = listOf("Ice cream & Soda", "Water & Warm Sunlight", "Darkness & Noise"),
            correctIndex = 1,
            explanation = "Plants need gentle water and bright sunshine to make food and grow into flowers!",
            category = "Nature"
        ),
        QuizQuestion(
            id = 8,
            question = "Which snack gives your body long-lasting power and energy?",
            emoji = "🍎",
            options = listOf("A crunchy red apple", "Five lollipops", "Bag of salty chips"),
            correctIndex = 0,
            explanation = "Fruits like apples give your brain vitamins and super natural energy!",
            category = "Nutrition"
        )
    )

    // Phonics & Vocabulary
    val phonicsList: List<PhonicsItem> = listOf(
        PhonicsItem("A", "Apple", "🍎", "A is for Apple! Fresh and crunchy!"),
        PhonicsItem("B", "Brush", "🪥", "B is for Brush! Brush teeth twice a day!"),
        PhonicsItem("C", "Clean", "✨", "C is for Clean! Tidy room, happy mind!"),
        PhonicsItem("D", "Drop", "💧", "D is for Drop! Every water drop counts!"),
        PhonicsItem("E", "Earth", "🌍", "E is for Earth! Love and protect our planet!"),
        PhonicsItem("F", "Fruit", "🍌", "F is for Fruit! Yummy healthy colors!"),
        PhonicsItem("G", "Green", "🌱", "G is for Green! Grow plants with love!"),
        PhonicsItem("H", "Hands", "🧼", "H is for Hands! Wash hands with bubbly soap!"),
        PhonicsItem("P", "Pack", "🎒", "P is for Pack! Pack your school bag ready!"),
        PhonicsItem("R", "Recycle", "♻️", "R is for Recycle! Sort plastic and paper!"),
        PhonicsItem("S", "Safety", "🚦", "S is for Safety! Look left and right!"),
        PhonicsItem("Z", "Zebra", "🦓", "Z is for Zebra Crossing! Walk safely across!")
    )

    // Leaderboard generator with friendly simulated peers + child's real position
    fun getLeaderboard(): List<LeaderboardKid> {
        val currentProfile = _profile.value
        val peers = listOf(
            LeaderboardKid("p1", "Leo Star", "🦁", 680, "Life Skills Master"),
            LeaderboardKid("p2", "Maya Sun", "🦸‍♀️", 540, "Eco Champion"),
            LeaderboardKid("p3", "Pip Panda", "🐼", 460, "Tidy Room Hero"),
            LeaderboardKid("p4", "Sammy Bot", "🤖", 380, "Water Saver"),
            LeaderboardKid("p5", "Zara Bunny", "🐰", 290, "Hygiene Star"),
            LeaderboardKid("p6", "Noah Seed", "🌱", 210, "Plant Whisperer"),
            LeaderboardKid("p7", "Oliver", "🚀", 150, "Little Helper")
        )

        val userKid = LeaderboardKid(
            id = "user_me",
            name = currentProfile.name + " (You)",
            avatar = availableAvatars.firstOrNull { it.id == currentProfile.avatarId }?.emoji ?: "🦸‍♂️",
            xp = currentProfile.totalXp,
            title = currentProfile.superTitle,
            isUser = true
        )

        val combined = (peers + userKid).sortedByDescending { it.xp }
        return combined.mapIndexed { index, kid ->
            kid.copy(rank = index + 1)
        }
    }

    // Award XP and complete skill
    fun completeSkill(skillId: LifeSkillId, earnedXp: Int, earnedStars: Int = 1) {
        val current = _profile.value
        val newXp = current.totalXp + earnedXp
        val newStars = current.totalStars + earnedStars

        // Calculate title based on XP
        val newTitle = when {
            newXp >= 600 -> "Grand Master Hero"
            newXp >= 400 -> "Life Skills Champion"
            newXp >= 250 -> "Super Helper"
            newXp >= 100 -> "Habit Explorer"
            else -> "Little Learner"
        }

        val updated = current.copy(
            totalXp = newXp,
            totalStars = newStars,
            superTitle = newTitle
        )
        saveProfile(updated)

        val completedSet = _completedSkillIds.value.toMutableSet()
        completedSet.add(skillId.key)
        _completedSkillIds.value = completedSet
        saveCompletedSkills(completedSet)
    }

    fun updateProfile(name: String, age: Int, avatarId: String, title: String? = null) {
        val current = _profile.value
        val updated = current.copy(
            name = name.ifBlank { "Little Hero" },
            age = age.coerceIn(3, 12),
            avatarId = avatarId,
            superTitle = title ?: current.superTitle,
            isOnboarded = true
        )
        saveProfile(updated)
    }

    fun toggleRoutineTask(taskId: String) {
        val currentList = _routineTasks.value.map { task ->
            if (task.id == taskId) {
                val newState = !task.isCompleted
                if (newState) {
                    // Award routine task XP
                    val curProfile = _profile.value
                    saveProfile(curProfile.copy(totalXp = curProfile.totalXp + task.xpReward))
                }
                task.copy(isCompleted = newState)
            } else {
                task
            }
        }
        _routineTasks.value = currentList
        saveRoutineTasks(currentList)
    }

    fun setSoundEnabled(enabled: Boolean) {
        val updated = _profile.value.copy(soundEnabled = enabled)
        saveProfile(updated)
    }

    fun setSpeechEnabled(enabled: Boolean) {
        val updated = _profile.value.copy(speechEnabled = enabled)
        saveProfile(updated)
    }

    // ==========================================
    // BACKUP & RESTORE DATA (Anti-loss feature)
    // ==========================================
    fun exportBackupJson(): String {
        val curProfile = _profile.value
        val obj = JSONObject()
        obj.put("version", 1)
        obj.put("name", curProfile.name)
        obj.put("age", curProfile.age)
        obj.put("avatarId", curProfile.avatarId)
        obj.put("superTitle", curProfile.superTitle)
        obj.put("totalXp", curProfile.totalXp)
        obj.put("totalStars", curProfile.totalStars)
        obj.put("streakDays", curProfile.streakDays)
        obj.put("isOnboarded", curProfile.isOnboarded)

        val skillsArray = JSONArray()
        _completedSkillIds.value.forEach { skillsArray.put(it) }
        obj.put("completedSkills", skillsArray)

        return obj.toString(2)
    }

    fun importBackupJson(jsonString: String): Boolean {
        return try {
            val obj = JSONObject(jsonString)
            val name = obj.optString("name", "Little Hero")
            val age = obj.optInt("age", 6)
            val avatarId = obj.optString("avatarId", "hero_boy")
            val superTitle = obj.optString("superTitle", "Life Skills Explorer")
            val totalXp = obj.optInt("totalXp", 120)
            val totalStars = obj.optInt("totalStars", 8)
            val streakDays = obj.optInt("streakDays", 3)
            val isOnboarded = obj.optBoolean("isOnboarded", true)

            val restored = KidProfile(
                name = name,
                age = age,
                avatarId = avatarId,
                superTitle = superTitle,
                totalXp = totalXp,
                totalStars = totalStars,
                streakDays = streakDays,
                isOnboarded = isOnboarded
            )
            saveProfile(restored)

            val skillsArray = obj.optJSONArray("completedSkills")
            val completedSet = mutableSetOf<String>()
            if (skillsArray != null) {
                for (i in 0 until skillsArray.length()) {
                    completedSet.add(skillsArray.getString(i))
                }
            }
            _completedSkillIds.value = completedSet
            saveCompletedSkills(completedSet)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun resetData() {
        val defaultProfile = KidProfile(
            name = "Little Hero",
            age = 6,
            avatarId = "hero_boy",
            superTitle = "Life Skills Explorer",
            totalXp = 50,
            totalStars = 2,
            streakDays = 1,
            isOnboarded = false
        )
        saveProfile(defaultProfile)
        _completedSkillIds.value = emptySet()
        saveCompletedSkills(emptySet())
        _routineTasks.value = defaultRoutines()
        saveRoutineTasks(defaultRoutines())
    }

    private fun loadProfile(): KidProfile {
        val name = prefs.getString("kid_name", "Little Hero") ?: "Little Hero"
        val age = prefs.getInt("kid_age", 6)
        val avatarId = prefs.getString("kid_avatar_id", "hero_boy") ?: "hero_boy"
        val superTitle = prefs.getString("kid_super_title", "Life Skills Explorer") ?: "Life Skills Explorer"
        val totalXp = prefs.getInt("kid_total_xp", 100)
        val totalStars = prefs.getInt("kid_total_stars", 6)
        val streakDays = prefs.getInt("kid_streak_days", 2)
        val isOnboarded = prefs.getBoolean("kid_is_onboarded", false)
        val sound = prefs.getBoolean("kid_sound_enabled", true)
        val speech = prefs.getBoolean("kid_speech_enabled", true)

        return KidProfile(
            name = name,
            age = age,
            avatarId = avatarId,
            superTitle = superTitle,
            totalXp = totalXp,
            totalStars = totalStars,
            streakDays = streakDays,
            isOnboarded = isOnboarded,
            soundEnabled = sound,
            speechEnabled = speech
        )
    }

    private fun saveProfile(p: KidProfile) {
        prefs.edit()
            .putString("kid_name", p.name)
            .putInt("kid_age", p.age)
            .putString("kid_avatar_id", p.avatarId)
            .putString("kid_super_title", p.superTitle)
            .putInt("kid_total_xp", p.totalXp)
            .putInt("kid_total_stars", p.totalStars)
            .putInt("kid_streak_days", p.streakDays)
            .putBoolean("kid_is_onboarded", p.isOnboarded)
            .putBoolean("kid_sound_enabled", p.soundEnabled)
            .putBoolean("kid_speech_enabled", p.speechEnabled)
            .apply()
        _profile.value = p
    }

    private fun loadCompletedSkills(): Set<String> {
        return prefs.getStringSet("completed_skills", emptySet()) ?: emptySet()
    }

    private fun saveCompletedSkills(skills: Set<String>) {
        prefs.edit().putStringSet("completed_skills", skills).apply()
    }

    private fun defaultRoutines(): List<RoutineTask> = listOf(
        RoutineTask("r1", "Wake up with big stretch ☀️", "🥱", isMorning = true, xpReward = 15),
        RoutineTask("r2", "Make my bed tidy 🛏️", "🛏️", isMorning = true, xpReward = 20),
        RoutineTask("r3", "Brush teeth for 2 minutes 🪥", "🪥", isMorning = true, xpReward = 25),
        RoutineTask("r4", "Wash face with clean water 🧼", "🧼", isMorning = true, xpReward = 15),
        RoutineTask("r5", "Eat a healthy breakfast 🥣", "🥣", isMorning = true, xpReward = 20),
        RoutineTask("r6", "Pack my school backpack 🎒", "🎒", isMorning = true, xpReward = 25),
        RoutineTask("r7", "Put shoes away neatly 👟", "👟", isMorning = false, xpReward = 15),
        RoutineTask("r8", "Evening toothbrush & bedtime story 🌙", "📖", isMorning = false, xpReward = 25)
    )

    private fun loadRoutineTasks(): List<RoutineTask> {
        val defaults = defaultRoutines()
        val completedIds = prefs.getStringSet("completed_routines_today", emptySet()) ?: emptySet()
        return defaults.map { it.copy(isCompleted = completedIds.contains(it.id)) }
    }

    private fun saveRoutineTasks(tasks: List<RoutineTask>) {
        val doneIds = tasks.filter { it.isCompleted }.map { it.id }.toSet()
        prefs.edit().putStringSet("completed_routines_today", doneIds).apply()
    }
}
