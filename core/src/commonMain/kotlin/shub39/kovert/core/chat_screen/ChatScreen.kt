package shub39.kovert.core.chat_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    state: ChatScreenState,
    onAction: (ChatScreenAction) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = "Chat Screen"
        )
        Button(
            onClick = onNavigateUp
        ) {
            Text("Go Back")
        }
        Text(
            text = state.mystery?.uiContext.toString()
        )

        state.chatMessages.forEach { message ->
            Text(
                text = "${message.sender}: ${message.content}"
            )
        }

        var newMessage by remember { mutableStateOf("") }
        OutlinedTextField(
            value = newMessage,
            onValueChange = { newMessage = it }
        )

        Button(
            onClick = {
                onAction(ChatScreenAction.SendMessage(newMessage))
                newMessage = ""
            },
            enabled = newMessage.isNotBlank()
        ) {
            Text(text = "Send Message")
        }
    }
}