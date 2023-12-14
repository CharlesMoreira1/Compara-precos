package com.z1.comparaprecos.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.z1.comparaprecos.feature.listacompra.presentation.ListaCompraContainer
import com.z1.comparaprecos.feature.listaproduto.presentation.ListaProdutoContainer

enum class ComparaPrecosTelas(val titulo: String) {
    ListaCompra("Lista de Compras"),
    NovaListaCompra("Nova Lista"),
    NovaListaCompraComparada("Lista de compra com comparação")
}

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
//    val pilhaDeTela by navController.currentBackStackEntryAsState()
//    val telaAtual = ComparaPrecosTelas.valueOf(
//        pilhaDeTela?.destination?.route ?: ComparaPrecosTelas.ListaCompra.name
//    )
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ComparaPrecosTelas.ListaCompra.name
    ) {
        composable(route = ComparaPrecosTelas.ListaCompra.name) {
            ListaCompraContainer(
                goToListaProduto = { idListaCompra, isComparar ->
                    navController.navigate("${ComparaPrecosTelas.NovaListaCompra.name}/$idListaCompra/$isComparar")
                }
            )
        }

        val idListaCompraArg = "idListaCompra"
        val isCompararArg = "isComparar"
        composable(
            route = "${ComparaPrecosTelas.NovaListaCompra.name}/{$idListaCompraArg}/{$isCompararArg}",
            arguments = listOf(
                navArgument(idListaCompraArg) { type = NavType.LongType},
                navArgument(isCompararArg) { type = NavType.BoolType}
            )
        ) { backStackEntry ->
            val idListaCompra = backStackEntry.arguments?.getLong(idListaCompraArg) ?: -1
            val isComparar = backStackEntry.arguments?.getBoolean(isCompararArg) ?: false
            ListaProdutoContainer(
                idListaCompra = idListaCompra,
                isComparar = isComparar,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}