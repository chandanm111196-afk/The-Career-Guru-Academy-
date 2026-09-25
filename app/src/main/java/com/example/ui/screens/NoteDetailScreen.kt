package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyNote
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    viewModel: AcademyViewModel,
    note: StudyNote,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var fontSizeSp by remember { mutableFloatStateOf(15f) }
    var currentPage by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            color = Color.White
                        )
                        Text(
                            text = "${note.subject} • Page $currentPage of ${note.pagesCount}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Gold400
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleNoteBookmark(note.id) }) {
                        Icon(
                            if (note.isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = Gold400
                        )
                    }
                    IconButton(onClick = {
                        val shareText = "Study Note: ${note.title}\nThe Career Guru Academy, Suri & Durgapur. Call: 9093200422"
                        Toast.makeText(context, "Shared note details", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy900)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                tonalElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { if (fontSizeSp > 12f) fontSizeSp -= 2f },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("A-", fontWeight = FontWeight.Bold, color = Slate700)
                        }
                        IconButton(
                            onClick = { if (fontSizeSp < 22f) fontSizeSp += 2f },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("A+", fontWeight = FontWeight.Bold, color = Slate700)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { if (currentPage > 1) currentPage-- },
                            enabled = currentPage > 1,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Prev")
                        }

                        Text(
                            text = " $currentPage / ${note.pagesCount} ",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Slate800,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Button(
                            onClick = { if (currentPage < note.pagesCount) currentPage++ },
                            enabled = currentPage < note.pagesCount,
                            colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Next")
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Slate50)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .testTag("note_reader_view")
        ) {
            // Academy watermark badge
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Blue50,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = Blue800, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "The Career Guru Academy Official Study Material • Suri & Durgapur Branches",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Blue800
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "${note.title} (Part $currentPage)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Slate900
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = note.contentPreview.ifBlank {
                            "Detailed academic study content for ${note.title}. Curated by senior faculty covering fundamental concepts, standard formulas, mnemonics, and previous 10 years question trends for West Bengal and Central competitive exams."
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = fontSizeSp.sp,
                            lineHeight = (fontSizeSp * 1.5).sp
                        ),
                        color = Slate800
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Key Takeaways for Revision:\n• Regularly practice Mock Test series after completing this module.\n• Clarify any ambiguities at Suri or Durgapur doubt counters.\n• Recommended next lecture: Live Session with Prof. Anirban Sen.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = (fontSizeSp - 1).sp
                        ),
                        color = Slate700
                    )
                }
            }
        }
    }
}
