package com.z1.comparaprecos.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ListaCompraWithProdutosEntity(
    @Embedded val detalhes: ListaCompraEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_lista_compra"
    )
    val produtos: List<ProdutoEntity>
)
