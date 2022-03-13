package co.ukwksk.domain.test

import co.ukwksk.domain.value.ClassroomId
import co.ukwksk.domain.value.StudentId
import co.ukwksk.domain.value.StudentLoginId
import co.ukwksk.domain.value.StudentName
import co.ukwksk.repository.db.ClassroomStudentTable
import co.ukwksk.repository.db.StudentTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert

class StudentPreparation(
    val id: StudentId,
    val name: StudentName,
    val loginId: StudentLoginId,
) {
    constructor(row: ResultRow) : this(
        id = StudentId(row[StudentTable.id].value),
        name = StudentName(row[StudentTable.name]),
        loginId = StudentLoginId(row[StudentTable.loginId]),
    )

    var classId: ClassroomId? = null
}

object StudentTester : AbstractTester() {

    fun insertStudent(): StudentPreparation {
        val now = nowStr()
        return StudentTable.insert {
            it[name] = "生徒$now"
            it[loginId] = "student_$now"
        }.resultedValues!!.single()
            .let { StudentPreparation(it) }
    }

    fun StudentPreparation.addClassroom(classroom: ClassroomPreparation): StudentPreparation {
        val studentIdRaw = this.id.value
        return ClassroomStudentTable.insert {
            it[studentId] = studentIdRaw
            it[classroomId] = classroom.id.value
        }.resultedValues!!.single().let { row ->
            this.also {
                classId = ClassroomId(row[ClassroomStudentTable.classroomId])
            }
        }
    }
}
