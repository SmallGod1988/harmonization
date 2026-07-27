package ru.dabudetsvet.develop.harmonization.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.dabudetsvet.develop.harmonization.data.Sphere
import kotlin.math.cos
import kotlin.math.sin

/**
 * Draws the "wheel of harmony": one axis per life sphere, filled according
 * to its current satisfaction score (0..maxScore).
 */
@Composable
fun WheelChart(
    scores: Map<Sphere, Int>,
    modifier: Modifier = Modifier,
    maxScore: Int = 10,
    showLabels: Boolean = true,
    fillColor: Color = MaterialTheme.colorScheme.primary,
    gridColor: Color = MaterialTheme.colorScheme.outlineVariant,
    labelColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val spheres = Sphere.ORDERED

    Canvas(modifier = modifier.aspectRatio(1f)) {
        val gridStroke = Stroke(width = 1.dp.toPx())
        val scoreStroke = Stroke(width = 2.dp.toPx())
        val center = Offset(size.width / 2f, size.height / 2f)
        val labelReserve = if (showLabels) size.minDimension * 0.18f else size.minDimension * 0.04f
        val radius = size.minDimension / 2f - labelReserve
        val angleStep = (2f * Math.PI / spheres.size).toFloat()
        val startAngle = -Math.PI.toFloat() / 2f

        fun pointAt(index: Int, fraction: Float): Offset {
            val angle = startAngle + angleStep * index
            return Offset(
                x = center.x + radius * fraction * cos(angle),
                y = center.y + radius * fraction * sin(angle)
            )
        }

        val ringSteps = 5
        for (step in 1..ringSteps) {
            val fraction = step / ringSteps.toFloat()
            val path = Path().apply {
                spheres.indices.forEach { i ->
                    val p = pointAt(i, fraction)
                    if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
                }
                close()
            }
            drawPath(path, color = gridColor, style = gridStroke)
        }

        spheres.indices.forEach { i ->
            drawLine(gridColor, center, pointAt(i, 1f), strokeWidth = gridStroke.width)
        }

        val scorePath = Path().apply {
            spheres.indices.forEach { i ->
                val score = scores[spheres[i]] ?: 0
                val fraction = (score.toFloat() / maxScore).coerceIn(0f, 1f)
                val p = pointAt(i, fraction)
                if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
            }
            close()
        }
        drawPath(scorePath, color = fillColor.copy(alpha = 0.35f))
        drawPath(scorePath, color = fillColor, style = scoreStroke)

        spheres.indices.forEach { i ->
            val score = scores[spheres[i]] ?: 0
            val fraction = (score.toFloat() / maxScore).coerceIn(0f, 1f)
            drawCircle(fillColor, radius = scoreStroke.width * 1.6f, center = pointAt(i, fraction))
        }

        if (showLabels) {
            val paint = android.graphics.Paint().apply {
                color = labelColor.toArgb()
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 11.sp.toPx()
            }
            drawIntoCanvas { canvas ->
                spheres.indices.forEach { i ->
                    val p = pointAt(i, 1.22f)
                    canvas.nativeCanvas.drawText(spheres[i].title, p.x, p.y, paint)
                }
            }
        }
    }
}
