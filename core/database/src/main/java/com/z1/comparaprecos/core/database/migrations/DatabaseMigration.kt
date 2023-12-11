package com.z1.comparaprecos.core.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Nos casos em que uma migração envolve mudanças de esquema complexas, é possível que o Room não
 * consiga gerar um caminho de migração adequado de forma automática. Por exemplo, se você for
 * dividir os dados de uma tabela em duas, o Room não conseguirá determinar como realizar essa divisão.
 * Nesses casos, é necessário definir manualmente um caminho de migração, implementando uma classe Migration.
 *
 * Uma classe Migration define explicitamente um caminho de migração entre uma startVersion e
 * uma endVersion, substituindo o método Migration.migrate(). Adicione as classes Migration ao
 * builder do banco de dados usando o método [addMigrations()]
 */
object DatabaseMigration {
    const val TRUE = 1
    const val FALSE = 0

    val MIGRATION_1_TO_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE tb_produtos ADD COLUMN is_alterado INTEGER DEFAULT $TRUE NOT NULL"
            )
        }
    }

//    Exemplo
//    val MIGRATION_1_TO_2 = object : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("")
//        }
//
//    }
}