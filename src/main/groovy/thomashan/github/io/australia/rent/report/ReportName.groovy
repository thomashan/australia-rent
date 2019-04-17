package thomashan.github.io.australia.rent.report

import thomashan.github.io.australia.rent.search.Search

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReportName {
    private String value

    ReportName(Search search, LocalDate localDate) {
        DateTimeFormatter f = DateTimeFormatter.BASIC_ISO_DATE
        this.value = "${search.name}/${localDate.format(f)}.csv"
    }

    String getValue() {
        return value
    }
}
