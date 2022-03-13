package co.ukwksk.repository

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like


private const val likeEscapeChar = "\\"
private val likeEscapeTarget = setOf('_', '%')

fun likeEscape(like: String): String {
    return like.fold("") { cur, next ->
        cur + when (likeEscapeTarget.contains(next)) {
            true -> likeEscapeChar + next
            false -> next.toString()
        }
    }
}

/**
 * 部分一致検索の条件を指定します。
 */
infix fun <T : String?> Expression<T>.likeWithEscape(pattern: String) =
    this.like("%${likeEscape(pattern)}%")
