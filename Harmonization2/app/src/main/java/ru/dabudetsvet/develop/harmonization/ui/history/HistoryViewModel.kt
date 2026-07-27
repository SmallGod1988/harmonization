package ru.dabudetsvet.develop.harmonization.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.dabudetsvet.develop.harmonization.data.AssessmentWithScores
import ru.dabudetsvet.develop.harmonization.data.HarmonizationRepository

class HistoryViewModel(repository: HarmonizationRepository) : ViewModel() {

    val history: StateFlow<List<AssessmentWithScores>> = repository.observeHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
