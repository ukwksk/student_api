package co.ukwksk.usecase

import org.koin.dsl.module
import org.koin.experimental.builder.create

val usecaseModule = module {
    single<StudentSearchUsecase> { create() }
}
