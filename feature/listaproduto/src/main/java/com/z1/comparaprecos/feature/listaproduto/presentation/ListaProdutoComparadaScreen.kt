@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)

package com.z1.comparaprecos.feature.listaproduto.presentation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.ShoppingCartCheckout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.ui.components.CustomDivider
import com.z1.comparaprecos.common.ui.components.CustomFloatingActionButton
import com.z1.comparaprecos.common.ui.components.CustomLoadingScreen
import com.z1.comparaprecos.common.ui.components.CustomSnackBar
import com.z1.comparaprecos.common.ui.components.CustomTopAppBar
import com.z1.comparaprecos.common.ui.components.ETipoSnackbar
import com.z1.comparaprecos.common.ui.components.Mensagem
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.ui.theme.MediumSeaGreen
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.feature.listaproduto.presentation.viewmodel.OnEvent
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class TabItem(
    @StringRes val titleId: Int,
)

@Composable
fun ListaProdutoComparadaScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    uiEvent: UiEvent,
    onEvent: (OnEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
    val elevateTopAppBar by remember { mutableStateOf(false) }

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
        scope.launch {
            pagerState.animateScrollToPage(selectedIndex)
        }
    }

    LaunchedEffect(key1 = pagerState.currentPage, key2 = pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedIndex = pagerState.currentPage
        }
    }

    Scaffold(
        modifier = modifier
//            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .systemBarsPadding()
            .fillMaxSize(),
        topBar = {
            CustomTopAppBar(
//                scrollBehavior = scrollBehavior,
                elevateTopAppBar = elevateTopAppBar
            ) {
                TituloListaProduto(
                    titulo = when (selectedIndex) {
                        0 -> uiState.listaCompra.titulo
                        1 -> uiState.listaCompraComparada.detalhes.titulo
                        else -> stringResource(id = R.string.label_resumo)
                    },
                    valorLista = when (selectedIndex) {
                        0 -> uiState.listaProduto.sumOf { (it.valorProduto()) }
                        1 -> uiState.listaCompraComparada.produtos.sumOf { (it.valorProduto()) }
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
                beyondBoundsPageCount = 3,
                verticalAlignment = Alignment.Top
            ) { index ->

                Column( modifier = modifier
                    .fillMaxSize()
                ) {
                    Box(
                        modifier = modifier
                            .weight(1f),
                    ) {
                        when(index) {
                            0 -> {
                                ListaProduto(
                                    listaProduto = uiState.listaProduto,
                                    listaProdutoComparada = uiState.listaCompraComparada.produtos,
                                    isOnTopOfList = {},
                                    onProdutoClick = { produto ->
                                        onEvent(OnEvent.ProdutoSelecionado(produto))
                                    }
                                )

                                if (uiState.listaProduto.isNotEmpty()) {
                                    FloatingActionButton(
                                        modifier = Modifier.align(Alignment.BottomEnd),
                                        containerColor = MediumSeaGreen,
                                        icon = Icons.Rounded.ShoppingCartCheckout,
                                        onClick = {}
                                    )
                                }
                            }
                            1 -> {
                                this@Column.AnimatedVisibility(
                                    visible = uiState.listaCompraComparada.produtos.isNotEmpty(),
                                    enter = scaleIn(animationSpec = tween(300, easing = LinearEasing)) + fadeIn(),
                                    exit = scaleOut(animationSpec = tween(300, easing = LinearEasing)) + fadeOut()
                                ) {
                                    ListaProduto(
                                        listaProduto = uiState.listaCompraComparada.produtos,
                                        listaProdutoComparada = uiState.listaProduto,
                                        isOnTopOfList = {},
                                        onProdutoClick = {}
                                    )
                                }

                                this@Column.AnimatedVisibility(
                                    visible = uiState.listaCompraComparada.produtos.isEmpty(),
                                    enter = slideInVertically(animationSpec = tween(500, easing = LinearEasing)) { it },
                                    exit = slideOutVertically(animationSpec = tween(500, easing = LinearEasing)) { it }
                                ) {
                                    CustomLoadingScreen(
                                        titulo =
                                        R.string.label_desc_adicionar_lista_to_comparar,
                                        image = R.drawable.bg_add_lista
                                    )
                                }

                                FloatingActionButton(
                                    modifier = Modifier.align(Alignment.BottomEnd),
                                    icon =
                                    if (uiState.listaCompraComparada.produtos.isEmpty()) Icons.Rounded.Add
                                    else Icons.Rounded.Edit,
                                    onClick = {
                                        onEvent(OnEvent.GetAllListaCompra)
                                    }
                                )
                            }
                            2 -> {
                                if (uiState.listaProduto.isNotEmpty() && uiState.listaCompraComparada.produtos.isNotEmpty()) {
                                    ResumoComparacaoListaScreen(
                                        listaProduto = uiState.listaCompra.titulo to uiState.listaProduto,
                                        listaProdutoComparada = uiState.listaCompraComparada.detalhes.titulo to uiState.listaCompraComparada.produtos
                                    )
                                    FloatingActionButton(
                                        modifier = Modifier.align(Alignment.BottomEnd),
                                        icon = Icons.Rounded.Share,
                                        onClick = {}
                                    )
                                } else {
                                    CustomLoadingScreen(
                                        titulo = R.string.label_desc_necessario_duas_listas_to_comparar,
                                        image = R.drawable.bg_aviso
                                    )
                                }
                            }
                        }
                    }

                    if (index == 0) {
                        FormularioProduto(
                            produtoSelecionado = uiState.produtoSelecionado,
                            idListaCompra = uiState.listaCompra.id,
                            onAdicionarProdutoClick = { produto ->
                                if (produto.id == 0L) onEvent(OnEvent.InsertProduto(produto))
                                else onEvent(OnEvent.UpdateProduto(produto))
                            },
                            onCancelarEdicaoProduto = { onEvent(OnEvent.ProdutoSelecionado(null)) },
                            onDeletarProdutoClick = { onEvent(OnEvent.DeleteProduto(it)) }
                        )
                    }
                }
            }
        }
    }

    when(uiEvent) {
        is UiEvent.ShowAlertDialog -> {
            AlertDialogListaCompra(
                allListaCompra = uiState.allListaCompra,
                onEvent = onEvent
            )
        }
        is UiEvent.ShowSnackbar -> {
            val message = Mensagem(
                uiEvent.message.asResId(),
                ETipoSnackbar.ERRO
            )
            CustomSnackBar(
                modifier = Modifier.fillMaxWidth(),
                mensagem = message, // aqui vai a event.message do uiEvent (corrigir lógica)
                duracao = TimeUnit.SECONDS.toMillis(3),
                onFimShowMensagem = {
                    onEvent(OnEvent.UpdateUiEvent(UiEvent.Default))
                }
            )
        }
        else -> Unit
    }
}

@Composable
fun AlertDialogListaCompra(
    modifier: Modifier = Modifier,
    allListaCompra: List<Pair<String, Long>>,
    onEvent: (OnEvent) -> Unit
) {
    var idListaCompraSelecionada by remember {
        mutableLongStateOf(-1)
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { },
    ) {
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(dimensionResource(id = R.dimen.big))
                )
        ) {
            Text(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.big)),
                text = "Selecione uma lista de compra",
                style = MaterialTheme.typography.titleLarge
            )
            CustomDivider(
                padding = 0.dp
            )
            LazyColumn(
                modifier = Modifier
                    .heightIn(min = 150.dp, max = 250.dp)
            ) {
                items(allListaCompra) { item ->
                    ListaCompraOpcoes(item, idListaCompraSelecionada) {
                        idListaCompraSelecionada = it
                    }
                }
            }
            CustomDivider(
                padding = 0.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.big)),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onEvent(OnEvent.UpdateUiEvent(UiEvent.Default)) }
                ) {
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                TextButton(onClick = { onEvent(OnEvent.GetListaCompraToComparar(idListaCompraSelecionada)) }
                ) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ListaCompraOpcoes(
    item: Pair<String, Long>,
    listaCompraSelecionada: Long,
    onClickListener: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onClickListener(item.second)
            }
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.big),
                vertical = dimensionResource(id = R.dimen.normal)
            ),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium))
    ) {
        RadioButton(
            modifier = Modifier.size(24.dp),
            selected = (item.second == listaCompraSelecionada),
            onClick = {
                onClickListener(item.second)
            }
        )

        Text(
            item.first,
            style = MaterialTheme.typography.bodySmall,
        )
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
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.medium)),
        onClick = onClick,
        containerColor = containerColor,
        iconTint = MaterialTheme.colorScheme.onPrimary,
        imageVector = icon
    )
}