package co.ukwksk.domain.service.param

import co.ukwksk.domain.repository.Order
import co.ukwksk.domain.repository.SortBy
import co.ukwksk.domain.value.ClassroomId

enum class StudentSortBy(
    private val sortBy: SortBy,
    private val order: Order,
) {
    NAME_ASC(SortBy.NAME, Order.ASC),
    NAME_DESC(SortBy.NAME, Order.DESC),
    LOGIN_ID_ASC(SortBy.LOGIN_ID, Order.ASC),
    LOGIN_ID_DESC(SortBy.LOGIN_ID, Order.DESC),
    ;

    fun toPair(): Pair<SortBy, Order> = Pair(sortBy, order)
}

data class StudentLike(
    val name: String? = null,
    val loginId: String? = null,
)

data class StudentListParameter(
    val classroomIdSet: Set<ClassroomId>,
    val sortParam: StudentSortBy?,
    val likeParam: StudentLike,
)
