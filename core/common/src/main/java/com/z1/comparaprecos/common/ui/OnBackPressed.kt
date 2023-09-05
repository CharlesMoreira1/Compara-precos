package com.z1.comparaprecos.common.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun OnBackPressed(
    condition: Boolean,
    onBackPressed: () -> Unit
) {
    BackHandler(enabled = condition) {
        onBackPressed()
    }
}