package thomashan.github.io.australia.rent

import groovy.transform.Immutable

@Immutable(knownImmutableClasses = [Optional])
class SearchQuery {
    int minPrice
    int maxPrice
    int minBedroom
    Optional<Integer> maxBedroom
}
