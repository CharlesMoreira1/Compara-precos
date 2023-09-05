package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color,
    iconTint: Color,
    imageVector: ImageVector,
    iconContentDescription: String? = null
) {
    FloatingActionButton(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.big)),
        onClick = onClick,
        containerColor = containerColor
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = iconContentDescription,
            tint = iconTint
        )
    }
}

@Preview
@Composable
fun PreviewFloatingActionButton() {
    ComparaPrecosTheme {
        CustomFloatingActionButton(
            onClick = { },
            containerColor = MaterialTheme.colorScheme.primary,
            iconTint = MaterialTheme.colorScheme.onPrimary,
            imageVector = Icons.Rounded.Add
        )
    }
}