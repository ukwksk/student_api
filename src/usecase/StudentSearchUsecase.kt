package co.ukwksk.usecase

import co.ukwksk.domain.model.Classroom
import co.ukwksk.domain.model.Student
import co.ukwksk.domain.service.ClassroomService
import co.ukwksk.domain.service.StudentService
import co.ukwksk.domain.service.TeacherService
import co.ukwksk.domain.service.param.PagingParam
import co.ukwksk.domain.service.param.StudentLike
import co.ukwksk.domain.service.param.StudentListParameter
import co.ukwksk.domain.service.param.StudentSortBy
import co.ukwksk.domain.value.TeacherId
import co.ukwksk.usecase.exception.ResourceNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction

data class StudentSearchRequestDTO(
    val teacherId: TeacherId,
    val pagingParam: PagingParam,
    val sortParam: StudentSortBy?,
    val likeParam: StudentLike,
)

data class StudentSearchResultDTO(
    val studentList: List<Pair<Student, Classroom>>
) {
    companion object {
        fun empty() = StudentSearchResultDTO(listOf())
    }
}

class StudentSearchUsecase(
    private val teacherService: TeacherService,
    private val classroomService: ClassroomService,
    private val studentService: StudentService,
) {

    fun search(request: StudentSearchRequestDTO): StudentSearchResultDTO {
        return transaction {
            searchInternal(request)
        }
    }

    internal fun searchInternal(request: StudentSearchRequestDTO): StudentSearchResultDTO {
        val teacher = teacherService.findById(request.teacherId)
            ?: throw ResourceNotFoundException()
        val classroomMap = classroomService.listByTeacherId(teacher.id)
            .associateBy { it.id }
            .takeIf { it.isNotEmpty() }
            ?: return StudentSearchResultDTO.empty()
        return studentService.listByClassIdList(
            StudentListParameter(
                classroomIdSet = classroomMap.keys,
                sortParam = request.sortParam,
                likeParam = request.likeParam,
            ),
            paging = request.pagingParam,
        ).list
            .also { it.map { s -> s.belongingClassroomId }.requireNoNulls() }
            .map { it to classroomMap.getValue(it.belongingClassroomId!!) }
            .let { StudentSearchResultDTO(it) }
    }
}
