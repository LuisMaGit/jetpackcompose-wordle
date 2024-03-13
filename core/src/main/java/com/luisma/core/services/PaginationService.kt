package com.luisma.core.services

class PaginationService(
    private val desiredItemsInPage: Int = 50
) {

    fun toSql(page: Int): PaginationToSql {
        return PaginationToSql(
            offset = page * desiredItemsInPage,
            limit = desiredItemsInPage
        )
    }

    fun isLastPage(itemsInLastPage: Int): Boolean {
        return itemsInLastPage < desiredItemsInPage
    }
}

data class PaginationToSql(
    val offset: Int,
    val limit: Int
)
