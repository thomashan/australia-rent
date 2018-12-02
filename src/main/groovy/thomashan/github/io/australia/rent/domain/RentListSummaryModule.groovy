package thomashan.github.io.australia.rent.domain

import geb.Module

class RentListSummaryModule extends Module {
    static content = {
        price { $("p.listing-result__price").text() }
        addressLine1 { $("span.address-line1").text().strip() - "," }
        suburb { $("span.address-line2 span[itemprop='addressLocality']").text() }
        state { $("span.address-line2 span[itemprop='addressRegion']").text() }
        postcode { $("span.address-line2 span[itemprop='postalCode']").text() }
    }
}
