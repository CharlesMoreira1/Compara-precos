package com.z1.comparaprecos.core.navigation.navgraph.listaproduto

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.z1.comparaprecos.feature.listaproduto.presentation.ListaProdutoContainer

class ListaProdutoNavGraphImpl: ListaProdutoNavGraph {
    private val baseRoute = "lista_produto"
    private val idListaCompraArg = "idListaCompra"
    private val isCompararArg = "isComparar"
    private var completeRoute = "$baseRoute/{$idListaCompraArg}/{$isCompararArg}"

    override fun route(idListaCompraArg: Long, isCompararArg: Boolean) =
        StringBuilder()
            .append(baseRoute)
            .append("/$idListaCompraArg")
            .append("/$isCompararArg")
            .toString()


    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(
            route = completeRoute,
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