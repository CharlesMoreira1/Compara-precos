@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class
)

package com.z1.feature.onboarding.presentation.screen

import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.z1.comparaprecos.common.ui.components.CustomButton
import com.z1.comparaprecos.common.ui.components.CustomCard
import com.z1.comparaprecos.common.util.supportsBlurEffect
import com.z1.comparaprecos.core.common.R.dimen
import com.z1.comparaprecos.core.common.R.string
import com.z1.feature.onboarding.R
import com.z1.feature.onboarding.presentation.state.UiState
import com.z1.feature.onboarding.presentation.viewmodel.OnEvent
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onEvent: (OnEvent) -> Unit,
    uiState: UiState,
    goToListaCompras: () -> Unit
) {

    var showStartButton by remember {
        mutableStateOf(false)
    }

    val horizontalState = rememberPagerState(
        pageCount = { onboardingPages.size },
        initialPage = 0
    )

    val pageColor by animateColorAsState(
        onboardingPages[horizontalState.currentPage].backgroundColor,
        animationSpec = tween(TimeUnit.SECONDS.toMillis(1).toInt()),
        label = "background color indicator"
    )

    LaunchedEffect(uiState.onboarded) {
        if (uiState.onboarded) {
            goToListaCompras()
        }
    }

    LaunchedEffect(horizontalState.currentPage) {
        if (horizontalState.currentPage == onboardingPages.size - 1) {
            showStartButton = true
        }
    }

    Column(
        modifier = modifier
    ) {

        TitleHorizontalPager(
            modifier = Modifier
                .weight(5f)
                .padding(top = dimensionResource(id = dimen.large)),
            horizontalState = horizontalState,
            pageColor = pageColor
        )

        SubtitleVerticalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalState = horizontalState,
            pageColor = pageColor
        )

        HorizontalPagerIndicator(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = dimen.medium)),
            horizontalState = horizontalState,
            pageColor = pageColor
        )

        AnimatedContent(
            targetState = showStartButton,
            label = "",
            transitionSpec = {
                slideInVertically(tween(500, 0, FastOutLinearInEasing)) { it } togetherWith
                        (slideOutVertically(tween(500, 0, FastOutSlowInEasing)) { it })
            }
        ) { state ->
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        horizontal = dimensionResource(id = dimen.large),
                        vertical = dimensionResource(id = dimen.medium)
                    )
                    .alpha(if (state) 1f else 0f),
                containerColor = MaterialTheme.colorScheme.onBackground,
                textColor = MaterialTheme.colorScheme.onPrimary,
                titulo = "Começar",
                textStyle = MaterialTheme.typography.bodyLarge,
                onClick = {
                    onEvent(OnEvent.Onboarded(true))
                }
            )
        }
    }
}

@Composable
fun TitleHorizontalPager(
    modifier: Modifier = Modifier,
    horizontalState: PagerState,
    pageColor: Color
) {
    HorizontalPager(
        modifier = modifier,
        state = horizontalState,
        pageSpacing = 1.dp,
        beyondBoundsPageCount = 9,
    ) { page ->
        CustomCard(
            modifier = Modifier
                .zIndex(page * 10f)
                .padding(
                    horizontal = dimensionResource(id = dimen.large),
                )
                .fillMaxSize()
                .graphicsLayer {
                    val startOffset =
                        ((horizontalState.currentPage - page) + horizontalState.currentPageOffsetFraction).absoluteValue
                    translationX = size.width * (startOffset * .99f)

                    alpha = (2f - startOffset) / 2f

                    if (supportsBlurEffect()) {
                        val blur = (startOffset * 20f).coerceAtLeast(0.1f)
                        renderEffect = RenderEffect
                            .createBlurEffect(
                                blur, blur, Shader.TileMode.DECAL
                            )
                            .asComposeRenderEffect()
                    }

                    val scale = 1f - (startOffset * .1f)
                    scaleX = scale
                    scaleY = scale

                    shadowElevation = 8f
                    shape = RoundedCornerShape(24.dp)
                }
                .clip(RoundedCornerShape(dimensionResource(id = dimen.big))),
            containerColor = pageColor,
            onCardClick = { }
        ) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(id = dimen.medium))
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = onboardingPages[page].title),
                    style = if (page == 0) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Image(
                    modifier = Modifier
                        .sizeIn(
                            maxWidth = 500.dp,
                            maxHeight = 500.dp
                        )
                        .padding(dimensionResource(id = dimen.medium)),
                    painter = painterResource(id = onboardingPages[page].image),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                )
            }
        }
    }
}

@Composable
fun SubtitleVerticalPager(
    modifier: Modifier = Modifier,
    horizontalState: PagerState,
    pageColor: Color
) {
    val verticalState = rememberPagerState(
        pageCount = { onboardingPages.size }
    )

    VerticalPager(
        state = verticalState,
        modifier = modifier,
        userScrollEnabled = false,
        reverseLayout = true
    ) { page ->
        CustomCard(
            modifier = Modifier
                .heightIn(min = 180.dp, max = 220.dp)
                .padding(horizontal = dimensionResource(id = dimen.large), vertical = 8.dp)
                .graphicsLayer {
                    val startOffset =
                        ((verticalState.currentPage - page) + verticalState.currentPageOffsetFraction).absoluteValue
                    //translationY = size.height * (startOffset * .99f)

                    alpha = (2f - startOffset) / 2f

                    if (supportsBlurEffect()) {
                        val blur = (startOffset * 20f).coerceAtLeast(0.1f)
                        renderEffect = RenderEffect
                            .createBlurEffect(
                                blur, blur, Shader.TileMode.DECAL
                            )
                            .asComposeRenderEffect()
                    }

                    val scale = 1f - (startOffset * .1f)
                    scaleX = scale
                    scaleY = scale

                    shadowElevation = 8f
                    shape = RoundedCornerShape(24.dp)
                }
                .clip(RoundedCornerShape(dimensionResource(id = dimen.big))),
            containerColor = pageColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = dimen.medium)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = onboardingPages[page].subtitle),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow {
            Pair(
                horizontalState.currentPage,
                horizontalState.currentPageOffsetFraction
            )
        }.collect { (page, offset) ->
            verticalState.scrollToPage(page, offset)
        }
    }
}

@Composable
fun HorizontalPagerIndicator(
    modifier: Modifier = Modifier,
    horizontalState: PagerState,
    pageColor: Color,
) {

    val interpolatedPosition by remember {
        derivedStateOf {
            val position = horizontalState.getOffsetFractionForPage(0)
            val from = floor(position).roundToInt().coerceAtMost(onboardingPages.size - 1)
            val to = ceil(position).roundToInt().coerceAtMost(onboardingPages.size - 1)

            val fraction = position - position.toInt()
            from + ((to - from) * fraction)
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {

        for (i in onboardingPages.indices) {
            val animatedScale by animateFloatAsState(
                targetValue = if (floor(interpolatedPosition) >= i) {
                    1f
                } else if (ceil(interpolatedPosition) < i) {
                    0f
                } else {
                    interpolatedPosition - interpolatedPosition.toInt()
                },
                animationSpec = spring(
                    stiffness = Spring.StiffnessMedium
                ),
                label = ""
            )

            Box(
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Rounded.Circle),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(id = dimen.medium))
                        .alpha(.1f),
                )
                Icon(
                    painter = rememberVectorPainter(image = Icons.Rounded.Circle),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(id = dimen.medium))
                        .scale(animatedScale),
                    tint = pageColor
                )
            }

        }
    }
}

data class OnboardingPage(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
    val backgroundColor: Color
)

val onboardingPages = listOf(
    OnboardingPage(
        image = R.drawable.onboarding0,
        title = string.label_onboarding_0_title,
        subtitle = string.label_onboarding_0_subtitle,
        backgroundColor = Color(0xFF6794dd)
    ),
    OnboardingPage(
        image = R.drawable.onboarding1,
        title = string.label_onboarding_1_title,
        subtitle = string.label_onboarding_1_subtitle,
        backgroundColor = Color(0xFFf97272)
    ),
    OnboardingPage(
        image = R.drawable.onboarding2,
        title = string.label_onboarding_2_title,
        subtitle = string.label_onboarding_2_subtitle,
        backgroundColor = Color(0xFF67dd86)
    ),
    OnboardingPage(
        image = R.drawable.onboarding3,
        title = string.label_onboarding_3_title,
        subtitle = string.label_onboarding_3_subtitle,
        backgroundColor = Color(0xFFDDC067)
    ),
)