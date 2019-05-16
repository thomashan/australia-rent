package thomashan.github.io.australia.rent.search

import thomashan.github.io.australia.rent.SearchQuery

trait Search {
    abstract SearchQuery getSearchQuery()

    abstract void search()

    String getName() {
        return "prices_${prices}_bedrooms_${bedrooms}"
    }

    String getPrices() {
        return "${searchQuery.minPrice}-${searchQuery.maxPrice}"
    }

    String getBedrooms() {
        if (searchQuery.maxBedroom.isEmpty()) {
            return "${searchQuery.minBedroom}+"
        }

        return "${searchQuery.minBedroom}-${searchQuery.maxBedroom.get()}"
    }
}
