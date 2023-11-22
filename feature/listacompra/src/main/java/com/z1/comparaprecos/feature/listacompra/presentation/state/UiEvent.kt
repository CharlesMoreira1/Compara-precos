package com.z1.comparaprecos.feature.listacompra.presentation.state

import androidx.annotation.StringRes
import com.z1.comparaprecos.common.ui.components.ETipoSnackbar
import com.z1.comparaprecos.common.util.UiText

sealed class UiEvent {
    data object Default: UiEvent()
    data class Inserted(@StringRes val message: Int): UiEvent()
    data class Deleted(@StringRes val message: Int): UiEvent()
    data class Updated(@StringRes val message: Int): UiEvent()
    data class Error(val message: UiText): UiEvent()
    data class ShowSnackbar(val message: UiText, val tipoMensagem: ETipoSnackbar): UiEvent()
    data object ShowBottomSheet: UiEvent()
    data object HideBottomSheet: UiEvent()
    data object ShowBottomSheetOptions: UiEvent()
    data object HideBottomSheetOptions: UiEvent()
    data object ShowDialogTemas: UiEvent()
}
