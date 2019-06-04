package thomashan.github.io.australia.rent.report

import spock.lang.Specification
import thomashan.github.io.australia.rent.search.Search
import thomashan.github.io.australia.rent.search.SearchQuery

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.regex.Matcher
import java.util.regex.Pattern

import static java.util.Optional.empty as e
import static java.util.Optional.of

class ReportNameSpec extends Specification {
    private Search search = new SearchImpl()
    private LocalDate today = LocalDate.now()
    private DateTimeFormatter f = DateTimeFormatter.BASIC_ISO_DATE
    private fileName = today.format(f)

    def "fullPath without suffix"() {
        when:
        ReportName reportName = new ReportName(search, today)

        then:
        reportName.fullPath == "/test/prices_200-400_bedrooms_2-any/${fileName}.csv"
    }

    def "fullPath with suffix"() {
        when:
        ReportName reportName = new ReportName(search, today, Optional.of("suffix"))

        then:
        reportName.fullPath == "/test/prices_200-400_bedrooms_2-any/${fileName}_suffix.csv"
    }

    def "fileName without suffix"() {
        when:
        ReportName reportName = new ReportName(search, today)

        then:
        reportName.fileName == "${fileName}.csv"
    }

    def "fileName with suffix"() {
        when:
        ReportName reportName = new ReportName(search, today, Optional.of("suffix"))

        then:
        reportName.fileName == "${fileName}_suffix.csv"
    }

    def "fileNamePattern without suffix"() {
        when:
        ReportName reportName = new ReportName(search, today)

        then:
        reportName.fileNamePattern == "\\d{8}\\.csv"
    }

    def "fileNamePattern with suffix"() {
        when:
        ReportName reportName = new ReportName(search, today, Optional.of("suffix"))

        then:
        reportName.fileNamePattern == "\\d{8}_suffix\\.csv"
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

    private static final class SearchImpl implements Search {
        @Override
        SearchQuery getSearchQuery() {
            return new SearchQuery(e(), of(200), of(400), of(2), e())
        }

        @Override
        void search() {
            throw new UnsupportedOperationException()
        }

        @Override
        String getName() {
            return "test/${[prices, bedrooms].findAll { it }.join("_")}"
        }
    }
}
