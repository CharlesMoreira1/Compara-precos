@file:OptIn(ExperimentalMaterial3Api::class)

package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(dimensionResource(id = R.dimen.big)),
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
        shape = shape,
        onClick = onCardClick,
        content = { content() }
    )
}