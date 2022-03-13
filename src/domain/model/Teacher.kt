package co.ukwksk.domain.model

import co.ukwksk.domain.value.TeacherId


class Teacher private constructor(
    val id: TeacherId,
) {
    companion object {
        fun fromRepository(id: TeacherId) = Teacher(
            id = id,
        )
    }
}
