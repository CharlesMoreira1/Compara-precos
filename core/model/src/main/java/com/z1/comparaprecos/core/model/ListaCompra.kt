package com.z1.comparaprecos.core.model

data class ListaCompra(
    val id: Long,
    val titulo: String,
    val isComparar: Boolean,
    val idListaToComparar: Long,
    val dataCriacao: Long
) {
    fun validarDadosCompra(): Boolean {
        if (isComparar) {
            return idListaToComparar >= 0 && titulo.isNotBlank() && dataCriacao >= 0L
        }
        return titulo.isNotBlank() && dataCriacao >= 0L
    }

    fun isTituloVazio() = titulo.isBlank()
}
