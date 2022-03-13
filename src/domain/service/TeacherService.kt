package co.ukwksk.domain.service

import co.ukwksk.domain.model.Teacher
import co.ukwksk.domain.repository.TeacherRepository
import co.ukwksk.domain.repository.TeacherSpec
import co.ukwksk.domain.value.TeacherId

class TeacherService(
    private val repository: TeacherRepository
) {

    fun findById(teacherId: TeacherId): Teacher? {
        return repository.findBy(
            TeacherSpec(teacherId)
        )
    }
}
