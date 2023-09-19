package com.z1.comparaprecos.feature.listacompra.di

import com.z1.comparaprecos.core.database.repository.listacompra.ListaCompraRepository
import com.z1.comparaprecos.feature.listacompra.domain.ListaCompraUseCaseImpl
import com.z1.comparaprecos.feature.listacompra.domain.ListaCompraUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ListaCompraModule {

    @Provides
    @Singleton
    fun provideListaCompraUseCase(
        listaCompraRepository: ListaCompraRepository
    ) : ListaCompraUseCase = ListaCompraUseCaseImpl(listaCompraRepository)
}