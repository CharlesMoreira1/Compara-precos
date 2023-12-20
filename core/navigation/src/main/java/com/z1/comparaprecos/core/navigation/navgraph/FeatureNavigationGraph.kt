package com.z1.comparaprecos.core.navigation.navgraph

import com.z1.comparaprecos.core.navigation.navgraph.listacompra.ListaCompraNavGraph
import com.z1.comparaprecos.core.navigation.navgraph.listaproduto.ListaProdutoNavGraph
import com.z1.comparaprecos.core.navigation.navgraph.onboarding.OnboardingNavGraph

interface FeatureNavigationGraph {
    fun onboarding(): OnboardingNavGraph
    fun listaCompra(): ListaCompraNavGraph
    fun listaProduto(): ListaProdutoNavGraph
}