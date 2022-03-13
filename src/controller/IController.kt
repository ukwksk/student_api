package co.ukwksk.controller

import io.ktor.routing.*

interface IController {
    fun route(route: Route)
}
