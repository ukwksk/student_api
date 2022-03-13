package co.ukwksk.controller

import co.ukwksk.domain.service.param.PagingParam
import co.ukwksk.domain.service.param.StudentLike
import co.ukwksk.domain.service.param.StudentSortBy
import co.ukwksk.domain.value.TeacherId
import co.ukwksk.usecase.StudentSearchRequestDTO
import co.ukwksk.usecase.StudentSearchResultDTO
import co.ukwksk.usecase.StudentSearchUsecase
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
class StudentController(
    private val studentSearchUsecase: StudentSearchUsecase
) : IController {
    override fun route(route: Route) {
        route.studentRoute()
    }

    private fun Route.studentRoute() {
        get<StudentsLocation> { loc ->
            studentSearchUsecase.search(loc.requestDto)
                .let { StudentSearchResponse.of(it) }
                .also { call.respond(it) }
        }
    }
}

@KtorExperimentalLocationsAPI
@Location("/students/")
class StudentsLocation(
    private val teacherId: Int,
    private val page: String = "",
    private val limit: String = "",
    private val sort: String = "",
    private val order: String = "",
    private val name_like: String = "",
    private val loginId_like: String = "",
) {
    companion object {
        private const val PAGE_DEFAULT = 0
        private const val PAGE_MIN = 0

        private const val LIMIT_DEFAULT = 10
        private const val LIMIT_MIN = 1
        private const val LIMIT_MAX = 100

        private const val ORDER_DEFAULT = "desc"

        fun toPagingParam(
            page: String,
            limit: String,
        ) = PagingParam(
            page = page
                .toIntOrBadRequest(min = PAGE_MIN)
                ?: PAGE_DEFAULT,
            limit = limit
                .toIntOrBadRequest(min = LIMIT_MIN, max = LIMIT_MAX)
                ?: LIMIT_DEFAULT,
        )

        fun toStudentSort(
            sort: String,
            order: String,
        ) = sort.nullIfBlank()
            ?.let {
                Sort.queryOf(it) ?: throw BadRequestException("")
            }?.getSortBy(
                Order.queryOf(order.nullIfBlank() ?: ORDER_DEFAULT)
                    ?: throw BadRequestException("")
            )

        private fun String.nullIfBlank() =
            this.takeIf { it.isNotBlank() }

        private fun String.toIntOrBadRequest(
            min: Int? = null,
            max: Int? = null,
        ) =
            this.nullIfBlank()
                ?.let {
                    it.toIntOrNull()
                        ?: throw BadRequestException("")
                }?.also { param ->
                    min?.run {
                        if (param < this) throw BadRequestException("")
                    }
                    max?.run {
                        if (param > this) throw BadRequestException("")
                    }
                }
    }

    val requestDto = StudentSearchRequestDTO(
        teacherId = TeacherId(teacherId),
        pagingParam = toPagingParam(page, limit),
        sortParam = toStudentSort(sort, order),
        likeParam = StudentLike(
            name = name_like.nullIfBlank(),
            loginId = loginId_like.nullIfBlank(),
        )
    )
}

private enum class Sort(
    private val query: String,
    private val asc: StudentSortBy,
    private val desc: StudentSortBy,
) {
    NAME(
        "name",
        StudentSortBy.NAME_ASC,
        StudentSortBy.NAME_DESC,
    ),
    LOGIN_ID(
        "loginId",
        StudentSortBy.LOGIN_ID_ASC,
        StudentSortBy.LOGIN_ID_DESC,
    ),
    ;

    private val map = mapOf(
        Order.ASC to asc,
        Order.DESC to desc,
    )

    fun getSortBy(order: Order) = map.getOrDefault(order, asc)

    companion object {
        fun queryOf(query: String) =
            values().singleOrNull { it.query == query }
    }
}

private enum class Order(private val query: String) {
    ASC("asc"),
    DESC("desc")
    ;

    companion object {
        fun queryOf(query: String) =
            values().singleOrNull { it.query == query }
    }
}

data class StudentSearchResponse(
    val students: List<Student>,
    val totalCount: Int
) {
    data class Student(
        val id: Int,
        val name: String,
        val loginId: String,
        val classrooms: List<Classroom>,
    )

    data class Classroom(
        val id: Int,
        val name: String,
    )

    companion object {
        fun of(dto: StudentSearchResultDTO) = StudentSearchResponse(
            students = dto.studentList.map {
                val student = it.first
                val classroom = it.second
                Student(
                    id = student.id.value,
                    name = student.name.value,
                    loginId = student.loginId.value,
                    classrooms = listOf(
                        Classroom(
                            id = classroom.id.value,
                            name = classroom.name.value
                        )
                    )
                )
            },
            totalCount = dto.studentList.size
        )
    }
}
