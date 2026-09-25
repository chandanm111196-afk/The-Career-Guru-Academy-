package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    onLoginSuccess: (UserProfile) -> Unit
) {
    val context = LocalContext.current
    var isRegisterMode by remember { mutableStateOf(false) }
    var selectedAuthType by remember { mutableIntStateOf(0) } // 0: Student, 1: Teacher/Admin

    // Fields
    var fullName by remember { mutableStateOf("Sourav Mukherjee") }
    var mobileNumber by remember { mutableStateOf("9093200422") }
    var email by remember { mutableStateOf("sourav.wbcs@careerguru.in") }
    var password by remember { mutableStateOf("••••••••") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedBranch by remember { mutableStateOf("Suri, Birbhum") }
    var selectedExam by remember { mutableStateOf("WBCS") }
    var isOtpMode by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .verticalScroll(rememberScrollState())
            .testTag("auth_screen")
    ) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy900)
                .padding(top = 28.dp, bottom = 24.dp, start = 20.dp, end = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Gold500),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = Navy900,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "THE CAREER GURU ACADEMY",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color = Color.White
                )
                Text(
                    text = "“Your Success is Our Achievement”",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gold400
                )
            }
        }

        // Tab Selector: Student vs Teacher/Admin
        TabRow(
            selectedTabIndex = selectedAuthType,
            containerColor = Color.White,
            contentColor = Blue800,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedAuthType == 0,
                onClick = { selectedAuthType = 0 },
                text = { Text("Student Portal", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.School, contentDescription = null) },
                modifier = Modifier.testTag("tab_student_auth")
            )
            Tab(
                selected = selectedAuthType == 1,
                onClick = { selectedAuthType = 1 },
                text = { Text("Teacher / Admin", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null) },
                modifier = Modifier.testTag("tab_admin_auth")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = if (selectedAuthType == 0) {
                        if (isRegisterMode) "Student Registration" else "Student Login"
                    } else {
                        "Faculty & Admin Login"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate900
                )
                Text(
                    text = if (selectedAuthType == 0) {
                        "Enter details to access classes, notes and mock tests."
                    } else {
                        "Secure staff portal for batch management & test publishing."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate600
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (selectedAuthType == 0 && isRegisterMode) {
                    // Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("auth_name_input")
                    )

                    // Branch Selection
                    Text(
                        text = "Academy Branch",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = Slate700
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Suri, Birbhum", "Durgapur, West Burdwan").forEach { branch ->
                            val isSelected = selectedBranch == branch
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedBranch = branch },
                                label = { Text(branch, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Blue800,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    // Preferred Exam
                    Text(
                        text = "Preferred Exam Target",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = Slate700,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("WBCS", "SSC", "RRB", "BANKING", "TET").forEach { exam ->
                            val isSelected = selectedExam == exam
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedExam = exam },
                                label = { Text(exam, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Gold600,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Mobile / Email
                OutlinedTextField(
                    value = if (isOtpMode) mobileNumber else email,
                    onValueChange = {
                        if (isOtpMode) mobileNumber = it else email = it
                    },
                    label = { Text(if (isOtpMode) "Mobile Number (10 digits)" else "Email Address") },
                    leadingIcon = {
                        Icon(
                            if (isOtpMode) Icons.Default.Phone else Icons.Default.Email,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("auth_email_mobile_input")
                )

                if (selectedAuthType == 0 && isOtpMode) {
                    if (!isOtpSent) {
                        Button(
                            onClick = {
                                isOtpSent = true
                                Toast.makeText(context, "OTP 8492 sent to $mobileNumber", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text("Send OTP")
                        }
                    } else {
                        OutlinedTextField(
                            value = otpCode,
                            onValueChange = { otpCode = it },
                            label = { Text("Enter OTP (e.g. 8492)") },
                            leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .testTag("auth_otp_input")
                        )
                    }
                } else {
                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(if (selectedAuthType == 1) "Staff Passcode / Key" else "Password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                            .testTag("auth_password_input")
                    )
                }

                if (selectedAuthType == 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { isOtpMode = !isOtpMode }) {
                            Text(
                                if (isOtpMode) "Use Password Login" else "Login via OTP",
                                style = MaterialTheme.typography.labelMedium,
                                color = Blue800
                            )
                        }
                        TextButton(onClick = {
                            Toast.makeText(context, "Password reset link sent to $email", Toast.LENGTH_SHORT).show()
                        }) {
                            Text("Forgot?", style = MaterialTheme.typography.labelMedium, color = Slate600)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Primary Login / Register Button
                Button(
                    onClick = {
                        val role = if (selectedAuthType == 1) UserRole.TEACHER else UserRole.STUDENT
                        val user = UserProfile(
                            id = "current_user",
                            name = if (selectedAuthType == 1) "Prof. Anirban Sen (Faculty)" else fullName,
                            mobile = mobileNumber,
                            email = email,
                            role = role,
                            branch = selectedBranch,
                            preferredExam = selectedExam,
                            streakDays = 7,
                            xpPoints = 1450
                        )
                        Toast.makeText(context, "Welcome, ${user.name}!", Toast.LENGTH_SHORT).show()
                        onLoginSuccess(user)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedAuthType == 1) Navy900 else Blue800
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("auth_submit_button")
                ) {
                    Text(
                        text = if (selectedAuthType == 1) {
                            "Login as Faculty / Admin"
                        } else {
                            if (isRegisterMode) "Complete Registration" else "Login to Academy"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                if (selectedAuthType == 0) {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            val user = UserProfile(
                                id = "current_user",
                                name = "Sourav Mukherjee",
                                mobile = "9093200422",
                                email = "sourav.wbcs@careerguru.in",
                                role = UserRole.STUDENT,
                                branch = "Suri, Birbhum",
                                preferredExam = "WBCS"
                            )
                            Toast.makeText(context, "Signed in with Google Account", Toast.LENGTH_SHORT).show()
                            onLoginSuccess(user)
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("google_login_button")
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Blue800)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Continue with Google", color = Slate800, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isRegisterMode) "Already have an account?" else "New student at Career Guru?",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate600
                        )
                        TextButton(onClick = { isRegisterMode = !isRegisterMode }) {
                            Text(
                                text = if (isRegisterMode) "Login Here" else "Register Now",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Gold600
                            )
                        }
                    }
                }

                // Quick Demo Switcher shortcuts for convenience
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Text(
                    text = "Quick Role Access:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Slate600
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SuggestionChip(
                        onClick = {
                            val user = UserProfile(
                                id = "current_user",
                                name = "Sourav Mukherjee",
                                role = UserRole.STUDENT,
                                branch = "Suri, Birbhum",
                                preferredExam = "WBCS"
                            )
                            onLoginSuccess(user)
                        },
                        label = { Text("Student", fontSize = 11.sp) },
                        modifier = Modifier.testTag("quick_login_student")
                    )
                    SuggestionChip(
                        onClick = {
                            val user = UserProfile(
                                id = "current_user",
                                name = "Prof. Anirban Sen (Teacher)",
                                role = UserRole.TEACHER,
                                branch = "Suri, Birbhum",
                                preferredExam = "WBCS"
                            )
                            onLoginSuccess(user)
                        },
                        label = { Text("Teacher", fontSize = 11.sp) },
                        modifier = Modifier.testTag("quick_login_teacher")
                    )
                    SuggestionChip(
                        onClick = {
                            val user = UserProfile(
                                id = "current_user",
                                name = "Director / Admin (Career Guru)",
                                role = UserRole.ADMIN,
                                branch = "Durgapur, West Burdwan",
                                preferredExam = "All Exams"
                            )
                            onLoginSuccess(user)
                        },
                        label = { Text("Admin", fontSize = 11.sp) },
                        modifier = Modifier.testTag("quick_login_admin")
                    )
                }
            }
        }
    }
}
