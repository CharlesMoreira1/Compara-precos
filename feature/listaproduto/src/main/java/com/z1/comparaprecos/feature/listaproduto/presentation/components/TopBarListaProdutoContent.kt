package com.z1.comparaprecos.feature.listaproduto.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.ui.components.CustomIconButton
import com.z1.comparaprecos.common.ui.components.CustomTextPriceCounter
import com.z1.comparaprecos.core.common.R
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale

@Composable
fun TituloListaProduto(
    modifier: Modifier = Modifier,
    titulo: String,
    valorLista: BigDecimal?,
    onOrdenarListaClick: () -> Unit
) {
    val currencySymbol by remember {
        mutableStateOf("${Currency.getInstance(Locale.getDefault()).symbol} ")
    }

    Row(
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.normal)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f),
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier
                .wrapContentWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            valorLista?.let {
                Row() {
                    Text(
                        text = currencySymbol,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.titleMedium
                    )

                    CustomTextPriceCounter(
                        price = it,
                        textStyle = MaterialTheme.typography.titleMedium
                    )
                }
            }

            CustomIconButton(
                onIconButtonClick = onOrdenarListaClick,
                iconImageVector = Icons.Rounded.FilterList,
                iconTint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}