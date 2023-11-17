package com.z1.comparaprecos.feature.listacompra.domain

import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.database.repository.listacompra.ListaCompraRepository
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.exceptions.ErrorDelete
import com.z1.comparaprecos.core.model.exceptions.ErrorInsert
import com.z1.comparaprecos.core.model.exceptions.ErrorUpdate
import com.z1.comparaprecos.core.model.exceptions.ExceptionsCode
import com.z1.comparaprecos.testing.BaseTest
import com.z1.comparaprecos.testing.data.listaCompraWithProductTestData
import com.z1.comparaprecos.testing.data.listaProdutoDataTest
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ListaCompraUseCaseImplTest: BaseTest() {

    private lateinit var usecase: ListaCompraUseCase
    private lateinit var listaCompra: ListaCompra
    private val repository: ListaCompraRepository = mockk()

    override fun beforeEach() {
        super.beforeEach()
        usecase = ListaCompraUseCaseImpl(repository)
        listaCompra = ListaCompra(0, "Teste", 0L)
    }

    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }

    //Get list
    @Test
    fun `should return a list of listaCompraWithProdutos when start de application`() = runTest {
        //Given - Dado
        coEvery { repository.getListaCompraWithProdutos() } returns flowOf(listaCompraWithProductTestData)

        //When - quando
        val result = usecase.getListaCompraWithProdutos()

        //Then - Entao
        assertTrue(result.first().isNotEmpty())
        assertTrue(result.first().size == 3)
    }
    //Get list

    //Insert
    @Test
    fun `should return true when inserted a new listaCompra`() = runTest {
        //Given - Dado
        coEvery { repository.insertListaCompra(listaCompra) } returns 1

        //When - Quando
        val result = usecase.insertNovaListaCompra(listaCompra) > 0

        //Then - Entao
        assertTrue(result)
    }

    @Test
    fun `should return ErrorInsert when not inserted a new listaCompra`() = runTest {
        //Given - Dado
        coEvery { repository.insertListaCompra(listaCompra) } returns -1

        //When - Quando
        val exception = assertFailsWith<ErrorInsert> {
            usecase.insertNovaListaCompra(listaCompra)
        }

        //Then - Entao
        assertTrue(exception.message!!.contains(ExceptionsCode.ERROR_CREATE.toString()))
    }
    //Insert

    //Duplicate
    @Test
    fun `should return true when duplicate a listaCompra`() = runTest {
        val listaProduto = listaProdutoDataTest
        //Given - Dado
        coEvery { repository.insertListaCompraAndListaProduto(listaCompra, listaProduto) } returns true

        //When - Quando
        val result = usecase.duplicateListaCompra(listaCompra, listaProduto)

        //Then - Entao
        assertEquals(result, R.string.label_lista_compra_duplicada)
    }

    @Test
    fun `should return false when duplicate a listaCompra`() = runTest {
        val listaProduto = listaProdutoDataTest
        //Given - Dado
        coEvery { repository.insertListaCompraAndListaProduto(listaCompra, listaProduto) } returns false

        //When - Quando
        val result = usecase.duplicateListaCompra(listaCompra, listaProduto)

        //Then - Entao
        assertEquals(result, R.string.label_desc_erro_criar_lista)
    }

    @Test
    fun `should return error when duplicate a listaCompra`() = runTest {
        val listaProduto = listaProdutoDataTest
        //Given - Dado
        coEvery { repository.insertListaCompraAndListaProduto(listaCompra, listaProduto) } throws ErrorInsert()

        //When - Quando
        val result = usecase.duplicateListaCompra(listaCompra, listaProduto)

        //Then - Entao
        assertEquals(result, R.string.label_desc_erro_criar_lista)
    }
    //Duplicate

    //Update
    @Test
    fun `should return true when update a listaCompra`() = runTest {
        //Given - Dado
        coEvery { repository.updateListaCompra(listaCompra) } returns 1

        //When - Quando
        val result = usecase.updateListaCompra(listaCompra) > 0

        //Then - Entao
        assertTrue(result)
    }

    @Test
    fun `should return ErrorUpdate when not updated a listaCompra`() = runTest {
        //Given - Dado
        coEvery { repository.updateListaCompra(listaCompra) } returns -1

        //When - Quando
        val exception = assertFailsWith<ErrorUpdate> {
            usecase.updateListaCompra(listaCompra)
        }

        //Then - Entao
        assertTrue(exception.message!!.contains(ExceptionsCode.ERROR_UPDATE.toString()))
    }
    //Update

    //Delete
    @Test
    fun `should return true when delete a listaCompra`() = runTest {
        //Given - Dado
        coEvery { repository.deleteListaCompra(0L) } returns 1

        //When - Quando
        val result = usecase.deleteListaCompra(0L) > 0

        //Then - Entao
        assertTrue(result)
    }

    @Test
    fun `should return ErrorDelete when not delete a listaCompra`() = runTest {
        //Given - Dado
        coEvery { repository.deleteListaCompra(0L) } returns -1

        //When - Quando
        val exception = assertFailsWith<ErrorDelete> {
            usecase.deleteListaCompra(0L)
        }

        //Then - Entao
        assertTrue(exception.message!!.contains(ExceptionsCode.ERROR_DELETE.toString()))
    }
    //Delete
}