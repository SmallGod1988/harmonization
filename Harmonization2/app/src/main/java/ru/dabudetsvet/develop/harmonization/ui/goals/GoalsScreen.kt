package ru.dabudetsvet.develop.harmonization.ui.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.dabudetsvet.develop.harmonization.data.GoalEntity
import ru.dabudetsvet.develop.harmonization.data.Sphere
import ru.dabudetsvet.develop.harmonization.ui.harmonizationViewModel

@Composable
fun GoalsScreen(modifier: Modifier = Modifier) {
    val viewModel = harmonizationViewModel { repo -> GoalsViewModel(repo) }
    val goals by viewModel.activeGoals.collectAsStateWithLifecycle()
    val hasAssessment by viewModel.hasAssessment.collectAsStateWithLifecycle()

    if (!hasAssessment) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Text(
                    text = "Целей пока нет",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Сначала оцени своё колесо гармонии на вкладке «Колесо» — " +
                        "и здесь появятся 4 цели под твои самые слабые сферы.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        return
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Твои цели",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(goals, key = { it.id }) { goal ->
                GoalCard(goal = goal, onComplete = { viewModel.completeGoal(goal.id) })
            }
        }
    }
}

@Composable
private fun GoalCard(goal: GoalEntity, onComplete: () -> Unit) {
    val sphere = runCatching { Sphere.valueOf(goal.sphere) }.getOrNull()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            sphere?.let {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Text(
                text = goal.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = goal.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )
            Button(onClick = onComplete, modifier = Modifier.fillMaxWidth()) {
                Text("Выполнено")
            }
        }
    }
}
