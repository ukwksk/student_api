package co.ukwksk.domain.service

import co.ukwksk.TransactionalTest
import co.ukwksk.domain.service.param.PagingParam
import co.ukwksk.domain.service.param.StudentLike
import co.ukwksk.domain.service.param.StudentListParameter
import co.ukwksk.domain.test.ClassroomTester
import co.ukwksk.domain.test.StudentTester
import co.ukwksk.domain.test.StudentTester.addClassroom
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.test.inject
import kotlin.test.assertEquals

internal class StudentServiceTest : TransactionalTest() {
    @Nested
    inner class TestOfFindById {
        private val target: StudentService by inject()

        @Test
        fun success_found() {
            withRollback {
                val classroom = ClassroomTester.insertClass()
                val student = StudentTester.insertStudent()
                    .addClassroom(classroom)
                StudentTester.insertStudent()
                    .addClassroom(ClassroomTester.insertClass())

                val actual = target.listByClassIdList(
                    StudentListParameter(
                        classroomIdSet = setOf(classroom.id),
                        sortParam = null,
                        likeParam = StudentLike(null, null),
                    ),
                    paging = PagingParam(0, 10),
                )

                assertEquals(1, actual.list.size)
                val foundStudent = actual.list.single()
                assertEquals(student.id, foundStudent.id)
                assertEquals(student.name, foundStudent.name)
                assertEquals(student.loginId, foundStudent.loginId)
                assertEquals(student.classId, foundStudent.belongingClassroomId)
            }
        }

        @Test
        fun success_notFound() {
            withRollback {
                StudentTester.insertStudent()
                    .addClassroom(ClassroomTester.insertClass())

                val actual = target.listByClassIdList(
                    StudentListParameter(
                        classroomIdSet = setOf(ClassroomTester.NO_EXIST_ID),
                        sortParam = null,
                        likeParam = StudentLike(null, null),
                    ),
                    paging = PagingParam(0, 10),
                )

                assertTrue(actual.list.isEmpty())
            }
        }

        @Test
        fun fail_emptyClassSet() {
            withRollback {
                assertThrows<IllegalArgumentException> {
                    target.listByClassIdList(
                        StudentListParameter(
                            classroomIdSet = setOf(),
                            sortParam = null,
                            likeParam = StudentLike(null, null),
                        ),
                        paging = PagingParam(0, 10),
                    )
                }
            }
        }
    }
}
