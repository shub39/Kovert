package shub39.kovert.core.chat_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kovert.core.generated.resources.Res
import kovert.core.generated.resources.send
import kovert.core.generated.resources.type_message
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import shub39.kovert.core.chat_screen.ChatScreenAction
import shub39.kovert.core.chat_screen.ChatScreenState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatScreenToolBar(
    modifier: Modifier = Modifier,
    state: ChatScreenState,
    onAction: (ChatScreenAction) -> Unit
) {
    var newMessage by remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            shape = MaterialTheme.shapes.extraExtraLarge
        ) {
            OutlinedTextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                placeholder = { Text(stringResource(Res.string.type_message)) },
                shape = MaterialTheme.shapes.extraLarge,
                enabled = !state.isLoadingNewMessage,
                singleLine = true,
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .padding(12.dp)
            )
        }

        FilledTonalIconButton(
            onClick = {
                onAction(ChatScreenAction.SendMessage(newMessage))
                newMessage = ""
            },
            enabled = newMessage.isNotBlank() && state.mystery != null && !state.isLoadingNewMessage,
            modifier = Modifier.size(IconButtonDefaults.mediumContainerSize())
        ) {
            Icon(
                painter = painterResource(Res.drawable.send),
                contentDescription = null,
                modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
            )
        }
    }
}