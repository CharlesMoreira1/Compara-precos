package com.z1.comparaprecos.core.model

data class ListaCompra(
    val id: Long,
    val titulo: String,
    val dataCriacao: Long
) {
    fun isTituloVazio() = titulo.isBlank()
}
