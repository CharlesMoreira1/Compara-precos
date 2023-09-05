package com.z1.comparaprecos.core.database.di

import android.content.Context
import androidx.room.Room
import com.z1.comparaprecos.core.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    //Provides DAOs
    @Provides
    fun provideListaCompraDao(appDatabase: AppDatabase) = appDatabase.getListaCompraDao()
    //Provides DAOs

    //Provide Database

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "db.compara_precos"
        ).addMigrations(

        ).build()
    }
}