package com.z1.comparaprecos.feature.listaproduto.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.z1.comparaprecos.common.ui.components.CustomLoadingScreen
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.feature.listaproduto.presentation.viewmodel.OnEvent
import com.z1.comparaprecos.feature.listaproduto.presentation.viewmodel.ProdutoViewModel

@Composable
fun ListaProdutoContainer(
    modifier: Modifier = Modifier,
    idListaCompra: Long,
    isComparar: Boolean,
    navigateUp: () -> Unit,
) {
    val viewModel: ProdutoViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Default)

    AnimatedVisibility(
        visible = !uiState.isListaProdutoCarregada,
        enter = fadeIn(),
        exit = slideOutVertically(animationSpec = tween(500, easing = LinearEasing)) { it }
    ) {
        CustomLoadingScreen(
            titulo = stringResource(id = R.string.label_desc_aguarde_carregando_lista_produto),
            image = R.drawable.bg_loading
        )
        viewModel.onEvent(OnEvent.GetListaCompra(idListaCompra))
    }

    AnimatedVisibility(
        visible = uiState.isListaProdutoCarregada,
        enter = scaleIn(animationSpec = tween(300, easing = LinearEasing)) + fadeIn(),
        exit = scaleOut(animationSpec = tween(300, easing = LinearEasing)) + fadeOut()
    ) {
        if (uiState.isListaProdutoCarregada) {
            if (isComparar) {
                ListaProdutoComparadaScreen(
                    uiState = uiState,
                    onEvent = { viewModel.onEvent(it) }
                )
            } else {
                ListaProdutoScreen(
                    modifier = modifier,
                    navigateUp = navigateUp,
                    uiState = uiState,
                    uiEvent = uiEvent.value,
                    onEvent = { viewModel.onEvent(it) }
                )
            }
        }
    }
}