@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package com.z1.comparaprecos.common.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.ui.theme.MediumSeaGreen
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomOutlinedTextInput(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    placeholder: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions
) {

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        isError = isError,
        singleLine = true,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.big)),
        colors = OutlinedTextFieldDefaults.colors(),
        label = {
            Text(text = label)
        },
        placeholder = {
            placeholder?.let {
                Text(text = it)
            }
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation
    )
}

@Composable
fun CustomOutlinedTextInputQuantidade(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onQuantidadeChange: (String) -> Unit,
) {
    var botaoPlusPressionado by remember { mutableStateOf(false) }
    var quantidade = value.toInt()

    CompositionLocalProvider(
        LocalTextInputService provides null
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                visualTransformation = VisualTransformation.None,
                value = " ",
                onValueChange = {},
                singleLine = true,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.big)),
                colors = OutlinedTextFieldDefaults.colors(),
                readOnly = true,
                label = {
                    Text(text = label)
                },
            )

            CustomIconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(
                        top = dimensionResource(id = R.dimen.small),
                        start = dimensionResource(id = R.dimen.normal)
                    ),
                onIconButtonClick = {
                    botaoPlusPressionado = false
                    if (quantidade != 1) quantidade--
                    onQuantidadeChange(quantidade.toString())
                },
                iconImageVector = Icons.Rounded.RemoveCircleOutline,
                iconTint = MaterialTheme.colorScheme.error,
                iconContentDescription = "Remover quantidade"
            )

            Row(modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.small))) {
                CustomTextCounter(
                    counter = quantidade
                )
            }

            CustomIconButton(
                modifier = Modifier

                    .align(Alignment.CenterEnd)
                    .padding(
                        top = dimensionResource(id = R.dimen.small),
                        end = dimensionResource(id = R.dimen.normal)
                    ),
                onIconButtonClick = {
                    botaoPlusPressionado = true
                    quantidade++
                    onQuantidadeChange(quantidade.toString())
                },
                iconImageVector = Icons.Rounded.AddCircleOutline,
                iconTint = MediumSeaGreen,
                iconContentDescription = "Adicionar quantidade"
            )
        }
    }
}

@Preview
@Composable
fun CustomAdicionarQuantidadePreview() {
    ComparaPrecosTheme {
        CustomOutlinedTextInputQuantidade(
            label = "Quantidade",
            onQuantidadeChange = {},
            value = "0"
        )
    }
}

@Preview
@Composable
fun CustomOutlinedTextInputPreview() {
    ComparaPrecosTheme {
        CustomOutlinedTextInput(
            label = "Produto",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            value = "",
            onValueChange = {},
            isError = false
        )
    }
}