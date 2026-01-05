package shub39.kovert.core.presentation.chat_screen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.MeshGradient
import kovert.core.generated.resources.Res
import kovert.core.generated.resources.arrow_back
import kovert.core.generated.resources.hint
import org.jetbrains.compose.resources.painterResource
import shub39.kovert.core.presentation.chat_screen.ChatScreenState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatScreenTopBar(
    modifier: Modifier = Modifier,
    state: ChatScreenState,
    onNavigateUp: () -> Unit,
    onShowHints: () -> Unit
) {
    Column(
        modifier = modifier.animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateUp,
                enabled = state.mystery != null
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back),
                    contentDescription = null
                )
            }
            if (state.mystery != null) {
                IconButton(onClick = onShowHints) {
                    Icon(
                        painter = painterResource(Res.drawable.hint),
                        contentDescription = "Hints"
                    )
                }
            }
        }

        Box(
            modifier = Modifier.height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            val size by animateDpAsState(
                targetValue = if (state.mystery != null) 200.dp else 160.dp
            )

            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shaderBackground(
                            shader = MeshGradient(
                                colors = if (state.mystery == null) {
                                    arrayOf(Color.Black, Color.Gray, Color.DarkGray, Color.White)
                                } else {
                                    state.chatOrb.colors.toTypedArray()
                                }
                            ),
                            speed = 2f
                        )
                )
            }

            if (state.mystery == null) {
                ContainedLoadingIndicator()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        state.mystery?.let { mystery ->
            Text(
                text = mystery.persona.name,
                style = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center)
            )
            Text(
                text = mystery.uiContext
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()
    }
}