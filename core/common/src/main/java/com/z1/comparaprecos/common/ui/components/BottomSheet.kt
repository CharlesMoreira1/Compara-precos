@file:OptIn(ExperimentalMaterial3Api::class)

package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomBottomSheet(
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState,
    tituloBottomSheet: String,
    descricaoBottomSheet: String = "",
    onFecharBottomSheetClick: () -> Unit,
    conteudoBottomSheet: @Composable () -> Unit,
    conteudoAtrasBottomSheet: @Composable () -> Unit
) {
    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetSwipeEnabled = false,
        sheetDragHandle = null,
        sheetShadowElevation = 2.dp,
        sheetTonalElevation = 0.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        sheetContent = {
            Column {
                TituloBottomSheet(
                    titulo = tituloBottomSheet,
                    descricao = descricaoBottomSheet,
                    onFecharBottomSheetClick = onFecharBottomSheetClick
                )
                CustomDivider()
                conteudoBottomSheet()
            }
        },
        content = { conteudoAtrasBottomSheet() }
    )
}

@Composable
fun TituloBottomSheet(
    modifier: Modifier = Modifier,
    titulo: String,
    descricao: String,
    onFecharBottomSheetClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.medium)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = titulo,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineLarge
            )
            if (descricao.isNotEmpty()) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal)))
                Text(
                    text = descricao,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        CustomIconButton(
            onIconButtonClick = onFecharBottomSheetClick,
            iconImageVector = Icons.Rounded.Close,
            iconTint = MaterialTheme.colorScheme.onSurface
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun PreviewTituloBottomSheet() {
    ComparaPrecosTheme {
        TituloBottomSheet(
            titulo = "Titulo",
            descricao = LoremIpsum(25).values.first(),
            onFecharBottomSheetClick = {}
        )
    }
}