package co.ukwksk.domain.repository

data class PagingSpec(
    val page: Int,
    val limit: Int,
) {
    init {
        require(page >= 0)
        require(limit >= 0)
    }

    val offset = (page * limit).toLong()
}
