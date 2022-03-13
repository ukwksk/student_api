package co.ukwksk

import co.ukwksk.controller.IController
import co.ukwksk.controller.controllerModule
import co.ukwksk.domain.service.serviceModule
import co.ukwksk.log.writeRequestLog
import co.ukwksk.repository.repositoryModule
import co.ukwksk.usecase.exception.ResourceNotFoundException
import co.ukwksk.usecase.usecaseModule
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.getKoin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@EngineAPI
@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    this.environment.config.toDbConfig().connect()

    install(CORS)
    {
        method(HttpMethod.Options)
        anyHost()
    }
    install(IgnoreTrailingSlash)
    install(Locations) {
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Koin) {
        modules(
            repositoryModule,
            serviceModule,
            usecaseModule,
            controllerModule,
        )
    }

    routing {
        get("/") {
            call.respond(HttpStatusCode.OK)
        }
        getKoin().getAll<IController>().forEach {
            it.route(this)
        }
    }

    install(StatusPages) {
        exception<ResourceNotFoundException> {
            call.respond(HttpStatusCode.NotFound)
            writeRequestLog(call)
        }
        exception<Throwable> {
            writeRequestLog(call, it)
            throw it
        }
    }
    intercept(ApplicationCallPipeline.Monitoring) {
        proceed()
        writeRequestLog(call)
    }
}

