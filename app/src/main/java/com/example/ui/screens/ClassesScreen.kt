package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LiveClass
import com.example.data.model.RecordedClass
import com.example.ui.components.ExamFilterChipsRow
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@Composable
fun ClassesScreen(
    viewModel: AcademyViewModel,
    onOpenRecordedClass: (RecordedClass) -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Live & Upcoming, 1: Recorded Lectures
    val liveClasses by viewModel.liveClasses.collectAsState()
    val recordedClasses by viewModel.recordedClasses.collectAsState()
    val selectedExam by viewModel.selectedExamFilter.collectAsState()

    val filteredLive = remember(liveClasses, selectedExam) {
        if (selectedExam == "All") liveClasses else liveClasses.filter { it.examCategory.equals(selectedExam, ignoreCase = true) }
    }

    val filteredRecorded = remember(recordedClasses, selectedExam) {
        if (selectedExam == "All") recordedClasses else recordedClasses.filter { it.examCategory.equals(selectedExam, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .testTag("classes_screen")
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
                    text = "Digital Classroom",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = "High-definition interactive live sessions & recorded archive",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gold400
                )
            }
        }

        // Tab Selector
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Blue800,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("🔴 Live & Upcoming", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_live_classes")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("▶ Recorded Lectures", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_recorded_classes")
            )
        }

        // Exam Filter Row
        ExamFilterChipsRow(
            selectedExam = selectedExam,
            onSelectExam = { viewModel.setExamFilter(it) }
        )

        if (selectedTab == 0) {
            // Live & Upcoming
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredLive, key = { it.id }) { liveItem ->
                    LiveClassCard(
                        liveClass = liveItem,
                        onJoin = {
                            Toast.makeText(context, "Entering ${liveItem.title} Live Classroom...", Toast.LENGTH_SHORT).show()
                        },
                        onSetReminder = {
                            Toast.makeText(context, "Reminder set for ${liveItem.timeLabel}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        } else {
            // Recorded Classes
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredRecorded, key = { it.id }) { recordedItem ->
                    RecordedClassCard(
                        item = recordedItem,
                        onPlay = { onOpenRecordedClass(recordedItem) },
                        onToggleBookmark = { viewModel.toggleClassBookmark(recordedItem.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun LiveClassCard(
    liveClass: LiveClass,
    onJoin: () -> Unit,
    onSetReminder: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("live_class_card_${liveClass.id}"),
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
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (liveClass.isLiveNow) ErrorRed else Gold600
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        if (liveClass.isLiveNow) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                        Text(
                            text = if (liveClass.isLiveNow) "🔴 LIVE NOW" else "SCHEDULED",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Blue100
                ) {
                    Text(
                        text = liveClass.examCategory,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Blue800,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = liveClass.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Slate900
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Subject: ${liveClass.subject} • Faculty: ${liveClass.teacherName}",
                style = MaterialTheme.typography.bodySmall,
                color = Slate700
            )

            Text(
                text = "Time: ${liveClass.timeLabel} (${liveClass.durationMinutes} mins)",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = if (liveClass.isLiveNow) ErrorRed else Slate600,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (liveClass.isLiveNow) {
                    Text(
                        text = "👥 ${liveClass.viewersCount} watching",
                        style = MaterialTheme.typography.labelSmall,
                        color = Slate600
                    )
                } else {
                    OutlinedButton(
                        onClick = onSetReminder,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Set Reminder", fontSize = 12.sp)
                    }
                }

                Button(
                    onClick = onJoin,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (liveClass.isLiveNow) ErrorRed else Blue800
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("action_join_${liveClass.id}")
                ) {
                    Icon(
                        imageVector = if (liveClass.isLiveNow) Icons.Default.PlayArrow else Icons.Default.Videocam,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (liveClass.isLiveNow) "Join Class" else "Enter Waiting Room",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun RecordedClassCard(
    item: RecordedClass,
    onPlay: () -> Unit,
    onToggleBookmark: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlay() }
            .testTag("recorded_class_card_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Video Thumbnail Box
            Box(
                modifier = Modifier
                    .size(width = 96.dp, height = 72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Navy900),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = "Play",
                    tint = Gold400,
                    modifier = Modifier.size(36.dp)
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                ) {
                    Text(
                        text = item.duration,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Blue100
                    ) {
                        Text(
                            text = item.examCategory,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                            color = Blue800,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (item.isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (item.isBookmarked) Gold600 else Slate400,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Slate900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${item.teacherName} • ${item.chapter}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate600,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "👁 ${item.viewsCount} views • Uploaded ${item.dateUploaded}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Slate400,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
