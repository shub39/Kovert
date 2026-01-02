package shub39.kovert.core.chat_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import shub39.kovert.core.domain.ChatMessage
import shub39.kovert.core.domain.Entity

@Composable
fun ChatMessage(
    chatMessage: ChatMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .blur(
                radius = if (chatMessage.isBlurred) 12.dp else 0.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            ),
        horizontalArrangement = when (chatMessage.sender) {
            Entity.USER -> Arrangement.End
            Entity.AI_AGENT -> Arrangement.Start
        }
    ) {
        Card(
            shape = when (chatMessage.sender) {
                Entity.USER -> RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 6.dp)
                Entity.AI_AGENT -> RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 6.dp, bottomEnd = 12.dp)
            },
            modifier = Modifier.fillMaxWidth(0.7f),
            colors = when (chatMessage.sender) {
                Entity.USER -> CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
                Entity.AI_AGENT -> CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        ) {
            Text(
                text = chatMessage.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}