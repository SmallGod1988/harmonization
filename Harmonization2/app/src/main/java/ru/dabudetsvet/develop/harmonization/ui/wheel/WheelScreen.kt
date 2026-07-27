package ru.dabudetsvet.develop.harmonization.ui.wheel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.dabudetsvet.develop.harmonization.data.Sphere
import ru.dabudetsvet.develop.harmonization.ui.components.WheelChart
import ru.dabudetsvet.develop.harmonization.ui.harmonizationViewModel
import kotlin.math.roundToInt

@Composable
fun WheelScreen(modifier: Modifier = Modifier) {
    val viewModel = harmonizationViewModel { repo -> WheelViewModel(repo) }
    val draft by viewModel.draft.collectAsStateWithLifecycle()
    val justSaved by viewModel.justSaved.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(justSaved) {
        if (justSaved) {
            snackbarHostState.showSnackbar("Оценка сохранена. Цели обновлены.")
            viewModel.acknowledgeSaved()
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Колесо гармонии",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Оцени, насколько ты удовлетворён каждой сферой жизни сейчас.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )
                WheelChart(
                    scores = draft,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            items(Sphere.ORDERED) { sphere ->
                val score = draft[sphere] ?: 5
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = sphere.title, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "$score / 10", style = MaterialTheme.typography.bodyLarge)
                    }
                    Slider(
                        value = score.toFloat(),
                        onValueChange = { viewModel.updateScore(sphere, it.roundToInt()) },
                        valueRange = 0f..10f,
                        steps = 9
                    )
                }
            }

            item {
                Button(
                    onClick = { viewModel.saveAssessment() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить оценку")
                }
            }
        }
        SnackbarHost(hostState = snackbarHostState)
    }
}
