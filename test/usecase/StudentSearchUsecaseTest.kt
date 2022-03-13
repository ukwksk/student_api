package usecase

import co.ukwksk.MockTest
import co.ukwksk.domain.model.PagingResult
import co.ukwksk.domain.service.ClassroomService
import co.ukwksk.domain.service.StudentService
import co.ukwksk.domain.service.TeacherService
import co.ukwksk.domain.service.param.PagingParam
import co.ukwksk.domain.service.param.StudentLike
import co.ukwksk.domain.service.param.StudentListParameter
import co.ukwksk.domain.value.TeacherId
import co.ukwksk.usecase.test.DomainHolder
import co.ukwksk.usecase.StudentSearchRequestDTO
import co.ukwksk.usecase.StudentSearchUsecase
import co.ukwksk.usecase.exception.ResourceNotFoundException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class StudentSearchUsecaseTest {

    companion object {
        val anyPagingParam = PagingParam(0, 0)
        val anySortParam = null
        val anyLikeParam = StudentLike(null, null)
    }

    @Nested
    inner class TestOfSearchInternal : MockTest() {

        @Mock
        lateinit var teacherService: TeacherService

        @Mock
        lateinit var classroomService: ClassroomService

        @Mock
        lateinit var studentService: StudentService

        @InjectMocks
        lateinit var target: StudentSearchUsecase

        @Test
        fun `fail 先生IDが存在しない`() {
            val teacherId = TeacherId(1)
            whenever(teacherService.findById(eq(teacherId))).thenReturn(null)

            assertThrows<ResourceNotFoundException> {
                target.searchInternal(
                    StudentSearchRequestDTO(
                        teacherId = teacherId,
                        pagingParam = PagingParam(0, 0),
                        sortParam = null,
                        likeParam = StudentLike(null, null)
                    )
                )
            }
        }

        @Test
        fun `success クラスが存在しない`() {
            val teacher = DomainHolder.createTeacher()
            val teacherId = teacher.id
            whenever(teacherService.findById(eq(teacherId)))
                .thenReturn(teacher)
            whenever(classroomService.listByTeacherId(eq(teacherId)))
                .thenReturn(listOf())

            val actual = target.searchInternal(
                StudentSearchRequestDTO(
                    teacherId = teacherId,
                    pagingParam = PagingParam(0, 0),
                    sortParam = null,
                    likeParam = StudentLike(null, null)
                )
            )

            assertTrue(actual.studentList.isEmpty())
        }

        @Test
        fun `success 生徒が存在しない`() {
            val teacher = DomainHolder.createTeacher()
            val teacherId = teacher.id
            val classroom = DomainHolder.createClass()
            val parameter = StudentListParameter(
                classroomIdSet = setOf(classroom.id),
                sortParam = anySortParam,
                likeParam = anyLikeParam,
            )
            whenever(teacherService.findById(eq(teacherId)))
                .thenReturn(teacher)
            whenever(classroomService.listByTeacherId(eq(teacherId)))
                .thenReturn(listOf(classroom))
            whenever(studentService.listByClassIdList(
                eq(parameter),
                eq(anyPagingParam),
            ))
                .thenReturn(
                    PagingResult(
                        anyPagingParam.page,
                        anyPagingParam.limit,
                        listOf()
                    )
                )

            val actual = target.searchInternal(
                StudentSearchRequestDTO(
                    teacherId = teacherId,
                    pagingParam = anyPagingParam,
                    sortParam = anySortParam,
                    likeParam = anyLikeParam,
                )
            )

            assertTrue(actual.studentList.isEmpty())
        }

        @Test
        fun `success 生徒が存在する`() {
            val teacher = DomainHolder.createTeacher()
            val teacherId = teacher.id
            val classroom = DomainHolder.createClass()
            val student = DomainHolder.createStudent(classroom.id)
            val parameter = StudentListParameter(
                classroomIdSet = setOf(classroom.id),
                sortParam = anySortParam,
                likeParam = anyLikeParam,
            )
            whenever(teacherService.findById(eq(teacherId)))
                .thenReturn(teacher)
            whenever(classroomService.listByTeacherId(eq(teacherId)))
                .thenReturn(listOf(classroom))
            whenever(studentService.listByClassIdList(
                eq(parameter),
                eq(anyPagingParam),
            ))
                .thenReturn(
                    PagingResult(
                        anyPagingParam.page,
                        anyPagingParam.limit,
                        listOf(student)
                    )
                )

            val actual = target.searchInternal(
                StudentSearchRequestDTO(
                    teacherId = teacherId,
                    pagingParam = anyPagingParam,
                    sortParam = anySortParam,
                    likeParam = anyLikeParam,
                )
            )

            assertTrue(actual.studentList.size == 1)
            val actualStudent = actual.studentList[0].first
            val actualClassroom = actual.studentList[0].second
            assertEquals(student, actualStudent)
            assertEquals(classroom, actualClassroom)
        }
    }
}
