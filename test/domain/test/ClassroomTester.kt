package co.ukwksk.domain.test

import co.ukwksk.domain.value.ClassroomId
import co.ukwksk.domain.value.ClassroomName
import co.ukwksk.domain.value.TeacherId
import co.ukwksk.repository.db.ClassroomTable
import co.ukwksk.repository.db.TeacherClassroomTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert

class ClassroomPreparation(
    val id: ClassroomId,
    val name: ClassroomName,
) {
    constructor(row: ResultRow) : this(
        id = ClassroomId(row[ClassroomTable.id].value),
        name = ClassroomName(row[ClassroomTable.name]),
    )

    val teacherIdList: MutableList<TeacherId> = mutableListOf()
}

object ClassroomTester : AbstractTester() {
    val NO_EXIST_ID = ClassroomId(Companion.NO_EXIST_ID)

    fun insertClass(): ClassroomPreparation {
        val now = nowStr()
        return ClassroomTable.insert {
            it[name] = "クラス$now"
        }.resultedValues!!.single()
            .let { ClassroomPreparation(it) }
    }

    fun ClassroomPreparation.addTeacher(teacher: TeacherPreparation): ClassroomPreparation {
        val classroomIdRaw = this.id.value
        return TeacherClassroomTable.insert {
            it[classroomId] = classroomIdRaw
            it[teacherId] = teacher.id.value
        }.resultedValues!!.single().let { row ->
            this.also {
                teacherIdList.add(
                    TeacherId(row[TeacherClassroomTable.teacherId])
                )
            }
        }
    }
}
