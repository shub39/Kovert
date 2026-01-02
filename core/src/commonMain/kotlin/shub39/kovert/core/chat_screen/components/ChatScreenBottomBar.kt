package shub39.kovert.core.chat_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kovert.core.generated.resources.Res
import kovert.core.generated.resources.send
import org.jetbrains.compose.resources.painterResource
import shub39.kovert.core.chat_screen.ChatScreenAction
import shub39.kovert.core.chat_screen.ChatScreenState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatScreenBottomBar(
    modifier: Modifier = Modifier,
    state: ChatScreenState,
    onAction: (ChatScreenAction) -> Unit
) {
    BottomAppBar(
        modifier = modifier
            .clip(RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            ))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            var newMessage by remember { mutableStateOf("") }

            OutlinedTextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium,
                enabled = !state.isLoadingNewMessage,
                singleLine = true
            )

            if (!state.isLoadingNewMessage) {
                FilledTonalIconButton(
                    onClick = {
                        onAction(ChatScreenAction.SendMessage(newMessage))
                        newMessage = ""
                    },
                    enabled = newMessage.isNotBlank() && state.mystery != null
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.send),
                        contentDescription = null
                    )
                }
            } else {
                LoadingIndicator()
            }
        }
    }
}