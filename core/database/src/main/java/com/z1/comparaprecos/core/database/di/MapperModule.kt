package com.z1.comparaprecos.core.database.di

import com.z1.comparaprecos.core.database.mapper.ListaCompraMapper
import com.z1.comparaprecos.core.database.mapper.ListaCompraWithProdutosMapper
import com.z1.comparaprecos.core.database.mapper.ProdutoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MapperModule {

    @Singleton
    @Provides
    fun provideListaCompraMapper() = ListaCompraMapper()

    @Singleton
    @Provides
    fun provideProdutoMapper() = ProdutoMapper()

    @Singleton
    @Provides
    fun provideListaCompraWithProdutosMapper(
        listaCompraMapper: ListaCompraMapper,
        produtoMapper: ProdutoMapper
    ) = ListaCompraWithProdutosMapper(listaCompraMapper, produtoMapper)
}