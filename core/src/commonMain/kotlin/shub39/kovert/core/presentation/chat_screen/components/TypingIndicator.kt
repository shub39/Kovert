package shub39.kovert.core.presentation.chat_screen.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TypingIndicator(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.7f),
            colors = CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 6.dp, bottomEnd = 12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val circleSize = 12.dp
                val animationDelay = 200

                val circles = List(3) {
                    remember { Animatable(0.5f) }
                }

                circles.forEachIndexed { index, animatable ->
                    LaunchedEffect(animatable) {
                        delay(index * animationDelay.toLong())
                        animatable.animateTo(
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = 500),
                                repeatMode = RepeatMode.Reverse
                            )
                        )
                    }
                }

                circles.forEach { animatable ->
                    Box(
                        modifier = Modifier
                            .size(circleSize)
                            .graphicsLayer {
                                scaleX = animatable.value
                                scaleY = animatable.value
                            }
                            .background(
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}