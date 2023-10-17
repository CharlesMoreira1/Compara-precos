package com.z1.comparaprecos.feature.listaproduto.presentation

import com.z1.comparaprecos.common.ui.components.ETipoSnackbar
import com.z1.comparaprecos.common.util.UiText

sealed class UiEvent {
    data object Default: UiEvent()
    data object Success: UiEvent()
    data class Error(val message: UiText): UiEvent()
    data object NavigateUp: UiEvent()
    data class ShowSnackbar(val message: UiText, val tipoMensagem: ETipoSnackbar): UiEvent()
    data object ShowAlertDialog: UiEvent()
    data object Finished: UiEvent()
}
