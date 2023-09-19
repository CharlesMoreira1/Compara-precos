package com.z1.comparaprecos.feature.novalista.di

import com.z1.comparaprecos.core.database.repository.produto.ProdutoRepository
import com.z1.comparaprecos.feature.novalista.domain.ProdutoUseCase
import com.z1.comparaprecos.feature.novalista.domain.ProdutoUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ProdutoModule {

    @Provides
    @Singleton
    fun provideProdutoUseCase(
        produtoRepository: ProdutoRepository
    ): ProdutoUseCase = ProdutoUseCaseImpl(produtoRepository)
}