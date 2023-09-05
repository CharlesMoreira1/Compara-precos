package com.z1.comparaprecos.feature.listacompra.di

import com.z1.comparaprecos.core.database.repository.listacompra.ListaCompraRepository
import com.z1.comparaprecos.feature.listacompra.domain.CompraUseCaseImpl
import com.z1.comparaprecos.feature.listacompra.domain.CompraUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CompraModule {

    @Provides
    @Singleton
    fun provideCompraUseCase(
        listaCompraRepository: ListaCompraRepository
    ) : CompraUseCase = CompraUseCaseImpl(listaCompraRepository)
}