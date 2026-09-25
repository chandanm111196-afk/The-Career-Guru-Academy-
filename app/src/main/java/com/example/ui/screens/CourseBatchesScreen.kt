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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CourseBatch
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@Composable
fun CourseBatchesScreen(
    viewModel: AcademyViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val batches by viewModel.courseBatches.collectAsState()
    var selectedBatchForPurchase by remember { mutableStateOf<CourseBatch?>(null) }
    var showPaymentSuccessDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .testTag("course_batches_screen")
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Navy900,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Column {
                    Text(
                        text = "Courses & Batches (Suri & Durgapur)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "Comprehensive foundation & crash courses with live mentorship",
                        style = MaterialTheme.typography.bodySmall,
                        color = Gold400
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(batches, key = { it.id }) { batch ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("batch_card_${batch.id}"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
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
                                    text = batch.exam,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Blue800,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Gold500, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${batch.rating}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Slate800
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = batch.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Slate900
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Mentors: ${batch.faculty}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate600
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text(text = "⏱ ${batch.duration}", style = MaterialTheme.typography.labelSmall, color = Slate700)
                            Text(text = "🎥 ${batch.lecturesCount}+ Lectures", style = MaterialTheme.typography.labelSmall, color = Slate700)
                            Text(text = "📝 Mock Tests Included", style = MaterialTheme.typography.labelSmall, color = Slate700)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = batch.price,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                    color = Navy900
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = batch.originalPrice,
                                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                                    color = Slate400,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }

                            if (batch.isEnrolled) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = SuccessGreen.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "✓ Enrolled",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = SuccessGreen,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            } else {
                                Button(
                                    onClick = { selectedBatchForPurchase = batch },
                                    colors = ButtonDefaults.buttonColors(containerColor = Gold600),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Enroll Now", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Checkout / Payment Modal
        if (selectedBatchForPurchase != null) {
            val b = selectedBatchForPurchase!!
            AlertDialog(
                onDismissRequest = { selectedBatchForPurchase = null },
                title = { Text("Enroll in ${b.title}", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Fee: ${b.price} (Inclusive of GST)", fontWeight = FontWeight.SemiBold, color = Blue800)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Select Secure Payment Mode:")
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Slate100,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("• UPI (Google Pay / PhonePe / Paytm / BHIM)", style = MaterialTheme.typography.bodySmall)
                                Text("• NetBanking / Debit Card (SBI, HDFC, PNB, ICICI)", style = MaterialTheme.typography.bodySmall)
                                Text("• Suri / Durgapur Branch Cash Counter receipt", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.enrollInCourse(b.id)
                            selectedBatchForPurchase = null
                            showPaymentSuccessDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Text("Proceed & Pay ${b.price}")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedBatchForPurchase = null }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showPaymentSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showPaymentSuccessDialog = false },
                title = { Text("Enrollment Confirmed! 🎉", fontWeight = FontWeight.Bold) },
                text = {
                    Text("Congratulations! Your batch enrollment is verified. You now have full access to live classes, recorded vault, notes and mock test series.")
                },
                confirmButton = {
                    Button(onClick = { showPaymentSuccessDialog = false }) {
                        Text("Start Learning")
                    }
                }
            )
        }
    }
}
