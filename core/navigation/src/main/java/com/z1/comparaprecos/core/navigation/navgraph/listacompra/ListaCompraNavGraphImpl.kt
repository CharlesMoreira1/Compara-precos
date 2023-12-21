package com.z1.comparaprecos.core.navigation.navgraph.listacompra

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.z1.comparaprecos.core.navigation.navgraph.FeatureNavigationGraph
import com.z1.comparaprecos.core.navigation.navgraph.listaproduto.ListaProdutoNavGraph
import com.z1.comparaprecos.core.navigation.navgraph.listaproduto.ListaProdutoNavGraphImpl
import com.z1.comparaprecos.core.navigation.navgraph.onboarding.OnboardingNavGraph
import com.z1.comparaprecos.core.navigation.navgraph.onboarding.OnboardingNavGraphImpl
import com.z1.comparaprecos.feature.listacompra.presentation.ListaCompraContainer
import javax.inject.Inject

class ListaCompraNavGraphImpl: ListaCompraNavGraph {
    private val baseRoute = "lista_compra"

    override fun route() = baseRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(baseRoute) {
            val onboardingNavGraph: OnboardingNavGraph by lazy { OnboardingNavGraphImpl() }
            val listaProdutoNavGraph: ListaProdutoNavGraph by lazy { ListaProdutoNavGraphImpl() }
            ListaCompraContainer(
                modifier = modifier,
                goToOnboarding = {
                   navController.navigate(onboardingNavGraph.route())
                },
                goToListaProduto = { idListaCompra, isComparar ->
                    navController.navigate(listaProdutoNavGraph.route(idListaCompra, isComparar))
                }
            )
        }
    }
}