package com.z1.comparaprecos.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tb_produtos",
    foreignKeys = [
        ForeignKey(
            entity = ListaCompraEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_lista_compra"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
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
    @ColumnInfo(name = "is_medida_peso")
    val isMedidaPeso: Boolean
)
