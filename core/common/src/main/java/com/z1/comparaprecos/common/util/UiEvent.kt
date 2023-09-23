package com.z1.comparaprecos.common.util

sealed class UiEvent {
    data object Default: UiEvent()
    data object Success: UiEvent()
    data class Error(val message: UiText): UiEvent()
    data object NavigateUp: UiEvent()
    data class ShowSnackbar(val message: UiText): UiEvent()
}
