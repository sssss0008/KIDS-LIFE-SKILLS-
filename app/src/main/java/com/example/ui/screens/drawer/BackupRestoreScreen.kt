package com.example.ui.screens.drawer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DataRepository
import com.example.sound.SoundHelper
import com.example.ui.components.KidButton
import com.example.ui.theme.CleanWhite
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.TextDark

@Composable
fun BackupRestoreScreen(
    repository: DataRepository,
    soundHelper: SoundHelper,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var backupCode by remember { mutableStateOf(repository.exportBackupJson()) }
    var inputRestoreCode by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }

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
                        tint = SkyBlue
                    )
                }
                Text(
                    text = "💾 Backup & Restore Data",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF1F5F9))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Anti-loss guarantee card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CloudDone,
                        contentDescription = "Cloud Safe",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Column {
                        Text(
                            text = "Safe & Protected! 🛡️",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color(0xFF1B5E20)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Android Cloud Auto-Backup is enabled. You can also save your personal Backup Code below so progress is never lost even if you change phones!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Export Section
            Text(
                text = "1. Export / Copy My Progress Code:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            KidButton(
                text = "Copy Backup Code to Clipboard 📋",
                icon = "📤",
                onClick = {
                    val code = repository.exportBackupJson()
                    backupCode = code
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("KidsLifeSkillsBackup", code))
                    soundHelper.playSuccess()
                    statusMessage = "Backup code copied to clipboard! Keep it safe!"
                    Toast.makeText(context, "Backup code copied!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                containerColor = SkyBlue,
                testTag = "copy_backup_button"
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Import Section
            Text(
                text = "2. Restore Progress (Paste Code):",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = inputRestoreCode,
                onValueChange = { inputRestoreCode = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .testTag("restore_code_input"),
                placeholder = { Text("Paste your saved backup code here...") },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CleanWhite,
                    unfocusedContainerColor = CleanWhite
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            KidButton(
                text = "Restore My Progress Now! 📥",
                icon = "🔄",
                onClick = {
                    if (inputRestoreCode.isBlank()) {
                        soundHelper.playError()
                        statusMessage = "Please paste a backup code first!"
                    } else {
                        val success = repository.importBackupJson(inputRestoreCode)
                        if (success) {
                            soundHelper.playFanfare()
                            statusMessage = "Progress restored successfully! Welcome back! 🎉"
                            Toast.makeText(context, "Data restored!", Toast.LENGTH_SHORT).show()
                        } else {
                            soundHelper.playError()
                            statusMessage = "Invalid backup code format. Please check the code."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color(0xFF43A047),
                testTag = "restore_backup_button"
            )

            if (statusMessage != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite)
                ) {
                    Text(
                        text = statusMessage ?: "",
                        modifier = Modifier.padding(14.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0277BD)
                    )
                }
            }
        }
    }
}
