package co.ukwksk.domain.repository

import co.ukwksk.domain.model.Classroom
import co.ukwksk.domain.value.TeacherId

data class ClassroomSpec(
    val teacherId: TeacherId
)

interface ClassroomRepository {
    fun findBy(spec: ClassroomSpec): List<Classroom>
}
