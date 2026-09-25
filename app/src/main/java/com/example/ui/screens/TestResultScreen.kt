package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.data.model.TestAttempt
import com.example.data.model.TestQuestion
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@Composable
fun TestResultScreen(
    viewModel: AcademyViewModel,
    attempt: TestAttempt,
    test: MockTest,
    onBackToHome: () -> Unit
) {
    var showSolutions by remember { mutableStateOf(false) }
    val questions by viewModel.activeQuestions.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .testTag("test_result_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Result Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Gold500),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Navy900, modifier = Modifier.size(36.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Test Completed!",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = Color.White
                    )

                    Text(
                        text = attempt.testTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gold400
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Score Big Box
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = String.format("%.1f", attempt.score),
                            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.White
                        )
                        Text(
                            text = " / ${attempt.maxScore.toInt()} Marks",
                            style = MaterialTheme.typography.titleMedium,
                            color = Slate400,
                            modifier = Modifier.padding(top = 16.dp, start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Estimated Percentile: ${(attempt.accuracyPercentage * 0.95).toInt() + 10}th • Rank #12",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Gold400
                    )
                }
            }
        }

        // Stats Grid Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Performance Breakdown",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Slate900
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem("Attempted", "${attempt.correctCount + attempt.incorrectCount}", Blue800)
                        StatItem("Correct", "${attempt.correctCount}", SuccessGreen)
                        StatItem("Incorrect", "${attempt.incorrectCount}", ErrorRed)
                        StatItem("Unattempted", "${attempt.unattemptedCount}", Slate600)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem("Accuracy", "${attempt.accuracyPercentage}%", Gold600)
                        StatItem("Time Taken", "${attempt.timeSpentSeconds / 60}m ${attempt.timeSpentSeconds % 60}s", InfoCyan)
                        StatItem("XP Earned", "+${(attempt.score * 10).toInt()} XP", Gold500)
                    }
                }
            }
        }

        // Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { showSolutions = !showSolutions },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("toggle_solutions_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        if (showSolutions) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (showSolutions) "Hide Solutions" else "Detailed Solutions")
                }

                Button(
                    onClick = onBackToHome,
                    colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("result_done_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Back to Hub")
                }
            }
        }

        // Detailed Solutions Section
        if (showSolutions) {
            item {
                Text(
                    text = "Question-wise Solutions & Answers",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate900,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            itemsIndexed(questions) { idx, q ->
                val userPick = userAnswers[idx]
                val isCorrect = userPick == q.correctOptionIndex
                val isUnattempted = userPick == null

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("solution_card_$idx"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Q${idx + 1}. (${q.subject})",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Blue800
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when {
                                    isUnattempted -> Slate100
                                    isCorrect -> SuccessGreen.copy(alpha = 0.15f)
                                    else -> ErrorRed.copy(alpha = 0.15f)
                                }
                            ) {
                                Text(
                                    text = when {
                                        isUnattempted -> "Skipped"
                                        isCorrect -> "✓ Correct"
                                        else -> "✗ Incorrect"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = when {
                                        isUnattempted -> Slate600
                                        isCorrect -> SuccessGreen
                                        else -> ErrorRed
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = q.questionText,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = Slate900
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val optionStrings = listOf(q.optionA, q.optionB, q.optionC, q.optionD)
                        optionStrings.forEachIndexed { optIdx, text ->
                            val isCorrectAnswer = optIdx == q.correctOptionIndex
                            val isUserChoice = optIdx == userPick

                            val rowColor = when {
                                isCorrectAnswer -> SuccessGreen.copy(alpha = 0.12f)
                                isUserChoice && !isCorrect -> ErrorRed.copy(alpha = 0.12f)
                                else -> Color.Transparent
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                shape = RoundedCornerShape(8.dp),
                                color = rowColor
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val mark = when {
                                        isCorrectAnswer -> "✓"
                                        isUserChoice -> "✗"
                                        else -> "•"
                                    }
                                    Text(
                                        text = mark,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCorrectAnswer) SuccessGreen else if (isUserChoice) ErrorRed else Slate400,
                                        modifier = Modifier.width(20.dp)
                                    )
                                    Text(
                                        text = "${('A'.code + optIdx).toChar()}. $text",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = if (isCorrectAnswer || isUserChoice) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (isCorrectAnswer) SuccessGreen else if (isUserChoice) ErrorRed else Slate800
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Explanation Box
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = Gold100.copy(alpha = 0.6f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "💡 Explanation:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Gold600
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = q.explanation,
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
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = color)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Slate600)
    }
}
