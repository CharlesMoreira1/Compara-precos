@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)

package com.z1.comparaprecos.feature.listacompra.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
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
import com.z1.comparaprecos.common.ui.components.CustomBottomSheetDialogAviso
import com.z1.comparaprecos.common.ui.components.CustomButton
import com.z1.comparaprecos.common.ui.components.CustomCard
import com.z1.comparaprecos.common.ui.components.CustomDivider
import com.z1.comparaprecos.common.ui.components.CustomFloatingActionButton
import com.z1.comparaprecos.common.ui.components.CustomIconButton
import com.z1.comparaprecos.common.ui.components.CustomRadioButton
import com.z1.comparaprecos.common.ui.components.CustomSnackBar
import com.z1.comparaprecos.common.ui.components.ETipoSnackbar
import com.z1.comparaprecos.common.ui.components.Mensagem
import com.z1.comparaprecos.common.ui.theme.supportsDynamicTheming
import com.z1.comparaprecos.common.util.ThemeOptions
import com.z1.comparaprecos.common.util.UiText
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.common.R.dimen
import com.z1.comparaprecos.core.common.R.string
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.feature.listacompra.model.OpcoesItem
import com.z1.comparaprecos.feature.listacompra.presentation.state.UiEvent
import com.z1.comparaprecos.feature.listacompra.presentation.state.UiState
import com.z1.comparaprecos.common.util.listaTemas
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

    fun hideBottomSheet() {
        scope.launch {
            focusManager.clearFocus()
            keyboardController?.hide()
            delay(500)
            scaffoldState.bottomSheetState.hide()
        }.invokeOnCompletion {
            onEvent(OnEvent.Reset)
        }
    }

    LaunchedEffect(key1 = uiEvent) {
        when (uiEvent) {
            is UiEvent.Inserted -> scope.launch {
                scaffoldState.bottomSheetState.hide()
                onEvent(
                    OnEvent.UpdateUiEvent(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(uiEvent.message),
                            ETipoSnackbar.SUCESSO
                        )
                    )
                )
            }

            is UiEvent.Updated -> scope.launch {
                scaffoldState.bottomSheetState.hide()
                onEvent(
                    OnEvent.UpdateUiEvent(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(uiEvent.message),
                            ETipoSnackbar.SUCESSO
                        )
                    )
                )
            }

            is UiEvent.Deleted -> scope.launch {
                scaffoldState.bottomSheetState.hide()
                onEvent(
                    OnEvent.UpdateUiEvent(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(uiEvent.message),
                            ETipoSnackbar.SUCESSO
                        )
                    )
                )
            }

            is UiEvent.ShowBottomSheet -> scope.launch {
                scaffoldState.bottomSheetState.expand()
            }

            is UiEvent.HideBottomSheet -> hideBottomSheet()

            else -> Unit
        }
    }

    CustomBottomSheet(
        modifier = modifier,
        scaffoldState = scaffoldState,
        tituloBottomSheet = stringResource(id = uiState.tituloBottomSheet),
        descricaoBottomSheet = stringResource(
            id = uiState.descricaoBottomSheet,
            uiState.listaCompraSelecionada?.detalhes?.titulo ?: ""
        ),
        onFecharBottomSheetClick = { onEvent(OnEvent.UpdateUiEvent(UiEvent.HideBottomSheet)) },
        conteudoBottomSheet = {
            NovaListaCompraBottomSheet(
                modifier = Modifier
                    .fillMaxHeight(),
                tituloListaCompra = uiState.tituloListaCompra,
                buttonTitle = stringResource(id = uiState.buttonBottomSheet),
                isEmptyTitle = uiState.isTituloVazio,
                onTituloChange = { novoTitulo ->
                    if (novoTitulo.length <= 25) onEvent(OnEvent.UpdateTituloListaCompra(novoTitulo))
                },
                sheetState = sheetState,
                onClick = { titulo, dataCriacao ->
                    val listaCompra = ListaCompra(
                        id =
                        if (uiState.isRenomearListaCompra) uiState.listaCompraSelecionada?.detalhes?.id ?: 0
                        else 0,
                        titulo = titulo,
                        dataCriacao = dataCriacao
                    )
                    val event = when {
                        uiState.isRenomearListaCompra -> OnEvent.UpdateListaCompra(listaCompra)
                        uiState.isDuplicarListaCompra -> OnEvent.DuplicateListaCompra(
                            listaCompra,
                            uiState.listaCompraSelecionada?.produtos ?: emptyList()
                        )
                        else -> OnEvent.Insert(listaCompra)
                    }
                    onEvent(event)
                }
            )
        },
        conteudoAtrasBottomSheet = {
            OnBackPressed(
                condition = scaffoldState.bottomSheetState.isVisible
            ) {
                onEvent(OnEvent.UpdateUiEvent(UiEvent.HideBottomSheet))
            }
            ListaCompra(
                uiState = uiState,
                onClickNovaLista = { onEvent(OnEvent.UiCreateNewListaCompra) },
                onListaCompraClick = { listaCompraSelecionada ->
                    onEvent(OnEvent.ListaCompraSelecionada(listaCompraSelecionada))
                },
                onChangeThemeClick = {
                    onEvent(OnEvent.UpdateUiEvent(UiEvent.ShowDialogTemas))
                }
            )

            when (uiEvent) {
                is UiEvent.ShowBottomSheetOptions -> {
                    ListaCompraOpcoes(
                        listaCompraSelecionada = uiState.listaCompraSelecionada!!,
                        uiState = uiState,
                        onDismissRequest = {
                            onEvent(OnEvent.UpdateUiEvent(UiEvent.HideBottomSheetOptions))
                        },
                        onOpcoesClick = { listaCompraSelecionada, icone ->
                            when (icone) {
                                //Abrir lista
                                Icons.Rounded.ArrowForward -> {
                                    scope.launch {
                                        goToListaProduto(listaCompraSelecionada.detalhes.id, false)
                                    }
                                }

                                //Abrir lista comparando
                                Icons.Rounded.CompareArrows -> {
                                    scope.launch {
                                        goToListaProduto(listaCompraSelecionada.detalhes.id, true)
                                    }
                                }

                                //Duplicar lista
                                Icons.Rounded.ContentCopy -> {
                                    onEvent(OnEvent.UiDuplicateListaCompra)
                                }

                                //Editar lista
                                Icons.Rounded.Edit -> {
                                    onEvent(OnEvent.UiRenameListaCompra)
                                }

                                // Deletar lista
                                Icons.Rounded.Delete -> {
                                    onEvent(OnEvent.Delete(listaCompraSelecionada.detalhes.id))
                                }
                            }
                        }
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
                        duracao = TimeUnit.SECONDS.toMillis(2),
                        onFimShowMensagem = {
                            onEvent(OnEvent.UpdateUiEvent(UiEvent.Default))
                        }
                    )
                }

                is UiEvent.Error -> {
                    CustomBottomSheetDialogAviso(
                        titulo = stringResource(id = string.label_atencao),
                        mensagem = uiEvent.message.asString(),
                        onDismissRequest = {
                            onEvent(OnEvent.UpdateUiEvent(UiEvent.Default))
                        },
                        onAcaoPositivaClick = {
                            onEvent(OnEvent.UpdateUiEvent(UiEvent.Default))
                        },
                        textoBotaoPositivo = stringResource(id = string.label_entendi)
                    )
                }

                is UiEvent.ShowDialogTemas -> {
                    DialogTemas(
                        atualTheme = uiState.userData.themeId,
                        onSelectedTheme = { themeId ->
                            onEvent(OnEvent.ChangeTheme(themeId))
                        },
                        useDynamicColor = uiState.userData.useDynamicColor,
                        onUseDynamicColorClick = { useDynamicColor ->
                            onEvent(OnEvent.ChangeDynamicColor(useDynamicColor))
                        },
                        darkThemeMode = uiState.userData.darkThemeMode,
                        onDarkThemeModeClick = { darkThemeMode ->
                            onEvent(OnEvent.ChangeDarkThemeMode(darkThemeMode))
                        },
                        onDismiss = {
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
    onListaCompraClick: (ListaCompraWithProdutos) -> Unit,
    onChangeThemeClick: () -> Unit
) {
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
    val listState = rememberLazyListState()
    val isOnTopAppBar by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset != 0 }
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                isNotOnTopoLista = isOnTopAppBar,
                onChangeThemeClick = onChangeThemeClick
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
    isNotOnTopoLista: Boolean,
    onChangeThemeClick: () -> Unit
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
        actions = {
                  CustomIconButton(
                      onIconButtonClick = onChangeThemeClick,
                      iconImageVector = Icons.Outlined.Palette,
                      iconTint = MaterialTheme.colorScheme.onBackground
                  )
        },
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
        containerColor = MaterialTheme.colorScheme.secondary,
        iconTint = MaterialTheme.colorScheme.onPrimary,
        imageVector = Icons.Rounded.Add,
        iconContentDescription = stringResource(id = string.label_criar_lista_compra)
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
            OpcoesItem(
                Icons.Rounded.ArrowForward,
                stringResource(id = string.label_abrir_lista)
            ),
            OpcoesItem(
                Icons.Rounded.CompareArrows,
                stringResource(id = string.label_abrir_lista_comparando)
            ),
            OpcoesItem(
                Icons.Rounded.ContentCopy,
                stringResource(id = string.label_duplicar_lista)
            ),
            OpcoesItem(
                Icons.Rounded.Edit,
                stringResource(id = string.label_renomear_lista)
            ),
            OpcoesItem(
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
fun DialogTemas(
    modifier: Modifier = Modifier,
    atualTheme: Long,
    onSelectedTheme: (Long) -> Unit,
    useDynamicColor: Long,
    onUseDynamicColorClick: (Long) -> Unit,
    darkThemeMode: Long,
    onDarkThemeModeClick: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        modifier = modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { },
        title = {
            Text(
                text = stringResource(string.label_temas),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column {
                CustomDivider()
                AnimatedVisibility(
                    visible = darkThemeMode == 0L && useDynamicColor == 0L,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    DialogTemasOpcoes(
                        atualTheme = atualTheme,
                        onSelectedTheme = onSelectedTheme
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = dimen.medium)))
                AnimatedVisibility(
                    visible =
                    atualTheme == ThemeOptions.DEFAULT.idTema &&
                    supportsDynamicTheming(),
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    DialogTemasDynamicColor(
                        useDynamicColor = useDynamicColor,
                        onUseDynamicColorClick = {
                            onUseDynamicColorClick(it)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = dimen.medium)))
                DialogTemasDarkModePreference(
                    darkThemeMode = darkThemeMode,
                    onDarkThemeModeClick = {
                        onDarkThemeModeClick(it)
                    }
                )
                CustomDivider()
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }
            ) {
                Text(
                    text = stringResource(id = string.label_ok),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
    )
}

@Composable
fun DialogTemasOpcoes(
    modifier: Modifier = Modifier,
    atualTheme: Long,
    onSelectedTheme: (Long) -> Unit
) {
    var selectedTheme by remember {
        mutableLongStateOf(atualTheme)
    }

    LazyColumn(
        modifier = modifier
            .heightIn(min = 150.dp, max = 250.dp)
    ) {
        items(listaTemas) { item ->
            CustomRadioButton(
                item = item,
                selectedOption = selectedTheme,
                onClickListener = {
                    selectedTheme = it
                    onSelectedTheme(it)
                }
            )
        }
    }
}

@Composable
fun DialogTemasDynamicColor(
    modifier: Modifier = Modifier,
    useDynamicColor: Long,
    onUseDynamicColorClick: (Long) -> Unit
) {
    val options = listOf(
        string.label_sim to 1L,
        string.label_nao to 0L
    )

    var selectedOption by remember {
        mutableLongStateOf(useDynamicColor)
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = dimensionResource(id = dimen.normal)),
            text = stringResource(id = string.label_usar_tema_dinamico),
            style = MaterialTheme.typography.titleMedium,
        )
        Column(
            modifier = Modifier.selectableGroup()
        ) {
            CustomRadioButton(
                item = options[0],
                selectedOption = selectedOption,
                onClickListener = {
                    selectedOption = it
                    onUseDynamicColorClick(it)
                }
            )
            CustomRadioButton(
                item = options[1],
                selectedOption = selectedOption,
                onClickListener = {
                    selectedOption = it
                    onUseDynamicColorClick(it)
                }
            )
        }
    }
}

@Composable
fun DialogTemasDarkModePreference(
    modifier: Modifier = Modifier,
    darkThemeMode: Long,
    onDarkThemeModeClick: (Long) -> Unit
) {
    val options = listOf(
        string.label_claro to 0L,
        string.label_escuro to 1L,
        string.label_padrao_sistema to 2L
    )

    var selectedOption by remember {
        mutableLongStateOf(darkThemeMode)
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = dimensionResource(id = dimen.normal)),
            text = stringResource(id = string.label_preferencia_modo_noturno),
            style = MaterialTheme.typography.titleMedium,
        )
        Column(
            modifier = Modifier.selectableGroup()
        ) {
            CustomRadioButton(
                item = options[0],
                selectedOption = selectedOption,
                onClickListener = {
                    selectedOption = it
                    onDarkThemeModeClick(it)
                }
            )
            CustomRadioButton(
                item = options[1],
                selectedOption = selectedOption,
                onClickListener = {
                    selectedOption = it
                    onDarkThemeModeClick(it)
                }
            )
            CustomRadioButton(
                item = options[2],
                selectedOption = selectedOption,
                onClickListener = {
                    selectedOption = it
                    onDarkThemeModeClick(it)
                }
            )
        }
    }
}

@Composable
fun NovaListaCompraBottomSheet(
    modifier: Modifier = Modifier,
    tituloListaCompra: String,
    buttonTitle: String,
    isEmptyTitle: Boolean,
    sheetState: SheetState,
    onTituloChange: (String) -> Unit,
    onClick: (String, Long) -> Unit,
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
                    value = tituloListaCompra,
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
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        if (tituloListaCompra.isEmpty()) {
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
                    color = if (isEmptyTitle) MaterialTheme.colorScheme.error else Color.Transparent
                )
            }

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        dimensionResource(id = dimen.medium)
                    ),
                containerColor = MaterialTheme.colorScheme.primary,
                titulo = buttonTitle,
                textColor = MaterialTheme.colorScheme.onPrimary,
                textStyle = MaterialTheme.typography.bodyLarge,
                onClick = {
                    onClick(tituloListaCompra, Instant.now().toEpochMilli())
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            )
        }
    }
}