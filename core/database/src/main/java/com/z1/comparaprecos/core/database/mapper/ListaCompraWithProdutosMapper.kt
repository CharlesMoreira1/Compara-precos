package com.z1.comparaprecos.core.database.mapper

import com.z1.comparaprecos.core.database.model.ListaCompraWithProdutosEntity
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos

class ListaCompraWithProdutosMapper(
    val listaCompraMapper: ListaCompraMapper,
    val produtoMapper: ProdutoMapper
) : BaseMapper<ListaCompraWithProdutos, ListaCompraWithProdutosEntity>() {
    override fun mapEntityToModel(entity: ListaCompraWithProdutosEntity): ListaCompraWithProdutos =
        entity.run {
            ListaCompraWithProdutos(
                listaCompraMapper.mapEntityToModel(detalhes),
                produtoMapper.mapEntityListToModelList(produtos)
            )
        }

    override fun mapModelToEntity(model: ListaCompraWithProdutos): ListaCompraWithProdutosEntity =
        model.run {
            ListaCompraWithProdutosEntity(
                listaCompraMapper.mapModelToEntity(detalhes),
                produtoMapper.mapModelListToEntityList(produtos)
            )
        }
}