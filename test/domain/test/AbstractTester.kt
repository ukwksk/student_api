package co.ukwksk.domain.test

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class AbstractTester {
    protected fun nowStr() = LocalDateTime.now().format(datetimeFormatter)!!

    companion object {
        private val datetimeFormatter = DateTimeFormatter.ISO_DATE_TIME

        @JvmStatic
        protected val NO_EXIST_ID = Int.MIN_VALUE
    }
}
