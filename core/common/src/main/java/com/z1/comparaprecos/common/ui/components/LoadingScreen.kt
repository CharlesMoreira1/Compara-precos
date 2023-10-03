package com.z1.comparaprecos.common.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.z1.comparaprecos.core.common.R
import kotlinx.coroutines.delay

@Composable
fun CustomLoadingScreen(
    modifier: Modifier = Modifier,
    titulo: String,
    image: Int? = null
) {
    var isFadingIn by remember { mutableStateOf(true) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val transition = infiniteTransition.animateFloat(
        initialValue = if (isFadingIn) 0f else 1f,
        targetValue = if (isFadingIn) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse // Inverte a animação após cada repetição
        ), label = ""
    )
    LaunchedEffect(transition) {
        while (true) {
            isFadingIn = !isFadingIn
            delay(3000)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.medium)),
        verticalArrangement = Arrangement.Center
    ) {
        image?.let {
            Image(
                modifier = Modifier
                    .graphicsLayer(alpha = transition.value)
                    .fillMaxWidth()
                    .heightIn(max = max(160.dp, with(LocalDensity.current) { 160.sp.toDp() })),
                painter = painterResource(id = it),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium)))
        }

        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}