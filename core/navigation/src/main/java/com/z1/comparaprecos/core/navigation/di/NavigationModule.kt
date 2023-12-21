package com.z1.comparaprecos.core.navigation.di

import com.z1.comparaprecos.core.navigation.navgraph.FeatureNavigationGraph
import com.z1.comparaprecos.core.navigation.navgraph.FeatureNavigationGraphImpl
import com.z1.comparaprecos.core.navigation.navgraph.listacompra.ListaCompraNavGraph
import com.z1.comparaprecos.core.navigation.navgraph.listacompra.ListaCompraNavGraphImpl
import com.z1.comparaprecos.core.navigation.navgraph.listaproduto.ListaProdutoNavGraph
import com.z1.comparaprecos.core.navigation.navgraph.listaproduto.ListaProdutoNavGraphImpl
import com.z1.comparaprecos.core.navigation.navgraph.onboarding.OnboardingNavGraph
import com.z1.comparaprecos.core.navigation.navgraph.onboarding.OnboardingNavGraphImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NavigationModule {

    @Provides
    @Singleton
    fun provideOnboardingNavGraph(
    ): OnboardingNavGraph = OnboardingNavGraphImpl()

    @Provides
    @Singleton
    fun provideListaCompraNavGraph(
    ): ListaCompraNavGraph = ListaCompraNavGraphImpl()

    @Provides
    @Singleton
    fun provideListaProdutoNavGraph(
    ): ListaProdutoNavGraph = ListaProdutoNavGraphImpl()


    @Provides
    @Singleton
    fun provideNavigation(
        onboardingNavGraph: OnboardingNavGraph,
        listaCompraNavGraph: ListaCompraNavGraph,
        listaProdutoNavGraph: ListaProdutoNavGraph
    ): FeatureNavigationGraph = FeatureNavigationGraphImpl(
            onboardingNavGraph,
            listaCompraNavGraph,
        listaProdutoNavGraph
        )
}