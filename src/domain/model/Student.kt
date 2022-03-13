package co.ukwksk.domain.model

import co.ukwksk.domain.value.ClassroomId
import co.ukwksk.domain.value.StudentId
import co.ukwksk.domain.value.StudentLoginId
import co.ukwksk.domain.value.StudentName


class Student private constructor(
    val id: StudentId,
    val name: StudentName,
    val loginId: StudentLoginId,
    val belongingClassroomId: ClassroomId?,
) {
    companion object {
        fun fromRepository(
            id: StudentId,
            name: StudentName,
            loginId: StudentLoginId,
            belongingClassroomId: ClassroomId?,
        ) = Student(
            id = id,
            name = name,
            loginId = loginId,
            belongingClassroomId = belongingClassroomId,
        )
    }
}
