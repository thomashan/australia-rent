package thomashan.github.io.australia.rent.search

import spock.lang.Specification
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.SearchQuery
import thomashan.github.io.australia.rent.file.dropbox.DropboxFileRepository

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static java.util.Optional.empty

class SearchSpec extends Specification {
    private static final LocalDate today = LocalDate.now()
    private static final String rootPath = "/test"
    private static final String basePath = "${rootPath}/search"
    private static final DateTimeFormatter f = DateTimeFormatter.BASIC_ISO_DATE
    private static final DropboxFileRepository dropboxFileRepository = new DropboxFileRepository()

    def setupSpec() {
        File oneEntryFile = new File(this.class.getResource("/rent_details.csv").toURI())
        File threeEntryFile = new File(this.class.getResource("/rent_details_3_entries.csv").toURI())
        dropboxFileRepository.upload(oneEntryFile, fullPath(today.minusDays(4)))
        sleep(1000)
        dropboxFileRepository.upload(threeEntryFile, fullPath(today.minusDays(2)))
    }

    def cleanupSpec() {
        dropboxFileRepository.delete(rootPath)
    }

    def "getPrices"() {
        when:
        Search search = new SearchImpl(new SearchQuery(200, 400, 2, empty()))

        then:
        search.prices == "200-400"
    }

    def "getBedrooms"() {
        when:
        Search search = new SearchImpl(new SearchQuery(200, 400, 2, Optional.of(2)))

        then:
        search.bedrooms == "2-2"
    }

    def "getBedrooms return correct value when max bedrooms empty"() {
        when:
        Search search = new SearchImpl(new SearchQuery(200, 400, 2, empty()))

        then:
        search.bedrooms == "2+"
    }

    def "getPrevious returns the latest available from fileRepository"() {
        given:
        Search search = new SearchImpl(new SearchQuery(200, 400, 2, empty()))

        when:
        List<RentDetails> rentDetails = search.latest

        then:
        rentDetails.size() == 3
    }

    private String fullPath(LocalDate localDate) {
        return "${basePath}/${localDate.format(f)}.csv"
    }

    private static class SearchImpl implements Search {
        final SearchQuery searchQuery

        SearchImpl(SearchQuery searchQuery) {
            this.searchQuery = searchQuery
        }

        @Override
        void search() {
            throw new UnsupportedOperationException()
        }

        String getName() {
            return "test/search"
        }
    }
}
