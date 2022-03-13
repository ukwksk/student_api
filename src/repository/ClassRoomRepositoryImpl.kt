package co.ukwksk.repository

import co.ukwksk.domain.model.Classroom
import co.ukwksk.domain.repository.ClassroomRepository
import co.ukwksk.domain.repository.ClassroomSpec
import co.ukwksk.domain.value.ClassroomId
import co.ukwksk.domain.value.ClassroomName
import co.ukwksk.repository.db.ClassroomTable
import co.ukwksk.repository.db.TeacherClassroomTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

class ClassroomRepositoryImpl : ClassroomRepository {
    override fun findBy(spec: ClassroomSpec): List<Classroom> {
        return ClassroomTable
            .innerJoin(TeacherClassroomTable)
            .select {
                TeacherClassroomTable.teacherId eq
                        spec.teacherId.value
            }
            .map { Classroom.fromResultRow(it) }
    }
}

private fun Classroom.Companion.fromResultRow(row: ResultRow) = fromRepository(
    id = ClassroomId(row[ClassroomTable.id].value),
    name = ClassroomName(row[ClassroomTable.name]),
)
