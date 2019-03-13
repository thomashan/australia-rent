package thomashan.github.io.australia.rent

import groovy.transform.Immutable

@Immutable(knownImmutableClasses = [Optional])
class SearchQuery {
    double minPrice
    double maxPrice
    double minBedroom
    Optional<Double> maxBedroom
}
