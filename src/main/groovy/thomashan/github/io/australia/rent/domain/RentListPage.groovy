package thomashan.github.io.australia.rent.domain

import geb.Page
import geb.navigator.Navigator
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.search.SearchParameterExtractor as s
import thomashan.github.io.australia.rent.search.SearchQuery

class RentListPage extends Page {
    private static final emptyString = ""
    static url = "https://www.domain.com.au/rent"

    static content = {
        list {
            $("ul[data-testid='results']").$("li[data-testid^='listing'],li[data-testid^='topspot']").moduleList(RentListSummaryModule)
        }
        pageNumber { $("span[data-testid='paginator-page-button']").text() as Integer }
        pageEnd {
            Navigator navigator = $("a[data-testid='paginator-page-button']")
            return !navigator.empty ? navigator.last().text() as Integer : 1
        }
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
        return "/?${[suburbs(searchQuery), price(searchQuery), bedrooms(searchQuery), "sort=suburb-asc", "excludedeposittaken=1"].findAll { it }.join("&")}"
    }

    // in the format parkville-vic-3052
    private static String suburbs(SearchQuery searchQuery) {
        return "suburb=${s.suburbs(searchQuery)}"
    }

    private static String bedrooms(SearchQuery searchQuery) {
        String bedrooms = s.bedrooms(searchQuery)
        return bedrooms ? "bedrooms=${bedrooms}" : bedrooms
    }

    private static String price(SearchQuery searchQuery) {
        String price = s.prices(searchQuery)
        return price ? "price=${price}" : price
    }
}
