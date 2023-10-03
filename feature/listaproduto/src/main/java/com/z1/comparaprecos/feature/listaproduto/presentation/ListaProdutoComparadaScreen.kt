@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)

package com.z1.comparaprecos.feature.listaproduto.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.ShoppingCartCheckout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.ui.components.CustomFloatingActionButton
import com.z1.comparaprecos.common.ui.components.CustomTextPriceCounter
import com.z1.comparaprecos.common.ui.components.CustomTopAppBar
import com.z1.comparaprecos.common.ui.theme.MediumSeaGreen
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.feature.listaproduto.presentation.viewmodel.OnEvent
import java.util.Currency
import java.util.Locale

data class TabItem(
    @StringRes val titleId: Int,
)

@Composable
fun ListaProdutoComparadaScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onEvent: (OnEvent) -> Unit
) {
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
    val listState = rememberLazyListState()

    val tabItems by remember {
        mutableStateOf(
            listOf(
                TabItem(R.string.label_lista_atual),
                TabItem(R.string.label_lista_comparada),
                TabItem(R.string.label_resumo)
            )
        )
    }

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        tabItems.size
    }

    LaunchedEffect(key1 = selectedIndex) {
        pagerState.animateScrollToPage(selectedIndex)
    }

    LaunchedEffect(key1 = pagerState.currentPage, key2 = pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedIndex = pagerState.currentPage
        }
    }
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .systemBarsPadding()
            .fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                scrollBehavior = scrollBehavior,
                isNotOnTopoLista = false
            ) {
                TituloListaProduto(
                    titulo = when(selectedIndex) {
                        0 -> uiState.listaCompra.titulo
                        1 -> uiState.listaCompraComparada.detalhes.titulo
                        else -> stringResource(id = R.string.label_resumo)
                    },
                    listaProduto = when (selectedIndex) {
                        0 -> uiState.listaProduto
                        1 -> uiState.listaCompraComparada.produtos
                        else -> null
                    }
                )
            }
        },
    ) { innerPadding ->

        Column(
            modifier = modifier
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            TabRow(
                containerColor = MaterialTheme.colorScheme.background,
                selectedTabIndex = selectedIndex
            ) {
                tabItems.forEachIndexed { index, item ->
                    Tab(
                        selected = index == selectedIndex,
                        onClick = { selectedIndex = index },
                        text = {
                            Text(text = stringResource(id = item.titleId))
                        }
                    )
                }
            }
            HorizontalPager(
                modifier = Modifier
                    .weight(1f),
                state = pagerState,
                verticalAlignment = Alignment.Top,
            ) { index ->

                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Scaffold(
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f),
                        floatingActionButton = {
                            when(index) {
                                0 -> {
                                    if (uiState.listaProduto.isNotEmpty()) {
                                        FloatingActionButton(
                                            containerColor = MediumSeaGreen,
                                            icon = Icons.Rounded.ShoppingCartCheckout,
                                            onClick = {}
                                        )
                                    }
                                }

                                1 -> {
                                    FloatingActionButton(
                                        icon =
                                        if (uiState.listaCompraComparada.produtos.isEmpty()) Icons.Rounded.Add
                                        else Icons.Rounded.Edit,
                                        onClick = {}
                                    )
                                }

                                2 -> {
                                    FloatingActionButton(
                                        icon = Icons.Rounded.Share,
                                        onClick = {}
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        when(index) {
                            in 0..1 -> {
                                ListaProduto(
                                    listState = listState,
                                    listaProduto =
                                    if (index == 0) uiState.listaProduto
                                    else uiState.listaCompraComparada.produtos,
                                    listaProdutoComparada =
                                    if (index == 1) uiState.listaProduto
                                    else uiState.listaCompraComparada.produtos,
                                    canDeleteProduto = selectedIndex == 0,
                                    onEvent = onEvent
                                )
                            }

                            2 -> {

                            }
                        }
                    }

                    if (index == 0) {
                        FormularioProduto(
                            uiState = uiState,
                            onAdicionarProdutoClick = { produto ->
                                if (produto.id == 0L) onEvent(OnEvent.InsertProduto(produto))
                                else onEvent(OnEvent.UpdateProduto(produto))
                            },
                            onCancelarEdicaoProduto = { onEvent(OnEvent.ProdutoSelecionado(null)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FloatingActionButton(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    icon: ImageVector,
    onClick: () -> Unit
) {
    CustomFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = containerColor,
        iconTint = MaterialTheme.colorScheme.onPrimary,
        imageVector = icon
    )
}