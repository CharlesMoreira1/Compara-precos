@file:OptIn(ExperimentalFoundationApi::class)

package com.z1.comparaprecos.feature.listaproduto.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.z1.comparaprecos.common.extensions.toMoedaLocal
import com.z1.comparaprecos.common.ui.components.CustomCard
import com.z1.comparaprecos.common.ui.components.CustomTag
import com.z1.comparaprecos.common.ui.theme.CelticBlue
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.ui.theme.MediumSeaGreen
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.testing.data.listaProdutoDataTest2
import java.math.BigDecimal

@Composable
fun ListaProduto(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(dimensionResource(id = R.dimen.medium)),
    listaProduto: List<Produto>,
    listaProdutoComparada: List<Produto> = emptyList(),
    isOnTopOfList: (Boolean) -> Unit,
    onProdutoClick: (Produto) -> Unit,
) {
    val listState = rememberLazyListState()

    val isElevateTopAppBar by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset != 0 }
    }

    LaunchedEffect(key1 = isElevateTopAppBar) {
        isOnTopOfList(isElevateTopAppBar)
    }

    if (listaProduto.isEmpty()) {
        ListaProdutoVazia(
            modifier = modifier.padding(innerPadding),
            message = R.string.label_desc_lista_produto_vazia
        )
    } else {
        LazyColumn(
            modifier = modifier, contentPadding = PaddingValues(
                bottom = dimensionResource(id = R.dimen.fab_padding_bottom),
                top = innerPadding.calculateTopPadding(),
                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
            ), state = listState
        ) {
            itemsIndexed(items = listaProduto,
                key = { _, produto -> produto.id }) { index, produto ->

                val shape = when (index) {
                    0 -> {
                        RoundedCornerShape(
                            topStart = dimensionResource(id = R.dimen.big),
                            topEnd = dimensionResource(id = R.dimen.big),
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    }

                    listaProduto.size - 1 -> {
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = dimensionResource(id = R.dimen.big),
                            bottomEnd = dimensionResource(id = R.dimen.big)
                        )
                    }

                    else -> RoundedCornerShape(0.dp)
                }

                CardItem(modifier = Modifier.animateItemPlacement(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                    produto = produto,
                    index = index,
                    listaProdutoComparada = listaProdutoComparada,
                    shape = shape,
                    onProdutoClick = { onProdutoClick(produto) })
                Divider(color = MaterialTheme.colorScheme.background)
            }
        }
    }
}

@Composable
fun ListaProdutoVazia(
    modifier: Modifier = Modifier, @StringRes message: Int
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
            text = stringResource(id = message),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    produto: Produto,
    index: Int,
    listaProdutoComparada: List<Produto>,
    shape: RoundedCornerShape,
    onProdutoClick: () -> Unit
) {
    val produtoComparado = listaProdutoComparada.find { it.nomeProduto == produto.nomeProduto }

    CustomCard(
        modifier = modifier, shape = shape, onCardClick = onProdutoClick
    ) {
        Row(
            modifier = Modifier.padding(
                    vertical = dimensionResource(id = R.dimen.normal),
                    horizontal = dimensionResource(id = R.dimen.normal)
                ),
        ) {
            Column(
                modifier = Modifier.weight(1.5f), verticalArrangement = Arrangement.Top
            ) {

                Text(
                    text = "${index + 1}º ${produto.nomeProduto}",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        dimensionResource(id = R.dimen.normal)
                    )
                ) {
                    if (produtoComparado == null && listaProdutoComparada.isNotEmpty()) {
                        CustomTag(
                            tagContainerColor = MaterialTheme.colorScheme.secondary,
                            tagText = R.string.label_exclusivo
                        )
                    }

                    if (!produto.isAlterado && listaProdutoComparada.isNotEmpty()) {
                        CustomTag(
                            tagContainerColor = MaterialTheme.colorScheme.tertiary,
                            tagText = R.string.label_confirmar
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.normal)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (produto.isMedidaPeso) produto.quantidade
                        else produto.quantidade.toInt().toString(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Text(
                        text = if (produto.isMedidaPeso) stringResource(id = R.string.label_medida_peso)
                        else stringResource(id = R.string.label_medida_unidade),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Text(
                        text = " = ",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Text(
                        text = produto.valorProduto().toMoedaLocal(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = produto.precoUnitario.toMoedaLocal(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = if (produto.isMedidaPeso) stringResource(id = R.string.label_medida_peso)
                        else stringResource(id = R.string.label_medida_unidade),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                produtoComparado?.let {
                    val diferencaPreco = produto.compararPreco(produtoComparado.precoUnitario)
                    val porcentagemColor = when {
                        diferencaPreco < BigDecimal.ZERO -> MediumSeaGreen
                        diferencaPreco == BigDecimal("0.00") -> CelticBlue
                        else -> MaterialTheme.colorScheme.error
                    }

                    val porcentagem = when {
                        diferencaPreco < BigDecimal.ZERO -> "$diferencaPreco %"
                        diferencaPreco == BigDecimal("0.00") -> "$diferencaPreco %"
                        else -> "+$diferencaPreco %"
                    }
                    Box(
                        modifier = Modifier.clip(
                            RoundedCornerShape(dimensionResource(id = R.dimen.small))
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .background(porcentagemColor.copy(alpha = 0.1f))
                                .padding(
                                    vertical = dimensionResource(id = R.dimen.minimum),
                                    horizontal = dimensionResource(id = R.dimen.little)
                                ),
                            text = porcentagem,
                            color = porcentagemColor,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ListaProdutoPrev() {
    ComparaPrecosTheme {
        ListaProduto(
            listaProduto = listaProdutoDataTest2,
            isOnTopOfList = {},
            onProdutoClick = {}

        )
    }
}