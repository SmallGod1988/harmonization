package ru.dabudetsvet.develop.harmonization.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

/**
 * Coordinates the wheel-of-harmony assessments and the 4 active goal slots.
 * After every new assessment (or whenever a goal is completed) the empty
 * slots are refilled with goals picked at random, weighted toward the
 * spheres that currently score the lowest.
 */
class HarmonizationRepository(private val database: HarmonizationDatabase) {

    private val assessmentDao = database.assessmentDao()
    private val goalDao = database.goalDao()

    fun observeLatestAssessment(): Flow<AssessmentWithScores?> =
        assessmentDao.observeLatestWithScores().map { it?.let(::toDomain) }

    fun observeHistory(): Flow<List<AssessmentWithScores>> =
        assessmentDao.observeAllWithScores().map { list -> list.map(::toDomain) }

    fun observeActiveGoals(): Flow<List<GoalEntity>> = goalDao.observeActiveGoals()

    fun observeCompletedGoals(): Flow<List<GoalEntity>> = goalDao.observeCompletedGoals()

    suspend fun saveAssessment(scores: Map<Sphere, Int>) {
        val assessmentId = assessmentDao.insertAssessment(
            AssessmentEntity(timestamp = System.currentTimeMillis())
        )
        assessmentDao.insertScores(
            scores.map { (sphere, score) ->
                SphereScoreEntity(assessmentId = assessmentId, sphere = sphere.name, score = score)
            }
        )
        fillEmptySlots(scores)
    }

    suspend fun completeGoal(goalId: Long) {
        val goal = goalDao.getById(goalId) ?: return
        goalDao.update(goal.copy(status = GoalStatus.COMPLETED, completedAt = System.currentTimeMillis()))
        val latestScores = assessmentDao.getLatestWithScores()?.let { toDomain(it).scores } ?: emptyMap()
        fillEmptySlots(latestScores)
    }

    private suspend fun fillEmptySlots(scores: Map<Sphere, Int>) {
        val active = goalDao.getActiveGoals()
        val usedSlots = active.map { it.slotIndex }.toSet()
        val usedSpheres = active.map { Sphere.valueOf(it.sphere) }.toMutableSet()
        val freeSlots = (0 until SLOT_COUNT).filterNot { it in usedSlots }

        for (slot in freeSlots) {
            val sphere = pickWeightedSphere(scores, usedSpheres)
            usedSpheres += sphere
            val template = pickTemplate(sphere)
            goalDao.insert(
                GoalEntity(
                    sphere = sphere.name,
                    title = template.title,
                    description = template.description,
                    status = GoalStatus.ACTIVE,
                    slotIndex = slot,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    private suspend fun pickTemplate(sphere: Sphere): GoalTemplate {
        val recentTitles = goalDao.getRecentGoalTitles(RECENT_HISTORY_SIZE).toSet()
        val templates = GoalTemplates.templatesFor(sphere)
        val fresh = templates.filter { it.title !in recentTitles }
        return fresh.ifEmpty { templates }.random()
    }

    private fun pickWeightedSphere(scores: Map<Sphere, Int>, excludeSpheres: Set<Sphere>): Sphere {
        val candidates = Sphere.ORDERED.filterNot { it in excludeSpheres }.ifEmpty { Sphere.ORDERED }
        // Lower satisfaction score => higher chance of being picked (min weight 1).
        val weights = candidates.map { sphere -> (11 - (scores[sphere] ?: 5)).coerceAtLeast(1) }
        val total = weights.sum()
        var roll = Random.nextInt(total)
        candidates.forEachIndexed { index, sphere ->
            roll -= weights[index]
            if (roll < 0) return sphere
        }
        return candidates.last()
    }

    private fun toDomain(entity: AssessmentWithScoreEntities): AssessmentWithScores {
        val scores = entity.scores.associate { Sphere.valueOf(it.sphere) to it.score }
        return AssessmentWithScores(entity.assessment, scores)
    }

    companion object {
        const val SLOT_COUNT = 4
        private const val RECENT_HISTORY_SIZE = 16
    }
}
