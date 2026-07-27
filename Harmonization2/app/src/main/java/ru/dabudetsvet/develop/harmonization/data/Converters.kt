package ru.dabudetsvet.develop.harmonization.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromGoalStatus(status: GoalStatus): String = status.name

    @TypeConverter
    fun toGoalStatus(value: String): GoalStatus = GoalStatus.valueOf(value)
}
