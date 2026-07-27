package ru.dabudetsvet.develop.harmonization.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.dabudetsvet.develop.harmonization.data.GoalEntity
import ru.dabudetsvet.develop.harmonization.data.HarmonizationRepository

class GoalsViewModel(private val repository: HarmonizationRepository) : ViewModel() {

    val activeGoals: StateFlow<List<GoalEntity>> = repository.observeActiveGoals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val hasAssessment: StateFlow<Boolean> = repository.observeLatestAssessment()
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun completeGoal(goalId: Long) {
        viewModelScope.launch { repository.completeGoal(goalId) }
    }
}
