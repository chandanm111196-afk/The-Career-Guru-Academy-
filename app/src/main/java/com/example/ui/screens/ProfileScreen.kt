package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@Composable
fun ProfileScreen(
    viewModel: AcademyViewModel,
    userProfile: UserProfile,
    onNavigateToBatches: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToContact: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val currentLang by viewModel.selectedLanguage.collectAsState()
    var showEditProfileDialog by remember { mutableStateOf(false) }

    var editName by remember { mutableStateOf(userProfile.name) }
    var editMobile by remember { mutableStateOf(userProfile.mobile) }
    var editEmail by remember { mutableStateOf(userProfile.email) }
    var editBranch by remember { mutableStateOf(userProfile.branch) }
    var editExam by remember { mutableStateOf(userProfile.preferredExam) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
            .testTag("profile_screen")
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Navy900,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Gold500),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userProfile.name.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                        color = Navy900
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = userProfile.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )

                Text(
                    text = "${userProfile.role.name} • 📍 ${userProfile.branch}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gold400
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Navy800
                    ) {
                        Text(
                            text = "🔥 ${userProfile.streakDays} Day Streak",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Navy800
                    ) {
                        Text(
                            text = "⭐ ${userProfile.xpPoints} XP Points",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Gold400,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Role Switcher (Crucial for testing Student vs Teacher vs Admin features)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Active Role & Permissions",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Slate900
                    )
                    Text(
                        text = "Switch between Student and Faculty/Admin views to access staff tools:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate600
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        UserRole.values().forEach { role ->
                            val isSelected = userProfile.role == role
                            Button(
                                onClick = {
                                    viewModel.switchRole(role)
                                    Toast.makeText(context, "Switched role to ${role.name}", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) Blue800 else Slate100,
                                    contentColor = if (isSelected) Color.White else Slate800
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                            ) {
                                Text(role.name, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (userProfile.role != UserRole.STUDENT) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onNavigateToAdmin,
                            colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Faculty Management Console", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Academic Details & Preferred Exam
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Student Information",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Slate900
                        )
                        TextButton(onClick = { showEditProfileDialog = true }) {
                            Text("Edit Profile", color = Blue800, fontWeight = FontWeight.Bold)
                        }
                    }

                    ProfileDetailRow("Primary Mobile:", userProfile.mobile)
                    ProfileDetailRow("Email:", userProfile.email)
                    ProfileDetailRow("Enrolled Branch:", userProfile.branch)
                    ProfileDetailRow("Target Exam:", userProfile.preferredExam)
                }
            }

            // Multilingual Support
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Preferred Language (ভাষা / भाषा)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("English" to "🇬🇧 English", "বাংলা" to "🇮🇳 বাংলা", "हिंदी" to "🇮🇳 हिंदी").forEach { (langKey, display) ->
                            val isSelected = currentLang == langKey
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.setLanguage(langKey)
                                    Toast.makeText(context, "Language changed to $langKey", Toast.LENGTH_SHORT).show()
                                },
                                label = { Text(display, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Blue800,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Quick Nav Links: Courses, Contact Support, Logout
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    ProfileMenuRow("Enrolled Batches & Courses", Icons.Default.CardMembership, onNavigateToBatches)
                    ProfileMenuRow("Contact Suri & Durgapur Branches", Icons.Default.ContactPhone, onNavigateToContact)
                    ProfileMenuRow("Help Desk & WhatsApp Support", Icons.Default.SupportAgent, onNavigateToContact)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    ProfileMenuRow("Logout / Switch User", Icons.Default.Logout, onLogout, color = ErrorRed)
                }
            }
        }

        // Edit Profile Dialog
        if (showEditProfileDialog) {
            AlertDialog(
                onDismissRequest = { showEditProfileDialog = false },
                title = { Text("Update Profile", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editMobile,
                            onValueChange = { editMobile = it },
                            label = { Text("Mobile Number") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editEmail,
                            onValueChange = { editEmail = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val updated = userProfile.copy(
                                name = editName,
                                mobile = editMobile,
                                email = editEmail
                            )
                            viewModel.switchRole(updated.role)
                            Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
                            showEditProfileDialog = false
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditProfileDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Slate600)
        Text(text = value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Slate800)
    }
}

@Composable
private fun ProfileMenuRow(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    color: Color = Slate800
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = color, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Slate400, modifier = Modifier.size(18.dp))
    }
}
