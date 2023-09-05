package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    onIconButtonClick: () -> Unit,
    iconImageVector: ImageVector,
    iconTint: Color,
    iconContentDescription: String? = null
) {
    IconButton(
        modifier = modifier,
        onClick = onIconButtonClick
    ) {
        Icon(
            imageVector = iconImageVector,
            tint = iconTint,
            contentDescription = iconContentDescription
        )
    }
}