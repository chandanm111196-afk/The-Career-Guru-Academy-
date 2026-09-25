package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 'current_user' LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Query("UPDATE user_profile SET role = :newRole WHERE id = 'current_user'")
    suspend fun updateRole(newRole: UserRole)

    @Query("UPDATE user_profile SET preferredExam = :exam WHERE id = 'current_user'")
    suspend fun updatePreferredExam(exam: String)

    @Query("UPDATE user_profile SET xpPoints = xpPoints + :points WHERE id = 'current_user'")
    suspend fun addXp(points: Int)
}

@Dao
interface ClassroomDao {
    @Query("SELECT * FROM live_classes ORDER BY isLiveNow DESC, id ASC")
    fun getAllLiveClasses(): Flow<List<LiveClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveClasses(classes: List<LiveClass>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleLiveClass(liveClass: LiveClass)

    @Query("SELECT * FROM recorded_classes ORDER BY dateUploaded DESC")
    fun getAllRecordedClasses(): Flow<List<RecordedClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecordedClasses(classes: List<RecordedClass>)

    @Query("UPDATE recorded_classes SET isBookmarked = NOT isBookmarked WHERE id = :classId")
    suspend fun toggleRecordedClassBookmark(classId: String)
}

@Dao
interface StudyNotesDao {
    @Query("SELECT * FROM study_notes ORDER BY dateAdded DESC")
    fun getAllNotes(): Flow<List<StudyNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<StudyNote>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleNote(note: StudyNote)

    @Query("UPDATE study_notes SET isBookmarked = NOT isBookmarked WHERE id = :noteId")
    suspend fun toggleBookmark(noteId: String)

    @Query("UPDATE study_notes SET isDownloaded = NOT isDownloaded WHERE id = :noteId")
    suspend fun toggleDownloaded(noteId: String)
}

@Dao
interface MockTestDao {
    @Query("SELECT * FROM mock_tests")
    fun getAllMockTests(): Flow<List<MockTest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMockTests(tests: List<MockTest>)

    @Query("SELECT * FROM test_questions WHERE testId = :testId ORDER BY questionNumber ASC")
    suspend fun getQuestionsForTest(testId: String): List<TestQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<TestQuestion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: TestAttempt)

    @Query("SELECT * FROM test_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<TestAttempt>>
}

@Dao
interface CommunityDao {
    @Query("SELECT * FROM current_affairs ORDER BY date DESC")
    fun getAllCurrentAffairs(): Flow<List<CurrentAffair>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentAffairs(list: List<CurrentAffair>)

    @Query("UPDATE current_affairs SET isBookmarked = NOT isBookmarked WHERE id = :id")
    suspend fun toggleCurrentAffairBookmark(id: String)

    @Query("SELECT * FROM exam_alerts ORDER BY id ASC")
    fun getAllExamAlerts(): Flow<List<ExamAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamAlerts(alerts: List<ExamAlert>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleAlert(alert: ExamAlert)

    @Query("SELECT * FROM doubts ORDER BY timestamp DESC")
    fun getAllDoubts(): Flow<List<Doubt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoubt(doubt: Doubt)

    @Query("UPDATE doubts SET status = 'Answered', replyText = :reply, teacherName = :teacher WHERE id = :doubtId")
    suspend fun answerDoubt(doubtId: Long, reply: String, teacher: String)

    @Query("SELECT * FROM attendance ORDER BY id DESC")
    fun getAllAttendance(): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(record: AttendanceRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceList(records: List<AttendanceRecord>)

    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationItem>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationRead(id: String)

    @Query("SELECT * FROM course_batches")
    fun getAllBatches(): Flow<List<CourseBatch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatches(batches: List<CourseBatch>)

    @Query("UPDATE course_batches SET isEnrolled = 1 WHERE id = :batchId")
    suspend fun enrollInBatch(batchId: String)
}
