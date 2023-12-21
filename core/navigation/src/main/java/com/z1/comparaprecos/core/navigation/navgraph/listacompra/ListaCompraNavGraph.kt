package com.z1.comparaprecos.core.navigation.navgraph.listacompra

import com.z1.comparaprecos.core.navigation.RegisterNavGraph

interface ListaCompraNavGraph: RegisterNavGraph {
    fun route(): String
}