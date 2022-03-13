package co.ukwksk.domain.service

import co.ukwksk.domain.model.PagingResult
import co.ukwksk.domain.model.Student
import co.ukwksk.domain.repository.PagingSpec
import co.ukwksk.domain.repository.StudentRepository
import co.ukwksk.domain.repository.StudentSpec
import co.ukwksk.domain.service.param.PagingParam
import co.ukwksk.domain.service.param.StudentListParameter

class StudentService(
    private val repository: StudentRepository
) {
    fun listByClassIdList(
        param: StudentListParameter,
        paging: PagingParam,
    ): PagingResult<Student> {
        return repository.pagingBy(
            StudentSpec(
                classIdSet = param.classroomIdSet,
                sort = param.sortParam?.toPair(),
                nameLike = param.likeParam.name,
                loginIdLike = param.likeParam.loginId,
            ),
            paging.let {
                PagingSpec(
                    page = it.page,
                    limit = it.limit,
                )
            },
        )
    }
}
