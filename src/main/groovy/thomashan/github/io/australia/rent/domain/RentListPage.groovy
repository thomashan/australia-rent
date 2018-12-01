package thomashan.github.io.australia.rent.domain

import geb.Page

class RentListPage extends Page {
    static url = "https://www.domain.com.au/rent/melbourne-region-vic/?sort=suburb-asc&bedrooms=3-any&price=550-600&excludedeposittaken=1"

    static content = {
        list { $("ul.search-results__results").find("li").hasNot("div.search-results__ads-block") }
    }
}
