package com.z1.comparaprecos.common.util

import androidx.compose.runtime.State

sealed class UiEvent {
    data object Success: UiEvent()
    data class Error(val message: UiText): UiEvent()
    data object NavigateUp: UiEvent()

    data class ShowSnackbar(val message: UiText): UiEvent()
}
