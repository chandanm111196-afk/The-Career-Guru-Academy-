package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceRecord
import com.example.data.model.TestAttempt
import com.example.data.model.UserProfile
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@Composable
fun ProgressScreen(
    viewModel: AcademyViewModel,
    userProfile: UserProfile,
    onNavigateToClasses: () -> Unit,
    onNavigateToTests: () -> Unit,
    onNavigateToNotes: () -> Unit
) {
    var selectedSection by remember { mutableIntStateOf(0) } // 0: Performance & Analytics, 1: Leaderboard, 2: My Attendance
    val attempts by viewModel.testAttempts.collectAsState()
    val attendanceList by viewModel.attendance.collectAsState()

    val totalTests = attempts.size.coerceAtLeast(3)
    val avgScore = if (attempts.isNotEmpty()) attempts.map { it.score }.average() else 14.5
    val avgAccuracy = if (attempts.isNotEmpty()) attempts.map { it.accuracyPercentage }.average().toInt() else 78

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .testTag("progress_screen")
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
                    .padding(16.dp)
            ) {
                Text(
                    text = "Student Progress & Analytics",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = "Real-time accuracy metrics, subject diagnostics & batch rankings",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gold400
                )
            }
        }

        // Sub Tabs
        TabRow(
            selectedTabIndex = selectedSection,
            containerColor = Color.White,
            contentColor = Blue800,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedSection == 0,
                onClick = { selectedSection = 0 },
                text = { Text("📊 Analytics", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedSection == 1,
                onClick = { selectedSection = 1 },
                text = { Text("🏆 Leaderboard", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedSection == 2,
                onClick = { selectedSection = 2 },
                text = { Text("📋 Attendance", fontWeight = FontWeight.Bold) }
            )
        }

        when (selectedSection) {
            0 -> {
                // Performance Analytics
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Overall High-Level Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Overall Preparation Index",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Slate900
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    AnalyticsPill("Tests Taken", "$totalTests", Blue800)
                                    AnalyticsPill("Avg Accuracy", "$avgAccuracy%", SuccessGreen)
                                    AnalyticsPill("Study Streak", "${userProfile.streakDays} Days", Gold600)
                                    AnalyticsPill("XP Points", "${userProfile.xpPoints}", InfoCyan)
                                }
                            }
                        }
                    }

                    // Subject Proficiency Breakdown
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Subject-Wise Strength & Weakness",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                SubjectProgressBar("Reasoning & Logic", 0.84f, "84% (Strong)", SuccessGreen)
                                SubjectProgressBar("Quantitative Aptitude", 0.78f, "78% (Good)", Blue700)
                                SubjectProgressBar("English Language", 0.72f, "72% (Moderate)", Gold600)
                                SubjectProgressBar("General Knowledge & Science", 0.65f, "65% (Weak Area ⚠️)", ErrorRed)
                            }
                        }
                    }

                    // Improve Weak Areas Recommendation Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Blue50),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Blue800.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoGraph, contentDescription = null, tint = Blue800)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "“Improve Your Weak Areas”",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Blue800
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Based on your mock tests, your General Studies score is 65%. Boost your score with tailored material:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate700
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = onNavigateToClasses,
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Text("Watch GK Class", fontSize = 11.sp)
                                    }
                                    Button(
                                        onClick = onNavigateToNotes,
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Text("Read History PDF", fontSize = 11.sp)
                                    }
                                    Button(
                                        onClick = onNavigateToTests,
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Gold600),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Text("Take Daily Quiz", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    // Test History
                    if (attempts.isNotEmpty()) {
                        item {
                            Text(
                                text = "Recent Mock Test Attempts",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Slate900,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        items(attempts) { att ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = att.testTitle,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = Slate900
                                        )
                                        Text(
                                            text = "Correct: ${att.correctCount} • Incorrect: ${att.incorrectCount} • Accuracy: ${att.accuracyPercentage}%",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Slate600
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = SuccessGreen.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "${String.format("%.1f", att.score)} / ${att.maxScore.toInt()}",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = SuccessGreen,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // Leaderboard
                LeaderboardSection(userProfile = userProfile)
            }
            2 -> {
                // My Attendance
                AttendanceSection(attendanceList = attendanceList)
            }
        }
    }
}

@Composable
fun LeaderboardSection(userProfile: UserProfile) {
    var isAnonymous by remember { mutableStateOf(userProfile.isAnonymousOnLeaderboard) }

    val leaders = listOf(
        Triple("Sneha Karmakar", "94% Accuracy • 18 Tests", "#1"),
        Triple("Arpan Sen", "91% Accuracy • 16 Tests", "#2"),
        Triple("Debabrata Das", "89% Accuracy • 15 Tests", "#3"),
        Triple(if (isAnonymous) "Aspirant #9093 (You)" else "${userProfile.name} (You)", "85% Accuracy • 12 Tests", "#14"),
        Triple("Priya Mandal", "84% Accuracy • 11 Tests", "#15"),
        Triple("Rohan Bhattacharya", "82% Accuracy • 10 Tests", "#16")
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Leaderboard Privacy",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Slate900
                        )
                        Text(
                            text = "Hide my full name from public batch rankings",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate600
                        )
                    }
                    Switch(
                        checked = isAnonymous,
                        onCheckedChange = { isAnonymous = it }
                    )
                }
            }
        }

        items(leaders) { (name, stats, rank) ->
            val isUser = name.contains("(You)")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUser) Blue50 else Color.White
                ),
                border = if (isUser) androidx.compose.foundation.BorderStroke(1.5.dp, Blue800) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                when (rank) {
                                    "#1" -> Gold500
                                    "#2" -> Color(0xFF94A3B8)
                                    "#3" -> Color(0xFFB45309)
                                    else -> Navy900
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = rank,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isUser) FontWeight.ExtraBold else FontWeight.Bold
                            ),
                            color = if (isUser) Blue800 else Slate900
                        )
                        Text(text = stats, style = MaterialTheme.typography.labelSmall, color = Slate600)
                    }

                    if (rank == "#1" || rank == "#2" || rank == "#3") {
                        Text(text = "👑", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceSection(attendanceList: List<AttendanceRecord>) {
    val total = attendanceList.size
    val present = attendanceList.count { it.isPresent }
    val absent = total - present
    val pct = if (total > 0) ((present * 100) / total) else 0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "My Classroom & Live Attendance",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AnalyticsPill("Total Classes", "$total", Slate800)
                        AnalyticsPill("Attended", "$present", SuccessGreen)
                        AnalyticsPill("Absent", "$absent", ErrorRed)
                        AnalyticsPill("Percentage", "$pct%", Blue800)
                    }
                }
            }
        }

        item {
            Text(
                text = "Class-by-Class Attendance Log",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Slate900
            )
        }

        items(attendanceList) { record ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = record.subject, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = Slate900)
                        Text(text = "${record.date} • ${record.remarks}", style = MaterialTheme.typography.labelSmall, color = Slate600)
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (record.isPresent) SuccessGreen.copy(alpha = 0.15f) else ErrorRed.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (record.isPresent) "✓ Present" else "✗ Absent",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (record.isPresent) SuccessGreen else ErrorRed,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalyticsPill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = color)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Slate600)
    }
}

@Composable
private fun SubjectProgressBar(subject: String, progress: Float, label: String, color: Color) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = subject, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = Slate800)
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = Slate200
        )
    }
}
