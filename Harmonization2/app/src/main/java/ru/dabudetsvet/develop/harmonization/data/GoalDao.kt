package ru.dabudetsvet.develop.harmonization.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Insert
    suspend fun insert(goal: GoalEntity): Long

    @Update
    suspend fun update(goal: GoalEntity)

    @Query("SELECT * FROM goals WHERE status = 'ACTIVE' ORDER BY slotIndex ASC")
    fun observeActiveGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE status = 'ACTIVE' ORDER BY slotIndex ASC")
    suspend fun getActiveGoals(): List<GoalEntity>

    @Query("SELECT * FROM goals WHERE status = 'COMPLETED' ORDER BY completedAt DESC")
    fun observeCompletedGoals(): Flow<List<GoalEntity>>

    @Query("SELECT title FROM goals ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecentGoalTitles(limit: Int): List<String>

    @Query("SELECT * FROM goals WHERE id = :id")
    suspend fun getById(id: Long): GoalEntity?
}
