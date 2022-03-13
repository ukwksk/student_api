package co.ukwksk.domain.service

import co.ukwksk.domain.model.Classroom
import co.ukwksk.domain.repository.ClassroomRepository
import co.ukwksk.domain.repository.ClassroomSpec
import co.ukwksk.domain.value.TeacherId

class ClassroomService(
    private val repository: ClassroomRepository
) {

    fun listByTeacherId(teacherId: TeacherId): List<Classroom> {
        return repository.findBy(
            ClassroomSpec(teacherId)
        )
    }
}
