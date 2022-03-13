package co.ukwksk

import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.test.KoinTest
import org.mockito.MockitoAnnotations
import kotlin.test.Test
import kotlin.test.assertEquals

private val testEnv = createTestEnvironment {
    config = HoconApplicationConfig(ConfigFactory.load())
}

abstract class TransactionalTest : KoinTest {
    fun <R> withRollback(test: Transaction.() -> R) {
        withApplication(
            environment = testEnv,
            test = {
                transaction {
                    test.invoke(this)
                    rollback()
                }
            }
        )
    }
}

abstract class MockTest {
    private lateinit var closeable: AutoCloseable

    @BeforeEach
    fun openMocks() {
        closeable = MockitoAnnotations.openMocks(this)
    }

    @AfterEach
    fun releaseMocks() {
        closeable.close()
    }
}

class ApplicationTest {
    @Test
    fun testRoot() {
        withApplication(testEnv) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}

