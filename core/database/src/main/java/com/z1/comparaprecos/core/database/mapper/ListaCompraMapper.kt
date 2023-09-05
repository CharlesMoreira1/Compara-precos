package com.z1.comparaprecos.core.database.mapper

import com.z1.comparaprecos.core.database.model.ListaCompraEntity
import com.z1.comparaprecos.core.model.ListaCompra

class ListaCompraMapper : BaseMapper<ListaCompra, ListaCompraEntity>() {
    override fun mapEntityToModel(entity: ListaCompraEntity): ListaCompra =
        entity.run {
            ListaCompra(
                id = id,
                titulo = titulo,
                isComparar = isComparar,
                idListaToComparar = idListaToComparar,
                dataCriacao = dataCriacao
            )
        }

    override fun mapModelToEntity(model: ListaCompra): ListaCompraEntity =
        model.run {
            ListaCompraEntity(
                id = id,
                titulo = titulo,
                isComparar = isComparar,
                idListaToComparar = idListaToComparar,
                dataCriacao = dataCriacao
            )
        }
}