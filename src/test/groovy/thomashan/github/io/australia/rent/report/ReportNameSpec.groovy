package thomashan.github.io.australia.rent.report

import spock.lang.Specification
import thomashan.github.io.australia.rent.SearchQuery
import thomashan.github.io.australia.rent.search.Search

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReportNameSpec extends Specification {
    private Search search = new SearchImpl()
    private LocalDate today = LocalDate.now()
    private DateTimeFormatter f = DateTimeFormatter.BASIC_ISO_DATE

    def "getValue no suffix"() {
        when:
        ReportName reportName = new ReportName(search, today)

        then:
        reportName.value == "/prices_200-400_bedrooms_2+/${today.format(f)}.csv"
    }

    def "getValue suffix"() {
        when:
        ReportName reportName = new ReportName(search, today, Optional.of("suffix"))

        then:
        reportName.value == "/prices_200-400_bedrooms_2+/${today.format(f)}_suffix.csv"
    }

    private static final class SearchImpl implements Search {
        @Override
        SearchQuery getSearchQuery() {
            return new SearchQuery(200, 400, 2, Optional.empty())
        }

        @Override
        void search() {
            throw new UnsupportedOperationException()
        }
    }
}
