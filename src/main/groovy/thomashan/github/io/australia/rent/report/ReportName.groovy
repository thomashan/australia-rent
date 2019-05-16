package thomashan.github.io.australia.rent.report

import thomashan.github.io.australia.rent.search.Search

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReportName {
    final String value

    ReportName(Search search, LocalDate localDate, Optional<String> suffix = Optional.empty()) {
        DateTimeFormatter f = DateTimeFormatter.BASIC_ISO_DATE
        String tempValue1 = "/${search.name}/${localDate.format(f)}"
        String tempValue2 = suffix.isEmpty() ? ".csv" : "_${suffix.get()}.csv"
        this.value = tempValue1 + tempValue2
    }
}
