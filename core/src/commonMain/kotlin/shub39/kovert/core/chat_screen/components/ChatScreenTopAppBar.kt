package shub39.kovert.core.chat_screen.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kovert.core.generated.resources.Res
import kovert.core.generated.resources.arrow_back
import kovert.core.generated.resources.generating_mystery
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import shub39.kovert.core.chat_screen.ChatScreenState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatScreenTopAppBar(
    modifier: Modifier = Modifier,
    state: ChatScreenState,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigateUp: () -> Unit
) {
    MediumFlexibleTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = state.mystery?.persona?.name ?: stringResource(Res.string.generating_mystery)
            )
        },
        subtitle = {
            Text(
                text = state.mystery?.uiContext ?: "",
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigateUp
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back),
                    contentDescription = null
                )
            }
        }
    )
}