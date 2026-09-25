package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CbtTestScreen(
    viewModel: AcademyViewModel,
    test: MockTest,
    onCancel: () -> Unit
) {
    val questions by viewModel.activeQuestions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val markedForReview by viewModel.reviewMarked.collectAsState()
    val secondsLeft by viewModel.timeRemainingSeconds.collectAsState()

    var showSubmitDialog by remember { mutableStateOf(false) }
    var showPaletteSheet by remember { mutableStateOf(false) }

    // Intercept hardware back button
    BackHandler {
        showSubmitDialog = true
    }

    val minutes = secondsLeft / 60
    val secs = secondsLeft % 60
    val timerString = String.format("%02d:%02d", minutes, secs)

    val currentQ = questions.getOrNull(currentIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = test.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            color = Color.White
                        )
                        Text(
                            text = "${test.examCategory} • ${questions.size} Questions",
                            style = MaterialTheme.typography.labelSmall,
                            color = Slate200
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { showSubmitDialog = true }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                },
                actions = {
                    // Timer Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (secondsLeft < 120) ErrorRed else Navy800,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = null,
                                tint = if (secondsLeft < 120) Color.White else Gold400,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = timerString,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black),
                                color = Color.White
                            )
                        }
                    }

                    // Submit Button
                    Button(
                        onClick = { showSubmitDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .testTag("submit_test_header_button")
                    ) {
                        Text("Submit", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy900)
            )
        },
        bottomBar = {
            // Test Navigation Controls
            Surface(
                color = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Action Buttons Row: Clear, Mark For Review, Palette Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { viewModel.clearAnswer(currentIndex) },
                            modifier = Modifier.testTag("cbt_clear_button")
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp), tint = Slate600)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear", color = Slate600, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.toggleMarkForReview(currentIndex) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("cbt_review_button")
                        ) {
                            val isMarked = markedForReview.contains(currentIndex)
                            Icon(
                                if (isMarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = if (isMarked) Gold600 else Slate700,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                if (isMarked) "Marked" else "Mark for Review",
                                color = if (isMarked) Gold600 else Slate700,
                                fontSize = 12.sp
                            )
                        }

                        IconButton(
                            onClick = { showPaletteSheet = true },
                            modifier = Modifier.testTag("cbt_palette_open_button")
                        ) {
                            Icon(Icons.Default.GridView, contentDescription = "Question Matrix", tint = Blue800)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Next / Prev Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.prevQuestion() },
                            enabled = currentIndex > 0,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("cbt_prev_button")
                        ) {
                            Icon(Icons.Default.ChevronLeft, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Previous")
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                if (currentIndex < questions.size - 1) {
                                    viewModel.nextQuestion()
                                } else {
                                    showSubmitDialog = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("cbt_next_button")
                        ) {
                            Text(if (currentIndex < questions.size - 1) "Save & Next" else "Review & Submit")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ChevronRight, contentDescription = null)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (currentQ != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Slate50)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Question Header Strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Blue100
                    ) {
                        Text(
                            text = "Question ${currentIndex + 1} of ${questions.size}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Blue800,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Slate200
                    ) {
                        Text(
                            text = currentQ.subject,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = Slate800,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Question Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = currentQ.questionText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp
                            ),
                            color = Slate900
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Options (A, B, C, D)
                val options = listOf(
                    0 to currentQ.optionA,
                    1 to currentQ.optionB,
                    2 to currentQ.optionC,
                    3 to currentQ.optionD
                )
                val userPick = userAnswers[currentIndex]

                options.forEach { (optionIdx, optionText) ->
                    val isSelected = userPick == optionIdx
                    val letter = when (optionIdx) {
                        0 -> "A"
                        1 -> "B"
                        2 -> "C"
                        else -> "D"
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clickable { viewModel.selectAnswer(currentIndex, optionIdx) }
                            .testTag("option_${letter}_$currentIndex"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Blue50 else Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Blue800 else Slate200
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Blue800 else Slate100),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = letter,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) Color.White else Slate700
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) Blue800 else Slate800,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }

    // Question Palette BottomSheet
    if (showPaletteSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPaletteSheet = false },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Question Palette (${test.title})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate900
                )

                // Legend
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LegendItem("Answered", SuccessGreen)
                    LegendItem("Marked", Gold500)
                    LegendItem("Unanswered", Slate400)
                }

                HorizontalDivider()

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(questions) { idx, _ ->
                        val isAnswered = userAnswers.containsKey(idx)
                        val isMarked = markedForReview.contains(idx)
                        val isCurrent = idx == currentIndex

                        val bgColor = when {
                            isAnswered -> SuccessGreen
                            isMarked -> Gold500
                            else -> Slate200
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(bgColor)
                                .then(
                                    if (isCurrent) Modifier.border(2.5.dp, Blue800, CircleShape)
                                    else Modifier
                                )
                                .clickable {
                                    viewModel.goToQuestion(idx)
                                    showPaletteSheet = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isAnswered || isMarked) Color.White else Slate800
                            )
                        }
                    }
                }
            }
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitDialog) {
        val totalQ = questions.size
        val answeredQ = userAnswers.size
        val unattemptedQ = totalQ - answeredQ
        val markedQ = markedForReview.size

        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = {
                Text("Submit Test Confirmation", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("Are you ready to submit your test for scoring?")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("• Total Questions: $totalQ")
                    Text("• Attempted: $answeredQ", color = SuccessGreen, fontWeight = FontWeight.Bold)
                    Text("• Unattempted: $unattemptedQ", color = ErrorRed)
                    Text("• Marked for Review: $markedQ", color = Gold600)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        viewModel.submitCurrentTest()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) {
                    Text("Submit Test Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog = false }) {
                    Text("Resume Test")
                }
            }
        )
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Slate700)
    }
}
