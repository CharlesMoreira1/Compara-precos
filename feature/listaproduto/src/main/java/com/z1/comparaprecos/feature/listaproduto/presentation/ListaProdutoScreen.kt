@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.z1.comparaprecos.feature.listaproduto.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.ShoppingCartCheckout
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.z1.comparaprecos.common.extensions.thenIf
import com.z1.comparaprecos.common.extensions.toMoedaLocal
import com.z1.comparaprecos.common.ui.components.CustomBottomSheetDialog
import com.z1.comparaprecos.common.ui.components.CustomBottomSheetDialogContent
import com.z1.comparaprecos.common.ui.components.CustomCard
import com.z1.comparaprecos.common.ui.components.CustomFloatingActionButton
import com.z1.comparaprecos.common.ui.components.CustomSnackBar
import com.z1.comparaprecos.common.ui.components.CustomTextPriceCounter
import com.z1.comparaprecos.common.ui.components.ETipoSnackbar
import com.z1.comparaprecos.common.ui.components.Mensagem
import com.z1.comparaprecos.common.ui.theme.CoralRed
import com.z1.comparaprecos.common.ui.theme.MediumSeaGreen
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.feature.listaproduto.presentation.viewmodel.OnEvent
import kotlinx.coroutines.delay
import java.util.Currency
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun ListaProdutoScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    uiState: UiState,
    uiEvent: UiEvent,
    idListaCompra: Long,
    onEvent: (OnEvent) -> Unit
) {
    val context = LocalContext.current

    AnimatedVisibility(
        visible = uiState.isListaProdutoCarregada,
        enter = scaleIn(animationSpec = tween(300, easing = LinearEasing)) + fadeIn(),
        exit = scaleOut(animationSpec = tween(300, easing = LinearEasing)) + fadeOut()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            ListaProduto(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                uiState = uiState,
                onEvent = onEvent
            )
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

    AnimatedVisibility(
        visible = !uiState.isListaProdutoCarregada,
        enter = fadeIn(),
        exit = slideOutVertically(animationSpec = tween(500, easing = LinearEasing)) { it }
    ) {
        LoadingScreen()
        onEvent(OnEvent.GetListaCompra(idListaCompra))
    }

    when (uiEvent) {
        is UiEvent.Default -> Unit
        is UiEvent.Success -> Unit
        is UiEvent.Error -> {
            CustomBottomSheetDialog(
                onDismissRequest = {
                    onEvent(OnEvent.UpdateQuantidadeProdutoExistente(null))
                }
            ) {
                CustomBottomSheetDialogContent(
                    titulo = stringResource(id = R.string.label_atencao),
                    mensagem = uiState.produtoJaExiste?.run {
                        uiEvent.message.asString(
                            context,
                            nomeProduto,
                            quantidade
                        )
                    } ?: "",
                    textoBotaoPositivo = stringResource(id = R.string.label_sim),
                    onAcaoPositivaClick = {
                        onEvent(OnEvent.UpdateQuantidadeProdutoExistente(uiState.produtoJaExiste))
                    },
                    textoBotaoNegativo = stringResource(id = R.string.label_nao),
                    onAcaoNegativaClick = {
                        onEvent(OnEvent.UpdateQuantidadeProdutoExistente(null))
                    }
                )
            }
        }
        is UiEvent.ShowSnackbar -> {
            val message = Mensagem(
                uiEvent.message.asResId(),
                ETipoSnackbar.SUCESSO
            )
            CustomSnackBar(
                modifier = Modifier.width(220.dp),
                mensagem = message, // aqui vai a event.message do uiEvent (corrigir lógica)
                duracao = TimeUnit.SECONDS.toMillis(2),
                onFimShowMensagem = {
                    onEvent(OnEvent.UpdateUiEvent(UiEvent.Default))
                }
            )
        }
        is UiEvent.Finished -> {
            CustomBottomSheetDialog(
                onDismissRequest = {
                    onEvent(OnEvent.UpdateQuantidadeProdutoExistente(null))
                }
            ) {
                CustomBottomSheetDialogContent(
                    titulo = stringResource(id = R.string.label_atencao),
                    mensagem = stringResource(id = R.string.label_desc_finalizar_lista),
                    textoBotaoPositivo = stringResource(id = R.string.label_sim),
                    onAcaoPositivaClick = {
                        onEvent(OnEvent.UpdateUiEvent(UiEvent.NavigateUp))
                    },
                    textoBotaoNegativo = stringResource(id = R.string.label_nao),
                    onAcaoNegativaClick = {
                        onEvent(OnEvent.UpdateUiEvent(UiEvent.Default))
                    }
                )
            }
        }
        is UiEvent.NavigateUp -> navigateUp()
        else -> Unit
    }
}

@Composable
fun ListaProduto(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onEvent: (OnEvent) -> Unit
) {
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = uiState.listaProduto.size) {
        if (uiState.listaProduto.isNotEmpty()) listState.animateScrollToItem(index = 0)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                uiState = uiState,
                scrollBehavior = scrollBehavior,
                isNotOnTopoLista = listState.firstVisibleItemScrollOffset != 0
            )
        },
        floatingActionButton = {
            if (uiState.listaProduto.isNotEmpty()) {
                FimListaActionButton {
                    onEvent(OnEvent.UpdateUiEvent(UiEvent.Finished))
                }
            }
        }
    ) { innerPadding ->
        if (uiState.listaProduto.isEmpty()) {
            ListaProdutoVazia(
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    bottom = dimensionResource(id = R.dimen.fab_padding_bottom),
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                ),
                state = listState
            ) {
                itemsIndexed(
                    key = { _, produto -> produto.id },
                    items = uiState.listaProduto
                ) { index, produto ->
                    val dismissState = rememberDismissState()

                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        onEvent(OnEvent.DeleteProduto(produto))
                    }

                    val shape = when (index) {
                        0 -> {
                            RoundedCornerShape(
                                topStart = dimensionResource(id = R.dimen.big),
                                topEnd = dimensionResource(id = R.dimen.big),
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        }

                        uiState.listaProduto.size - 1 -> {
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = dimensionResource(id = R.dimen.big),
                                bottomEnd = dimensionResource(id = R.dimen.big)
                            )
                        }

                        else -> RoundedCornerShape(0.dp)
                    }

                    SwipeToDismiss(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.medium))
                            .animateItemPlacement(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ),
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart),
                        background = {
                            BackgroundDismiss(
                                dismissState = dismissState,
                                shape = shape
                            )
                        },
                        dismissContent = {
                            CustomCard(
                                modifier = modifier
                                    .padding(horizontal = 8.dp)
                                    .animateItemPlacement(
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    ),
                                shape = shape,
                                onCardClick = { onEvent(OnEvent.ProdutoSelecionado(produto)) }
                            ) {
                                CardConteudoProduto(
                                    produto = produto,
                                    uiState = uiState
                                )
                            }
                        }
                    )
                    Divider(color = MaterialTheme.colorScheme.background)
                }
            }
        }
    }
}

@Composable
fun BackgroundDismiss(
    modifier: Modifier = Modifier,
    dismissState: DismissState,
    shape: RoundedCornerShape
) {
    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> MaterialTheme.colorScheme.surface
            else -> CoralRed
        },
        label = ""
    )

    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
        label = ""
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color, shape)
            .padding(horizontal = dimensionResource(id = R.dimen.medium)),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            Icons.Rounded.DeleteOutline,
            modifier = Modifier.scale(scale),
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = "Delete Icon",
        )
    }
}

@Composable
fun CardConteudoProduto(
    modifier: Modifier = Modifier,
    produto: Produto,
    uiState: UiState
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = dimensionResource(id = R.dimen.medium),
                horizontal = dimensionResource(id = R.dimen.normal)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = produto.nomeProduto,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text =
                        if (produto.isMedidaPeso) produto.quantidade
                        else produto.quantidade.toInt().toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.minimum)))
                    Text(
                        text =
                        if (produto.isMedidaPeso) stringResource(id = R.string.label_medida_peso)
                        else stringResource(id = R.string.label_medida_unidade),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small)))
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = produto.precoUnitario.toMoedaLocal(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (uiState.listaCompra.isComparar) {
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small)))
                        Row(
                            modifier = Modifier
                                .background(
                                    color = MediumSeaGreen,
                                    shape = RoundedCornerShape(9.dp)
                                )
                                .padding(dimensionResource(id = R.dimen.minimum)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "-0,16%",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = produto.precoProdutoTotal().toMoedaLocal(),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun FimListaActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CustomFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = MediumSeaGreen,
        iconTint = MaterialTheme.colorScheme.onPrimary,
        imageVector = Icons.Rounded.ShoppingCartCheckout
    )
}

@Composable
fun ListaProdutoVazia(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.medium)),
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
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium)))
        Text(
            text = stringResource(id = R.string.label_desc_lista_produto_vazia),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    var isFadingIn by remember { mutableStateOf(true) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val transition = infiniteTransition.animateFloat(
        initialValue = if (isFadingIn) 0f else 1f,
        targetValue = if (isFadingIn) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse // Inverte a animação após cada repetição
        ), label = ""
    )
    LaunchedEffect(transition) {
        while (true) {
            isFadingIn = !isFadingIn
            delay(3000)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.medium)),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .graphicsLayer(alpha = transition.value)
                .fillMaxWidth()
                .heightIn(max = max(160.dp, with(LocalDensity.current) { 160.sp.toDp() })),
            painter = painterResource(id = R.drawable.bg_loading),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium)))
        Text(
            text = "Aguarde um momento.\nEstamos preparando sua lista de compra.",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    uiState: UiState,
    scrollBehavior: TopAppBarScrollBehavior,
    isNotOnTopoLista: Boolean
) {
    val currencySymbol by remember {
        mutableStateOf("${Currency.getInstance(Locale.getDefault()).symbol} ")
    }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.medium)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.5f),
                    text = "Lista compra Set/23",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.2f)
                        .padding(top = dimensionResource(id = R.dimen.small)),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = currencySymbol,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.titleLarge
                    )
                    CustomTextPriceCounter(
                        price = uiState.listaProduto.sumOf { (it.precoProdutoTotal()) },
                        textStyle = MaterialTheme.typography.titleLarge
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),

        )
}