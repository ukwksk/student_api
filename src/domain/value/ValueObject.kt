package co.ukwksk.domain.value

sealed interface ValueObject<T> {
    val value: T
}

sealed interface IdValueObject : ValueObject<Int>
sealed interface StringValueObject : ValueObject<String>

data class TeacherId(override val value: Int) : IdValueObject

data class ClassroomId(override val value: Int) : IdValueObject
data class ClassroomName(override val value: String) : StringValueObject

data class StudentId(override val value: Int) : IdValueObject
data class StudentName(override val value: String) : StringValueObject
data class StudentLoginId(override val value: String) : StringValueObject
