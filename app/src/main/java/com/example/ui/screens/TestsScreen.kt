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
import com.example.data.model.MockTest
import com.example.ui.components.ExamFilterChipsRow
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@Composable
fun TestsScreen(
    viewModel: AcademyViewModel,
    onStartTest: (MockTest) -> Unit
) {
    var selectedCategoryTab by remember { mutableIntStateOf(0) } // 0: All Mocks, 1: Daily Quiz, 2: Subject Tests, 3: PYQs
    val mockTests by viewModel.mockTests.collectAsState()
    val selectedExam by viewModel.selectedExamFilter.collectAsState()

    val filteredTests = remember(mockTests, selectedExam, selectedCategoryTab) {
        var list = if (selectedExam == "All") mockTests else mockTests.filter { it.examCategory.equals(selectedExam, ignoreCase = true) }
        when (selectedCategoryTab) {
            1 -> list.filter { it.testType == "Daily Quiz" }
            2 -> list.filter { it.testType == "Subject Test" || it.testType == "Chapter Test" }
            3 -> list.filter { it.testType == "PYQ" }
            else -> list
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .testTag("tests_screen")
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
                    text = "Examination & Mock Test Hub",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = "Real exam simulation with timer, negative marking & ranking",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gold400
                )
            }
        }

        // Sub Category Scrollable Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedCategoryTab,
            containerColor = Color.White,
            contentColor = Blue800,
            edgePadding = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedCategoryTab == 0,
                onClick = { selectedCategoryTab = 0 },
                text = { Text("All Mocks", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_all_mocks")
            )
            Tab(
                selected = selectedCategoryTab == 1,
                onClick = { selectedCategoryTab = 1 },
                text = { Text("Daily Quiz 🔥", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_daily_quiz")
            )
            Tab(
                selected = selectedCategoryTab == 2,
                onClick = { selectedCategoryTab = 2 },
                text = { Text("Subject Tests", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_subject_tests")
            )
            Tab(
                selected = selectedCategoryTab == 3,
                onClick = { selectedCategoryTab = 3 },
                text = { Text("Previous Year (PYQ)", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_pyqs")
            )
        }

        // Exam Filter Chips
        ExamFilterChipsRow(
            selectedExam = selectedExam,
            onSelectExam = { viewModel.setExamFilter(it) }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredTests, key = { it.id }) { testItem ->
                MockTestCard(
                    test = testItem,
                    onStart = { onStartTest(testItem) }
                )
            }
        }
    }
}

@Composable
fun MockTestCard(
    test: MockTest,
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("test_card_${test.id}"),
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
                        text = test.examCategory,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Blue800,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (test.difficulty == "Challenging") ErrorRed.copy(alpha = 0.1f) else Gold100
                ) {
                    Text(
                        text = "${test.difficulty} • ${test.testType}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (test.difficulty == "Challenging") ErrorRed else Gold600,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = test.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Slate900
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${test.questionsCount} Questions", style = MaterialTheme.typography.bodySmall, color = Slate700)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${test.durationMinutes} Mins", style = MaterialTheme.typography.bodySmall, color = Slate700)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Grade, contentDescription = null, tint = Gold500, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${test.totalMarks} Marks", style = MaterialTheme.typography.bodySmall, color = Slate700)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👥 ${test.attemptsCount} Aspirants attempted",
                    style = MaterialTheme.typography.labelSmall,
                    color = Slate400
                )

                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("action_start_${test.id}")
                ) {
                    Text("Start Test", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
