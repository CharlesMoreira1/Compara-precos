package com.z1.comparaprecos.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.z1.comparaprecos.feature.listacompra.presentation.ListaCompraScreen
import com.z1.comparaprecos.feature.novalista.NovaListaScreen

enum class ComparaPrecosTelas(val titulo: String) {
    ListaCompra("Lista de Compras"),
    NovaListaCompra("Nova Lista"),
    NovaListaCompraComparada("Lista de compra com comparação")
}

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val pilhaDeTela by navController.currentBackStackEntryAsState()
    val telaAtual = ComparaPrecosTelas.valueOf(
        pilhaDeTela?.destination?.route ?: ComparaPrecosTelas.ListaCompra.name
    )
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ComparaPrecosTelas.ListaCompra.name
    ) {
        composable(route = ComparaPrecosTelas.ListaCompra.name) {
            ListaCompraScreen(
                goToNovaListaCompra = { idNovaLista ->
                    navController.navigate(ComparaPrecosTelas.NovaListaCompra.name)
                }
            )
        }

        composable(route = ComparaPrecosTelas.NovaListaCompra.name) {
            NovaListaScreen()
        }
    }
}