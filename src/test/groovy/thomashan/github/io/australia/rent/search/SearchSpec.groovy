package thomashan.github.io.australia.rent.search

import spock.lang.Specification
import thomashan.github.io.australia.rent.SearchQuery

import static java.util.Optional.empty

class SearchSpec extends Specification {
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

    private static class SearchImpl implements Search {
        final SearchQuery searchQuery

        SearchImpl(SearchQuery searchQuery) {
            this.searchQuery = searchQuery
        }

        @Override
        void search() {
            throw new UnsupportedOperationException()
        }
    }
}
