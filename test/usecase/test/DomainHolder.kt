package co.ukwksk.usecase.test

import co.ukwksk.domain.model.Classroom
import co.ukwksk.domain.model.Student
import co.ukwksk.domain.model.Teacher
import co.ukwksk.domain.value.*
import java.util.concurrent.atomic.AtomicInteger

object DomainHolder {
    private val teacherIdAtom = AtomicInteger()
    private val classIdAtom = AtomicInteger()
    private val studentIdAtom = AtomicInteger()

    fun createTeacher() =
        Teacher.fromRepository(
            id = TeacherId(teacherIdAtom.getAndIncrement())
        )

    fun createClass() =
        classIdAtom.incrementAndGet().let {
            Classroom.fromRepository(
                id = ClassroomId(it),
                name = ClassroomName("クラス$it"),
            )
        }

    fun createStudent(classroomId: ClassroomId?) =
        studentIdAtom.incrementAndGet().let {
            Student.fromRepository(
                id = StudentId(it),
                name = StudentName("生徒$it"),
                loginId = StudentLoginId("student_$it"),
                belongingClassroomId = classroomId
            )
        }


}
