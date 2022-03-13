package co.ukwksk.domain.test

import co.ukwksk.domain.value.TeacherId
import co.ukwksk.repository.db.TeacherTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert

class TeacherPreparation(
    val id: TeacherId
) {
    constructor(row: ResultRow) : this(
        id = TeacherId(row[TeacherTable.id].value)
    )
}

object TeacherTester : AbstractTester() {
    val NO_EXIST_ID = TeacherId(Companion.NO_EXIST_ID)

    fun insertTeacher(): TeacherPreparation {
        val now = nowStr()
        return TeacherTable.insert {
            it[name] = "先生$now"
            it[loginId] = "teacher_$now"
        }.resultedValues!!.single()
            .let { TeacherPreparation(it) }
    }
}
