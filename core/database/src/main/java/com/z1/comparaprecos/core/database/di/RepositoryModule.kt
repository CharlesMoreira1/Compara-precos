package com.z1.comparaprecos.core.database.di

import com.z1.comparaprecos.core.database.dao.ListaCompraDao
import com.z1.comparaprecos.core.database.mapper.ListaCompraMapper
import com.z1.comparaprecos.core.database.mapper.ListaCompraWithProdutosMapper
import com.z1.comparaprecos.core.database.repository.listacompra.ListaCompraRepository
import com.z1.comparaprecos.core.database.repository.listacompra.ListaCompraRepositoryImpl
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
        listaCompraDao: ListaCompraDao,
        listaCompraMapper: ListaCompraMapper,
        listaCompraWithProdutosMapper: ListaCompraWithProdutosMapper
    ): ListaCompraRepository = ListaCompraRepositoryImpl(listaCompraDao, listaCompraMapper, listaCompraWithProdutosMapper)
}