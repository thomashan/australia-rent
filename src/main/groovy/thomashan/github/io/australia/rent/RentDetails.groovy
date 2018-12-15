package thomashan.github.io.australia.rent

import groovy.transform.Immutable

@Immutable(knownImmutableClasses = [Optional])
class RentDetails {
    Optional<BigDecimal> price
    String address
    String suburb
    String state
    String postcode
}
