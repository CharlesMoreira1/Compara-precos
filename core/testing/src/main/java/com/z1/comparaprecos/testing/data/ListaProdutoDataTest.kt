package com.z1.comparaprecos.testing.data

import com.z1.comparaprecos.core.model.Produto
import java.math.BigDecimal

val listaProdutoDataTest = listOf(
    Produto(
        id = 0,
        idListaCompra = 0,
        nomeProduto = "Arroz",
        quantidade = "1",
        precoUnitario = BigDecimal("23.69"),
        isMedidaPeso = false,
        isAlterado = true
    ),
    Produto(
        id = 1,
        idListaCompra = 0,
        nomeProduto = "Feijao",
        quantidade = "5",
        precoUnitario = BigDecimal("8.49"),
        isMedidaPeso = false,
        isAlterado = true
    ),
    Produto(
        id = 2,
        idListaCompra = 0,
        nomeProduto = "Banana",
        quantidade = "0.700",
        precoUnitario = BigDecimal("2.09"),
        isMedidaPeso = true,
        isAlterado = true
    ),
)

val listaProdutoDataTest2 = listOf(
    Produto(
        id = 0,
        idListaCompra = 1,
        nomeProduto = "Arroz",
        quantidade = "2",
        precoUnitario = BigDecimal("22.69"),
        isMedidaPeso = false,
        isAlterado = true
    ),
    Produto(
        id = 1,
        idListaCompra = 1,
        nomeProduto = "Feijao",
        quantidade = "2",
        precoUnitario = BigDecimal("6.49"),
        isMedidaPeso = false,
        isAlterado = true
    ),
    Produto(
        id = 2,
        idListaCompra = 1,
        nomeProduto = "Açucar",
        quantidade = "3",
        precoUnitario = BigDecimal("3.49"),
        isMedidaPeso = false,
        isAlterado = true
    ),
    Produto(
        id = 3,
        idListaCompra = 1,
        nomeProduto = "Banana",
        quantidade = "0.200",
        precoUnitario = BigDecimal("3.09"),
        isMedidaPeso = true,
        isAlterado = true
    ),
)

val listaProdutoDataTest3 = listOf(
    Produto(
        id = 0,
        idListaCompra = 0,
        nomeProduto = "Suco de uva tinto 1,5l kljdfhljkghsdlj",
        quantidade = "1",
        precoUnitario = BigDecimal("23.69"),
        isMedidaPeso = false,
        isAlterado = true
    ),
    Produto(
        id = 1,
        idListaCompra = 0,
        nomeProduto = "Pacote de arroz 5kg",
        quantidade = "5",
        precoUnitario = BigDecimal("8.49"),
        isMedidaPeso = false,
        isAlterado = true
    ),
    Produto(
        id = 2,
        idListaCompra = 0,
        nomeProduto = "Sabão liqúido omo 1l",
        quantidade = "0.700",
        precoUnitario = BigDecimal("2.09"),
        isMedidaPeso = true,
        isAlterado = true
    ),
)