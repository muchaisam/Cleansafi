package com.cleansafi.core.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SuccessAnimation(
    modifier: Modifier = Modifier,
    onAnimationEnd: () -> Unit = {}
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatable = remember { Animatable(0f) }
    val scaleAnimatable = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animationPlayed = true
        // Circle scale animation
        scaleAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        // Checkmark draw animation
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(600, easing = FastOutSlowInEasing)
        )
        delay(500)
        onAnimationEnd()
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(120.dp)) {
            val canvasSize = size.minDimension
            val strokeWidth = canvasSize * 0.08f
            val radius = (canvasSize / 2) * 0.8f
            val center = Offset(size.width / 2, size.height / 2)

            // Draw circle
            drawCircle(
                color = Color(0xFF4CAF50),
                radius = radius * scaleAnimatable.value,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            // Draw checkmark
            if (animatable.value > 0f) {
                val checkPath = Path()
                val checkSize = radius * 1.2f
                val startX = center.x - checkSize * 0.3f
                val startY = center.y
                val midX = center.x - checkSize * 0.1f
                val midY = center.y + checkSize * 0.3f
                val endX = center.x + checkSize * 0.4f
                val endY = center.y - checkSize * 0.4f

                checkPath.moveTo(startX, startY)

                // First segment of checkmark
                if (animatable.value <= 0.5f) {
                    val progress = animatable.value / 0.5f
                    checkPath.lineTo(
                        startX + (midX - startX) * progress,
                        startY + (midY - startY) * progress
                    )
                } else {
                    checkPath.lineTo(midX, midY)
                    // Second segment of checkmark
                    val progress = (animatable.value - 0.5f) / 0.5f
                    checkPath.lineTo(
                        midX + (endX - midX) * progress,
                        midY + (endY - midY) * progress
                    )
                }

                drawPath(
                    path = checkPath,
                    color = Color(0xFF4CAF50),
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }
}

@Composable
fun ConfettiAnimation(modifier: Modifier = Modifier) {
    val confettiPieces = remember {
        List(50) {
            ConfettiPiece(
                x = (0..100).random() / 100f,
                initialY = -(10..30).random() / 100f,
                color = listOf(
                    Color(0xFFFFEB3B),
                    Color(0xFF4CAF50),
                    Color(0xFF2196F3),
                    Color(0xFFF44336),
                    Color(0xFF9C27B0)
                ).random(),
                size = (5..15).random().toFloat()
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val animationProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confetti"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        confettiPieces.forEach { piece ->
            val x = size.width * piece.x
            val y = size.height * (piece.initialY + animationProgress.value * 1.3f)
            
            if (y < size.height) {
                drawCircle(
                    color = piece.color,
                    radius = piece.size,
                    center = Offset(x, y)
                )
            }
        }
    }
}

private data class ConfettiPiece(
    val x: Float,
    val initialY: Float,
    val color: Color,
    val size: Float
)
