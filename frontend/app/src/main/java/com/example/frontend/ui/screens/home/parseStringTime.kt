import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun parseStringToDateTime(dateString: String): OffsetDateTime {
    // Define a formatter for the date string
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

// Parse the date string to OffsetDateTime
    val offsetDateTime = OffsetDateTime.parse(dateString, formatter)
    return offsetDateTime
}