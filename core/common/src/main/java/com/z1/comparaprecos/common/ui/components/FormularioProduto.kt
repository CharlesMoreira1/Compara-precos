package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.ui.components.mask.MascaraPeso
import com.z1.comparaprecos.common.ui.components.mask.MascaraPreco
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.core.common.R
import java.math.BigDecimal

@Composable
fun FormularioProduto(
    modifier: Modifier = Modifier,
    onAdicionarProdutoClick: (String, BigDecimal, BigDecimal, Boolean) -> Unit
) {
    var isPeso by remember {
        mutableStateOf(false)
    }
    var valuePreco by remember {
        mutableStateOf("")
    }

    var valueQuantidade by remember {
        mutableStateOf("")
    }
    if(!isPeso) valueQuantidade = ""

    var nomeProduto by remember {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = isPeso) {
        if (isPeso) focusRequester.requestFocus()
    }

    Card(
        modifier = modifier
            .shadow(
                elevation = dimensionResource(id = R.dimen.normal),
                shape = RoundedCornerShape(
                    topStart = dimensionResource(id = R.dimen.big),
                    topEnd = dimensionResource(id = R.dimen.big),
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp)
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
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.medium)),
            verticalArrangement = Arrangement.Center
        ) {
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
                        nomeProduto = it
                    },
                    value = nomeProduto
                )
                CustomCheckBox(
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.small)),
                    titulo = stringResource(id = R.string.label_peso),
                    value = isPeso,
                    onValueChange = {
                        isPeso = it
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
                            else -> valuePreco = it
                        }
                    },
                    value = valuePreco
                )
                if (isPeso) {
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
                                else -> valueQuantidade = it
                            }
                        },
                        mask = MascaraPeso(),
                        value = valueQuantidade
                    )
                } else {
                    CustomOutlinedTextInputQuantidade(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        label = stringResource(id = R.string.label_quantidade),
                        onQuantidadChange = {
                            valueQuantidade = it
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.medium)))
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primary,
                titulo = stringResource(id = R.string.label_adicionar_produto),
                textColor = MaterialTheme.colorScheme.onPrimary,
                textStyle = MaterialTheme.typography.bodyLarge,
                onClick = {
                    onAdicionarProdutoClick(
                        nomeProduto,
                        BigDecimal(valuePreco).movePointLeft(2),
                        if (isPeso) BigDecimal(valueQuantidade).movePointLeft(3)
                        else BigDecimal(valueQuantidade),
                        isPeso
                    )
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
            onAdicionarProdutoClick = { _, _, _, _ -> }
        )
    }
}