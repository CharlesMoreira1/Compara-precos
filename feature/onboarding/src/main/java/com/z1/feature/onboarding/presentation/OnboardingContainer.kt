package com.z1.feature.onboarding.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.z1.feature.onboarding.presentation.screen.OnboardingScreen
import com.z1.feature.onboarding.presentation.viewmodel.OnboardingViewModel

@Composable
fun OnboardingContainer(
    modifier: Modifier = Modifier,
    goToListaCompras: () -> Unit,
) {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    AnimatedVisibility(
        visible = !uiState.value.onboarded,
        exit = slideOutVertically() { it }
    ) {
        OnboardingScreen(
            modifier = modifier,
            goToListaCompras = goToListaCompras,
            uiState = uiState.value,
            onEvent = { viewModel.onEvent(it) }
        )
    }
}