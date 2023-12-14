package com.z1.comparaprecos.core.database.di

import android.content.Context
import androidx.room.Room
import com.z1.comparaprecos.core.database.AppDatabase
import com.z1.comparaprecos.core.database.migrations.DatabaseMigration.MIGRATION_1_TO_2
import com.z1.comparaprecos.core.database.migrations.DatabaseMigration.MIGRATION_2_TO_3
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
    @Provides
    fun provideProdutoDao(appDatabase: AppDatabase) = appDatabase.getprodutoDao()
    //Provides DAOs

    //Provide Database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "db.compara_precos"
        ).addMigrations( //DatabaseMigration file
            MIGRATION_1_TO_2,
            MIGRATION_2_TO_3
        ).build()
    }
}