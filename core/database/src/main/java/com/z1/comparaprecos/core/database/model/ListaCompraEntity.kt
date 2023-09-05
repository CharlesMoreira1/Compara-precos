package com.z1.comparaprecos.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_lista_compra")
data class ListaCompraEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val titulo: String,
    @ColumnInfo(name = "is_comparar")
    val isComparar: Boolean,
    @ColumnInfo(name = "id_lista_to_comparar")
    val idListaToComparar: Long,
    @ColumnInfo(name = "data_criacao")
    val dataCriacao: Long
)
