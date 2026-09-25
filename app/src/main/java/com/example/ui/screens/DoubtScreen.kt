package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@Composable
fun DoubtScreen(
    viewModel: AcademyViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Faculty Doubts, 1: AI Instant Solver
    val doubts by viewModel.doubts.collectAsState()

    var showAskDialog by remember { mutableStateOf(false) }
    var doubtSubject by remember { mutableStateOf("Geography") }
    var doubtQuestion by remember { mutableStateOf("") }

    // AI Tab State
    var aiQueryInput by remember { mutableStateOf("") }
    val aiResponse by viewModel.aiResponse.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .testTag("doubt_screen")
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Column {
                        Text(
                            text = "Doubt Solving Corner",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Get personal guidance from Suri & Durgapur mentors + AI Solver",
                            style = MaterialTheme.typography.bodySmall,
                            color = Gold400
                        )
                    }
                }
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
                text = { Text("👨‍🏫 Faculty Doubts", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_faculty_doubts")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("✨ AI Doubt Solver", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_ai_doubt_solver")
            )
        }

        if (selectedTab == 0) {
            // Faculty Doubts List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Button(
                        onClick = { showAskDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("ask_doubt_button")
                    ) {
                        Icon(Icons.Default.AddComment, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ask New Doubt to Teachers", fontWeight = FontWeight.Bold)
                    }
                }

                items(doubts, key = { it.id }) { doubt ->
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
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Blue100
                                ) {
                                    Text(
                                        text = doubt.subject,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Blue800,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (doubt.status == "Answered") SuccessGreen.copy(alpha = 0.15f) else Gold100
                                ) {
                                    Text(
                                        text = if (doubt.status == "Answered") "🔵 Answered" else "🟡 Pending",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (doubt.status == "Answered") SuccessGreen else Gold600,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "“${doubt.questionText}”",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Slate900
                            )

                            Text(
                                text = "Asked by ${doubt.studentName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Slate400,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            if (doubt.status == "Answered" && doubt.replyText.isNotBlank()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Slate50,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "👨‍🏫 Faculty Answer (${doubt.teacherName}):",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Blue800
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = doubt.replyText,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Slate800
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // AI Doubt Solver Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "✨", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Instant AI Doubt Assistant",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Slate900
                            )
                        }
                        Text(
                            text = "Ask any question from WBCS, SSC, Railway, Banking syllabus for instant conceptual clarity.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate600
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = aiQueryInput,
                            onValueChange = { aiQueryInput = it },
                            placeholder = { Text("e.g., Difference between SI and CI, River Teesta origin, Fundamental Rights...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .testTag("ai_doubt_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                viewModel.askAiAssistant(aiQueryInput)
                            },
                            enabled = !isAiLoading && aiQueryInput.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ai_solve_button")
                        ) {
                            if (isAiLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Analyzing Syllabus...")
                            } else {
                                Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Solve with AI", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // AI Response Card
                if (aiResponse != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ai_response_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Blue50),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Blue800.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Academic AI Explanation",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Blue800
                                )
                                Text(
                                    text = "Verified for Exam",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = SuccessGreen
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = aiResponse ?: "",
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                color = Slate800
                            )
                        }
                    }
                }
            }
        }

        // Ask Doubt Dialog
        if (showAskDialog) {
            AlertDialog(
                onDismissRequest = { showAskDialog = false },
                title = { Text("Ask Doubt to Faculty", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Select Subject:", style = MaterialTheme.typography.labelMedium)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("Geography", "History", "Math", "Polity").forEach { s ->
                                FilterChip(
                                    selected = doubtSubject == s,
                                    onClick = { doubtSubject = s },
                                    label = { Text(s, fontSize = 11.sp) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = doubtQuestion,
                            onValueChange = { doubtQuestion = it },
                            placeholder = { Text("Type your query in detail...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (doubtQuestion.isNotBlank()) {
                                viewModel.askDoubt(doubtSubject, doubtQuestion)
                                Toast.makeText(context, "Doubt submitted to faculty!", Toast.LENGTH_SHORT).show()
                                doubtQuestion = ""
                                showAskDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Blue800)
                    ) {
                        Text("Submit Doubt")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAskDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
