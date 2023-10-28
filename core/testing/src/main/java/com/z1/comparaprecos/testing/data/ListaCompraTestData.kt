package com.z1.comparaprecos.testing.data

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos

val listaCompraWithProductTestData = listOf(
    ListaCompraWithProdutos(
        detalhes = ListaCompra(
            id = 0,
            titulo = "ListaCompra 0",
            dataCriacao = 0L
        ),
        produtos = emptyList()

    ),
    ListaCompraWithProdutos(
        detalhes = ListaCompra(
            id = 1,
            titulo = "ListaCompra 1",
            dataCriacao = 0L
        ),
        produtos = emptyList()

    ),
    ListaCompraWithProdutos(
        detalhes = ListaCompra(
            id = 2,
            titulo = "ListaCompra 2",
            dataCriacao = 0L
        ),
        produtos = emptyList()

    )
)