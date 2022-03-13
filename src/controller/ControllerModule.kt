package co.ukwksk.controller

import io.ktor.locations.*
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.experimental.builder.create

@KtorExperimentalLocationsAPI
val controllerModule = module {
    single { create<StudentController>() } bind IController::class
}
