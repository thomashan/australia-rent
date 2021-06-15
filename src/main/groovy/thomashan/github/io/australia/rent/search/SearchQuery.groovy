package thomashan.github.io.australia.rent.search

import groovy.transform.Immutable

@Immutable(knownImmutableClasses = [Optional])
class SearchQuery {
    Optional<List<String>> suburbs = Optional.empty()
    Optional<Integer> minPrice = Optional.empty()
    Optional<Integer> maxPrice = Optional.empty()
    Optional<Integer> minBedroom = Optional.empty()
    Optional<Integer> maxBedroom = Optional.empty()

    String getName() {
        return [prices, bedrooms].findAll().join("_")
    }

    String getPrices() {
        String prices = SearchParameterExtractor.prices(this)
        return prices ? "prices_${prices}" : prices
    }

    String getBedrooms() {
        String bedrooms = SearchParameterExtractor.bedrooms(this)
        return bedrooms ? "bedrooms_${bedrooms}" : bedrooms
    }
}
