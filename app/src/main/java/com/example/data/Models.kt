package com.example.data

enum class LifeSkillId(val key: String) {
    BRUSH_TEETH("brush_teeth"),
    WASH_HANDS("wash_hands"),
    CLEAN_ROOM("clean_room"),
    SCHOOL_BAG("school_bag"),
    HEALTHY_FOOD("healthy_food"),
    CROSS_ROAD("cross_road"),
    RECYCLE_SORT("recycle_sort"),
    WATER_SAVING("water_saving"),
    PLANT_GROWING("plant_growing"),
    MORNING_ROUTINE("morning_routine")
}

enum class SkillCategory(val title: String, val icon: String) {
    HYGIENE("Clean & Healthy", "🧼"),
    INDEPENDENCE("I Can Do It!", "🎒"),
    SAFETY_ECO("Safety & Nature", "🌱"),
    DAILY_ROUTINE("Everyday Habits", "☀️")
}

data class LifeSkill(
    val id: LifeSkillId,
    val title: String,
    val subtitle: String,
    val icon: String,
    val category: SkillCategory,
    val colorHex: Long,
    val xpReward: Int,
    val description: String,
    val steps: List<String>,
    val funFacts: List<String>
)

data class KidProfile(
    val name: String = "Little Hero",
    val age: Int = 6,
    val avatarId: String = "hero_boy",
    val superTitle: String = "Life Skills Explorer",
    val totalXp: Int = 120,
    val totalStars: Int = 8,
    val streakDays: Int = 3,
    val isOnboarded: Boolean = false,
    val soundEnabled: Boolean = true,
    val speechEnabled: Boolean = true
)

data class AvatarInfo(
    val id: String,
    val emoji: String,
    val name: String,
    val tag: String,
    val bgHex: Long
)

data class RoutineTask(
    val id: String,
    val title: String,
    val icon: String,
    val isCompleted: Boolean = false,
    val isMorning: Boolean = true,
    val xpReward: Int = 15
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val emoji: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val category: String
)

data class LeaderboardKid(
    val id: String,
    val name: String,
    val avatar: String,
    val xp: Int,
    val title: String,
    val rank: Int = 0,
    val isUser: Boolean = false
)

data class BadgeInfo(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val requiredXp: Int,
    val isUnlocked: Boolean = false
)

data class PhonicsItem(
    val letter: String,
    val word: String,
    val icon: String,
    val sentence: String
)
