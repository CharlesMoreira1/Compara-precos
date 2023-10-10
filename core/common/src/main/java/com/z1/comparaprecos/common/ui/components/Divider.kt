package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomDivider(
    modifier: Modifier = Modifier,
    padding: Dp = dimensionResource(id = R.dimen.medium),
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f)
) {
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = padding),
        color = color
    )
}