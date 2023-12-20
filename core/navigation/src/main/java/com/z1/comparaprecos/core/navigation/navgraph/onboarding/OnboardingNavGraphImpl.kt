package com.z1.comparaprecos.core.navigation.navgraph.onboarding

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.z1.comparaprecos.core.navigation.navgraph.FeatureNavigationGraph
import com.z1.comparaprecos.core.navigation.navgraph.listacompra.ListaCompraNavGraph
import com.z1.comparaprecos.core.navigation.navgraph.listacompra.ListaCompraNavGraphImpl
import com.z1.feature.onboarding.presentation.OnboardingContainer
import javax.inject.Inject

class OnboardingNavGraphImpl: OnboardingNavGraph {
    private val baseRoute = "onboarding"
    override fun route() = baseRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(baseRoute) {
            val listaCompraNavGraph: ListaCompraNavGraph by lazy { ListaCompraNavGraphImpl() }

            OnboardingContainer(
                modifier = modifier,
                goToListaCompras = {
                    navController.navigate(listaCompraNavGraph.route())
                }
            )
        }
    }
}