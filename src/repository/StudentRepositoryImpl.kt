package co.ukwksk.repository

import co.ukwksk.domain.model.PagingResult
import co.ukwksk.domain.model.Student
import co.ukwksk.domain.repository.*
import co.ukwksk.domain.value.ClassroomId
import co.ukwksk.domain.value.StudentId
import co.ukwksk.domain.value.StudentLoginId
import co.ukwksk.domain.value.StudentName
import co.ukwksk.repository.db.ClassroomStudentTable
import co.ukwksk.repository.db.StudentTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList

class StudentRepositoryImpl : StudentRepository {
    override fun pagingBy(spec: StudentSpec, paging: PagingSpec): PagingResult<Student> {
        return PagingResult(
            page = paging.page,
            limit = paging.limit,
            list = StudentTable
                .innerJoin(ClassroomStudentTable)
                .select(spec.createOp())
                .paging(paging)
                .addOrderBy(spec.sort)
                .map { Student.fromResultRow(it) }
        )
    }

    private fun StudentSpec.createOp(): Op<Boolean> {
        val baseOp: Op<Boolean> =
            ClassroomStudentTable.classroomId inList
                    this.classIdSet.map { it.value }
        return listOfNotNull(
            this.nameLike?.let { StudentTable.name likeWithEscape it },
            this.loginIdLike?.let { StudentTable.loginId likeWithEscape it },
        ).fold(baseOp) { cur, next ->
            cur and next
        }
    }

    private fun Query.paging(paging: PagingSpec?) =
        paging?.let {
            this.limit(n = it.limit, offset = it.offset)
        } ?: this

    private fun Query.addOrderBy(sort: Pair<SortBy, Order>?) =
        sort?.let {
            this.orderBy(
                column = sortByColumnMap.getValue(it.first),
                order = orderSortOrderMap.getValue(it.second),
            )
        } ?: this
}

private val sortByColumnMap = mapOf(
    SortBy.NAME to StudentTable.name,
    SortBy.LOGIN_ID to StudentTable.loginId,
)
private val orderSortOrderMap = mapOf(
    Order.ASC to SortOrder.ASC,
    Order.DESC to SortOrder.DESC,
)

private fun Student.Companion.fromResultRow(row: ResultRow) = fromRepository(
    id = StudentId(row[StudentTable.id].value),
    name = StudentName(row[StudentTable.name]),
    loginId = StudentLoginId(row[StudentTable.loginId]),
    belongingClassroomId = when (row.hasValue(ClassroomStudentTable.studentId)) {
        true -> ClassroomId(row[ClassroomStudentTable.classroomId])
        false -> null
    }
)
