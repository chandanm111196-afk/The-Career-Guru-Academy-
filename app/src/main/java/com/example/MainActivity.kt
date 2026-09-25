package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AcademyBottomNavigationBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AcademyViewModel
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BottomTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CareerGuruApp()
            }
        }
    }
}

@Composable
fun CareerGuruApp(
    viewModel: AcademyViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val unreadNotifsCount = remember(notifications) { notifications.count { !it.isRead } }

    when (val screen = currentScreen) {
        is AppScreen.Splash -> {
            SplashScreen(
                onNavigateNext = { viewModel.navigateTo(AppScreen.Main) }
            )
        }
        is AppScreen.Auth -> {
            AuthScreen(
                onLoginSuccess = { profile ->
                    viewModel.navigateTo(AppScreen.Main)
                }
            )
        }
        is AppScreen.CbtExam -> {
            CbtTestScreen(
                viewModel = viewModel,
                test = screen.test,
                onCancel = { viewModel.navigateTo(AppScreen.Main) }
            )
        }
        is AppScreen.TestResult -> {
            TestResultScreen(
                viewModel = viewModel,
                attempt = screen.attempt,
                test = screen.test,
                onBackToHome = { viewModel.navigateTo(AppScreen.Main) }
            )
        }
        is AppScreen.NoteDetail -> {
            NoteDetailScreen(
                viewModel = viewModel,
                note = screen.note,
                onBack = { viewModel.navigateTo(AppScreen.Main) }
            )
        }
        is AppScreen.VideoPlayer -> {
            VideoPlayerScreen(
                viewModel = viewModel,
                video = screen.video,
                onBack = { viewModel.navigateTo(AppScreen.Main) }
            )
        }
        is AppScreen.DoubtsCorner -> {
            BackHandler { viewModel.navigateTo(AppScreen.Main) }
            DoubtScreen(
                viewModel = viewModel,
                onBack = { viewModel.navigateTo(AppScreen.Main) }
            )
        }
        is AppScreen.TeacherAdminPanel -> {
            BackHandler { viewModel.navigateTo(AppScreen.Main) }
            TeacherAdminScreen(
                viewModel = viewModel,
                onBack = { viewModel.navigateTo(AppScreen.Main) }
            )
        }
        is AppScreen.CourseBatches -> {
            BackHandler { viewModel.navigateTo(AppScreen.Main) }
            CourseBatchesScreen(
                viewModel = viewModel,
                onBack = { viewModel.navigateTo(AppScreen.Main) }
            )
        }
        is AppScreen.NotesLibrary -> {
            BackHandler { viewModel.navigateTo(AppScreen.Main) }
            NotesScreen(
                viewModel = viewModel,
                onOpenNote = { viewModel.navigateTo(AppScreen.NoteDetail(it)) }
            )
        }
        is AppScreen.CurrentAffairsFeed -> {
            BackHandler { viewModel.navigateTo(AppScreen.Main) }
            CurrentAffairsScreen(
                viewModel = viewModel,
                onBack = { viewModel.navigateTo(AppScreen.Main) }
            )
        }
        is AppScreen.ContactSupport -> {
            BackHandler { viewModel.navigateTo(AppScreen.Main) }
            ContactSupportScreen(
                onBack = { viewModel.navigateTo(AppScreen.Main) }
            )
        }
        is AppScreen.Main -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    AcademyBottomNavigationBar(
                        currentTab = currentTab,
                        onTabSelected = { viewModel.selectTab(it) },
                        unreadNotifsCount = unreadNotifsCount
                    )
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (currentTab) {
                        BottomTab.HOME -> {
                            HomeScreen(
                                viewModel = viewModel,
                                userProfile = userProfile,
                                onNavigateToClasses = { viewModel.selectTab(BottomTab.CLASSES) },
                                onNavigateToTests = { viewModel.selectTab(BottomTab.TESTS) },
                                onNavigateToNotes = { viewModel.navigateTo(AppScreen.NotesLibrary) },
                                onNavigateToAlerts = { viewModel.selectTab(BottomTab.ALERTS) },
                                onNavigateToProgress = { viewModel.selectTab(BottomTab.PROFILE) },
                                onNavigateToDoubts = { viewModel.navigateTo(AppScreen.DoubtsCorner) },
                                onNavigateToCurrentAffairs = { viewModel.navigateTo(AppScreen.CurrentAffairsFeed) },
                                onNavigateToContact = { viewModel.navigateTo(AppScreen.ContactSupport) },
                                onStartTest = { viewModel.startTest(it) },
                                onOpenNote = { viewModel.navigateTo(AppScreen.NoteDetail(it)) }
                            )
                        }
                        BottomTab.CLASSES -> {
                            ClassesScreen(
                                viewModel = viewModel,
                                onOpenRecordedClass = { viewModel.navigateTo(AppScreen.VideoPlayer(it)) }
                            )
                        }
                        BottomTab.TESTS -> {
                            TestsScreen(
                                viewModel = viewModel,
                                onStartTest = { viewModel.startTest(it) }
                            )
                        }
                        BottomTab.ALERTS -> {
                            AlertsScreen(
                                viewModel = viewModel
                            )
                        }
                        BottomTab.PROFILE -> {
                            ProfileScreen(
                                viewModel = viewModel,
                                userProfile = userProfile,
                                onNavigateToBatches = { viewModel.navigateTo(AppScreen.CourseBatches) },
                                onNavigateToAdmin = { viewModel.navigateTo(AppScreen.TeacherAdminPanel) },
                                onNavigateToContact = { viewModel.navigateTo(AppScreen.ContactSupport) },
                                onLogout = { viewModel.navigateTo(AppScreen.Auth) }
                            )
                        }
                    }
                }
            }
        }
    }
}
