@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)

package com.z1.comparaprecos.feature.listacompra.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.z1.comparaprecos.common.ui.OnBackPressed
import com.z1.comparaprecos.common.ui.components.CustomBottomSheet
import com.z1.comparaprecos.common.ui.components.CustomBottomSheetDialog
import com.z1.comparaprecos.common.ui.components.CustomButton
import com.z1.comparaprecos.common.ui.components.CustomCard
import com.z1.comparaprecos.common.ui.components.CustomDivider
import com.z1.comparaprecos.common.ui.components.CustomFloatingActionButton
import com.z1.comparaprecos.common.ui.components.CustomIconButton
import com.z1.comparaprecos.common.ui.components.CustomProgressDialog
import com.z1.comparaprecos.common.ui.components.CustomSnackBar
import com.z1.comparaprecos.common.ui.components.ETipoSnackbar
import com.z1.comparaprecos.common.ui.components.Mensagem
import com.z1.comparaprecos.common.ui.extensions.thenIf
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.ui.theme.MediumSeaGreen
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.common.R.dimen
import com.z1.comparaprecos.core.common.R.string
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.feature.listacompra.presentation.viewmodel.ListaCompraViewModel
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun ListaCompraScreen(
    modifier: Modifier = Modifier,
    viewModel: ListaCompraViewModel = hiltViewModel(),
    goToNovaListaCompra: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    when (uiState.isLoading) {
        ELoanding.AGUARDE -> {
            CustomProgressDialog(
                onDismiss = {}
            )
        }

        ELoanding.CRIANDO_COMPRA -> {
            CustomProgressDialog(
                onDismiss = {},
                titulo = string.label_criando_compra
            )
        }

        else -> Unit
    }

    uiState.isError.first?.let { error ->
        when (error) {
            ETypeErrors.TITULO_VAZIO -> Unit
            else -> {
                CustomBottomSheetDialog(
                    modifier = modifier,
                    onDismissRequest = {
                        viewModel.updateError()
                    }
                ) {
                    Text(text = uiState.isError.second)
                }
            }
        }
    }

    LaunchedEffect(key1 = uiState.statusListaCompra) {
        when (uiState.statusListaCompra) {
            EStatusListaCompra.CRIADA -> {
                viewModel.addMensagem(Mensagem(string.label_desc_lista_compra_criada, ETipoSnackbar.SUCESSO))
                scope.launch {
                    scaffoldState.bottomSheetState.hide()
//                goToNovaListaCompra(0)
                }
            }

            EStatusListaCompra.EXCLUIDA -> {
                viewModel.addMensagem(Mensagem(string.label_desc_lista_compra_excluida, ETipoSnackbar.ERRO))
            }

            else -> Unit
        }
    }

    CustomBottomSheet(
        modifier = modifier,
        scaffoldState = scaffoldState,
        tituloBottomSheet = stringResource(id = string.label_nova_lista),
        descricaoBottomSheet = stringResource(id = string.label_desc_nova_compra),
        onFecharBottomSheetClick = {
            scope.launch {
                viewModel.resetCompra()
                scaffoldState.bottomSheetState.hide()
            }
        },
        conteudoBottomSheet = {
            NovaCompraSheet(
                modifier = Modifier
                    .fillMaxHeight(0.94f),
                uiState = uiState,
                onTituloChange = { novoTitulo ->
                    if (novoTitulo.length <= 25) viewModel.updateTitulo(novoTitulo)
                },
                compararListaCompraValueChange = { novoEstado ->
                    viewModel.updateCompararCompra(novoEstado)
                },
                onCriarCompraClick = {
                    val novaListaCompra = ListaCompra(
                        id = 0,
                        titulo = uiState.titulo,
                        isComparar = uiState.compararListaCompra,
                        idListaToComparar = uiState.idListaToComparar,
                        dataCriacao = Instant.now().toEpochMilli()
                    )
                    viewModel.validarNovaCompra(novaListaCompra)
                },
                onListaToCompararClick = {
                    viewModel.updateIdListaToComparar(it)
                }
            )
        },
        conteudoAtrasBottomSheet = {
            OnBackPressed(
                condition = scaffoldState.bottomSheetState.isVisible
            ) {
                scope.launch {
                    viewModel.resetCompra()
                    scaffoldState.bottomSheetState.hide()
                }
            }
            ListaCompra(
                uiState = uiState,
                onClickNovaLista = {
                    scope.launch {
                        viewModel.resetCompra()
                        scaffoldState.bottomSheetState.expand()
                    }
                },
                onCardCompraClick = { compraSelecionada ->
                    viewModel.updateCompraSelecionada(compraSelecionada)
                }
            )

            uiState.listaListaCompraSelecionada?.let {
                CardCompraOpcoes(
                    listaCompraSelecionada = it,
                    uiState = uiState,
                    onDismissRequest = {
                        viewModel.updateCompraSelecionada(null)
                    },
                    onOpcoesClick = { compraSelecionada, icone ->
                        when (icone) {
                            //Abrir lista
                            Icons.Rounded.ArrowForward -> {
                                goToNovaListaCompra(compraSelecionada.id)
                            }

                            //Abrir lista comparando
                            Icons.Rounded.CompareArrows -> {

                            }

                            //Editar lista
                            Icons.Rounded.Edit -> {

                            }

                            // Deletar lista
                            Icons.Rounded.Delete -> {
                                viewModel.deletarListaCompra(compraSelecionada.id)
                            }
                        }
                    }
                )
            }

            uiState.listaMensagem.forEach { mensagem ->
                CustomSnackBar(
                    mensagem = mensagem,
                    onFimShowMensagem = {
                        viewModel.removerMensagem(it)
                    }
                )
            }
        }
    )
}

@Composable
fun ListaCompra(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onClickNovaLista: () -> Unit,
    onCardCompraClick: (ListaCompra) -> Unit
) {
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = uiState.statusListaCompra) {
        if (uiState.statusListaCompra == EStatusListaCompra.CRIADA) listState.animateScrollToItem(
            index = 0
        )
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .statusBarsPadding()
            .systemBarsPadding()
        ,
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                isNotOnTopoLista = listState.firstVisibleItemScrollOffset != 0
            )
        },
        floatingActionButton = {
            AddListaActionButton(
                onClick = onClickNovaLista
            )
        }
    ) { innerPadding ->
        if (uiState.listaCompra.isEmpty() && uiState.isLoading == ELoanding.NOTHING) {
            ListaCompraVazia()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    bottom = dimensionResource(id = dimen.fab_padding_bottom),
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                ),
                state = listState
            ) {
                items(
                    items = uiState.listaCompra,
                    key = { compra -> compra.dataCriacao }) { compra ->
                    CustomCard(
                        modifier = modifier
                            .animateItemPlacement()
                            .padding(
                                vertical = dimensionResource(id = dimen.normal),
                                horizontal = dimensionResource(id = dimen.medium)
                            ),
                        onCardClick = { onCardCompraClick(compra) }
                    ) {
                        CardConteudoCompra(
                            listaCompra = compra,
                            uiState = uiState
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ListaCompraVazia(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = dimen.medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = max(160.dp, with(LocalDensity.current) { 160.sp.toDp() })),
            painter = painterResource(id = R.drawable.bg_lista_vazia),
            contentDescription = null)
        Spacer(modifier = Modifier.height(dimensionResource(id = dimen.medium)))
        Text(
            text = stringResource(id = string.label_desc_lista_compra_vazia),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    isNotOnTopoLista: Boolean
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .thenIf(isNotOnTopoLista) {
                shadow(
                    elevation = 8.dp,
                    clip = true
                )
            },
        title = {
            Text(
                text = stringResource(id = string.label_lista_compras),
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun AddListaActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CustomFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        iconTint = MaterialTheme.colorScheme.onPrimary,
        imageVector = Icons.Rounded.Add
    )
}

@Composable
fun CardConteudoCompra(
    modifier: Modifier = Modifier,
    uiState: UiState,
    listaCompra: ListaCompra,
) {
    var expanded by remember { mutableStateOf(false) }
    val listaComparada = uiState.listaCompra.find { it.id == listaCompra.idListaToComparar }
    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {

        CardCompraTitulo(
            listaCompra = listaCompra,
            expanded = expanded,
            isShowIconButton = listaCompra.isComparar,
            onClick = {
                listaComparada?.let {
                    expanded = !expanded
                }
            }
        )
        CardCompraDetalhes(listaCompra = listaCompra)
        if (listaCompra.isComparar && !expanded) {
            CardListaComparadaIndicador()
        }
        if (expanded) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CustomDivider()
                Text(
                    modifier = Modifier
                        .width(150.dp)
                        .background(MaterialTheme.colorScheme.surface),
                    text = stringResource(id = string.label_lista_comparada_com),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f),
                    textAlign = TextAlign.Center
                )
            }
            listaComparada?.let {
                CardCompraTitulo(
                    listaCompra = it,
                    expanded = expanded,
                    isShowIconButton = false,
                    onClick = {}
                )
                CardCompraDetalhes(listaCompra = it)
                CardListaComparadaIndicador()
            }
        }
    }
}

@Composable
fun CardListaComparadaIndicador(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(dimensionResource(id = dimen.small))
            .fillMaxWidth()
            .background(MediumSeaGreen)
    )
}

@Composable
fun CardCompraTitulo(
    modifier: Modifier = Modifier,
    listaCompra: ListaCompra,
    expanded: Boolean,
    isShowIconButton: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .thenIf(!listaCompra.isComparar) {
                padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    end = 16.dp
                )
            }
            .padding(start = dimensionResource(id = dimen.medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = listaCompra.titulo,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium
        )

        if (isShowIconButton) {
            CustomIconButton(
                onIconButtonClick = onClick,
                iconImageVector =
                if (expanded) Icons.Rounded.ExpandLess
                else Icons.Rounded.ExpandMore,
                iconTint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CardCompraDetalhes(
    modifier: Modifier = Modifier,
    listaCompra: ListaCompra
) {
    Row(
        modifier = modifier.padding(
            start = dimensionResource(id = dimen.medium),
            bottom = dimensionResource(id = dimen.medium),
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = string.label_produtos),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "0",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = string.label_valor),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "R$ 0,00",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun CardCompraOpcoes(
    modifier: Modifier = Modifier,
    listaCompraSelecionada: ListaCompra,
    uiState: UiState,
    onDismissRequest: () -> Unit,
    onOpcoesClick: (ListaCompra, ImageVector) -> Unit
) {
    CustomBottomSheetDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) {

        val items = listOf(
            ListaCompraOpcoes(
                Icons.Rounded.ArrowForward,
                stringResource(id = string.label_abrir_lista)
            ),
            ListaCompraOpcoes(
                Icons.Rounded.CompareArrows,
                stringResource(id = string.label_abrir_lista_comparando)
            ),
            ListaCompraOpcoes(Icons.Rounded.Edit, stringResource(id = string.label_editar_lista)),
            ListaCompraOpcoes(
                Icons.Rounded.Delete,
                stringResource(id = string.label_deletar_lista)
            ),
        )
        val opcoes = items.filter {
            if (!listaCompraSelecionada.isComparar) {
                it.icone != Icons.Rounded.CompareArrows
            } else {
                true
            }
        }

        Column(Modifier.navigationBarsPadding()) {
            CustomCard(
                modifier = Modifier
                    .shadow(
                        elevation = dimensionResource(id = dimen.normal),
//                        spotColor = Color.Green,
                        shape = RoundedCornerShape(dimensionResource(id = dimen.big))
                    ),
                defaultElevetion = dimensionResource(id = dimen.normal),
                onCardClick = {}
            ) {
                CardConteudoCompra(
                    listaCompra = listaCompraSelecionada,
                    uiState = uiState
                )
            }

            opcoes.forEach { item ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        dimensionResource(id = dimen.large)
                    ),
                    modifier = Modifier
                        .clickable {
                            onDismissRequest()
                            onOpcoesClick(listaCompraSelecionada, item.icone)
                        }
                        .clip(RoundedCornerShape(dimensionResource(id = dimen.big)))
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(id = dimen.large),
                            vertical = dimensionResource(id = dimen.medium)
                        ),
                ) {
                    Icon(
                        item.icone,
                        null,
                        tint =
                        if (item.icone == Icons.Rounded.Delete) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        item.titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        color =
                        if (item.icone == Icons.Rounded.Delete) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun NovaCompraSheet(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onTituloChange: (String) -> Unit,
    compararListaCompraValueChange: (Boolean) -> Unit,
    onCriarCompraClick: () -> Unit,
    onListaToCompararClick: (Long) -> Unit,
) {
    val navigationBar = WindowInsets.navigationBars

    Column(
        modifier = modifier
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = dimensionResource(id = R.dimen.medium)),
                    value = uiState.titulo,
                    onValueChange = onTituloChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.headlineSmall,
                    decorationBox = { innerTextField ->
                        if (uiState.titulo.isEmpty()) {
                            Text(
                                text = stringResource(id = string.label_titulo),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.30f)
                            )
                        }
                        innerTextField()
                    }
                )
                Text(
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = dimen.normal),
                            start = dimensionResource(id = R.dimen.medium)
                        ),
                    text = stringResource(id = string.label_campo_obrigatorio),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (uiState.isError.first == ETypeErrors.TITULO_VAZIO) MaterialTheme.colorScheme.error else Color.Transparent
                )
                if (uiState.listaCompra.isNotEmpty()) {
                    Row(
                        Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.medium))
                            .toggleable(value = uiState.compararListaCompra,
                                role = Role.Checkbox,
                                onValueChange = { checked ->
                                    compararListaCompraValueChange(checked)
                                })
                            .wrapContentWidth()
                            .align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = string.label_comparar),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(
                            Modifier
                                .width(dimensionResource(id = dimen.medium))
                        )
                        Checkbox(checked = uiState.compararListaCompra, onCheckedChange = null)
                    }
                }

                if (uiState.compararListaCompra) {
                    OpcoesCompras(
                        uiState = uiState,
                        onListaToCompararClick = { onListaToCompararClick(it) }
                    )
                }
            }

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = navigationBar.getBottom(LocalDensity.current).dp / 2,
                        start = dimensionResource(id = dimen.medium),
                        end = dimensionResource(id = dimen.medium)
                    ),
                containerColor = MaterialTheme.colorScheme.primary,
                titulo = stringResource(id = string.label_criar_compra),
                textColor = MaterialTheme.colorScheme.onPrimary,
                textStyle = MaterialTheme.typography.bodyLarge,
                onClick = onCriarCompraClick
            )
        }
    }
}

@Composable
fun OpcoesCompras(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onListaToCompararClick: (Long) -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = dimensionResource(id = dimen.medium))
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = dimen.medium)
            ),
            text = stringResource(id = string.label_desc_selecionar_lista),
            style = MaterialTheme.typography.titleMedium
        )

        val listaFiltrada = uiState.listaCompra.filter { !it.isComparar }
        LazyHorizontalGrid(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = max(160.dp, with(LocalDensity.current) { 160.sp.toDp() })),
            rows = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = dimen.medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = dimen.medium)),
            contentPadding = PaddingValues(dimensionResource(id = dimen.medium))
        ) {
            items(listaFiltrada) { compra ->
                CustomCard(
                    modifier = Modifier
                        .width(312.dp)
                        .heightIn(min = 56.dp),
                    containerColor =
                    if (compra.id == uiState.idListaToComparar) MediumSeaGreen
                    else MaterialTheme.colorScheme.secondary,
                    onCardClick = {
                        onListaToCompararClick(
                            if (compra.id == uiState.idListaToComparar) -1
                            else compra.id
                        )
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(dimensionResource(id = dimen.normal)),
                            text = compra.titulo,
                            color =
                            if (compra.id == uiState.idListaToComparar) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCardCompraDetalhes() {
    ComparaPrecosTheme {
        CustomCard(onCardClick = {}) {
            CardConteudoCompra(
                listaCompra = ListaCompra(
                    id = 0,
                    titulo = "Compra do mês Ago 23",
                    isComparar = true,
                    idListaToComparar = -1,
                    dataCriacao = Instant.now().toEpochMilli()
                ),
                uiState = UiState()
            )
        }
    }
}