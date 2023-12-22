package com.z1.comparaprecos.common.ui.components


import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomTag(
    modifier: Modifier = Modifier,
    tagContainerColor: Color,
    @StringRes tagText: Int
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .background(tagContainerColor.copy(alpha = 0.1f))
                .padding(vertical = 2.dp, horizontal = 4.dp),
            text = stringResource(id = tagText),
            color = tagContainerColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview
@Composable
fun CustomChipPrev() {
    ComparaPrecosTheme {
        CustomTag(
            tagContainerColor = MaterialTheme.colorScheme.error,
            tagText = R.string.label_exclusivo
        )
    }
}