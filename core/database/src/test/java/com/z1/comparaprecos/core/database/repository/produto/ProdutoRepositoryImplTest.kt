package com.z1.comparaprecos.core.database.repository.produto

import com.z1.comparaprecos.core.database.dao.ProdutoDao
import com.z1.comparaprecos.core.database.mapper.ListaCompraMapper
import com.z1.comparaprecos.core.database.mapper.ListaCompraWithProdutosMapper
import com.z1.comparaprecos.core.database.mapper.ProdutoMapper
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
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProdutoRepositoryImplTest: BaseTest() {
    private lateinit var repository: ProdutoRepository
    private val produtoDao: ProdutoDao = mockk()
    private val listaCompraMapper = ListaCompraMapper()
    private val produtoMapper = ProdutoMapper()
    private val listaCompraWithProdutosMapper = ListaCompraWithProdutosMapper(listaCompraMapper, produtoMapper)

    override fun beforeEach() {
        super.beforeEach()
        repository = ProdutoRepositoryImpl(
            produtoDao,
            listaCompraMapper,
            produtoMapper,
            listaCompraWithProdutosMapper
        )
    }

    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }

    @Test
    fun `should return listaCompra`() = runTest {
        //Given - Dado
        val listaCompraEntity = listaCompraMapper.mapModelToEntity(listaCompraTestData[0])
        coEvery { produtoDao.getListaCompra(any()) } returns listaCompraEntity

        //When - Quando
        val result = repository.getListaCompra(0)

        //Then - Entao
        assertEquals(result.titulo, listaCompraEntity.titulo)
    }

    @Test
    fun `should return a list of listaCompra`() = runTest {
        //Given - Dado
        val listaCompraEntity = listaCompraMapper.mapModelListToEntityList(listaCompraTestData)
        coEvery { produtoDao.getAllListaCompra() } returns listaCompraEntity

        //When - Quando
        val result = repository.getAllListaCompra()

        //Then - Entao
        assertTrue(result.size == 3)
    }

    @Test
    fun `should return listaCompra to comparar`() = runTest {
        //Given - Dado
        val listaCompraEntity = listaCompraWithProdutosMapper.mapModelToEntity(
            listaCompraWithProductTestData[0])
        coEvery { produtoDao.getListaCompraComparada(any()) } returns listaCompraEntity

        //When - Quando
        val result = repository.getListaCompraComparada(0)

        //Then - Entao
        assertEquals(result.detalhes.titulo, listaCompraEntity.detalhes.titulo)
    }

    @Test
    fun `should return a list of Produto`() = runTest {
        //Given - Dado
        val listaProdutoEntity = produtoMapper.mapModelListToEntityList(listaProdutoDataTest)
        coEvery { produtoDao.getListaProduto(any()) } returns flowOf(listaProdutoEntity)

        //When - Quando
        val result = repository.getListaProduto(0)

        //Then - Entao
        assertTrue(result.first().size == 3)
    }

    @Test
    fun `should insert a new Produto`() = runTest {
        //Given - Dado
        val produto = listaProdutoDataTest[0]
        coEvery { produtoDao.insertProduto(any()) } returns 1

        //When - Quando
        val result = repository.insertProduto(produto) >= 0

        //Then - Entao
        assertTrue(result)
    }

    @Test
    fun `should update a Produto`() = runTest {
        //Given - Dado
        val produto = listaProdutoDataTest[0]
        coEvery { produtoDao.updateProduto(any()) } returns 1

        //When - Quando
        val result = repository.updateProduto(produto) >= 0

        //Then - Entao
        assertTrue(result)
    }

    @Test
    fun `should delete a Produto`() = runTest {
        //Given - Dado
        val produto = listaProdutoDataTest[0]
        coEvery { produtoDao.deleteProduto(any()) } returns 1

        //When - Quando
        val result = repository.deleteProduto(produto) >= 0

        //Then - Entao
        assertTrue(result)
    }
}