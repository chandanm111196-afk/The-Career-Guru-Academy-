package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    STUDENT,
    TEACHER,
    ADMIN
}

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: String = "current_user",
    val name: String = "Sourav Mukherjee",
    val mobile: String = "9093200422",
    val email: String = "sourav.wbcs@careerguru.in",
    val role: UserRole = UserRole.STUDENT,
    val branch: String = "Suri, Birbhum", // "Suri, Birbhum" or "Durgapur, West Burdwan"
    val preferredExam: String = "WBCS",
    val streakDays: Int = 7,
    val xpPoints: Int = 1250,
    val avatarRes: String = "avatar_student",
    val isAnonymousOnLeaderboard: Boolean = false
)

@Entity(tableName = "live_classes")
data class LiveClass(
    @PrimaryKey val id: String,
    val title: String,
    val teacherName: String,
    val subject: String,
    val examCategory: String, // WBCS, PSC, SSC, RRB, BANKING, TET
    val timeLabel: String, // "Today — 7:00 PM"
    val isLiveNow: Boolean,
    val meetingUrl: String = "https://meet.careerguru.in/live",
    val durationMinutes: Int = 60,
    val date: String = "25 Sep 2026",
    val viewersCount: Int = 142
)

@Entity(tableName = "recorded_classes")
data class RecordedClass(
    @PrimaryKey val id: String,
    val title: String,
    val teacherName: String,
    val subject: String,
    val examCategory: String,
    val chapter: String,
    val duration: String, // "48 mins"
    val videoUrl: String,
    val isBookmarked: Boolean = false,
    val viewsCount: Int = 520,
    val dateUploaded: String = "24 Sep 2026"
)

@Entity(tableName = "study_notes")
data class StudyNote(
    @PrimaryKey val id: String,
    val title: String,
    val examCategory: String,
    val subject: String,
    val description: String,
    val pagesCount: Int,
    val fileSize: String,
    val isBookmarked: Boolean = false,
    val isDownloaded: Boolean = false,
    val isFavorite: Boolean = false,
    val dateAdded: String = "22 Sep 2026",
    val contentPreview: String = ""
)

@Entity(tableName = "mock_tests")
data class MockTest(
    @PrimaryKey val id: String,
    val title: String,
    val examCategory: String,
    val testType: String, // "Full Mock", "Daily Quiz", "Subject Test", "PYQ"
    val questionsCount: Int,
    val durationMinutes: Int,
    val totalMarks: Int,
    val difficulty: String = "Moderate",
    val attemptsCount: Int = 340,
    val averageScore: Double = 68.5
)

@Entity(tableName = "test_questions")
data class TestQuestion(
    @PrimaryKey val id: String,
    val testId: String,
    val questionNumber: Int,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOptionIndex: Int, // 0 to 3
    val explanation: String,
    val subject: String
)

@Entity(tableName = "test_attempts")
data class TestAttempt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val testId: String,
    val testTitle: String,
    val examCategory: String,
    val score: Double,
    val maxScore: Double,
    val correctCount: Int,
    val incorrectCount: Int,
    val unattemptedCount: Int,
    val timeSpentSeconds: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val accuracyPercentage: Int
)

@Entity(tableName = "current_affairs")
data class CurrentAffair(
    @PrimaryKey val id: String,
    val title: String,
    val date: String,
    val category: String, // National, International, West Bengal, Economy, Banking, Sports, Science, Awards
    val summary: String,
    val fullDetails: String,
    val isBookmarked: Boolean = false
)

@Entity(tableName = "exam_alerts")
data class ExamAlert(
    @PrimaryKey val id: String,
    val examName: String,
    val organization: String,
    val vacancy: String,
    val applyStartDate: String,
    val applyEndDate: String,
    val examDate: String,
    val eligibility: String,
    val officialUrl: String,
    val statusBadge: String = "Application Open"
)

@Entity(tableName = "doubts")
data class Doubt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentName: String,
    val subject: String,
    val questionText: String,
    val status: String = "Pending", // "Pending" or "Answered"
    val replyText: String = "",
    val teacherName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "attendance")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val subject: String,
    val isPresent: Boolean,
    val remarks: String = "Regular Class"
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val category: String, // Class, Note, Mock, Alert, Announcement
    val timestamp: String,
    val isRead: Boolean = false
)

@Entity(tableName = "course_batches")
data class CourseBatch(
    @PrimaryKey val id: String,
    val title: String,
    val exam: String,
    val duration: String,
    val faculty: String,
    val price: String,
    val originalPrice: String,
    val isEnrolled: Boolean = false,
    val rating: Double = 4.9,
    val lecturesCount: Int = 120
)
