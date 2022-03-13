package co.ukwksk.domain.repository

import co.ukwksk.domain.model.PagingResult
import co.ukwksk.domain.model.Student
import co.ukwksk.domain.value.ClassroomId

/**
 * @param classIdSet 生徒が所属するクラスのIDの [Set]。１件以上の指定が必要。
 * @param sort ソートに指定する項目と順序の [Pair]。
 * @param nameLike 名前での部分一致検索。
 * @param loginIdLike ログインIDでの部分一致検索。
 */
data class StudentSpec(
    val classIdSet: Set<ClassroomId>,
    val sort: Pair<SortBy, Order>? = null,
    val nameLike: String? = null,
    val loginIdLike: String? = null,
) {
    init {
        require(classIdSet.isNotEmpty())
    }
}

enum class SortBy {
    NAME,
    LOGIN_ID,
    ;
}

enum class Order {
    ASC,
    DESC,
    ;
}

interface StudentRepository {
    fun pagingBy(spec: StudentSpec, paging: PagingSpec): PagingResult<Student>
}
