package com.luisma.core.services.db_services

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.core.db.Database
import com.core.db.WordsQueries

class DbSqlService(
    private val context: Context,
) {
    private fun sqlDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = Database.Schema,
            context = context,
            name = "wordle.db"
        )
    }

    fun wordsQueries(): WordsQueries {
        return WordsQueries(sqlDriver())
    }
}