package au.org.att.util

import au.org.att.DATE_FORMAT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun parseDate(stringDate: String): Date? {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return try {
            simpleDateFormat.parse(stringDate)
        } catch (exception: ParseException) {
            null
        }
    }
}