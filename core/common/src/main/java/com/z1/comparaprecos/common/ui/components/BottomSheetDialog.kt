@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.ui.theme.CoralRed
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomBottomSheetDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    var isSheetOpen by rememberSaveable {
        mutableStateOf(true)
    }
        ModalBottomSheet(
            modifier = modifier,
            sheetState = state,
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets(0.dp),
            dragHandle = null,
            onDismissRequest = {
                isSheetOpen = false
                onDismissRequest()
            },
            content = { content() }
        )
}

@Composable
fun CustomBottomSheetDialogContent(
    modifier: Modifier = Modifier,
    titulo: String,
    mensagem: String,
    onAcaoPositivaClick: () -> Unit,
    textoBotaoPositivo: String,
    onAcaoNegativaClick: (() -> Unit)? = null,
    textoBotaoNegativo: String? = null,

) {
    Column(modifier = modifier
        .padding(dimensionResource(id = R.dimen.medium))) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.medium)))
        Text(
            text = mensagem,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.big)))
        CustomButton(
            modifier = Modifier
                .fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary,
            titulo = textoBotaoPositivo,
            textStyle = MaterialTheme.typography.bodyLarge,
            onClick = onAcaoPositivaClick
        )
        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.normal)))
        if (textoBotaoNegativo != null && onAcaoNegativaClick != null) {
            CustomOutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                borderColor = CoralRed,
                textColor = CoralRed,
                titulo = textoBotaoNegativo,
                textStyle = MaterialTheme.typography.bodyLarge,
                onClick = onAcaoNegativaClick
            )
        }
        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.medium)))
    }
}

@Preview
@Composable
fun CustomBottomSheetDialogContentPreview() {
    ComparaPrecosTheme {
        CustomBottomSheetDialogContent(
            titulo = "Atenção",
            mensagem = LoremIpsum(25).values.first(),
            onAcaoPositivaClick = { /*TODO*/ },
            textoBotaoPositivo = "Aceitar",
            onAcaoNegativaClick = { /*TODO*/ },
            textoBotaoNegativo = "Cancelar"
        )
    }
}