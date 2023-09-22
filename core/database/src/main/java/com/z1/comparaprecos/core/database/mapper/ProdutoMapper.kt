package com.z1.comparaprecos.core.database.mapper

import com.z1.comparaprecos.core.database.model.ProdutoEntity
import com.z1.comparaprecos.core.model.Produto
import java.math.BigDecimal

class ProdutoMapper : BaseMapper<Produto, ProdutoEntity>() {
    override fun mapEntityToModel(entity: ProdutoEntity): Produto =
        entity.run {
            Produto(
                id = id,
                idListaCompra = idListaCompra,
                nomeProduto = nomeProduto,
                quantidade = quantidade,
                precoUnitario = BigDecimal.valueOf(precoUnitario),
                medida = medida
            )
        }

    override fun mapModelToEntity(model: Produto): ProdutoEntity =
        model.run {
            ProdutoEntity(
                id = id,
                idListaCompra = idListaCompra,
                nomeProduto = nomeProduto,
                quantidade = quantidade,
                precoUnitario = precoUnitario.toDouble(),
                medida = medida
            )
        }
}