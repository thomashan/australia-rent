package thomashan.github.io.australia.rent.search

import spock.lang.Specification
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.file.dropbox.DropboxFileRepository

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static java.util.Optional.empty as e
import static java.util.Optional.of

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
        sleep(1000)
        dropboxFileRepository.upload(oneEntryFile, fullPath(today))
    }

    def cleanupSpec() {
        dropboxFileRepository.delete(rootPath)
    }

    def "getPrices should prepend prices_ if minPrice or maxPrice not empty"() {
        when:
        Search search = new SearchImpl1(new SearchQuery(e(), of(200), of(400), e(), e()))

        then:
        search.prices == "prices_200-400"
    }

    def "getPrices should not prepend price_ if minPrice and maxPrice is empty"() {
        when:
        Search search = new SearchImpl1(new SearchQuery(e(), e(), e(), e(), e()))

        then:
        search.prices == ""
    }

    def "getBedrooms should prepend bedrooms_ if minBedrooms or maxBedroom not empty"() {
        when:
        Search search = new SearchImpl1(new SearchQuery(e(), e(), e(), of(1), of(1)))

        then:
        search.bedrooms == "bedrooms_1-1"
    }

    def "getBedrooms should not prepend bedrooms_ if minBedrooms and maxBedroom is empty"() {
        when:
        Search search = new SearchImpl1(new SearchQuery(e(), e(), e(), e(), e()))

        then:
        search.bedrooms == ""
    }

    def "getName prices and bedrooms defined"() {
        when:
        Search search = new SearchImpl1(new SearchQuery(e(), of(100), of(200), of(1), of(1)))

        then:
        search.name == "prices_100-200_bedrooms_1-1"
    }

    def "getName prices defined and bedrooms undefined"() {
        when:
        Search search = new SearchImpl1(new SearchQuery(e(), of(100), of(200), e(), e()))

        then:
        search.name == "prices_100-200"
    }

    def "getName prices undefined and bedrooms defined"() {
        when:
        Search search = new SearchImpl1(new SearchQuery(e(), e(), e(), of(1), of(1)))

        then:
        search.name == "bedrooms_1-1"
    }

    def "getName prices undefined and bedrooms undefined"() {
        when:
        Search search = new SearchImpl1(new SearchQuery(e(), e(), e(), e(), e()))

        then:
        search.name == ""
    }

    def "getLatest returns the latest available from fileRepository which is not today's list"() {
        given:
        Search search = new SearchImpl2(new SearchQuery(e(), of(200), of(400), of(2), e()))

        when:
        List<RentDetails> rentDetails = search.latest

        then:
        rentDetails.size() == 3
    }

    private String fullPath(LocalDate localDate) {
        return "${basePath}/${localDate.format(f)}.csv"
    }

    private static class SearchImpl1 implements Search {
        SearchQuery searchQuery

        SearchImpl1(SearchQuery searchQuery) {
            this.searchQuery = searchQuery
        }

        @Override
        void search() {
            throw new UnsupportedOperationException()
        }
    }

    private static class SearchImpl2 implements Search {
        SearchQuery searchQuery

        SearchImpl2(SearchQuery searchQuery) {
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
