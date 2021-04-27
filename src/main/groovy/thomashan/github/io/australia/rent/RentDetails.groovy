package thomashan.github.io.australia.rent

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable
import groovy.transform.ImmutableBase
import groovy.transform.ImmutableOptions


@ImmutableBase(copyWith = true)
@ImmutableOptions(knownImmutableClasses = [Optional])
@Immutable
@EqualsAndHashCode(excludes = ["coordinates", "daysInMarket"])
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
    Map<DataSource, Long> daysInMarket = new EnumMap<>(DataSource)
}
