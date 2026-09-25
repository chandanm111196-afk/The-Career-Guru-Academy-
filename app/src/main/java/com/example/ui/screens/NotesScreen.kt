package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FileDownload
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
import com.example.data.model.StudyNote
import com.example.ui.components.ExamFilterChipsRow
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@Composable
fun NotesScreen(
    viewModel: AcademyViewModel,
    onOpenNote: (StudyNote) -> Unit
) {
    val context = LocalContext.current
    val notes by viewModel.studyNotes.collectAsState()
    val selectedExam by viewModel.selectedExamFilter.collectAsState()
    var searchKeyword by remember { mutableStateOf("") }
    var selectedSubjectFilter by remember { mutableStateOf("All") }

    val subjects = listOf("All", "Geography", "History", "Polity", "Economics", "English", "Quantitative Aptitude", "General Science")

    val filteredNotes = remember(notes, selectedExam, searchKeyword, selectedSubjectFilter) {
        notes.filter { note ->
            val matchExam = selectedExam == "All" || note.examCategory.equals(selectedExam, ignoreCase = true)
            val matchSubject = selectedSubjectFilter == "All" || note.subject.equals(selectedSubjectFilter, ignoreCase = true)
            val matchSearch = searchKeyword.isBlank() ||
                    note.title.contains(searchKeyword, ignoreCase = true) ||
                    note.description.contains(searchKeyword, ignoreCase = true) ||
                    note.subject.contains(searchKeyword, ignoreCase = true)
            matchExam && matchSubject && matchSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .testTag("notes_screen")
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
                    text = "Notes & Study Material Library",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = "Comprehensive PDF notes curated by The Career Guru faculty",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gold400
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchKeyword,
                    onValueChange = { searchKeyword = it },
                    placeholder = { Text("Search by topic (e.g. Fundamental Rights, Rivers)...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Slate400) },
                    trailingIcon = {
                        if (searchKeyword.isNotEmpty()) {
                            IconButton(onClick = { searchKeyword = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Slate400)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Gold500,
                        unfocusedBorderColor = Slate400
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("notes_search_input")
                )
            }
        }

        // Exam Category Chips
        ExamFilterChipsRow(
            selectedExam = selectedExam,
            onSelectExam = { viewModel.setExamFilter(it) }
        )

        // Notes List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredNotes, key = { it.id }) { note ->
                StudyNoteCard(
                    note = note,
                    onOpen = { onOpenNote(note) },
                    onToggleBookmark = { viewModel.toggleNoteBookmark(note.id) },
                    onToggleDownload = {
                        viewModel.toggleNoteDownload(note.id)
                        val msg = if (note.isDownloaded) "Removed from offline downloads" else "Downloaded ${note.title} for offline study!"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
fun StudyNoteCard(
    note: StudyNote,
    onOpen: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleDownload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
            .testTag("note_card_${note.id}"),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Blue100
                    ) {
                        Text(
                            text = note.examCategory,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Blue800,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Slate100
                    ) {
                        Text(
                            text = note.subject,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = Slate800,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (note.isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (note.isBookmarked) Gold600 else Slate400,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleDownload,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (note.isDownloaded) Icons.Default.DownloadDone else Icons.Outlined.FileDownload,
                            contentDescription = "Download",
                            tint = if (note.isDownloaded) SuccessGreen else Slate400,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Slate900
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = note.description,
                style = MaterialTheme.typography.bodySmall,
                color = Slate600,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${note.pagesCount} Pages • ${note.fileSize}", style = MaterialTheme.typography.labelSmall, color = Slate600)
                }

                Button(
                    onClick = onOpen,
                    colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Read PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
