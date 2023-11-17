package com.z1.comparaprecos.feature.listaproduto.domain

import com.z1.comparaprecos.core.database.repository.produto.ProdutoRepository
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.core.model.exceptions.ErrorDelete
import com.z1.comparaprecos.core.model.exceptions.ErrorEmptyList
import com.z1.comparaprecos.core.model.exceptions.ErrorInsert
import com.z1.comparaprecos.core.model.exceptions.ErrorProductData
import com.z1.comparaprecos.core.model.exceptions.ErrorProductExists
import com.z1.comparaprecos.core.model.exceptions.ErrorUpdate
import com.z1.comparaprecos.core.model.exceptions.ExceptionsCode
import com.z1.comparaprecos.testing.BaseTest
import com.z1.comparaprecos.testing.data.listaCompraTestData
import com.z1.comparaprecos.testing.data.listaCompraWithProductTestData
import com.z1.comparaprecos.testing.data.listaProdutoDataTest
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class ProdutoUseCaseImplTest: BaseTest() {

    private lateinit var useCase: ProdutoUseCase
    private lateinit var novoProduto: Produto
    private val repository: ProdutoRepository = mockk()

    override fun beforeEach() {
        super.beforeEach()
        useCase = ProdutoUseCaseImpl(repository)
        novoProduto = Produto(
            id = 3,
            idListaCompra = 0,
            nomeProduto = "Pera",
            quantidade = "0.500",
            precoUnitario = BigDecimal("6.09"),
            isMedidaPeso = true
        )
    }

    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }

    @Test
    fun `should add new produto when user create a new produto which is not in the list`() = runTest {
        //Given - Dado
        coEvery { repository.insertProduto(novoProduto) } returns 1

        //When - Quando
        val result = useCase.insertProduto(novoProduto, listaProdutoDataTest) > 0

        //Then - Entao
        assertTrue(result)
    }

    @Test
    fun `should return ErrorProdutoExists when user create a new produto that is in the list`() = runTest {
        //Given - Dado
        novoProduto = listaProdutoDataTest[0]
        coEvery { repository.insertProduto(novoProduto) } returns -1

        //When - Quando
        val exception = assertFailsWith<ErrorProductExists> {
            useCase.insertProduto(novoProduto, listaProdutoDataTest)
        }

        //Then - Entao
        assertTrue(exception.message!!.contains(ExceptionsCode.PRODUCT_EXISTS.toString()))
    }

    @Test
    fun `should return ErrorProductData when user create a new produto with invalid data`() = runTest {
        //Given - Dado
        novoProduto = novoProduto.copy(nomeProduto = "")
        coEvery { repository.insertProduto(novoProduto) } returns -1

        //When - Quando
        val exception = assertFailsWith<ErrorProductData> {
            useCase.insertProduto(novoProduto, listaProdutoDataTest)
        }

        //Then - Entao
        assertTrue(exception.message!!.contains(ExceptionsCode.ERROR_PRODUCT_DATA.toString()))
    }

    @Test
    fun `should return ErrorInsert when any error occurs while saving data in database`() = runTest {
        //Given - Dado
        coEvery { repository.insertProduto(novoProduto) } returns -1

        //When - Quando
        val exception = assertFailsWith<ErrorInsert> {
            useCase.insertProduto(novoProduto, listaProdutoDataTest)
        }

        //Then - Entao
        assertTrue(exception.message!!.contains(ExceptionsCode.ERROR_CREATE.toString()))
    }

    @Test
    fun `should return a listaCompra when have data in database`() = runTest {
        //Given - Dado
        val listaCompra = listaCompraWithProductTestData[0].detalhes
        coEvery { repository.getListaCompra(0) } returns listaCompra

        //When - Quando
         val result = useCase.getListaCompra(0)

        //Then - Entao
        assertTrue(result.titulo == listaCompra.titulo)
    }

    @Test
    fun `should return a list of pair of all listaCompra when have data in database`() = runTest {
        //Given - Dado
        val listaCompraOptions = listaCompraTestData
        coEvery { repository.getAllListaCompra() } returns listaCompraOptions

        //When - Quando
        val result = useCase.getListaCompraOptions(0)

        //Then - Entao
        assertTrue(result.size == 2)
    }

    @Test
    fun `should return ErrorEmptyList when no data in database`() = runTest {
        //Given - Dado
        val listaCompraOptions = emptyList<ListaCompra>()
        coEvery { repository.getAllListaCompra() } returns listaCompraOptions

        //When - Quando
        val exception = assertFailsWith<ErrorEmptyList> {
            useCase.getListaCompraOptions(0)
        }

        //Then - Entao
        assertTrue(exception.message!!.contains(ExceptionsCode.EMPTY_LIST.toString()))
    }

    @Test
    fun `should return a list of Produto when user selected a list to compar`() = runTest {
        //Given - Dado
        val listaCompraComparada = listaCompraWithProductTestData[0]
        coEvery { repository.getListaCompraComparada(0) } returns listaCompraComparada

        //When - Quando
        val result = useCase.getListaCompraComparada(0)

        //Then - Entao
        assertTrue(result.produtos.size == listaCompraComparada.produtos.size)
    }

    @Test
    fun `should return a list of Produto when have data in database`() = runTest {
        //Given - Dado
        val listaProduto = listaProdutoDataTest
        coEvery { repository.getListaProduto(0) } returns flowOf(listaProduto)

        //When - Quando
        val result = useCase.getListaProduto(0)

        //Then - Entao
        assertTrue(result.first().isNotEmpty())
        assertTrue(result.first().size == 3)
    }

    @Test
    fun `should update a produto when user change data of an existing produto`() = runTest {
        //Given - Dado
        val produto = listaProdutoDataTest[0]
        coEvery { repository.updateProduto(produto) } returns 1

        //When - Quando
        val result = useCase.updateProduto(produto) > 0

        //Then - Entao
        assertTrue(result)
    }

    @Test
    fun `should return ErrorUpdate when any error occurs while updating a produto`() = runTest {
        //Given - Dado
        val produto = listaProdutoDataTest[0]
        coEvery { repository.updateProduto(produto) } returns -1

        //When - Quando
        val exception = assertFailsWith<ErrorUpdate> {
            useCase.updateProduto(produto)
        }

        //Then - Entao
        assertTrue(exception.message!!.contains(ExceptionsCode.ERROR_UPDATE.toString()))
    }

    @Test
    fun `should delete a produto when user delete an existing produto`() = runTest {
        //Given - Dado
        val produto = listaProdutoDataTest[0]
        coEvery { repository.deleteProduto(produto) } returns 1

        //When - Quando
        val result = useCase.deleteProduto(produto) > 0

        //Then - Entao
        assertTrue(result)
    }

    @Test
    fun `should return ErrorDelete when any error occurs while deleting a produto`() = runTest {
        //Given - Dado
        val produto = listaProdutoDataTest[0]
        coEvery { repository.deleteProduto(produto) } returns -1

        //When - Quando
        val exception = assertFailsWith<ErrorDelete> {
            useCase.deleteProduto(produto)
        }

        //Then - Entao
        assertTrue(exception.message!!.contains(ExceptionsCode.ERROR_DELETE.toString()))
    }
}