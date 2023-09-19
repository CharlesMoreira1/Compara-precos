package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomCheckBox(
    modifier: Modifier = Modifier,
    titulo: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .toggleable(value = value,
                role = Role.Checkbox,
                onValueChange = { checked ->
                    onValueChange(checked)
                }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(
            Modifier
                .width(dimensionResource(id = R.dimen.medium))
        )
        Checkbox(checked = value, onCheckedChange = null)
    }
}