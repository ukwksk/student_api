package co.ukwksk

import co.ukwksk.repository.db.DbConfig
import io.ktor.config.*

fun ApplicationConfig.getString(path: String) = this.property(path).getString()

fun ApplicationConfig.toDbConfig() = DbConfig(
    host = this.getString("database.host"),
    port = this.getString("database.port").toInt(),
    dbName = this.getString("database.db_name"),
    user = this.getString("database.user"),
    password = this.getString("database.pass"),
    useSsl = this.getString("database.use_ssl").lowercase() == "false"
)
