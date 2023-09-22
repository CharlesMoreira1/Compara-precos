package com.z1.comparaprecos.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_produtos")
data class ProdutoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "id_lista_compra")
    val idListaCompra: Long,
    @ColumnInfo(name = "nome_produto")
    val nomeProduto: String,
    val quantidade: String,
    @ColumnInfo(name = "preco_unitario")
    val precoUnitario: Double,
    val medida: String
)
