@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package com.z1.comparaprecos.common.ui.components

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.ui.theme.CoralRed
import com.z1.comparaprecos.common.ui.theme.MediumSeaGreen
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomOutlinedTextInput(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    mask: VisualTransformation? = null,
    keyboardOptions: KeyboardOptions
) {

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            onValueChange(it)
        },
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
        visualTransformation = mask ?: VisualTransformation.None
    )
}

@Composable
fun CustomOutlinedTextInputQuantidade(
    modifier: Modifier = Modifier,
    label: String,
    onQuantidadChange: (String) -> Unit,
) {
    var botaoPlusPressionado by remember { mutableStateOf(false) }
    var quantidade by remember { mutableIntStateOf(1) }

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

            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.small)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomIconButton(
                    modifier = Modifier.size(22.dp),
                    onIconButtonClick = {
                        botaoPlusPressionado = false
                        if (quantidade != 1) quantidade--
                        onQuantidadChange(quantidade.toString())
                    },
                    iconImageVector = Icons.Rounded.RemoveCircleOutline,
                    iconTint = CoralRed
                )

                AnimatedContent(
                    targetState = quantidade,
                    transitionSpec = {
                        slideIntoContainer(
                            towards =
                            if (botaoPlusPressionado) AnimatedContentTransitionScope.SlideDirection.Up
                            else AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(durationMillis = 500)
                        ) togetherWith
                                slideOutOfContainer(
                                    towards =
                                    if (botaoPlusPressionado) AnimatedContentTransitionScope.SlideDirection.Up
                                    else AnimatedContentTransitionScope.SlideDirection.Down,
                                    animationSpec = tween(
                                        durationMillis = 500,
                                        easing = LinearEasing
                                    )
                                )
                    },
                    contentAlignment = Alignment.Center,
                    label = ""
                ) { targetCount ->
                    Text(
                        modifier = Modifier.width(100.dp),
                        text = targetCount.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                CustomIconButton(
                    modifier = Modifier.size(22.dp),
                    onIconButtonClick = {
                        botaoPlusPressionado = true
                        quantidade++
                        onQuantidadChange(quantidade.toString())
                    },
                    iconImageVector = Icons.Rounded.AddCircleOutline,
                    iconTint = MediumSeaGreen
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomAdicionarQuantidadePreview() {
    ComparaPrecosTheme {
        CustomOutlinedTextInputQuantidade(
            label = "Quantidade",
            onQuantidadChange = {},
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
            onValueChange = {}
        )
    }
}