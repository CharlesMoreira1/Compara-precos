package com.z1.comparaprecos.feature.listaproduto.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.ui.components.CustomButton
import com.z1.comparaprecos.common.ui.components.CustomCheckBox
import com.z1.comparaprecos.common.ui.components.CustomIconButton
import com.z1.comparaprecos.common.ui.components.CustomOutlinedTextInput
import com.z1.comparaprecos.common.ui.components.CustomOutlinedTextInputQuantidade
import com.z1.comparaprecos.common.ui.components.mask.MascaraPeso
import com.z1.comparaprecos.common.ui.components.mask.MascaraPreco
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.ui.theme.CoolMint
import com.z1.comparaprecos.common.util.UiText
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.Produto
import java.math.BigDecimal

@Composable
fun FormularioProduto(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAdicionarProdutoClick: (Produto) -> Unit,
    onCancelarEdicaoProduto: () -> Unit
) {
    val context = LocalContext.current
    val produtoSelecionado = uiState.produtoSelecionado

    val containerColor by animateColorAsState(
        when (produtoSelecionado) {
            null -> MaterialTheme.colorScheme.surface
            else -> CoolMint
        },
        label = "backgroundcolor formulario"
    )

    var valueIsPeso by remember {
        mutableStateOf(false)
    }

    var isErrorPreco by remember {
        mutableStateOf(false)
    }
    var valuePreco by remember {
        mutableStateOf("")
    }

    var isResetQuantidade by remember {
        mutableStateOf(false)
    }
    var isErrorQuantidade by remember {
        mutableStateOf(false)
    }
    var valueQuantidade by remember {
        mutableStateOf("1")
    }

    if (!valueIsPeso && produtoSelecionado == null) valueQuantidade = "1"

    var isErrorNomeProduto by remember {
        mutableStateOf(false)
    }
    var valueNomeProduto by remember {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = valueIsPeso) {
        //if (valueIsPeso) focusRequester.requestFocus()
    }

    fun resetFormulario() {
        valueNomeProduto = ""
        valueIsPeso = false
        isResetQuantidade = true
        valueQuantidade = "1"
        valuePreco = ""
    }

    LaunchedEffect(key1 = produtoSelecionado) {
        produtoSelecionado?.run {
            valueNomeProduto = nomeProduto
            valueIsPeso =
                medida == UiText.StringResource(R.string.label_medida_peso).asString(context)
            isResetQuantidade = false
            valueQuantidade = quantidade.replace(".", "")
            valuePreco = precoUnitario.toString().replace(".", "")

            isErrorNomeProduto = false
            isErrorPreco = false
            isErrorQuantidade = false
        } ?: resetFormulario()
    }

    Card(
        modifier = modifier
            .shadow(
                elevation = dimensionResource(id = R.dimen.normal),
                shape = RoundedCornerShape(
                    topStart = dimensionResource(id = R.dimen.big),
                    topEnd = dimensionResource(id = R.dimen.big),
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(
            topStart = dimensionResource(id = R.dimen.big),
            topEnd = dimensionResource(id = R.dimen.big),
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.medium)),
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = produtoSelecionado != null,
                enter = expandVertically(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(id = R.dimen.normal)
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CustomIconButton(
                        onIconButtonClick = onCancelarEdicaoProduto,
                        iconImageVector = Icons.Rounded.Close,
                        iconTint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.normal)))
                    Text(
                        text = "Editando produto",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomOutlinedTextInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(end = dimensionResource(id = R.dimen.medium)),
                    label = pluralStringResource(
                        id = R.plurals.label_plural_produto,
                        count = 0
                    ),
                    placeholder = stringResource(id = R.string.label_esse_produto_e),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = {
                        valueNomeProduto = it
                        isErrorNomeProduto = false
                    },
                    isError = isErrorNomeProduto,
                    value = valueNomeProduto
                )
                CustomCheckBox(
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.small)),
                    titulo = stringResource(id = R.string.label_peso),
                    value = valueIsPeso,
                    onValueChange = {
                        valueQuantidade = if (valueIsPeso) "1" else ""
                        valueIsPeso = it
                    }
                )
            }

            Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.medium)))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomOutlinedTextInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(end = dimensionResource(id = R.dimen.medium)),
                    label = stringResource(id = R.string.label_valor),
                    mask = MascaraPreco(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = {
                        when {
                            it.length == 1 && it == "0" -> Unit
                            it.isBlank() -> valuePreco = ""
                            it.length > 8 -> Unit
                            else -> {
                                valuePreco = it
                                isErrorPreco = false
                            }
                        }
                    },
                    isError = isErrorPreco,
                    value = valuePreco
                )
                if (valueIsPeso) {
                    CustomOutlinedTextInput(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .focusRequester(focusRequester),
                        label = stringResource(id = R.string.label_peso),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done
                        ),
                        onValueChange = {
                            when {
                                it.length == 1 && it == "0" -> Unit
                                it.isBlank() -> valueQuantidade = ""
                                it.length > 6 -> Unit
                                else -> {
                                    valueQuantidade = it
                                    isErrorQuantidade = false
                                }
                            }
                        },
                        mask = MascaraPeso(),
                        value = valueQuantidade,
                        isError = isErrorQuantidade
                    )
                } else {
                    CustomOutlinedTextInputQuantidade(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        label = stringResource(id = R.string.label_quantidade),
                        value = valueQuantidade,
                        onQuantidadeChange = {
                            valueQuantidade = it
                            isResetQuantidade = false
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.medium)))
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primary,
                titulo =
                if (produtoSelecionado == null) stringResource(id = R.string.label_adicionar_produto)
                else stringResource(id = R.string.label_editar_produto),
                textColor = MaterialTheme.colorScheme.onPrimary,
                textStyle = MaterialTheme.typography.bodyLarge,
                onClick = {
                    if (valueNomeProduto.isBlank()) {
                        isErrorNomeProduto = true
                    } else if (valuePreco.isBlank() || valuePreco == "0") {
                        isErrorPreco = true
                    } else if (valueQuantidade.isBlank() || valueQuantidade == "0") {
                        isErrorQuantidade = true
                    } else {
                        onAdicionarProdutoClick(
                            Produto(
                                id = produtoSelecionado?.id ?: 0,
                                idListaCompra = produtoSelecionado?.idListaCompra
                                    ?: uiState.listaCompra.id,
                                nomeProduto = valueNomeProduto,
                                precoUnitario = BigDecimal(valuePreco).movePointLeft(2),
                                quantidade =
                                if (valueIsPeso) BigDecimal(valueQuantidade).movePointLeft(3).toString()
                                else BigDecimal(valueQuantidade).toString(),
                                medida =
                                if (valueIsPeso) UiText.StringResource(R.string.label_medida_peso)
                                    .asString(context)
                                else UiText.StringResource(R.string.label_medida_unidade)
                                    .asString(context)
                            )
                        )
                        resetFormulario()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun CustomProdutoInsertPreview() {
    ComparaPrecosTheme {
        FormularioProduto(
            uiState = UiState(),
            onAdicionarProdutoClick = { Produto -> },
            onCancelarEdicaoProduto = {}
        )
    }
}