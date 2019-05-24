package thomashan.github.io.australia.rent.report

import thomashan.github.io.australia.rent.search.Search

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReportName {
    final String base
    final String fullPath
    final String fileName
    final Optional<String> suffix
    final String fileNamePattern

    ReportName(Search search, LocalDate localDate, Optional<String> suffix = Optional.empty()) {
        DateTimeFormatter f = DateTimeFormatter.BASIC_ISO_DATE
        String fileNamePrefix = "${localDate.format(f)}"
        String fileNameSuffix = suffix.isEmpty() ? ".csv" : "_${suffix.get()}.csv"
        String fileNameSuffixPattern = suffix.isEmpty() ? "\\.csv" : "_${suffix.get()}\\.csv"

        this.base = "/${search.name}"
        this.suffix = suffix
        this.fileName = fileNamePrefix + fileNameSuffix
        this.fullPath = "${base}/${fileName}"
        this.fileNamePattern = "\\d{8}${fileNameSuffixPattern}"
    }
}
