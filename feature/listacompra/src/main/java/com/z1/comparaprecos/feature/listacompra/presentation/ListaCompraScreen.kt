@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)

package com.z1.comparaprecos.feature.listacompra.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material.icons.rounded.CopyAll
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.z1.comparaprecos.common.extensions.thenIf
import com.z1.comparaprecos.common.extensions.toMoedaLocal
import com.z1.comparaprecos.common.ui.OnBackPressed
import com.z1.comparaprecos.common.ui.components.CustomBottomSheet
import com.z1.comparaprecos.common.ui.components.CustomBottomSheetDialog
import com.z1.comparaprecos.common.ui.components.CustomButton
import com.z1.comparaprecos.common.ui.components.CustomCard
import com.z1.comparaprecos.common.ui.components.CustomFloatingActionButton
import com.z1.comparaprecos.common.ui.components.CustomSnackBar
import com.z1.comparaprecos.common.ui.components.ETipoSnackbar
import com.z1.comparaprecos.common.ui.components.Mensagem
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.common.util.UiText
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.common.R.dimen
import com.z1.comparaprecos.core.common.R.string
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.feature.listacompra.presentation.viewmodel.OnEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.concurrent.TimeUnit

@Composable
fun ListaCompraScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    uiEvent: UiEvent,
    onEvent: (OnEvent) -> Unit,
    goToListaProduto: (Long, Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = uiEvent) {
        when (uiEvent) {
            is UiEvent.Success -> {
                scope.launch {
                    scaffoldState.bottomSheetState.hide()
                    onEvent(
                        OnEvent.UpdateUiEvent(
                            UiEvent.ShowSnackbar(
                                UiText.StringResource(string.label_desc_lista_compra_criada)
                            )
                        )
                    )
                }
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
                focusManager.clearFocus()
                keyboardController?.hide()
                delay(500)
                scaffoldState.bottomSheetState.hide()
            }.invokeOnCompletion {
                onEvent(OnEvent.Reset)
            }
        },
        conteudoBottomSheet = {
            NovaListaCompraBottomSheet(
                modifier = Modifier
                    .fillMaxHeight(),
                uiState = uiState,
                onTituloChange = { novoTitulo ->
                    if (novoTitulo.length <= 25) onEvent(OnEvent.UpdateTituloListaCompra(novoTitulo))
                },
                sheetState = sheetState,
                onCriarCompraClick = {
                    val novaListaCompra = ListaCompra(
                        id = 0,
                        titulo = uiState.titulo,
                        isComparar = uiState.compararListaCompra,
                        idListaToComparar = uiState.idListaToComparar,
                        dataCriacao = Instant.now().toEpochMilli()
                    )
                    onEvent(OnEvent.Insert(novaListaCompra))
                }
            )

        },
        conteudoAtrasBottomSheet = {
            OnBackPressed(
                condition = scaffoldState.bottomSheetState.isVisible
            ) {
                scope.launch {
                    onEvent(OnEvent.Reset)
                    scaffoldState.bottomSheetState.hide()
                }
            }
            ListaCompra(
                uiState = uiState,
                onClickNovaLista = {
                    scope.launch {
                        onEvent(OnEvent.Reset)
                        scaffoldState.bottomSheetState.expand()
                    }
                },
                onListaCompraClick = { listaCompraSelecionada ->
                    onEvent(OnEvent.ListaCompraSelecionada(listaCompraSelecionada))
                }
            )


            uiState.listaCompraSelecionada?.let {
                ListaCompraOpcoes(
                    listaCompraSelecionada = it,
                    uiState = uiState,
                    onDismissRequest = {
                        onEvent(OnEvent.ListaCompraSelecionada(null))
                    },
                    onOpcoesClick = { compraSelecionada, icone ->
                        when (icone) {
                            //Abrir lista
                            Icons.Rounded.ArrowForward -> {
                                scope.launch {
                                    delay(100)
                                    goToListaProduto(compraSelecionada.detalhes.id, false)
                                }
                            }

                            //Abrir lista comparando
                            Icons.Rounded.CompareArrows -> {
                                scope.launch {
                                    delay(100)
                                    goToListaProduto(compraSelecionada.detalhes.id, true)
                                }
                            }

                            //Duplicar lista
                            Icons.Rounded.CopyAll -> {

                            }

                            //Editar lista
                            Icons.Rounded.Edit -> {

                            }

                            // Deletar lista
                            Icons.Rounded.Delete -> {
                                onEvent(OnEvent.Delete(compraSelecionada.detalhes.id))
                            }
                        }
                    }
                )
            }


            when (uiEvent) {
                is UiEvent.ShowSnackbar -> {
                    val message = Mensagem(
                        uiEvent.message.asResId(),
                        ETipoSnackbar.SUCESSO
                    )
                    CustomSnackBar(
                        modifier = Modifier.fillMaxWidth(),
                        mensagem = message, // aqui vai a event.message do uiEvent (corrigir lógica)
                        duracao = TimeUnit.SECONDS.toMillis(2),
                        onFimShowMensagem = {
                            onEvent(OnEvent.UpdateUiEvent(UiEvent.Default))
                        }
                    )
                }

                else -> Unit
            }
        }
    )
}

@Composable
fun ListaCompra(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onClickNovaLista: () -> Unit,
    onListaCompraClick: (ListaCompraWithProdutos) -> Unit
) {
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
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
        if (uiState.listaCompra.isEmpty()) {
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
                    key = { listacompra -> listacompra.detalhes.id }) { compra ->
                    CustomCard(
                        modifier = modifier
                            .animateItemPlacement()
                            .padding(
                                vertical = dimensionResource(id = dimen.normal),
                                horizontal = dimensionResource(id = dimen.medium)
                            ),
                        onCardClick = { onListaCompraClick(compra) }
                    ) {
                        CardConteudoCompra(
                            listaCompra = compra
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
            contentDescription = null
        )
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
    listaCompra: ListaCompraWithProdutos,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = dimen.medium)),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = listaCompra.detalhes.titulo,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(
            modifier = modifier.padding(
                start = dimensionResource(id = dimen.medium),
                bottom = dimensionResource(id = dimen.medium),
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val qtdProdutos = listaCompra.produtos.size
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = pluralStringResource(
                        id = R.plurals.label_plural_produto,
                        count = qtdProdutos
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = qtdProdutos.toString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            val qtdItens = listaCompra.produtos.sumOf { if (!it.isMedidaPeso) it.quantidade.toInt() else 1 }
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = pluralStringResource(
                        id = R.plurals.label_plural_item,
                        count = qtdItens
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = qtdItens.toString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = string.label_valor),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = listaCompra.valorTotal().toMoedaLocal(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun ListaCompraOpcoes(
    modifier: Modifier = Modifier,
    listaCompraSelecionada: ListaCompraWithProdutos,
    uiState: UiState,
    onDismissRequest: () -> Unit,
    onOpcoesClick: (ListaCompraWithProdutos, ImageVector) -> Unit
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
            ListaCompraOpcoes(
                Icons.Rounded.CopyAll,
                stringResource(id = string.label_duplicar_lista)
            ),
            ListaCompraOpcoes(Icons.Rounded.Edit, stringResource(id = string.label_editar_lista)),
            ListaCompraOpcoes(
                Icons.Rounded.Delete,
                stringResource(id = string.label_deletar_lista)
            ),
        )
        val opcoes = items.filter {
            if (uiState.listaCompra.size == 1) {
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
                onCardClick = {}
            ) {
                CardConteudoCompra(
                    listaCompra = listaCompraSelecionada
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
fun NovaListaCompraBottomSheet(
    modifier: Modifier = Modifier,
    uiState: UiState,
    sheetState: SheetState,
    onTituloChange: (String) -> Unit,
    onCriarCompraClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = sheetState.isVisible) {
        if (sheetState.isVisible) focusRequester.requestFocus()
        else {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }
    Box {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .heightIn(max = max(160.dp, with(LocalDensity.current) { 160.sp.toDp() })),
            painter = painterResource(id = R.drawable.bg_lista_compra),
            contentDescription = null
        )
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                BasicTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .padding(start = dimensionResource(id = R.dimen.medium)),
                    value = uiState.titulo,
                    onValueChange = onTituloChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
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
                    color = if (uiState.isTituloVazio) MaterialTheme.colorScheme.error else Color.Transparent
                )
            }

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        dimensionResource(id = dimen.medium)
                    ),
                containerColor = MaterialTheme.colorScheme.primary,
                titulo = stringResource(id = string.label_criar_lista_compra),
                textColor = MaterialTheme.colorScheme.onPrimary,
                textStyle = MaterialTheme.typography.bodyLarge,
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onCriarCompraClick()
                }
            )
        }
    }
}