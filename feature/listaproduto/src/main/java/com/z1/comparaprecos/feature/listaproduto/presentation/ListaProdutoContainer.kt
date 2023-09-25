package com.z1.comparaprecos.feature.listaproduto.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.feature.listaproduto.presentation.viewmodel.ProdutoViewModel

@Composable
fun NovaListaContainer(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    idListaCompra: Long
) {
    val viewModel: ProdutoViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Default)

    ListaProdutoScreen(
        modifier = modifier,
        navigateUp = navigateUp,
        uiState = uiState,
        uiEvent = uiEvent.value,
        idListaCompra = idListaCompra,
        onEvent = { viewModel.onEvent(it) }
    )
}