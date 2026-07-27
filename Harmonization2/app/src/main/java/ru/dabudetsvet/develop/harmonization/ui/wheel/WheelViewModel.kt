package ru.dabudetsvet.develop.harmonization.ui.wheel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.dabudetsvet.develop.harmonization.data.HarmonizationRepository
import ru.dabudetsvet.develop.harmonization.data.Sphere

class WheelViewModel(private val repository: HarmonizationRepository) : ViewModel() {

    private val defaultScores: Map<Sphere, Int> = Sphere.ORDERED.associateWith { 5 }

    private val _draft = MutableStateFlow(defaultScores)
    val draft: StateFlow<Map<Sphere, Int>> = _draft.asStateFlow()

    private val _justSaved = MutableStateFlow(false)
    val justSaved: StateFlow<Boolean> = _justSaved.asStateFlow()

    private var seededFromHistory = false

    init {
        viewModelScope.launch {
            repository.observeLatestAssessment().collect { latest ->
                if (!seededFromHistory && latest != null) {
                    _draft.value = defaultScores + latest.scores
                    seededFromHistory = true
                }
            }
        }
    }

    fun updateScore(sphere: Sphere, score: Int) {
        _draft.update { it + (sphere to score) }
        _justSaved.value = false
    }

    fun saveAssessment() {
        viewModelScope.launch {
            repository.saveAssessment(_draft.value)
            _justSaved.value = true
        }
    }

    fun acknowledgeSaved() {
        _justSaved.value = false
    }
}
