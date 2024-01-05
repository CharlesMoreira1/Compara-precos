package com.z1.comparaprecos.feature.listaproduto.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.DeleteOutline
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.extensions.removeZerosFromLeft
import com.z1.comparaprecos.common.ui.components.CustomButton
import com.z1.comparaprecos.common.ui.components.CustomCheckBox
import com.z1.comparaprecos.common.ui.components.CustomIconButton
import com.z1.comparaprecos.common.ui.components.CustomOutlinedTextInput
import com.z1.comparaprecos.common.ui.components.CustomOutlinedTextInputQuantidade
import com.z1.comparaprecos.common.ui.components.mask.MascaraPeso
import com.z1.comparaprecos.common.ui.components.mask.MascaraPreco
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.Produto
import java.math.BigDecimal

@Composable
fun FormularioProduto(
    modifier: Modifier = Modifier,
    produtoSelecionado: Produto?,
    idListaCompra: Long,
    onAdicionarProdutoClick: (Produto) -> Unit,
    onDeletarProdutoClick: (Produto) -> Unit,
    onCancelarEdicaoProduto: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    var isProdutoConfirmado by remember {
        mutableStateOf(true)
    }

    val topBarColor by animateColorAsState(
        if (isProdutoConfirmado) MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.tertiary,
        label = "topbar color formulario"
    )

    val onTopBarColor by animateColorAsState(
        if (isProdutoConfirmado) MaterialTheme.colorScheme.onSecondary
        else MaterialTheme.colorScheme.onTertiary,
        label = "on topbar color formulario"
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

    fun resetFormulario() {
        valueNomeProduto = ""
        valueIsPeso = false
        isResetQuantidade = true
        valueQuantidade = "1"
        valuePreco = ""
        focusManager.clearFocus()
    }

    LaunchedEffect(key1 = produtoSelecionado) {
        produtoSelecionado?.run {
            focusManager.clearFocus()
            valueNomeProduto = nomeProduto
            valueIsPeso = isMedidaPeso
            isResetQuantidade = false
            valueQuantidade =
                if (isMedidaPeso) quantidade.removeZerosFromLeft()
                else quantidade.replace(".", "")
            valuePreco = precoUnitario.removeZerosFromLeft()
            isErrorNomeProduto = false
            isErrorPreco = false
            isErrorQuantidade = false
            isProdutoConfirmado = isAlterado
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
            containerColor = MaterialTheme.colorScheme.surface
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
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = produtoSelecionado != null,
                enter = expandVertically(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .background(topBarColor)
                        .padding(
                            vertical = dimensionResource(id = R.dimen.normal)
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIconButton(
                            onIconButtonClick = {
                                focusManager.clearFocus()
                                onCancelarEdicaoProduto()
                             },
                            iconImageVector = Icons.Rounded.Close,
                            iconTint = onTopBarColor,
                            iconContentDescription = "Cancelar edicao"
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.normal)))
                        Text(
                            text = 
                            if (isProdutoConfirmado) stringResource(id = R.string.label_editando_produto)
                            else stringResource(id = R.string.label_confirmarcao_produto),
                            style = MaterialTheme.typography.titleMedium,
                            color = onTopBarColor
                        )
                    }
                    CustomIconButton(
                        modifier = Modifier
                            .padding(end = dimensionResource(id = R.dimen.little)),
                        onIconButtonClick = {
                            onDeletarProdutoClick(produtoSelecionado!!)
                            onCancelarEdicaoProduto()
                        },
                        iconImageVector = Icons.Rounded.DeleteOutline,
                        iconTint = onTopBarColor
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.medium),
                        end = dimensionResource(id = R.dimen.medium),
                        top = dimensionResource(id = R.dimen.medium)
                    ),
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
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.medium)
                    ),
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
                            else -> valuePreco = it
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
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.medium),
                        end = dimensionResource(id = R.dimen.medium),
                        bottom = dimensionResource(id = R.dimen.medium),
                    ),
                containerColor = MaterialTheme.colorScheme.primary,
                titulo =
                when {
                    produtoSelecionado == null -> stringResource(id = R.string.label_adicionar_produto)
                    !isProdutoConfirmado -> stringResource(id = R.string.label_confirmar_produto)
                    else -> stringResource(id = R.string.label_editar_produto)
                },
                textColor = MaterialTheme.colorScheme.onPrimary,
                textStyle = MaterialTheme.typography.bodyLarge,
                onClick = {
                    if (valueNomeProduto.isBlank()) {
                        isErrorNomeProduto = true
                    } else if (valueQuantidade.isBlank() || valueQuantidade == "0") {
                        isErrorQuantidade = true
                    } else {
                        onAdicionarProdutoClick(
                            Produto(
                                id = produtoSelecionado?.id ?: 0,
                                idListaCompra = produtoSelecionado?.idListaCompra
                                    ?: idListaCompra,
                                nomeProduto = valueNomeProduto.trimEnd(),
                                precoUnitario =
                                if (valuePreco.isBlank()) BigDecimal(0).movePointLeft(2)
                                else BigDecimal(valuePreco).movePointLeft(2),
                                quantidade =
                                if (valueIsPeso) BigDecimal(valueQuantidade).movePointLeft(3).toString()
                                else BigDecimal(valueQuantidade).toString(),
                                isMedidaPeso = valueIsPeso,
                                isAlterado = true
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
fun CustomProdutoSelecionadoPreview() {
    ComparaPrecosTheme {
        FormularioProduto(
            produtoSelecionado = Produto(-1, -1, "", "", BigDecimal.ZERO, false, true),
            idListaCompra = -1,
            onAdicionarProdutoClick = { _ -> },
            onCancelarEdicaoProduto = {},
            onDeletarProdutoClick = {_ ->  }
        )
    }
}

@Preview
@Composable
fun CustomProdutoInsertPreview() {
    ComparaPrecosTheme {
        FormularioProduto(
            produtoSelecionado = null,
            idListaCompra = -1,
            onAdicionarProdutoClick = { _ -> },
            onCancelarEdicaoProduto = {},
            onDeletarProdutoClick = {_ ->  }
        )
    }
}