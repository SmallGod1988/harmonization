package ru.dabudetsvet.develop.harmonization.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One "wheel of harmony" snapshot: the moment the user rated all 8 spheres.
 */
@Entity(tableName = "assessments")
data class AssessmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long
)

/**
 * A single sphere's score (0..10) within one assessment snapshot.
 */
@Entity(tableName = "sphere_scores")
data class SphereScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val assessmentId: Long,
    val sphere: String,
    val score: Int
)

data class AssessmentWithScores(
    val assessment: AssessmentEntity,
    val scores: Map<Sphere, Int>
) {
    val average: Double
        get() = if (scores.isEmpty()) 0.0 else scores.values.average()
}
