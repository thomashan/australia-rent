package thomashan.github.io.australia.rent.domain

import geb.Page
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.SearchQuery

class RentListPage extends Page {
    static url = "https://www.domain.com.au/rent/melbourne-region-vic/?sort=suburb-asc&excludedeposittaken=1"

    static content = {
        list {
            $("ul[data-testid='results']").$("li[data-testid]").moduleList(RentListSummaryModule)
        }
        pageNumber { $("a.paginator__page-button.is-current").text() as Integer }
        pageEnd { $("div.paginator__pages a.paginator__page-button").last().text() as Integer }
    }

    List<RentDetails> getRentDetails() {
        return list.collect {
            new RentDetails(price: it.price, address: it.addressLine1, suburb: it.suburb, state: it.state, postcode: it.postcode)
        }
    }

    String convertToPath(SearchQuery searchQuery) {
        String maxBedrooms = getMaxBedrooms(searchQuery)

        return "&price=${searchQuery.minPrice}-${searchQuery.maxPrice}&bedrooms=${searchQuery.minBedroom}-${maxBedrooms}"
    }

    String getMaxBedrooms(SearchQuery searchQuery) {
        return searchQuery.maxBedroom.empty ? "any" : searchQuery.maxBedroom.get().toString()
    }
}
