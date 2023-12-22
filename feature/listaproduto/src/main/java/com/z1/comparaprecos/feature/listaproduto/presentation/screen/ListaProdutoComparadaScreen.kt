@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)

package com.z1.comparaprecos.feature.listaproduto.presentation.screen

import android.widget.Toast
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.ui.components.CustomDialogOpcoes
import com.z1.comparaprecos.common.ui.components.CustomDivider
import com.z1.comparaprecos.common.ui.components.CustomFloatingActionButton
import com.z1.comparaprecos.common.ui.components.CustomLoadingScreen
import com.z1.comparaprecos.common.ui.components.CustomRadioButton
import com.z1.comparaprecos.common.ui.components.CustomSnackBar
import com.z1.comparaprecos.common.ui.components.CustomTopAppBar
import com.z1.comparaprecos.common.ui.components.Mensagem
import com.z1.comparaprecos.common.util.ordenacaoOptions
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.feature.listaproduto.presentation.components.FormularioProduto
import com.z1.comparaprecos.feature.listaproduto.presentation.components.ListaProduto
import com.z1.comparaprecos.feature.listaproduto.presentation.components.TituloListaProduto
import com.z1.comparaprecos.feature.listaproduto.presentation.state.UiEvent
import com.z1.comparaprecos.feature.listaproduto.presentation.state.UiState
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
//    val appBarState = rememberTopAppBarState()
//    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
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
            onEvent(OnEvent.UpdateUiEvent(UiEvent.Default))
        }
    }

    LaunchedEffect(key1 = pagerState.currentPage, key2 = pagerState.isScrollInProgress) {
        onEvent(OnEvent.UpdateUiEvent(UiEvent.Default))
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
                    },
                    pagerIndex = selectedIndex,
                    onOrdenarListaClick = {
                        onEvent(OnEvent.UpdateUiEvent(UiEvent.ShowDialogOrdenarLista))
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
                            }
                            1 -> {
                                this@Column.AnimatedVisibility(
                                    visible = uiState.listaCompraComparada.produtos.isNotEmpty(),
                                    enter = scaleIn(animationSpec = tween(300, easing = LinearEasing)) + fadeIn(),
                                    exit = scaleOut(animationSpec = tween(300, easing = LinearEasing)) + fadeOut(),
                                    label = "lista_produto"
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
                                    exit = slideOutVertically(animationSpec = tween(500, easing = LinearEasing)) { it },
                                    label = "loading_screen"
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
                                    contentDescription = stringResource(id = R.string.description_adicionar_lista_to_comparar),
                                    onClick = {
                                        onEvent(OnEvent.GetListaCompraOptions)
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
                                        contentDescription = stringResource(id = R.string.description_compartilhar_resumo),
                                        onClick = {
                                            Toast.makeText(context, R.string.label_em_breve, Toast.LENGTH_SHORT).show()
                                        }
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
                allListaCompra = uiState.listaCompraOptions,
                onEvent = onEvent
            )
        }
        is UiEvent.ShowSnackbar -> {
            val message = Mensagem(
                uiEvent.message.asString(),
                uiEvent.tipoMensagem
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
        is UiEvent.ShowDialogOrdenarLista -> {
            CustomDialogOpcoes(
                title = stringResource(id = R.string.label_ordenacao_lista),
                optionList = ordenacaoOptions,
                atualSelectedOption = uiState.ordenacaoSelecionada.id,
                onAtualSelectedOptionClick = {
                    onEvent(OnEvent.ChangeOrdenacaoLista(it))
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
    val configuration = LocalConfiguration.current

    var idListaCompraSelecionada by remember {
        mutableLongStateOf(-1)
    }

    AlertDialog(
        modifier = modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { },
        title = {
            Text(
                text = stringResource(id = R.string.label_selecione_lista_to_comparar),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                CustomDivider()
                LazyColumn(
                    modifier = Modifier
                        .heightIn(min = 150.dp, max = 250.dp)
                ) {
                    items(allListaCompra) { item ->
                        CustomRadioButton(
                            item = item,
                            selectedOption = idListaCompraSelecionada,
                        ) {
                            idListaCompraSelecionada = it
                        }
                    }
                }
                CustomDivider()
            }
        },
        confirmButton = {
            TextButton(onClick = { onEvent(OnEvent.GetListaCompraToComparar(idListaCompraSelecionada)) }
            ) {
                Text(
                    text = stringResource(id = R.string.label_ok),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(OnEvent.UpdateUiEvent(UiEvent.Default)) }
            ) {
                Text(
                    text = stringResource(id = R.string.label_cancelar),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    )
}

@Composable
fun FloatingActionButton(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondary,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    CustomFloatingActionButton(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.medium)),
        onClick = onClick,
        containerColor = containerColor,
        iconTint = MaterialTheme.colorScheme.onPrimary,
        imageVector = icon,
        iconContentDescription = contentDescription
    )
}