package com.example.data.repository

import com.example.data.local.AcademyDatabase
import com.example.data.local.SeedData
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class AcademyRepository(private val database: AcademyDatabase) {

    val userProfile: Flow<UserProfile?> = database.userProfileDao().getUserProfile()
    val liveClasses: Flow<List<LiveClass>> = database.classroomDao().getAllLiveClasses()
    val recordedClasses: Flow<List<RecordedClass>> = database.classroomDao().getAllRecordedClasses()
    val studyNotes: Flow<List<StudyNote>> = database.studyNotesDao().getAllNotes()
    val mockTests: Flow<List<MockTest>> = database.mockTestDao().getAllMockTests()
    val testAttempts: Flow<List<TestAttempt>> = database.mockTestDao().getAllAttempts()
    val currentAffairs: Flow<List<CurrentAffair>> = database.communityDao().getAllCurrentAffairs()
    val examAlerts: Flow<List<ExamAlert>> = database.communityDao().getAllExamAlerts()
    val doubts: Flow<List<Doubt>> = database.communityDao().getAllDoubts()
    val attendance: Flow<List<AttendanceRecord>> = database.communityDao().getAllAttendance()
    val notifications: Flow<List<NotificationItem>> = database.communityDao().getAllNotifications()
    val courseBatches: Flow<List<CourseBatch>> = database.communityDao().getAllBatches()

    suspend fun ensureDataSeeded() = withContext(Dispatchers.IO) {
        val existing = database.userProfileDao().getUserProfile().firstOrNull()
        if (existing == null) {
            database.userProfileDao().insertOrUpdateProfile(SeedData.initialProfile)
            database.classroomDao().insertLiveClasses(SeedData.liveClasses)
            database.classroomDao().insertRecordedClasses(SeedData.recordedClasses)
            database.studyNotesDao().insertNotes(SeedData.studyNotes)
            database.mockTestDao().insertMockTests(SeedData.mockTests)
            database.mockTestDao().insertQuestions(SeedData.testQuestions)
            database.communityDao().insertCurrentAffairs(SeedData.currentAffairs)
            database.communityDao().insertExamAlerts(SeedData.examAlerts)
            database.communityDao().insertNotifications(SeedData.initialNotifications)
            database.communityDao().insertAttendanceList(SeedData.initialAttendance)
            database.communityDao().insertBatches(SeedData.courseBatches)
            for (d in SeedData.initialDoubts) {
                database.communityDao().insertDoubt(d)
            }
        }
    }

    suspend fun getQuestionsForTest(testId: String): List<TestQuestion> = withContext(Dispatchers.IO) {
        database.mockTestDao().getQuestionsForTest(testId)
    }

    suspend fun submitAttempt(attempt: TestAttempt) = withContext(Dispatchers.IO) {
        database.mockTestDao().insertAttempt(attempt)
        database.userProfileDao().addXp((attempt.score * 10).toInt().coerceAtLeast(10))
    }

    suspend fun toggleNoteBookmark(noteId: String) = withContext(Dispatchers.IO) {
        database.studyNotesDao().toggleBookmark(noteId)
    }

    suspend fun toggleNoteDownloaded(noteId: String) = withContext(Dispatchers.IO) {
        database.studyNotesDao().toggleDownloaded(noteId)
    }

    suspend fun toggleRecordedClassBookmark(classId: String) = withContext(Dispatchers.IO) {
        database.classroomDao().toggleRecordedClassBookmark(classId)
    }

    suspend fun toggleCurrentAffairBookmark(id: String) = withContext(Dispatchers.IO) {
        database.communityDao().toggleCurrentAffairBookmark(id)
    }

    suspend fun submitDoubt(studentName: String, subject: String, question: String) = withContext(Dispatchers.IO) {
        val doubt = Doubt(
            studentName = studentName,
            subject = subject,
            questionText = question,
            status = "Pending"
        )
        database.communityDao().insertDoubt(doubt)
    }

    suspend fun answerDoubt(doubtId: Long, answer: String, teacherName: String) = withContext(Dispatchers.IO) {
        database.communityDao().answerDoubt(doubtId, answer, teacherName)
    }

    suspend fun updateRole(newRole: UserRole) = withContext(Dispatchers.IO) {
        database.userProfileDao().updateRole(newRole)
    }

    suspend fun updatePreferredExam(exam: String) = withContext(Dispatchers.IO) {
        database.userProfileDao().updatePreferredExam(exam)
    }

    suspend fun updateProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        database.userProfileDao().insertOrUpdateProfile(profile)
    }

    suspend fun markNotificationAsRead(id: String) = withContext(Dispatchers.IO) {
        database.communityDao().markNotificationRead(id)
    }

    suspend fun enrollBatch(batchId: String) = withContext(Dispatchers.IO) {
        database.communityDao().enrollInBatch(batchId)
    }

    suspend fun addLiveClass(liveClass: LiveClass) = withContext(Dispatchers.IO) {
        database.classroomDao().insertSingleLiveClass(liveClass)
    }

    suspend fun addStudyNote(note: StudyNote) = withContext(Dispatchers.IO) {
        database.studyNotesDao().insertSingleNote(note)
    }

    suspend fun addExamAlert(alert: ExamAlert) = withContext(Dispatchers.IO) {
        database.communityDao().insertSingleAlert(alert)
    }

    suspend fun markAttendance(date: String, subject: String, isPresent: Boolean, remarks: String) = withContext(Dispatchers.IO) {
        val record = AttendanceRecord(
            date = date,
            subject = subject,
            isPresent = isPresent,
            remarks = remarks
        )
        database.communityDao().insertAttendance(record)
    }
}
