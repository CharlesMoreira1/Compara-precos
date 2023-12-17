package com.z1.feature.onboarding.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.z1.feature.onboarding.presentation.screen.OnboardingScreen
import com.z1.feature.onboarding.presentation.viewmodel.OnboardingViewModel

@Composable
fun OnboardingContainer(
    goToListaCompras: () -> Unit,
) {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    OnboardingScreen(
        goToListaCompras = goToListaCompras,
        uiState = uiState.value,
        onEvent = { viewModel.onEvent(it) }
    )
}