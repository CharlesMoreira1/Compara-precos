package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    containerColor: Color,
    titulo: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier
            .height(dimensionResource(id = R.dimen.button_height)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.big)),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        ),
        onClick = onClick
    ) {
        Text(
            text = titulo,
            color = textColor,
            style = textStyle
        )
    }

}

@Composable
fun CustomOutlinedButton(
    modifier: Modifier = Modifier,
    borderColor: Color,
    titulo: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle,
    onClick: () -> Unit
) {

    OutlinedButton(
        modifier = modifier
            .height(dimensionResource(id = R.dimen.button_height)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.big)),

        border = BorderStroke(1.dp, borderColor),
        onClick = onClick
    ) {
        Text(
            text = titulo,
            color = textColor,
            style = textStyle
        )
    }

}

@Preview
@Composable
fun PreviewButton() {
    ComparaPrecosTheme {
        CustomButton(
            containerColor = MaterialTheme.colorScheme.primary,
            titulo = stringResource(id = R.string.label_criar_lista_compra),
            textColor = MaterialTheme.colorScheme.onPrimary,
            textStyle = MaterialTheme.typography.bodyMedium,
            onClick = {}
        )
    }
}