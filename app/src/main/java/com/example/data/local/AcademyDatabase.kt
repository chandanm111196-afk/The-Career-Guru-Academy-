package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfile::class,
        LiveClass::class,
        RecordedClass::class,
        StudyNote::class,
        MockTest::class,
        TestQuestion::class,
        TestAttempt::class,
        CurrentAffair::class,
        ExamAlert::class,
        Doubt::class,
        AttendanceRecord::class,
        NotificationItem::class,
        CourseBatch::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AcademyDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun classroomDao(): ClassroomDao
    abstract fun studyNotesDao(): StudyNotesDao
    abstract fun mockTestDao(): MockTestDao
    abstract fun communityDao(): CommunityDao

    companion object {
        @Volatile
        private var INSTANCE: AcademyDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AcademyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AcademyDatabase::class.java,
                    "career_guru_academy_db"
                )
                    .addCallback(AcademyDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AcademyDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(db: AcademyDatabase) {
            db.userProfileDao().insertOrUpdateProfile(SeedData.initialProfile)
            db.classroomDao().insertLiveClasses(SeedData.liveClasses)
            db.classroomDao().insertRecordedClasses(SeedData.recordedClasses)
            db.studyNotesDao().insertNotes(SeedData.studyNotes)
            db.mockTestDao().insertMockTests(SeedData.mockTests)
            db.mockTestDao().insertQuestions(SeedData.testQuestions)
            db.communityDao().insertCurrentAffairs(SeedData.currentAffairs)
            db.communityDao().insertExamAlerts(SeedData.examAlerts)
            db.communityDao().insertNotifications(SeedData.initialNotifications)
            db.communityDao().insertAttendanceList(SeedData.initialAttendance)
            db.communityDao().insertBatches(SeedData.courseBatches)
            for (doubt in SeedData.initialDoubts) {
                db.communityDao().insertDoubt(doubt)
            }
        }
    }
}
