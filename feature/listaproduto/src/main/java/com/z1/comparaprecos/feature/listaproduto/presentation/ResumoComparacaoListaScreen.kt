package com.z1.comparaprecos.feature.listaproduto.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.extensions.getPercentageDifference
import com.z1.comparaprecos.common.extensions.toMoedaLocal
import com.z1.comparaprecos.common.ui.components.CustomCard
import com.z1.comparaprecos.common.ui.components.CustomDivider
import com.z1.comparaprecos.common.ui.theme.CelticBlue
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.ui.theme.CoralRed
import com.z1.comparaprecos.common.ui.theme.MediumSeaGreen
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.core.common.R
import java.math.BigDecimal

@Composable
fun ResumoComparacaoListaScreen(
    modifier: Modifier = Modifier,
    listaProduto: Pair<String, List<Produto>>,
    listaProdutoComparada: Pair<String, List<Produto>>
) {
    CustomCard(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(
                bottom = dimensionResource(id = R.dimen.fab_padding_bottom),
                top = dimensionResource(id = R.dimen.medium),
                start = dimensionResource(id = R.dimen.medium),
                end = dimensionResource(id = R.dimen.medium)
            )
            .fillMaxWidth(),
        onCardClick = {}
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(id = R.dimen.medium)
                )
        ) {
            DetalhesLista(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.medium)
                    ),
                listaProduto = listaProduto
            )
            ListaComparadaCom()
            DetalhesLista(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.medium)
                    ),
                listaProduto = listaProdutoComparada
            )
            CustomDivider(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(id = R.dimen.medium)
                )
            )
            SomaProdutosQueEstaoNasDuasListas(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.medium)
                    ),
                listaProduto = listaProduto.second,
                listaProdutoComparada = listaProdutoComparada.second
            )
            CustomDivider(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(id = R.dimen.medium)
                    )
            )
            SomaTotal(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.medium)
                    ),
                listaProduto = listaProduto.second,
                listaProdutoComparada = listaProdutoComparada.second
            )
        }
    }
}

@Composable
fun DetalhesLista(
    modifier: Modifier = Modifier,
    listaProduto: Pair<String, List<Produto>>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = listaProduto.first,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val qtdProdutos = listaProduto.second.size
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = dimensionResource(id = R.dimen.medium)),
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
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal)))
                Text(
                    text = qtdProdutos.toString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            val qtdItens = listaProduto.second.sumOf { if (!it.isMedidaPeso) it.quantidade.toInt() else 1 }
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
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal)))
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
                    text = stringResource(id = R.string.label_valor),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = listaProduto.second.sumOf { it.valorProduto() }.toMoedaLocal(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun ListaComparadaCom(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CustomDivider(
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(id = R.dimen.medium)
                )
        )
        Text(
            modifier = Modifier
                .width(150.dp)
                .background(MaterialTheme.colorScheme.surface),
            text = stringResource(id = R.string.label_lista_comparada_com),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SomaProdutosQueEstaoNasDuasListas(
    modifier: Modifier = Modifier,
    listaProduto: List<Produto>,
    listaProdutoComparada: List<Produto>
) {
    val listaProdutoIgual = listaProduto.filter { produto ->
        listaProdutoComparada.any { produtoComparado ->
            produto.nomeProduto == produtoComparado.nomeProduto
        }
    }
    val listaProdutoComparadaIgual = listaProdutoComparada.filter { produtoComparado ->
        listaProduto.any { produto ->
            produto.nomeProduto == produtoComparado.nomeProduto
        }
    }
    val listaProdutoIgualQtd = listaProdutoIgual.size
    val valorListaAtual = listaProdutoIgual.sumOf { it.valorProduto() }
    val valorListaComparada = listaProdutoComparadaIgual.sumOf { it.valorProduto() }
    val diferencaPreco = valorListaAtual.minus(valorListaComparada)
    val diferencaPorcentagem = valorListaAtual.getPercentageDifference(valorListaComparada)

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Produtos que estão nas duas listas: $listaProdutoIgualQtd",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(
            modifier = modifier.padding(
                vertical = dimensionResource(id = R.dimen.medium),
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Atual",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal)))
                Text(
                    text = valorListaAtual.toMoedaLocal(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.medium))
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Comparada",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal)))
                Text(
                    text = valorListaComparada.toMoedaLocal(),
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
                    text = diferencaPreco.toMoedaLocal(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                val color = when {
                    diferencaPreco < BigDecimal.ZERO -> MediumSeaGreen
                    diferencaPreco == BigDecimal("0.00") -> CelticBlue
                    else -> CoralRed
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal)))
                Text(
                    text = diferencaPorcentagem,
                    color = color,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        val texto = when {
            diferencaPreco < BigDecimal.ZERO -> "Você economizou em relação a lista comparada somando somente os mesmos produtos. Parabéns!"
            diferencaPreco == BigDecimal("0.00") -> "Os preços estão iguais nas duas listas."
            else -> "Você gastou mais em relação a lista comparada somando somente os mesmos produtos."
        }
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SomaTotal(
    modifier: Modifier = Modifier,
    listaProduto: List<Produto>,
    listaProdutoComparada: List<Produto>
) {

    val valorListaAtual = listaProduto.sumOf { it.valorProduto() }
    val valorListaComparada = listaProdutoComparada.sumOf { it.valorProduto() }
    val diferencaPreco = valorListaAtual.minus(valorListaComparada)
    val diferencaPorcentagem = valorListaAtual.getPercentageDifference(valorListaComparada)

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Somando todos os produtos da lista",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(
            modifier = modifier.padding(
                vertical = dimensionResource(id = R.dimen.medium),
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Atual",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal)))
                Text(
                    text = valorListaAtual.toMoedaLocal(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = dimensionResource(id = R.dimen.medium)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Comparada",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal)))
                Text(
                    text = valorListaComparada.toMoedaLocal(),
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
                    text = diferencaPreco.toMoedaLocal(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                val color = when {
                    diferencaPreco < BigDecimal.ZERO -> MediumSeaGreen
                    diferencaPreco == BigDecimal("0.00") -> CelticBlue
                    else -> CoralRed
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal)))
                Text(
                    text = diferencaPorcentagem,
                    color = color,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        val texto = when {
            diferencaPreco < BigDecimal.ZERO -> "Você economizou em relação a lista comparada somando todos os produtos. Parabéns!"
            diferencaPreco == BigDecimal("0.00") -> "Os preços estão iguais nas duas listas."
            else -> "Você gastou mais em relação a lista comparada somando todos os produtos."
        }
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
fun ResumoComparacaoListaScreenPreview() {
    ComparaPrecosTheme {
        ResumoComparacaoListaScreen(
            listaProduto = "Lista compra out/23" to emptyList(),
            listaProdutoComparada = "Lista compra set/23" to emptyList()
        )
    }
}