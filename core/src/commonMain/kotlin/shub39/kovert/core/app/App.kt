package shub39.kovert.core.app

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import shub39.kovert.core.data.Koog

@Composable
fun App() {
    var text by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        text = Koog.test()
    }

    Text(text)
}