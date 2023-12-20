package com.z1.comparaprecos.core.navigation.navgraph.listaproduto

import com.z1.comparaprecos.core.navigation.RegisterNavGraph

interface ListaProdutoNavGraph: RegisterNavGraph {
    fun route(idListaCompraArg: Long, isCompararArg: Boolean): String
}