package thomashan.github.io.australia.rent.domain

import geb.Module

import java.util.regex.Matcher

class RentListSummaryModule extends Module {
    static content = {
        price {
            Matcher matcher = $("p.listing-result__price").text() =~ /(\d+\.?\d+)/
            matcher.getCount() > 0 ? Optional.of(matcher[0][0] as BigDecimal) : Optional.empty()
        }
        addressLine1 { $("span.address-line1").text().strip() - "," }
        suburb { $("span.address-line2 span[itemprop='addressLocality']").text() }
        state { $("span.address-line2 span[itemprop='addressRegion']").text() }
        postcode { $("span.address-line2 span[itemprop='postalCode']").text() }
    }
}
