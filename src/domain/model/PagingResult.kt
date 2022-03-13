package co.ukwksk.domain.model

data class PagingResult<T>(
    val page: Int,
    val limit: Int,
    val list: List<T>,
) {
    init {
        require(page >= 0)
    }
}
