package com.z1.comparaprecos.core.database.di

import com.z1.comparaprecos.core.database.AppDatabase
import com.z1.comparaprecos.core.database.dao.ListaCompraDao
import com.z1.comparaprecos.core.database.dao.ProdutoDao
import com.z1.comparaprecos.core.database.mapper.ListaCompraMapper
import com.z1.comparaprecos.core.database.mapper.ListaCompraWithProdutosMapper
import com.z1.comparaprecos.core.database.mapper.ProdutoMapper
import com.z1.comparaprecos.core.database.repository.listacompra.ListaCompraRepository
import com.z1.comparaprecos.core.database.repository.listacompra.ListaCompraRepositoryImpl
import com.z1.comparaprecos.core.database.repository.produto.ProdutoRepository
import com.z1.comparaprecos.core.database.repository.produto.ProdutoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideListaCompraRepository(
        appDatabase: AppDatabase,
        listaCompraDao: ListaCompraDao,
        produtoDao: ProdutoDao,
        listaCompraMapper: ListaCompraMapper,
        produtoMapper: ProdutoMapper,
        listaCompraWithProdutosMapper: ListaCompraWithProdutosMapper
    ): ListaCompraRepository = ListaCompraRepositoryImpl(
        appDatabase,
        listaCompraDao,
        produtoDao,
        listaCompraMapper,
        produtoMapper,
        listaCompraWithProdutosMapper
    )

    @Singleton
    @Provides
    fun provideProdutoRepository(
        produtoDao: ProdutoDao,
        listaCompraMapper: ListaCompraMapper,
        produtoMapper: ProdutoMapper,
        listaCompraWithProdutosMapper: ListaCompraWithProdutosMapper
    ): ProdutoRepository = ProdutoRepositoryImpl(produtoDao, listaCompraMapper, produtoMapper, listaCompraWithProdutosMapper)
}