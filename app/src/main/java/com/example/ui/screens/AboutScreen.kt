package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sound.SoundHelper
import com.example.ui.components.KidButton
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.TextDark

@Composable
fun AboutScreen(
    soundHelper: SoundHelper,
    onNavigateToBackup: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val linkedInUrl = "https://www.linkedin.com/in/awiskaracharya/"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "ℹ️ About Kids Life Skills",
            style = MaterialTheme.typography.headlineMedium,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Empowering children with essential daily habits and safety.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Developer Profile Card (Awiskar Acharya)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = CleanWhite),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE))
                            .border(2.5.dp, SkyBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👨‍💻", fontSize = 34.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Awiskar Acharya",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = TextDark
                        )
                        Text(
                            text = "Lead Software Engineer & App Creator",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0288D1)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Crafted with passion by Awiskar Acharya to give every child a joyful, engaging, and interactive playground to build independence, good hygiene, road safety, and eco-friendly habits.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF334155),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // LinkedIn Connection Button
                KidButton(
                    text = "Connect on LinkedIn",
                    icon = "🔗",
                    onClick = {
                        soundHelper.playClick()
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    },
                    containerColor = Color(0xFF0077B5), // LinkedIn Brand Color
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("linkedin_button")
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Data Loss Protection Banner (Reinstall & Cloud Safe)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .clickable {
                    soundHelper.playClick()
                    onNavigateToBackup()
                }
                .border(2.dp, Color(0xFF81C784), RoundedCornerShape(24.dp)),
            color = Color(0xFFE8F5E9),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(CleanWhite),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🛡️", fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Anti-Data Loss Feature",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                    Text(
                        text = "Your progress survives reinstallation! Tap to export or restore your personal backup code.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF2E7D32)
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.OpenInNew,
                    contentDescription = "Open Backup",
                    tint = Color(0xFF2E7D32)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Privacy & Kid-Safe Guarantee Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CleanWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "🔒 Kid-Safe & Parent Friendly",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• 100% Frontend & Offline-Ready\n• Zero Ads and Zero In-App Purchases\n• Positive Reinforcement & Gentle Learning\n• Compliant with COPPA & Child Safety Standards",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF475569),
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Version and Footer
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Kids Life Skills • Version 1.0.0",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8)
            )
            Text(
                text = "Designed with love for little heroes everywhere ❤️",
                fontSize = 12.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}
