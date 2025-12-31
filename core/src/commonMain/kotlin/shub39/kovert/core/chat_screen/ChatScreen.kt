package shub39.kovert.core.chat_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import shub39.kovert.core.data.AgentsHandler
import shub39.kovert.core.domain.Mystery

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    state: ChatScreenState,
    onAction: (ChatScreenAction) -> Unit
) {
    var mystery by rememberSaveable { mutableStateOf<Mystery?>(null) }

    LaunchedEffect(Unit) {
        if (mystery == null) {
            mystery = AgentsHandler().generateNewMystery().getOrNull()
        }
    }

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
            text = mystery.toString()
        )
    }
}