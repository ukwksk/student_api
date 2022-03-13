package co.ukwksk.domain.service

import co.ukwksk.TransactionalTest
import co.ukwksk.domain.test.TeacherTester
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.koin.test.inject

internal class TeacherServiceTest : TransactionalTest() {
    @Nested
    inner class TestOfFindById {
        private val target: TeacherService by inject()

        @Test
        fun success_found() {
            withRollback {
                val teacher = TeacherTester.insertTeacher()

                val actual = target.findById(teacherId = teacher.id)

                assertNotNull(actual)
                assertEquals(teacher.id, actual!!.id)
            }
        }

        @Test
        fun success_notFound() {
            withRollback {
                val actual = target.findById(teacherId = TeacherTester.NO_EXIST_ID)

                assertNull(actual)
            }
        }
    }
}
