package thomashan.github.io.australia.rent

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable

@Immutable(knownImmutableClasses = [Optional], copyWith = true)
@EqualsAndHashCode(excludes = ["coordinates"])
class RentDetails {
    Optional<BigDecimal> price
    String address
    String suburb
    String state
    String postcode
    Integer bedrooms
    Integer bathrooms
    Integer parking
    Optional<LatLongCoordinates> coordinates = Optional.empty()
}
