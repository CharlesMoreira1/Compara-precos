package com.z1.feature.onboarding.presentation.viewmodel

sealed class OnEvent {
    data class Onboarded(val onboarded: Boolean): OnEvent()
}
