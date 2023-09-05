@file:OptIn(ExperimentalMaterial3Api::class)

package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    defaultElevetion: Dp = 0.dp,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onCardClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = defaultElevetion,
//        ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.big)),
        onClick = onCardClick,
        content = { content() }
    )
}