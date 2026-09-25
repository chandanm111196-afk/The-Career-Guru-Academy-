package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.BottomTab
import kotlinx.coroutines.delay

@Composable
fun AcademyBrandHeader(
    studentName: String,
    roleName: String,
    branch: String,
    streakDays: Int,
    unreadNotifsCount: Int,
    onNotificationClick: () -> Unit,
    onContactClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("academy_brand_header"),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Academy Title & Contact Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Gold500, Gold600))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Academy Logo",
                            tint = Navy900,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "THE CAREER GURU ACADEMY",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "“Your Success is Our Achievement”",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp
                            ),
                            color = Gold400
                        )
                    }
                }

                // Quick Call & WhatsApp Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9093200422"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("header_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call Academy",
                            tint = Gold400
                        )
                    }

                    IconButton(
                        onClick = onNotificationClick,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("header_notif_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotifsCount > 0) {
                                    Badge(
                                        containerColor = ErrorRed,
                                        contentColor = Color.White
                                    ) {
                                        Text("$unreadNotifsCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Student Welcome & Branch Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Navy800)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hello, $studentName 👋",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "Ready to achieve your goal today?",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Branch Tag
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Blue800,
                        modifier = Modifier.clickable { onContactClick() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Gold400,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = branch,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Streak Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Gold600.copy(alpha = 0.25f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "🔥", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${streakDays}d Streak",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Gold400
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodayHighlightsTicker(
    onLiveClick: () -> Unit,
    onQuizClick: () -> Unit,
    onCaClick: () -> Unit,
    onAlertClick: () -> Unit,
    onNotesClick: () -> Unit
) {
    val highlights = listOf(
        Triple("🔴 LIVE NOW: West Bengal Geography Class (Join Room)", Icons.Default.PlayCircleFilled, onLiveClick),
        Triple("📝 TODAY'S RAPID QUIZ: 5 Mins Challenge live!", Icons.Default.Quiz, onQuizClick),
        Triple("📰 CURRENT AFFAIRS: Daily Special WB & National digest", Icons.Default.Article, onCaClick),
        Triple("📢 EXAM ALERT: SSC CGL 2026 17,727 Vacancy Active", Icons.Default.Campaign, onAlertClick),
        Triple("📚 STUDY MATERIAL: WB Economics & History PDF uploaded", Icons.Default.MenuBook, onNotesClick)
    )

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            currentIndex = (currentIndex + 1) % highlights.size
        }
    }

    val current = highlights[currentIndex]

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { current.third() }
            .testTag("highlights_ticker"),
        shape = RoundedCornerShape(12.dp),
        color = Blue50,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = WarningOrange,
                modifier = Modifier.size(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = current.second,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            AnimatedContent(
                targetState = current.first,
                transitionSpec = {
                    slideInVertically { height -> height } + fadeIn() togetherWith
                            slideOutVertically { height -> -height } + fadeOut()
                },
                label = "highlight_anim",
                modifier = Modifier.weight(1f)
            ) { text ->
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Blue800,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Blue800,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun MotivationalBanner() {
    val quotes = listOf(
        "“Small steps every day create big results.”",
        "“Consistency is the key to competitive exam success.”",
        "“Dedication at Suri & Durgapur today brings selection tomorrow.”",
        "“Your Success is Our Achievement.”"
    )
    var quoteIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(7000)
            quoteIndex = (quoteIndex + 1) % quotes.size
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("motivational_banner"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Gold100)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "💡", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = quotes[quoteIndex],
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    ),
                    color = Gold600
                )
                Text(
                    text = "— The Career Guru Mentorship Team",
                    style = MaterialTheme.typography.labelSmall,
                    color = Slate700
                )
            }
        }
    }
}

@Composable
fun ExamFilterChipsRow(
    selectedExam: String,
    onSelectExam: (String) -> Unit
) {
    val exams = listOf("All", "WBCS", "PSC", "SSC", "RRB", "BANKING", "INSURANCE", "TET", "CTET")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        exams.forEach { exam ->
            val isSelected = selectedExam.equals(exam, ignoreCase = true)
            FilterChip(
                selected = isSelected,
                onClick = { onSelectExam(exam) },
                label = {
                    Text(
                        text = exam,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Blue800,
                    selectedLabelColor = Color.White,
                    containerColor = Slate100,
                    labelColor = Slate800
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("filter_chip_$exam")
            )
        }
    }
}

@Composable
fun QuickAccessGrid(
    onClassesClick: () -> Unit,
    onMockTestClick: () -> Unit,
    onNotesClick: () -> Unit,
    onCurrentAffairsClick: () -> Unit,
    onExamAlertClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onResultsClick: () -> Unit,
    onProgressClick: () -> Unit
) {
    val items = listOf(
        QuickItem("Classes", "Live & Recorded", Icons.Default.VideoLibrary, Blue600, onClassesClick),
        QuickItem("Mock Test", "Full & Subject", Icons.Default.Quiz, WarningOrange, onMockTestClick),
        QuickItem("Notes", "PDFs & Chapters", Icons.Default.MenuBook, SuccessGreen, onNotesClick),
        QuickItem("Daily Current Affairs", "WB & National", Icons.Default.Newspaper, InfoCyan, onCurrentAffairsClick),
        QuickItem("Exam Alert 🚨", "Vacancies & Apply", Icons.Default.NotificationImportant, ErrorRed, onExamAlertClick),
        QuickItem("Exam Calendar", "Dates & Deadlines", Icons.Default.CalendarMonth, Gold600, onCalendarClick),
        QuickItem("Results", "Detailed Analysis", Icons.Default.EmojiEvents, Gold500, onResultsClick),
        QuickItem("My Progress", "Accuracy & Streak", Icons.Default.TrendingUp, Blue800, onProgressClick)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("quick_access_grid")
    ) {
        Text(
            text = "Quick Learning Hub",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Slate900,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // 2 rows of 4 or 4 rows of 2 for clean readability
        val chunked = items.chunked(4)
        chunked.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { item ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { item.onClick() }
                            .testTag("quick_card_${item.title.replace(" ", "_")}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(item.color.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = item.color,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Slate800,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class QuickItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
fun AcademyBottomNavigationBar(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    unreadNotifsCount: Int
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        NavigationBarItem(
            selected = currentTab == BottomTab.HOME,
            onClick = { onTabSelected(BottomTab.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Blue800,
                selectedTextColor = Blue800,
                indicatorColor = Blue100
            ),
            modifier = Modifier.testTag("nav_home")
        )
        NavigationBarItem(
            selected = currentTab == BottomTab.CLASSES,
            onClick = { onTabSelected(BottomTab.CLASSES) },
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "Classes") },
            label = { Text("Classes", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Blue800,
                selectedTextColor = Blue800,
                indicatorColor = Blue100
            ),
            modifier = Modifier.testTag("nav_classes")
        )
        NavigationBarItem(
            selected = currentTab == BottomTab.TESTS,
            onClick = { onTabSelected(BottomTab.TESTS) },
            icon = { Icon(Icons.Default.Assignment, contentDescription = "Tests") },
            label = { Text("Tests", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Blue800,
                selectedTextColor = Blue800,
                indicatorColor = Blue100
            ),
            modifier = Modifier.testTag("nav_tests")
        )
        NavigationBarItem(
            selected = currentTab == BottomTab.ALERTS,
            onClick = { onTabSelected(BottomTab.ALERTS) },
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadNotifsCount > 0) {
                            Badge(containerColor = ErrorRed) {
                                Text("$unreadNotifsCount")
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Campaign, contentDescription = "Alerts")
                }
            },
            label = { Text("Alerts", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Blue800,
                selectedTextColor = Blue800,
                indicatorColor = Blue100
            ),
            modifier = Modifier.testTag("nav_alerts")
        )
        NavigationBarItem(
            selected = currentTab == BottomTab.PROFILE,
            onClick = { onTabSelected(BottomTab.PROFILE) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Blue800,
                selectedTextColor = Blue800,
                indicatorColor = Blue100
            ),
            modifier = Modifier.testTag("nav_profile")
        )
    }
}
