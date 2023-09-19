@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class
)

package com.z1.comparaprecos.feature.novalista.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.ShoppingCartCheckout
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.z1.comparaprecos.common.extensions.thenIf
import com.z1.comparaprecos.common.extensions.toMoedaLocal
import com.z1.comparaprecos.common.ui.components.CustomBottomSheetDialog
import com.z1.comparaprecos.common.ui.components.CustomBottomSheetDialogContent
import com.z1.comparaprecos.common.ui.components.CustomCard
import com.z1.comparaprecos.common.ui.components.CustomFloatingActionButton
import com.z1.comparaprecos.common.ui.components.FormularioProduto
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.ui.theme.MediumSeaGreen
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.feature.novalista.presentation.viewmodel.OnEvent
import com.z1.comparaprecos.feature.novalista.presentation.viewmodel.ProdutoViewModel
import kotlinx.coroutines.delay
import java.math.BigDecimal

@Composable
fun NovaListaContainer(
    modifier: Modifier = Modifier,
    idListaCompra: Long
) {
    val viewModel: ProdutoViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Success)
    NovaListaScreen(
        modifier = modifier,
        uiState = uiState,
        uiEvent = uiEvent,
        idListaCompra = idListaCompra,
        onEvent = { viewModel.onEvent(it) }
    )
}

@Composable
fun NovaListaScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    uiEvent: State<UiEvent>,
    idListaCompra: Long,
    onEvent: (OnEvent) -> Unit
) {
    val context = LocalContext.current

    when (uiState.screen) {
        EStatusScreen.CARREGANDO_LISTA_COMPRA -> {
            LoadingScreen()
            onEvent(OnEvent.GetListaCompra(idListaCompra))
        }
        EStatusScreen.LISTA_COMPRA -> {
            ListaProdutoScreen(
                modifier = modifier,
                uiState = uiState,
                onEvent = onEvent
            )
        }
        EStatusScreen.LISTA_COMPRA_COMPARADA -> ListaProdutoComparadoScreen()
    }


    when (val event = uiEvent.value) {
        is UiEvent.Success -> Unit
        is UiEvent.Error ->  {
            CustomBottomSheetDialog(
                onDismissRequest = {

                }
            ) {
                CustomBottomSheetDialogContent(
                    titulo = "Atenção",
                    mensagem = event.message.asString(context),
                    onAcaoPositivaClick = { },
                    textoBotaoPositivo = "Entendi",
                    onAcaoNegativaClick = { },
                    textoBotaoNegativo = "Cancelar"
                )
            }
        }
        is UiEvent.ShowSnackbar -> Unit
        is UiEvent.NavigateUp -> Unit
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
fun ListaProdutoScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onEvent: (OnEvent) -> Unit
) {
    val context = LocalContext.current
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
            onEvent = onEvent,
            onCardProdutoClick = {}
        )
        FormularioProduto(
            onAdicionarProdutoClick = { nomeProduto, precoUnitario, quantidade, medidaProduto ->
                onEvent(
                    OnEvent.InsertProduto(
                    Produto(
                        id = 0,
                        idListaCompra = uiState.listaCompra.detalhes.id,
                        nomeProduto = nomeProduto,
                        precoUnitario = precoUnitario,
                        quantidade = quantidade.toDouble()
                    )
                ))
            }
        )
    }
}

@Composable
fun ListaProdutoComparadoScreen(
    modifier: Modifier = Modifier
) {
    
}

@Composable
fun ListaProduto(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onEvent: (OnEvent) -> Unit,
    onCardProdutoClick: (Produto) -> Unit
) {
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                isNotOnTopoLista = listState.firstVisibleItemScrollOffset != 0
            )
        },
        floatingActionButton = {
            if (uiState.listaCompra.produtos.isNotEmpty()) {
                FimListaActionButton {}
            }
            }
    ) { innerPadding ->
        if (uiState.listaCompra.produtos.isEmpty()) {
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
                    items = uiState.listaCompra.produtos
                    ) { index, produto ->
                    val shape = when {
                        index == 0 -> {
                            RoundedCornerShape(
                                topStart = dimensionResource(id = R.dimen.big),
                                topEnd = dimensionResource(id = R.dimen.big),
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        }
                        index == uiState.listaCompra.produtos.size - 1 -> {
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = dimensionResource(id = R.dimen.big),
                                bottomEnd = dimensionResource(id = R.dimen.big)
                            )
                        }
                        else -> RoundedCornerShape(0.dp)
                    }
                    CustomCard(
                        modifier = modifier
                            .animateItemPlacement()
                            .padding(horizontal = dimensionResource(id = R.dimen.medium)),
                        shape = shape,
                        onCardClick = { onCardProdutoClick(produto) }
                    ) {
                        CardConteudoProduto(
                            produto = produto,
                            uiState = uiState
                        )
                    }
                    Divider(color = MaterialTheme.colorScheme.background)
                }
            }
        }
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
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = dimensionResource(id = R.dimen.medium)),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                Text(
//                    modifier = Modifier
//                        .width(100.dp),
//                    text = "Nome da lista lalalalalalalalalala",
//                    style = MaterialTheme.typography.labelLarge,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Text(
//                    modifier = Modifier
//                        .align(Alignment.Center),
//                    text = "R$ 1.599,99",
//                    style = MaterialTheme.typography.titleLarge
//                )
//            }

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
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = "R$ 999.999,99",
                    textAlign =  TextAlign.End,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        scrollBehavior = scrollBehavior,
    )
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
                        text = produto.quantidade.toInt().toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.minimum)))
                    Text(
                        text = "un",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = produto.precoUnitario.toMoedaLocal(),
                        style = MaterialTheme.typography.bodySmall
                    )
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun Preview() {
    ComparaPrecosTheme {
        CardConteudoProduto(
            uiState = UiState(),
            produto = Produto(
                -1,
                -1,
                "Sabonete com cheiro de rosas coloridas",
                20.0,
                BigDecimal("150.0")
            )
        )
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
            contentDescription = null)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium)))
        Text(
            text = stringResource(id = R.string.label_desc_lista_produto_vazia),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    ComparaPrecosTheme {
        LoadingScreenPreview()
    }
}