package com.z1.comparaprecos.core.navigation.navgraph

import com.z1.comparaprecos.core.navigation.navgraph.listacompra.ListaCompraNavGraph
import com.z1.comparaprecos.core.navigation.navgraph.listaproduto.ListaProdutoNavGraph
import com.z1.comparaprecos.core.navigation.navgraph.onboarding.OnboardingNavGraph
import javax.inject.Inject

class FeatureNavigationGraphImpl @Inject constructor(
    private val onboardingNavGraph: OnboardingNavGraph,
    private val listaCompraNavGraph: ListaCompraNavGraph,
    private val listaProdutoNavGraph: ListaProdutoNavGraph
): FeatureNavigationGraph {
    override fun onboarding() = onboardingNavGraph
    override fun listaCompra() = listaCompraNavGraph
    override fun listaProduto() = listaProdutoNavGraph
}