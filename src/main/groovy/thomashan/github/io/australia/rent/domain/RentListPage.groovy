package thomashan.github.io.australia.rent.domain

import geb.Page
import thomashan.github.io.australia.rent.RentDetails

class RentListPage extends Page {
    static url = "https://www.domain.com.au/rent/melbourne-region-vic/?sort=suburb-asc&bedrooms=3-any&price=550-600&excludedeposittaken=1"

    static content = {
        list {
            $("ul.search-results__results").find("li").hasNot("div.search-results__ads-block").moduleList(RentListSummaryModule)
        }
        pageNumber { $("a.paginator__page-button.is-current").text() as Integer }
    }

    List<RentDetails> getRentDetails() {
        return list.collect {
            new RentDetails(price: it.price, address: it.addressLine1, suburb: it.suburb, state: it.state, postcode: it.postcode)
        }
    }
}
