package co.ukwksk.domain.model

import co.ukwksk.domain.value.ClassroomId
import co.ukwksk.domain.value.ClassroomName


class Classroom private constructor(
    val id: ClassroomId,
    val name: ClassroomName,
) {
    companion object {
        fun fromRepository(
            id: ClassroomId,
            name: ClassroomName,
        ) = Classroom(
            id = id,
            name = name
        )
    }
}
