package com.z1.feature.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z1.core.datastore.repository.UserPreferencesRepository
import com.z1.feature.onboarding.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        initialValue = _uiState.value
    )

    fun onEvent(onEvent: OnEvent) {
        if (onEvent is OnEvent.ShowedOnboarding) {
            updateShowedOnboarding()
        }
    }

    private fun updateShowedOnboarding() =
        viewModelScope.launch {
            userPreferencesRepository.putShowedOnboarding(true)
            _uiState.update {
                it.copy(finishedOnboarding = true)
            }
        }
}