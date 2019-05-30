package thomashan.github.io.australia.rent.domain

import geb.Page
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.SearchQuery

class RentListPage extends Page {
    private static final emptyString = ""
    static url = "https://www.domain.com.au/rent"

    static content = {
        list {
            $("ul[data-testid='results']").$("li[data-testid]").moduleList(RentListSummaryModule)
        }
        pageNumber { $("a.paginator__page-button.is-current").text() as Integer }
        pageEnd { $("div.paginator__pages a.paginator__page-button").last().text() as Integer }
    }

    List<RentDetails> getRentDetails() {
        return list.collect {
            new RentDetails(price: it.price,
                    address: it.addressLine1,
                    suburb: it.suburb,
                    state: it.state,
                    postcode: it.postcode,
                    bedrooms: it.bedrooms,
                    bathrooms: it.bathrooms,
                    parking: it.parking
            )
        }
    }

    String convertToPath(SearchQuery searchQuery) {
        return "/?${suburbs(searchQuery)}&sort=suburb-asc&excludedeposittaken=1${price(searchQuery)}${bedrooms(searchQuery)}"
    }

    // in the format parkville-vic-3052
    private static String suburbs(SearchQuery searchQuery) {
        String suburbs = searchQuery.suburbs.empty || searchQuery.suburbs.get().empty ? "melbourne-region-vic" : searchQuery.suburbs.get().join(",")

        return searchQuery.suburbs.present && searchQuery.suburbs.get().size() > 1 ? "suburb=${suburbs}" : suburbs
    }

    private static String bedrooms(SearchQuery searchQuery) {
        switch (searchQuery) {
            case { it.minBedroom.empty && it.maxBedroom.empty }:
                return emptyString
            case { !it.minBedroom.empty && !it.maxBedroom.empty }:
                return "&bedrooms=${searchQuery.minBedroom.get()}-${searchQuery.maxBedroom.get()}"
            case { !it.minBedroom.empty && it.maxBedroom.empty }:
                return "&bedrooms=${searchQuery.minBedroom.get()}-any"
            case { it.minBedroom.empty && !it.maxBedroom.empty }:
                return "&bedrooms=0-${searchQuery.maxBedroom.get()}"
            default:
                throw new RuntimeException("shouldn't get here")
        }
    }

    private static String price(SearchQuery searchQuery) {
        switch (searchQuery) {
            case { it.minPrice.empty && it.maxPrice.empty }:
                return emptyString
            case { !it.minPrice.empty && !it.maxPrice.empty }:
                return "&price=${searchQuery.minPrice.get()}-${searchQuery.maxPrice.get()}"
            case { !it.minPrice.empty && it.maxPrice.empty }:
                return "&price=${searchQuery.minPrice.get()}-any"
            case { it.minPrice.empty && !it.maxPrice.empty }:
                return "&price=0-${searchQuery.maxPrice.get()}"
            default:
                throw new RuntimeException("shouldn't get here")
        }
    }
}
