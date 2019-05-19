package thomashan.github.io.australia.rent.domain

import geb.Module
import geb.navigator.Navigator

import java.util.regex.Matcher

class RentListSummaryModule extends Module {
    private static final BEDROOMS = "Bed"
    private static final BATHROOMS = "Bath"
    private static final PARKING = "Parking"

    static content = {
        price {
            Matcher matcher = $("p.listing-result__price").text() =~ /(\d+\.?\d+)/
            matcher.getCount() > 0 ? Optional.of(matcher[0][0] as BigDecimal) : Optional.empty()
        }
        addressLine1 {
            String address1 = $("span.address-line1").text()
            return address1 ? address1?.strip() - "," : ""
        }
        suburb { $("span.address-line2 span[itemprop='addressLocality']").text() }
        state { $("span.address-line2 span[itemprop='addressRegion']").text() }
        postcode { $("span.address-line2 span[itemprop='postalCode']").text() }
        bedrooms {
            Navigator bedroomElements = $("span.property-features__feature-text", text: contains(BEDROOMS))
            return getValue(bedroomElements)
        }
        bathrooms {
            Navigator bathroomElements = $("span.property-features__feature-text", text: contains(BATHROOMS))
            return getValue(bathroomElements)
        }
        parking {
            Navigator parkingElements = $("span.property-features__feature-text", text: contains(PARKING))
            return getValue(parkingElements)
        }

        addressLine2 { $("span.address-line2").text() }
    }

    static Integer getValue(String text) {
        Matcher matcher = text =~ /.*(\d).*/
        if (matcher.count == 0) {
            return 0
        }

        Integer size = matcher[0].size()

        return matcher[0][size - 1] as Integer
    }

    static Integer getValue(Navigator element) {
        if (element.isEmpty()) {
            return 0
        }

        return getValue(element.parent().text())
    }
}
