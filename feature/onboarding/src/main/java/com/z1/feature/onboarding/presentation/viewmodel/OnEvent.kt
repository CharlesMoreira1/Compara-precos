package com.z1.feature.onboarding.presentation.viewmodel

sealed class OnEvent {
    data class ShowedOnboarding(val showedOnboarding: Boolean): OnEvent()
}
