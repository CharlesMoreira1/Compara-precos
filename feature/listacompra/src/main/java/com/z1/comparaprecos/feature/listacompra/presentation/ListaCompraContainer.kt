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
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.z1.comparaprecos.feature.listacompra.presentation.screen.ListaCompraScreen
import com.z1.comparaprecos.feature.listacompra.presentation.state.UiEvent
import com.z1.comparaprecos.feature.listacompra.presentation.viewmodel.ListaCompraViewModel

@Composable
fun ListaCompraContainer(
    modifier: Modifier = Modifier,
    goToListaProduto: (Long, Boolean) -> Unit,
    viewModel: ListaCompraViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Default)


        ListaCompraScreen(
            modifier = modifier,
            uiState = uiState,
            uiEvent = uiEvent.value,
            onEvent = { viewModel.onEvent(it) },
            goToListaProduto = { idListaCompra, isComparar ->
                goToListaProduto(idListaCompra, isComparar)
            }
        )

}