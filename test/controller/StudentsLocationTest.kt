package co.ukwksk.controller

import co.ukwksk.controller.StudentsLocation.Companion.toPagingParam
import co.ukwksk.controller.StudentsLocation.Companion.toStudentSort
import co.ukwksk.domain.service.param.PagingParam
import co.ukwksk.domain.service.param.StudentLike
import co.ukwksk.domain.service.param.StudentSortBy
import co.ukwksk.domain.value.TeacherId
import co.ukwksk.usecase.StudentSearchRequestDTO
import io.ktor.features.*
import io.ktor.locations.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.assertEquals

@KtorExperimentalLocationsAPI
internal class StudentsLocationTest {
    @Nested
    inner class TestOfConstructor {
        @ParameterizedTest
        @EnumSource(SuccessFixtureOfConstructor::class)
        fun success_empty(fixture: SuccessFixtureOfConstructor) {
            val actual = StudentsLocation(
                fixture.teacherId,
                fixture.page,
                fixture.limit,
                fixture.sort,
                fixture.order,
                fixture.name_like,
                fixture.loginId_like,
            ).requestDto

            assertEquals(fixture.expected, actual)
        }
    }

    enum class SuccessFixtureOfConstructor(
        val teacherId: Int,
        val page: String,
        val limit: String,
        val sort: String,
        val order: String,
        val name_like: String,
        val loginId_like: String,
        val expected: StudentSearchRequestDTO,
    ) {
        EMPTY(
            0, "", "", "", "", "", "",
            StudentSearchRequestDTO(
                teacherId = TeacherId(0),
                pagingParam = PagingParam(0, 10),
                sortParam = null,
                likeParam = StudentLike(
                    name = null,
                    loginId = null,
                )
            ),
        ),
        FULL(
            1, "2", "3", "name", "asc", "a", "b",
            StudentSearchRequestDTO(
                teacherId = TeacherId(1),
                pagingParam = PagingParam(2, 3),
                sortParam = StudentSortBy.NAME_ASC,
                likeParam = StudentLike(
                    name = "a",
                    loginId = "b",
                )
            ),
        ),
    }

    @Nested
    inner class TestOfToPagingParam {
        @ParameterizedTest
        @EnumSource(FixtureOfToPagingParam::class)
        fun test(fixture: FixtureOfToPagingParam) {
            fixture.onSuccess?.also {
                val actual = toPagingParam(fixture.page, fixture.limit)
                assertEquals(it, Pair(actual.page, actual.limit))
            } ?: run {
                assertThrows<BadRequestException> {
                    toPagingParam(fixture.page, fixture.limit)
                }
            }
        }
    }

    enum class FixtureOfToPagingParam(
        val page: String,
        val limit: String,
        val onSuccess: Pair<Int, Int>?
    ) {
        EMPTY("", "", Pair(0, 10)),
        FULL("1", "2", Pair(1, 2)),
        PAGE_EMPTY("", "3", Pair(0, 3)),
        PAGE_UNDER("-1", "4", null),
        PAGE_INVALID("a", "5", null),
        LIMIT_EMPTY("2", "", Pair(2, 10)),
        LIMIT_UNDER("3", "0", null),
        LIMIT_OVER("4", "101", null),
        LIMIT_INVALID("5", "b", null),
    }

    @Nested
    inner class TestOfToSortParam {
        @ParameterizedTest
        @EnumSource(FixtureOfToStudentSort::class)
        fun test(fixture: FixtureOfToStudentSort) {
            if (fixture.success) {
                val actual = toStudentSort(fixture.sort, fixture.order)
                assertEquals(fixture.expected, actual)
            } else {
                assertThrows<BadRequestException> {
                    toStudentSort(fixture.sort, fixture.order)
                }
            }
        }
    }

    enum class FixtureOfToStudentSort(
        val sort: String,
        val order: String,
        val success: Boolean,
        val expected: StudentSortBy?
    ) {
        EMPTY("", "", true, null),
        NAME_EMPTY("", "x", true, null),
        NAME_ASC("name", "asc", true, StudentSortBy.NAME_ASC),
        NAME_DESC("name", "desc", true, StudentSortBy.NAME_DESC),
        LOGIN_ID_ASC("loginId", "asc", true, StudentSortBy.LOGIN_ID_ASC),
        LOGIN_ID_DESC("loginId", "desc", true, StudentSortBy.LOGIN_ID_DESC),
        NAME_INVALID("x", "asc", false, null),
        ORDER_INVALID("name", "x", false, null),
    }
}
