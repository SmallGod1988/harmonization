package ru.dabudetsvet.develop.harmonization.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

data class AssessmentWithScoreEntities(
    @Embedded val assessment: AssessmentEntity,
    @Relation(parentColumn = "id", entityColumn = "assessmentId")
    val scores: List<SphereScoreEntity>
)

@Dao
interface AssessmentDao {

    @Insert
    suspend fun insertAssessment(assessment: AssessmentEntity): Long

    @Insert
    suspend fun insertScores(scores: List<SphereScoreEntity>)

    @Transaction
    @Query("SELECT * FROM assessments ORDER BY timestamp DESC")
    fun observeAllWithScores(): Flow<List<AssessmentWithScoreEntities>>

    @Transaction
    @Query("SELECT * FROM assessments ORDER BY timestamp DESC LIMIT 1")
    fun observeLatestWithScores(): Flow<AssessmentWithScoreEntities?>

    @Transaction
    @Query("SELECT * FROM assessments ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestWithScores(): AssessmentWithScoreEntities?
}
