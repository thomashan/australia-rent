package thomashan.github.io.australia.rent.report

import spock.lang.Specification
import thomashan.github.io.australia.rent.file.FileInformation

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Matcher
import java.util.regex.Pattern

import static thomashan.github.io.australia.rent.report.ReportType.ALL
import static thomashan.github.io.australia.rent.report.ReportType.COMMON

class ReportNameSpec extends Specification {
    private LocalDateTime now = LocalDateTime.now()
    private LocalDate today = now.toLocalDate()
    private DateTimeFormatter f = DateTimeFormatter.BASIC_ISO_DATE
    private fileName = today.format(f)

    def "fullPath without suffix"() {
        when:
        ReportName reportName = new ReportName("/test/prices_200-400_bedrooms_2-any", today, ALL)

        then:
        reportName.fullPath == "/test/prices_200-400_bedrooms_2-any/${fileName}.csv"
    }

    def "fullPath with suffix"() {
        when:
        ReportName reportName = new ReportName("/test/prices_200-400_bedrooms_2-any", today, COMMON)

        then:
        reportName.fullPath == "/test/prices_200-400_bedrooms_2-any/${fileName}_common.csv"
    }

    def "fileName without suffix"() {
        when:
        ReportName reportName = new ReportName("/test/prices_200-400_bedrooms_2-any", today, ALL)

        then:
        reportName.fileName == "${fileName}.csv"
    }

    def "fileName with suffix"() {
        when:
        ReportName reportName = new ReportName("/test/prices_200-400_bedrooms_2-any", today, COMMON)

        then:
        reportName.fileName == "${fileName}_common.csv"
    }

    def "fileNamePattern matches exact file name"() {
        when:
        Pattern pattern = Pattern.compile("\\d{8}\\.csv")
        Matcher matcher = pattern.matcher("20000101.csv")

        then:
        matcher.count == 1
        matcher[0] == "20000101.csv"
    }

    def "fileNamePattern does not match file name with suffix"() {
        when:
        Pattern pattern = Pattern.compile("\\d{8}\\.csv")
        Matcher matcher = pattern.matcher("20000101_suffix.csv")

        then:
        matcher.count == 0
    }

    def "getReportName should return correct ReportName"() {
        when:
        ReportName reportName1 = ReportName.getReportName(new FileInformation("/test", "/test/20000101_common.csv", "20000101_common.csv", now))
        ReportName reportName2 = ReportName.getReportName(new FileInformation("/test", "/test/20000101.csv", "20000101.csv", now))

        then:
        reportName1.base == "/test"
        reportName1.fileName == "20000101_common.csv"
        reportName1.localDate == LocalDate.of(2000, 1, 1)
        reportName1.suffix == Optional.of("common")

        reportName2.base == "/test"
        reportName2.fileName == "20000101.csv"
        reportName2.localDate == LocalDate.of(2000, 1, 1)
        reportName2.suffix == Optional.empty()
    }
}
