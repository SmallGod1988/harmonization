package ru.dabudetsvet.develop.harmonization.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [AssessmentEntity::class, SphereScoreEntity::class, GoalEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HarmonizationDatabase : RoomDatabase() {

    abstract fun assessmentDao(): AssessmentDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var instance: HarmonizationDatabase? = null

        fun getInstance(context: Context): HarmonizationDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    HarmonizationDatabase::class.java,
                    "harmonization.db"
                ).build().also { instance = it }
            }
    }
}
