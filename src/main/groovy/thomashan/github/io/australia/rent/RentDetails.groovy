package thomashan.github.io.australia.rent

import groovy.transform.Immutable

@Immutable
class RentDetails {
    BigDecimal price
    String address
    String suburb
    String state
    String postcode
}
