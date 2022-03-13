package co.ukwksk.domain.service

import org.koin.dsl.module
import org.koin.experimental.builder.create

val serviceModule = module {
    single<TeacherService> { create() }
    single<ClassroomService> { create() }
    single<StudentService> { create() }
}
