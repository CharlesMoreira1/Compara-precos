package com.z1.comparaprecos.feature.listacompra.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.z1.comparaprecos.common.ui.components.CustomProgressDialog
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.feature.listacompra.presentation.viewmodel.ListaCompraViewModel

@Composable
fun ListaCompraContainer(
    goToListaProduto: (Long, Boolean) -> Unit
) {
    val viewModel: ListaCompraViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Default)
    
    AnimatedVisibility(visible = !uiState.isListaCompraCarregada) {
        CustomProgressDialog(
            onDismiss = {},
            titulo = R.string.label_desc_carregando_listas
        )
    }
    
    AnimatedVisibility(
        visible = uiState.isListaCompraCarregada,
        enter = scaleIn(animationSpec = tween(300, easing = LinearEasing)) + fadeIn(),
        exit = scaleOut(animationSpec = tween(300, easing = LinearEasing)) + fadeOut()
    ) {
        ListaCompraScreen(
            uiState = uiState,
            uiEvent = uiEvent.value,
            onEvent = { viewModel.onEvent(it) },
            goToListaProduto = { idListaCompra, isComparar ->
                goToListaProduto(idListaCompra, isComparar)
            }
        )
    }
}