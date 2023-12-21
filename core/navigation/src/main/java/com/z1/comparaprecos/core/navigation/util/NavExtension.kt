package com.z1.comparaprecos.core.navigation.util

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.z1.comparaprecos.core.navigation.RegisterNavGraph

fun NavGraphBuilder.register(
    registerNavGraph: RegisterNavGraph,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    registerNavGraph.registerGraph(
        navGraphBuilder = this,
        navController = navController,
        modifier = modifier
    )
}