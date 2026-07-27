package ru.dabudetsvet.develop.harmonization.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GoalStatus { ACTIVE, COMPLETED }

/**
 * A "wish/goal" card occupying one of the 4 active slots, similar to the
 * wish slots from The Sims: it targets a sphere, and completing it frees
 * the slot for a new goal picked in favor of the currently weakest spheres.
 */
@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sphere: String,
    val title: String,
    val description: String,
    val status: GoalStatus,
    val slotIndex: Int,
    val createdAt: Long,
    val completedAt: Long? = null
)
