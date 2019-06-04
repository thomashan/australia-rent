package thomashan.github.io.australia.rent.search

import groovy.transform.Immutable

@Immutable(knownImmutableClasses = [Optional])
class SearchQuery {
    Optional<List<String>> suburbs = Optional.empty()
    Optional<Integer> minPrice = Optional.empty()
    Optional<Integer> maxPrice = Optional.empty()
    Optional<Integer> minBedroom = Optional.empty()
    Optional<Integer> maxBedroom = Optional.empty()
}
