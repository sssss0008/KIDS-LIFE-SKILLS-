package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = SkyBlue,
    onPrimary = CleanWhite,
    primaryContainer = SkyBlueLight,
    onPrimaryContainer = OceanBlue,

    secondary = WarmOrange,
    onSecondary = CleanWhite,
    secondaryContainer = OrangeContainer,
    onSecondaryContainer = Color(0xFFBF360C),

    tertiary = EmeraldGreen,
    onTertiary = CleanWhite,
    tertiaryContainer = MintGreenLight,
    onTertiaryContainer = MintGreen,

    background = SoftBackground,
    onBackground = TextDark,

    surface = CardBackground,
    onSurface = TextDark,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextMuted
)

val KidShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Keep bright, joyful kid-focused palette consistent
    MaterialTheme(
        colorScheme = LightColorScheme,
        shapes = KidShapes,
        typography = Typography,
        content = content
    )
}
