package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BottomTab

@Composable
fun HomeScreen(
    viewModel: AcademyViewModel,
    userProfile: UserProfile,
    onNavigateToClasses: () -> Unit,
    onNavigateToTests: () -> Unit,
    onNavigateToNotes: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToDoubts: () -> Unit,
    onNavigateToCurrentAffairs: () -> Unit,
    onNavigateToContact: () -> Unit,
    onStartTest: (MockTest) -> Unit,
    onOpenNote: (StudyNote) -> Unit
) {
    val context = LocalContext.current
    val liveClasses by viewModel.liveClasses.collectAsState()
    val mockTests by viewModel.mockTests.collectAsState()
    val currentAffairs by viewModel.currentAffairs.collectAsState()
    val examAlerts by viewModel.examAlerts.collectAsState()
    val courseBatches by viewModel.courseBatches.collectAsState()
    val selectedExam by viewModel.selectedExamFilter.collectAsState()

    val unreadNotifs = remember { 3 }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Academy Brand Header
        item {
            AcademyBrandHeader(
                studentName = userProfile.name,
                roleName = userProfile.role.name,
                branch = userProfile.branch,
                streakDays = userProfile.streakDays,
                unreadNotifsCount = unreadNotifs,
                onNotificationClick = onNavigateToAlerts,
                onContactClick = onNavigateToContact,
                onProfileClick = { viewModel.selectTab(BottomTab.PROFILE) }
            )
        }

        // Ticker & Motivational Banner
        item {
            TodayHighlightsTicker(
                onLiveClick = onNavigateToClasses,
                onQuizClick = {
                    val dailyQuiz = mockTests.firstOrNull { it.testType == "Daily Quiz" } ?: mockTests.firstOrNull()
                    if (dailyQuiz != null) onStartTest(dailyQuiz) else onNavigateToTests()
                },
                onCaClick = onNavigateToCurrentAffairs,
                onAlertClick = onNavigateToAlerts,
                onNotesClick = onNavigateToNotes
            )
        }

        item {
            MotivationalBanner()
        }

        // Exam Selector Chips (WBCS, PSC, SSC, RRB, BANKING, TET, CTET)
        item {
            Text(
                text = "Target Exam Category",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Slate900,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 2.dp)
            )
            ExamFilterChipsRow(
                selectedExam = selectedExam,
                onSelectExam = { viewModel.setExamFilter(it) }
            )
        }

        // Quick Access Grid (8 Cards)
        item {
            QuickAccessGrid(
                onClassesClick = onNavigateToClasses,
                onMockTestClick = onNavigateToTests,
                onNotesClick = onNavigateToNotes,
                onCurrentAffairsClick = onNavigateToCurrentAffairs,
                onExamAlertClick = onNavigateToAlerts,
                onCalendarClick = onNavigateToAlerts,
                onResultsClick = onNavigateToProgress,
                onProgressClick = onNavigateToProgress
            )
        }

        // Live Class Banner / Spotlight
        val activeLiveClass = liveClasses.firstOrNull { it.isLiveNow } ?: liveClasses.firstOrNull()
        if (activeLiveClass != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("live_class_spotlight_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Navy900),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (activeLiveClass.isLiveNow) ErrorRed else Gold600
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    if (activeLiveClass.isLiveNow) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(Color.White)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = if (activeLiveClass.isLiveNow) "🔴 LIVE NOW" else "UPCOMING CLASS",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Navy800
                            ) {
                                Text(
                                    text = activeLiveClass.examCategory,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Gold400,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = activeLiveClass.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Slate400,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Faculty: ${activeLiveClass.teacherName} (${activeLiveClass.subject})",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate200
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "👥 ${activeLiveClass.viewersCount} Aspirants Watching",
                                style = MaterialTheme.typography.labelSmall,
                                color = Slate400
                            )

                            Button(
                                onClick = onNavigateToClasses,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (activeLiveClass.isLiveNow) ErrorRed else Blue600
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
                                modifier = Modifier.testTag("join_live_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (activeLiveClass.isLiveNow) "Join Classroom" else "View Schedule",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Daily Rapid Quiz Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("daily_quiz_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Gold500.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚡", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TODAY'S DAILY QUIZ",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                color = Gold600
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Gold100
                            ) {
                                Text(
                                    text = "🔥 7 Day Streak",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Gold600,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "General Studies & Reasoning Booster",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Slate900
                        )
                        Text(
                            text = "5 Questions • 5 Minutes • +50 XP",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate600
                        )
                    }

                    Button(
                        onClick = {
                            val dq = mockTests.firstOrNull { it.testType == "Daily Quiz" } ?: mockTests.firstOrNull()
                            if (dq != null) onStartTest(dq) else onNavigateToTests()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Gold500, contentColor = Navy900),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("start_daily_quiz_button")
                    ) {
                        Text("Start", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Daily Current Affairs Digest Preview
        val topCa = currentAffairs.firstOrNull()
        if (topCa != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { onNavigateToCurrentAffairs() }
                        .testTag("current_affairs_preview_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Newspaper,
                                    contentDescription = null,
                                    tint = InfoCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "DAILY CURRENT AFFAIRS (WB SPECIAL)",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = InfoCyan
                                )
                            }
                            Text(
                                text = topCa.date,
                                style = MaterialTheme.typography.labelSmall,
                                color = Slate400
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = topCa.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Slate900
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = topCa.summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate600,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Category: ${topCa.category}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = Blue800
                            )

                            Text(
                                text = "Read More →",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Blue700
                            )
                        }
                    }
                }
            }
        }

        // Exam Alert 🚨 Spotlight
        val topAlert = examAlerts.firstOrNull()
        if (topAlert != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { onNavigateToAlerts() }
                        .testTag("exam_alert_spotlight_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ErrorRed.copy(alpha = 0.12f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "🚨", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "EXAM ALERT",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                        color = ErrorRed
                                    )
                                }
                            }

                            Text(
                                text = topAlert.statusBadge,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = SuccessGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = topAlert.examName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Slate900
                        )

                        Text(
                            text = "${topAlert.organization} • Vacancy: ${topAlert.vacancy}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = Slate700
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Last Date: ${topAlert.applyEndDate}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = WarningOrange
                            )

                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(topAlert.officialUrl))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("Apply Now", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            }
        }

        // Ask Doubt & AI Assistant Promo
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onNavigateToDoubts() }
                    .testTag("doubt_corner_promo_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Blue50),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Blue800),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Doubt Corner & AI Solver",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Blue800
                        )
                        Text(
                            text = "Ask questions directly to academy faculty or get instant AI explanations.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate600
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = Blue800,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Branches & Contact Strip Footer
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("home_contact_strip"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Slate100)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "THE CAREER GURU ACADEMY",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black),
                        color = Navy900
                    )
                    Text(
                        text = "📍 Suri, Birbhum   |   📍 Durgapur, West Burdwan",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate700,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "📞 9093200422   •   📞 7908032199",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Blue800
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9093200422"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Suri", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/919093200422?text=Hello%20Career%20Guru%20Academy%20I%20want%20admission%20details"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
