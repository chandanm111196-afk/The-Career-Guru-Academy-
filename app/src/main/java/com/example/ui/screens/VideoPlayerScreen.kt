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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RecordedClass
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(
    viewModel: AcademyViewModel,
    video: RecordedClass,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var playbackSpeed by remember { mutableStateOf("1.0x") }
    var currentProgress by remember { mutableFloatStateOf(0.35f) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Overview, 1: Chapters, 2: Doubts

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = video.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleClassBookmark(video.id) }) {
                        Icon(
                            if (video.isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = Gold400
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy900)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Slate50)
                .verticalScroll(rememberScrollState())
                .testTag("video_player_screen")
        ) {
            // Video Player Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Gold500)
                    ) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Navy900,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isPlaying) "Playing: ${video.chapter} ($playbackSpeed)" else "Paused",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }

                // Bottom Video Controls Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "18:24 / ${video.duration}", style = MaterialTheme.typography.labelSmall, color = Color.White)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Playback Speed Toggle
                        listOf("1.0x", "1.25x", "1.5x", "2.0x").forEach { spd ->
                            Text(
                                text = spd,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (playbackSpeed == spd) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (playbackSpeed == spd) Gold400 else Slate400,
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .clickable { playbackSpeed = spd }
                            )
                        }
                    }
                }
            }

            // Video Meta & Faculty
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Blue100
                    ) {
                        Text(
                            text = "${video.examCategory} • ${video.subject}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Blue800,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = video.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Slate900
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Faculty: ${video.teacherName} • ${video.viewsCount} Views • ${video.dateUploaded}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate600
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Saved class offline to app memory", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Download Video", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Notes associated with lecture opened", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Class Notes", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Chapter Timeline
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Lecture Timestamps & Chapters",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    listOf(
                        "00:00" to "Introduction & Syllabus Weightage",
                        "08:15" to "Core Conceptual Framework",
                        "22:40" to "Previous Year Questions (PYQs) Analysis",
                        "38:10" to "Summary & Memory Mnemonics"
                    ).forEach { (time, desc) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Blue50
                            ) {
                                Text(
                                    text = time,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Blue800,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = Slate800)
                        }
                    }
                }
            }
        }
    }
}
