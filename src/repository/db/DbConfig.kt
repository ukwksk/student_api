package co.ukwksk.repository.db

import org.jetbrains.exposed.sql.Database
import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory

data class DbConfig(
    val host: String,
    val port: Int,
    val dbName: String,
    val user: String,
    val password: String,
    val useSsl: Boolean,
) {
    private val url = "jdbc:postgresql://$host:$port/$dbName"

    fun connect() {
        val config = this
        Database.connect(
            PGSimpleDataSource().apply {
                setURL(url)
                user = config.user
                password = config.password
                reWriteBatchedInserts = true
            }.also {
                LoggerFactory.getLogger("db.connection")
                    .debug(it.getURL())
            }
        )
    }
}
