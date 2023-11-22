package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomRadioButton(
    item: Pair<*, Long>,
    selectedOption: Long,
    onClickListener: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onClickListener(item.second)
            }
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.medium),
                vertical = dimensionResource(id = R.dimen.normal)
            ),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium))
    ) {
        RadioButton(
            modifier = Modifier.size(dimensionResource(id = R.dimen.big)),
            selected = (item.second == selectedOption),
            onClick = {
                onClickListener(item.second)
            }
        )

        Text(
            text = when (item.first) {
                is String -> item.first.toString()
                is Int -> stringResource(id = item.first as Int)
                else -> ""
            },
            style = MaterialTheme.typography.bodySmall,
        )
    }
}