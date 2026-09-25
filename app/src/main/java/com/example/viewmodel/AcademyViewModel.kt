package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AcademyDatabase
import com.example.data.model.*
import com.example.data.repository.AcademyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AppScreen {
    object Splash : AppScreen()
    object Auth : AppScreen()
    object Main : AppScreen()
    data class CbtExam(val test: MockTest) : AppScreen()
    data class TestResult(val attempt: TestAttempt, val test: MockTest) : AppScreen()
    data class NoteDetail(val note: StudyNote) : AppScreen()
    data class VideoPlayer(val video: RecordedClass) : AppScreen()
    object DoubtsCorner : AppScreen()
    object TeacherAdminPanel : AppScreen()
    object CourseBatches : AppScreen()
    object NotesLibrary : AppScreen()
    object CurrentAffairsFeed : AppScreen()
    object ContactSupport : AppScreen()
}

enum class BottomTab {
    HOME,
    CLASSES,
    TESTS,
    ALERTS,
    PROFILE
}

class AcademyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AcademyRepository

    init {
        val db = AcademyDatabase.getDatabase(application, viewModelScope)
        repository = AcademyRepository(db)
        viewModelScope.launch {
            repository.ensureDataSeeded()
        }
    }

    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = com.example.data.local.SeedData.initialProfile
        )

    val liveClasses: StateFlow<List<LiveClass>> = repository.liveClasses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recordedClasses: StateFlow<List<RecordedClass>> = repository.recordedClasses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studyNotes: StateFlow<List<StudyNote>> = repository.studyNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mockTests: StateFlow<List<MockTest>> = repository.mockTests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val testAttempts: StateFlow<List<TestAttempt>> = repository.testAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentAffairs: StateFlow<List<CurrentAffair>> = repository.currentAffairs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val examAlerts: StateFlow<List<ExamAlert>> = repository.examAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val doubts: StateFlow<List<Doubt>> = repository.doubts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val attendance: StateFlow<List<AttendanceRecord>> = repository.attendance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val courseBatches: StateFlow<List<CourseBatch>> = repository.courseBatches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigation & UI State
    var currentScreen = MutableStateFlow<AppScreen>(AppScreen.Splash)
        private set

    var currentTab = MutableStateFlow(BottomTab.HOME)
        private set

    var selectedExamFilter = MutableStateFlow("All")
        private set

    var selectedLanguage = MutableStateFlow("English") // "English", "বাংলা", "हिंदी"
        private set

    var searchQuery = MutableStateFlow("")
        private set

    // CBT Active Test State
    var activeTest = MutableStateFlow<MockTest?>(null)
        private set
    var activeQuestions = MutableStateFlow<List<TestQuestion>>(emptyList())
        private set
    var currentQuestionIndex = MutableStateFlow(0)
        private set
    var userAnswers = MutableStateFlow<Map<Int, Int?>>(emptyMap())
        private set
    var reviewMarked = MutableStateFlow<Set<Int>>(emptySet())
        private set
    var timeRemainingSeconds = MutableStateFlow(0)
        private set

    private var timerJob: Job? = null

    // AI Doubt Assistant State
    var aiQuery = MutableStateFlow("")
    var aiResponse = MutableStateFlow<String?>(null)
    var isAiLoading = MutableStateFlow(false)

    // Navigation Actions
    fun navigateTo(screen: AppScreen) {
        currentScreen.value = screen
    }

    fun selectTab(tab: BottomTab) {
        currentTab.value = tab
        currentScreen.value = AppScreen.Main
    }

    fun setExamFilter(filter: String) {
        selectedExamFilter.value = filter
    }

    fun setLanguage(lang: String) {
        selectedLanguage.value = lang
    }

    fun setSearch(query: String) {
        searchQuery.value = query
    }

    // Role switching
    fun switchRole(role: UserRole) {
        viewModelScope.launch {
            repository.updateRole(role)
        }
    }

    // Bookmark / Download Actions
    fun toggleNoteBookmark(id: String) {
        viewModelScope.launch { repository.toggleNoteBookmark(id) }
    }

    fun toggleNoteDownload(id: String) {
        viewModelScope.launch { repository.toggleNoteDownloaded(id) }
    }

    fun toggleClassBookmark(id: String) {
        viewModelScope.launch { repository.toggleRecordedClassBookmark(id) }
    }

    fun toggleCurrentAffairBookmark(id: String) {
        viewModelScope.launch { repository.toggleCurrentAffairBookmark(id) }
    }

    fun markNotificationRead(id: String) {
        viewModelScope.launch { repository.markNotificationAsRead(id) }
    }

    fun enrollInCourse(id: String) {
        viewModelScope.launch { repository.enrollBatch(id) }
    }

    // CBT Test Logic
    fun startTest(test: MockTest) {
        viewModelScope.launch {
            val questions = repository.getQuestionsForTest(test.id).ifEmpty {
                com.example.data.local.SeedData.testQuestions
            }
            activeTest.value = test
            activeQuestions.value = questions
            currentQuestionIndex.value = 0
            userAnswers.value = emptyMap()
            reviewMarked.value = emptySet()
            timeRemainingSeconds.value = test.durationMinutes * 60

            timerJob?.cancel()
            timerJob = viewModelScope.launch {
                while (timeRemainingSeconds.value > 0) {
                    delay(1000)
                    timeRemainingSeconds.value -= 1
                }
                submitCurrentTest()
            }

            currentScreen.value = AppScreen.CbtExam(test)
        }
    }

    fun selectAnswer(questionIdx: Int, optionIdx: Int) {
        val current = userAnswers.value.toMutableMap()
        current[questionIdx] = optionIdx
        userAnswers.value = current
    }

    fun clearAnswer(questionIdx: Int) {
        val current = userAnswers.value.toMutableMap()
        current.remove(questionIdx)
        userAnswers.value = current
    }

    fun toggleMarkForReview(questionIdx: Int) {
        val current = reviewMarked.value.toMutableSet()
        if (current.contains(questionIdx)) {
            current.remove(questionIdx)
        } else {
            current.add(questionIdx)
        }
        reviewMarked.value = current
    }

    fun nextQuestion() {
        if (currentQuestionIndex.value < activeQuestions.value.size - 1) {
            currentQuestionIndex.value += 1
        }
    }

    fun prevQuestion() {
        if (currentQuestionIndex.value > 0) {
            currentQuestionIndex.value -= 1
        }
    }

    fun goToQuestion(idx: Int) {
        if (idx in activeQuestions.value.indices) {
            currentQuestionIndex.value = idx
        }
    }

    fun submitCurrentTest() {
        timerJob?.cancel()
        val test = activeTest.value ?: return
        val questions = activeQuestions.value
        val answers = userAnswers.value

        var correct = 0
        var incorrect = 0
        var unattempted = 0

        questions.forEachIndexed { index, q ->
            val userPick = answers[index]
            if (userPick == null) {
                unattempted++
            } else if (userPick == q.correctOptionIndex) {
                correct++
            } else {
                incorrect++
            }
        }

        val total = questions.size.coerceAtLeast(1)
        val score = (correct * 2.0) - (incorrect * 0.5).coerceAtLeast(0.0)
        val accuracy = if (correct + incorrect > 0) ((correct * 100) / (correct + incorrect)) else 0
        val timeSpent = (test.durationMinutes * 60) - timeRemainingSeconds.value

        val attempt = TestAttempt(
            testId = test.id,
            testTitle = test.title,
            examCategory = test.examCategory,
            score = score.coerceAtLeast(0.0),
            maxScore = (total * 2).toDouble(),
            correctCount = correct,
            incorrectCount = incorrect,
            unattemptedCount = unattempted,
            timeSpentSeconds = timeSpent.coerceAtLeast(1),
            accuracyPercentage = accuracy
        )

        viewModelScope.launch {
            repository.submitAttempt(attempt)
            currentScreen.value = AppScreen.TestResult(attempt, test)
        }
    }

    // Doubts & Community
    fun askDoubt(subject: String, question: String) {
        viewModelScope.launch {
            val user = userProfile.value
            repository.submitDoubt(user.name, subject, question)
        }
    }

    fun answerDoubt(doubtId: Long, reply: String) {
        viewModelScope.launch {
            repository.answerDoubt(doubtId, reply, userProfile.value.name)
        }
    }

    // Admin & Teacher Actions
    fun addNewLiveClass(title: String, teacher: String, subject: String, exam: String, time: String) {
        viewModelScope.launch {
            val newClass = LiveClass(
                id = "live_${System.currentTimeMillis()}",
                title = title,
                teacherName = teacher,
                subject = subject,
                examCategory = exam,
                timeLabel = time,
                isLiveNow = true
            )
            repository.addLiveClass(newClass)
        }
    }

    fun addNewNote(title: String, exam: String, subject: String, desc: String, pages: Int) {
        viewModelScope.launch {
            val note = StudyNote(
                id = "note_${System.currentTimeMillis()}",
                title = title,
                examCategory = exam,
                subject = subject,
                description = desc,
                pagesCount = pages,
                fileSize = "${(pages * 0.15).toInt().coerceAtLeast(1)}.2 MB",
                contentPreview = desc
            )
            repository.addStudyNote(note)
        }
    }

    fun addNewExamAlert(name: String, org: String, vacancy: String, applyEnd: String, examDate: String, url: String) {
        viewModelScope.launch {
            val alert = ExamAlert(
                id = "alert_${System.currentTimeMillis()}",
                examName = name,
                organization = org,
                vacancy = vacancy,
                applyStartDate = "Today",
                applyEndDate = applyEnd,
                examDate = examDate,
                eligibility = "Graduate / 10+2 from recognized board",
                officialUrl = url
            )
            repository.addExamAlert(alert)
        }
    }

    fun markAttendanceDirect(studentSubject: String, isPresent: Boolean) {
        viewModelScope.launch {
            repository.markAttendance("Today", studentSubject, isPresent, "Marked by Teacher")
        }
    }

    // AI Assistant
    fun askAiAssistant(query: String) {
        if (query.isBlank()) return
        aiQuery.value = query
        isAiLoading.value = true
        aiResponse.value = null

        viewModelScope.launch {
            delay(1200) // Realistic interactive response
            aiResponse.value = generateEducationalAiExplanation(query)
            isAiLoading.value = false
        }
    }

    private fun generateEducationalAiExplanation(query: String): String {
        val q = query.lowercase()
        return when {
            "wbcs" in q || "prelims" in q ->
                "**WBCS Strategy Guide:**\n1. Focus on English Composition (25 marks) & West Bengal Geography.\n2. Modern History of India & Indian National Movement carries 50 marks alone!\n3. Practice 50 PYQs daily at The Career Guru Academy.\n4. Refer to Birbhum & Suri branch class notes for West Bengal specific schematics."
            "ssc" in q || "math" in q || "algebra" in q ->
                "**SSC Quantitative Aptitude Concept:**\nFor rapid calculation, remember algebraic identities:\n• (a+b+c)² = a² + b² + c² + 2(ab+bc+ca)\n• If a + b + c = 0, then a³ + b³ + c³ = 3abc.\nAlways verify units digit to eliminate at least 2 options!"
            "damodar" in q || "river" in q || "geography" in q ->
                "**West Bengal River Systems:**\n• Damodar originates from Khamarpat hill in Chotanagpur Plateau.\n• Known as 'Sorrow of Bengal' prior to DVC multipurpose project (1948).\n• Important tributaries: Barakar, Konar, Bokaro."
            else ->
                "**The Career Guru Academy AI Solver:**\nRegarding \"$query\":\n• Core Concept: Reviewed against competitive exam syllabus (WBCS / SSC / RRB).\n• Key Fact: Pay attention to recent 5-year PYQ patterns and West Bengal PSC question trends.\n• Pro Tip: Review the corresponding chapter in the Notes section or ask in the Teacher Doubt Corner for personalized mentorship!"
        }
    }
}
