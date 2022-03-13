package co.ukwksk.repository

import co.ukwksk.domain.repository.ClassroomRepository
import co.ukwksk.domain.repository.StudentRepository
import co.ukwksk.domain.repository.TeacherRepository
import org.koin.dsl.module
import org.koin.experimental.builder.create

val repositoryModule = module {
    single<TeacherRepository> { create<TeacherRepositoryImpl>() }
    single<ClassroomRepository> { create<ClassroomRepositoryImpl>() }
    single<StudentRepository> { create<StudentRepositoryImpl>() }
}
