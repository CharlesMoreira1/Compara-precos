package com.z1.comparaprecos.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.z1.comparaprecos.core.navigation.navgraph.FeatureNavigationGraph
import com.z1.comparaprecos.core.navigation.util.register

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    featureNavigationGraph: FeatureNavigationGraph
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = featureNavigationGraph.onboarding().route()
    ) {
        register(
            registerNavGraph = featureNavigationGraph.onboarding(),
            navController = navController,
            modifier = modifier
        )

        register(
            registerNavGraph = featureNavigationGraph.listaCompra(),
            navController = navController,
            modifier = modifier
        )

        register(
            registerNavGraph = featureNavigationGraph.listaProduto(),
            navController = navController,
            modifier = modifier
        )
    }
}