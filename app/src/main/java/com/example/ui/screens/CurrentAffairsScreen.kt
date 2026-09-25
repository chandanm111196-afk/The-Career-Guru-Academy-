package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.CurrentAffair
import com.example.ui.theme.*
import com.example.viewmodel.AcademyViewModel

@Composable
fun CurrentAffairsScreen(
    viewModel: AcademyViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val currentAffairs by viewModel.currentAffairs.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    var expandedItem by remember { mutableStateOf<CurrentAffair?>(null) }

    val categories = listOf("All", "West Bengal", "Economy", "National", "Defence", "Science & Tech", "Banking", "Sports")

    val filtered = remember(currentAffairs, selectedCategory) {
        if (selectedCategory == "All") currentAffairs else currentAffairs.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .testTag("current_affairs_screen")
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
                            text = "Daily Current Affairs (WB & National)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Curated specifically for WBCS, PSC, SSC & Banking exams",
                            style = MaterialTheme.typography.bodySmall,
                            color = Gold400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Download Daily PDF button
                Button(
                    onClick = {
                        Toast.makeText(context, "Downloading Today's Daily Current Affairs Capsule PDF...", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Gold500, contentColor = Navy900),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Download Daily Current Affairs PDF Capsule", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Blue800,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filtered, key = { it.id }) { ca ->
                val isExpanded = expandedItem?.id == ca.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedItem = if (isExpanded) null else ca }
                        .testTag("ca_card_${ca.id}"),
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
                                color = if (ca.category == "West Bengal") Gold100 else Blue100
                            ) {
                                Text(
                                    text = ca.category,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (ca.category == "West Bengal") Gold600 else Blue800,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = ca.date, style = MaterialTheme.typography.labelSmall, color = Slate400)
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { viewModel.toggleCurrentAffairBookmark(ca.id) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = if (ca.isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                                        contentDescription = "Bookmark",
                                        tint = if (ca.isBookmarked) Gold600 else Slate400,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = ca.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Slate900
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isExpanded) ca.fullDetails else ca.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate700
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (isExpanded) "Show Less ↑" else "Read Comprehensive Analysis →",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Blue800
                        )
                    }
                }
            }
        }
    }
}
