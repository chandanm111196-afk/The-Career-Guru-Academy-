package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.data.model.UserRole
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@Composable
fun TeacherAdminScreen(
    viewModel: AcademyViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.collectAsState()
    val doubts by viewModel.doubts.collectAsState()

    var activeTab by remember { mutableIntStateOf(0) } // 0: Live Classes & Content, 1: Student Doubts, 2: Attendance & Admin

    // New Class state
    var classTitle by remember { mutableStateOf("") }
    var classTeacher by remember { mutableStateOf("Prof. Anirban Sen") }
    var classSubject by remember { mutableStateOf("Geography") }
    var classExam by remember { mutableStateOf("WBCS") }
    var classTime by remember { mutableStateOf("Today — 9:00 PM") }

    // New Note state
    var noteTitle by remember { mutableStateOf("") }
    var noteDesc by remember { mutableStateOf("") }
    var notePages by remember { mutableStateOf("30") }

    // Doubt answering state
    var selectedDoubtToAnswer by remember { mutableStateOf<Long?>(null) }
    var doubtReplyText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .testTag("teacher_admin_screen")
    ) {
        // Header
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Column {
                        Text(
                            text = if (userProfile.role == UserRole.ADMIN) "Director / Admin Console" else "Teacher Management Portal",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Manage classes, notes, mock tests & student queries",
                            style = MaterialTheme.typography.bodySmall,
                            color = Gold400
                        )
                    }
                }
            }
        }

        TabRow(
            selectedTabIndex = activeTab,
            containerColor = Color.White,
            contentColor = Blue800,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                text = { Text("Publish Content", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                text = { Text("Answer Doubts (${doubts.count { it.status == "Pending" }})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeTab == 2,
                onClick = { activeTab = 2 },
                text = { Text("Mark Attendance", fontWeight = FontWeight.Bold) }
            )
        }

        when (activeTab) {
            0 -> {
                // Content Publisher
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Schedule / Go Live Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "🔴 Schedule or Start Live Class",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Navy900
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = classTitle,
                                onValueChange = { classTitle = it },
                                label = { Text("Class Title (e.g. Modern Indian History - INM)") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = classSubject,
                                    onValueChange = { classSubject = it },
                                    label = { Text("Subject") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = classExam,
                                    onValueChange = { classExam = it },
                                    label = { Text("Exam") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = classTime,
                                onValueChange = { classTime = it },
                                label = { Text("Class Time (e.g. Today — 9:00 PM)") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (classTitle.isNotBlank()) {
                                        viewModel.addNewLiveClass(classTitle, classTeacher, classSubject, classExam, classTime)
                                        Toast.makeText(context, "Live Class scheduled and published to student feeds!", Toast.LENGTH_SHORT).show()
                                        classTitle = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Publish Live Class to Students", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Upload Note Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "📚 Upload PDF Study Material",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Navy900
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = noteTitle,
                                onValueChange = { noteTitle = it },
                                label = { Text("Note Title (e.g. Indian Constitution Articles 1 to 51A)") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = noteDesc,
                                onValueChange = { noteDesc = it },
                                label = { Text("Summary / Highlights") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (noteTitle.isNotBlank()) {
                                        val pages = notePages.toIntOrNull() ?: 25
                                        viewModel.addNewNote(noteTitle, "WBCS", "Polity", noteDesc, pages)
                                        Toast.makeText(context, "Note published to Academy Library!", Toast.LENGTH_SHORT).show()
                                        noteTitle = ""
                                        noteDesc = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Upload & Publish Study Note", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            1 -> {
                // Pending Doubts
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val pending = doubts.filter { it.status == "Pending" }
                    if (pending.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "All student queries have been answered! 🎉",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SuccessGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    items(pending, key = { it.id }) { d ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Student: ${d.studentName} (${d.subject})",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Blue800
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "“${d.questionText}”",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = Slate900
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = {
                                        selectedDoubtToAnswer = d.id
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Gold600),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Write Faculty Solution")
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Attendance Marker
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Mark Batch Attendance (Suri & Durgapur)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Navy900
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.markAttendanceDirect("WBCS Geography Class", true)
                                        Toast.makeText(context, "Marked Present for today's batch!", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Mark Present")
                                }

                                OutlinedButton(
                                    onClick = {
                                        viewModel.markAttendanceDirect("WBCS Geography Class", false)
                                        Toast.makeText(context, "Marked Absent for today's batch.", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Mark Absent")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Answer Doubt Dialog
        if (selectedDoubtToAnswer != null) {
            AlertDialog(
                onDismissRequest = { selectedDoubtToAnswer = null },
                title = { Text("Submit Faculty Solution") },
                text = {
                    OutlinedTextField(
                        value = doubtReplyText,
                        onValueChange = { doubtReplyText = it },
                        placeholder = { Text("Write clear explanation for student...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val id = selectedDoubtToAnswer
                            if (id != null && doubtReplyText.isNotBlank()) {
                                viewModel.answerDoubt(id, doubtReplyText)
                                Toast.makeText(context, "Reply sent to student!", Toast.LENGTH_SHORT).show()
                                doubtReplyText = ""
                                selectedDoubtToAnswer = null
                            }
                        }
                    ) {
                        Text("Send Solution")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedDoubtToAnswer = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
