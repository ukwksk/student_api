package co.ukwksk.repository

import co.ukwksk.domain.model.Teacher
import co.ukwksk.domain.repository.TeacherRepository
import co.ukwksk.domain.repository.TeacherSpec
import co.ukwksk.domain.value.TeacherId
import co.ukwksk.repository.db.TeacherTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

class TeacherRepositoryImpl : TeacherRepository {
    override fun findBy(spec: TeacherSpec): Teacher? {
        return TeacherTable
            .select { TeacherTable.id eq spec.teacherId.value }
            .singleOrNull()
            ?.let { Teacher.fromResultRow(it) }
    }
}

private fun Teacher.Companion.fromResultRow(row: ResultRow) = fromRepository(
    id = TeacherId(row[TeacherTable.id].value),
)
