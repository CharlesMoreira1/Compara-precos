package com.z1.comparaprecos.core.database.repository.listacompra

import androidx.room.withTransaction
import com.z1.comparaprecos.core.database.AppDatabase
import com.z1.comparaprecos.core.database.dao.ListaCompraDao
import com.z1.comparaprecos.core.database.dao.ProdutoDao
import com.z1.comparaprecos.core.database.mapper.ListaCompraMapper
import com.z1.comparaprecos.core.database.mapper.ListaCompraWithProdutosMapper
import com.z1.comparaprecos.core.database.mapper.ProdutoMapper
import com.z1.comparaprecos.testing.BaseTest
import com.z1.comparaprecos.testing.data.listaCompraTestData
import com.z1.comparaprecos.testing.data.listaCompraWithProductTestData
import com.z1.comparaprecos.testing.data.listaProdutoDataTest
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class ListaCompraRepositoryImplTest: BaseTest() {

    private lateinit var repository: ListaCompraRepository
    private val appDatabase: AppDatabase = mockk(relaxed = true)
    private val listaCompraDao: ListaCompraDao = mockk()
    private val produtoDao: ProdutoDao = mockk()
    private val listaCompraMapper = ListaCompraMapper()
    private val produtoMapper = ProdutoMapper()
    private val listaCompraWithProdutosMapper = ListaCompraWithProdutosMapper(listaCompraMapper, produtoMapper)

    private fun setupMockAppDatabase() {
        MockKAnnotations.init(this)
        mockkStatic("androidx.room.RoomDatabaseKt")

        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { appDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
    }

    override fun beforeEach() {
        super.beforeEach()
        repository = ListaCompraRepositoryImpl(
            appDatabase = appDatabase,
            listaCompraDao = listaCompraDao,
            produtoDao = produtoDao,
            listaCompraMapper = listaCompraMapper,
            produtoMapper = produtoMapper,
            listaCompraWithProdutosMapper = listaCompraWithProdutosMapper
        )
    }

    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }

    @Test
    fun `should return a list of compra`() = runTest {
        //Given - Dado
        coEvery { listaCompraDao.getListaCompra() } returns flowOf(
            listaCompraMapper.mapModelListToEntityList(listaCompraTestData)
        )

        //When - Quando
        val result = repository.getListaCompra()

        //Then - Entao
        assertTrue(result.first().size == 3)
    }

    @Test
    fun `should return a list of listaCompra with list of Produto`() = runTest {
        //Given - Dado
        coEvery { listaCompraDao.getListaCompraWithProdutos() } returns flowOf(
            listaCompraWithProdutosMapper.mapModelListToEntityList(listaCompraWithProductTestData)
        )

        //When - Quando
        val result = repository.getListaCompraWithProdutos()

        //Then - Entao
        assertTrue(result.first().size == 3)
    }

    @Test
    fun `should insert a new listaCompra`() = runTest {
        //Given - Dado
        val listaCompra = listaCompraTestData[0]

        coEvery { listaCompraDao.insertListaCompra(any()) } returns 0L

        //When - Quando
        val result = repository.insertListaCompra(listaCompra) == 0L

        //Then - Entao
        assertTrue(result)
    }

    @Test
    fun `should insert a new listaCompra and list of Produto`() = runTest {
        //Given - Dado
        setupMockAppDatabase()

        val listaCompra = listaCompraTestData[0]
        val listaProduto = listaProdutoDataTest

        coEvery { listaCompraDao.insertListaCompra(any()) } returns 0L
        coEvery { produtoDao.insertListaProduto(any()) } returns arrayListOf(0,1,2)

        //When - Quando
        val result = repository.insertListaCompraAndListaProduto(listaCompra, listaProduto)

        //Then - Entao
        coVerifySequence {
            listaCompraDao.insertListaCompra(any())
            produtoDao.insertListaProduto(any())
        }
        assertTrue(result)
    }

    @Test
    fun `should return false when occurs some error adding a listaCompra`() = runTest {
        //Given - Dado
        setupMockAppDatabase()

        val listaCompra = listaCompraTestData[0]
        val listaProduto = listaProdutoDataTest

        coEvery { listaCompraDao.insertListaCompra(any()) } throws Exception("Erro")
        coEvery { produtoDao.insertListaProduto(any()) } returns arrayListOf(0,1,2)

        //When - Quando
        val result = repository.insertListaCompraAndListaProduto(listaCompra, listaProduto)

        //Then - Entao
        coVerifySequence {
            listaCompraDao.insertListaCompra(any())
        }
        assertFalse(result)
    }

    @Test
    fun `should return false when occurs some error adding a list of Produto`() = runTest {
        //Given - Dado
        setupMockAppDatabase()

        val listaCompra = listaCompraTestData[0]
        val listaProduto = listaProdutoDataTest

        coEvery { listaCompraDao.insertListaCompra(any()) } returns 0L
        coEvery { produtoDao.insertListaProduto(any()) } throws Exception("Erro")

        //When - Quando
        val result = repository.insertListaCompraAndListaProduto(listaCompra, listaProduto)

        //Then - Entao
        coVerifySequence {
            listaCompraDao.insertListaCompra(any())
            produtoDao.insertListaProduto(any())
        }
        assertFalse(result)
    }

    @Test
    fun `should return 1 when update a listaCompra`() = runTest {
        //Given - Dado
        val listaCompra = listaCompraTestData[0]

        coEvery { listaCompraDao.updateListaCompra(any()) } returns 1

        //When - Quando
        val result = repository.updateListaCompra(listaCompra) == 1

        //Then - Entao
        assertTrue(result)
    }

    @Test
    fun `should return 1 when delete a listaCompra`() = runTest {
        //Given - Dado
        coEvery { listaCompraDao.deleteListaCompra(any()) } returns 1

        //When - Quando
        val result = repository.deleteListaCompra(0) == 1

        //Then - Entao
        assertTrue(result)
    }
}