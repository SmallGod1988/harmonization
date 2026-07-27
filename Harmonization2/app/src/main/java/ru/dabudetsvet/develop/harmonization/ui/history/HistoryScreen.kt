package ru.dabudetsvet.develop.harmonization.ui.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.dabudetsvet.develop.harmonization.data.AssessmentWithScores
import ru.dabudetsvet.develop.harmonization.ui.components.WheelChart
import ru.dabudetsvet.develop.harmonization.ui.harmonizationViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
    val viewModel = harmonizationViewModel { repo -> HistoryViewModel(repo) }
    val history by viewModel.history.collectAsStateWithLifecycle()

    if (history.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Здесь появится история твоих оценок колеса гармонии.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(32.dp)
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "История оценок",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        items(history, key = { it.assessment.id }) { entry ->
            HistoryRow(entry)
        }
    }
}

@Composable
private fun HistoryRow(entry: AssessmentWithScores) {
    val dateFormat = remember { SimpleDateFormat("d MMMM yyyy, HH:mm", Locale("ru")) }

    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WheelChart(
                scores = entry.scores,
                showLabels = false,
                modifier = Modifier.size(72.dp)
            )
            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(
                    text = dateFormat.format(Date(entry.assessment.timestamp)),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Средний балл: ${(entry.average * 10).roundToInt() / 10.0}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
