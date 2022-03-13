package co.ukwksk.repository.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object TeacherTable : IntIdTable("teacher") {
    val name: Column<String> = varchar("name", 128)
    val loginId: Column<String> = varchar("login_id", 128)
        .uniqueIndex()
}

object ClassroomTable : IntIdTable("classroom") {
    val name: Column<String> = varchar("name", 128)
}

object StudentTable : IntIdTable("student") {
    val name: Column<String> = varchar("name", 128)
    val loginId: Column<String> = varchar("login_id", 128)
        .uniqueIndex()
}

object TeacherClassroomTable : Table("teacher_classroom") {
    val teacherId: Column<Int> = integer("teacher_id")
        .references(TeacherTable.id)
    val classroomId: Column<Int> = integer("classroom_id")
        .references(ClassroomTable.id)

    init {
        uniqueIndex(this.teacherId, this.classroomId)
    }
}

object ClassroomStudentTable : Table("classroom_student") {
    val classroomId: Column<Int> = integer("classroom_id")
        .references(ClassroomTable.id)
    val studentId: Column<Int> = integer("student_id")
        .references(StudentTable.id)

    init {
        TeacherClassroomTable.uniqueIndex(this.classroomId, this.studentId)
    }
}
