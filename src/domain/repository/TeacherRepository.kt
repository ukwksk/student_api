package co.ukwksk.domain.repository

import co.ukwksk.domain.model.Teacher
import co.ukwksk.domain.value.TeacherId

data class TeacherSpec(
    val teacherId: TeacherId
)

interface TeacherRepository {
    fun findBy(spec: TeacherSpec): Teacher?
}
