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
    @ColumnInfo(name = "id_lista_compra", index = true)
    val idListaCompra: Long,
    @ColumnInfo(name = "nome_produto")
    val nomeProduto: String,
    val quantidade: String,
    @ColumnInfo(name = "preco_unitario")
    val precoUnitario: Double,
    @ColumnInfo(name = "is_medida_peso")
    val isMedidaPeso: Boolean,
    @ColumnInfo(name = "is_alterado")
    val isAlterado: Boolean
) {
    companion object {
        const val TABLE = "tb_produtos"
        const val COLUMN_ID_LISTA_COMPRA = "id_lista_compra"
        val COLUMN_NOME_PRODUTO = "nome_produto"
        const val COLUMN_ID = "id"
    }
}
