package co.ukwksk.domain.service

import co.ukwksk.TransactionalTest
import co.ukwksk.domain.test.ClassroomTester
import co.ukwksk.domain.test.ClassroomTester.addTeacher
import co.ukwksk.domain.test.TeacherTester
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.koin.test.inject
import kotlin.test.assertEquals

internal class ClassroomServiceTest : TransactionalTest() {
    @Nested
    inner class TestOfListByTeacherId {
        private val target: ClassroomService by inject()

        @Test
        fun success_found() {
            withRollback {
                val teacher1 = TeacherTester.insertTeacher()
                val classroom = ClassroomTester.insertClass()
                    .addTeacher(teacher1)
                    .addTeacher(TeacherTester.insertTeacher())
                ClassroomTester.insertClass()
                    .addTeacher(TeacherTester.insertTeacher())

                val actual = target.listByTeacherId(teacherId = teacher1.id)

                assertEquals(1, actual.size)
                val foundClassroom = actual.single()
                assertEquals(classroom.id, foundClassroom.id)
                assertEquals(classroom.name, foundClassroom.name)
            }
        }

        @Test
        fun success_notFound() {
            withRollback {
                val teacher = TeacherTester.insertTeacher()
                ClassroomTester.insertClass()
                    .addTeacher(TeacherTester.insertTeacher())

                val actual = target.listByTeacherId(teacherId = teacher.id)

                assertEquals(0, actual.size)
            }
        }
    }
}
