package thomashan.github.io.australia.rent.domain

import geb.Page

class RentDetailsPage extends Page {
    static url = "https://www.domain.com.au/75-chi-avenue-keysborough-vic-3173-12736283"

    static content = {
        price { $("div.listing-details__summary-title").text() }
    }
}
