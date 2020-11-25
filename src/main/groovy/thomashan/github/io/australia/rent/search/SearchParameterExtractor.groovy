package thomashan.github.io.australia.rent.search

final class SearchParameterExtractor {
    private static final emptyString = ""

    private SearchParameterExtractor() {
        // prevent instantiation
    }

    static String suburbs(SearchQuery searchQuery) {
        return searchQuery.suburbs.empty || searchQuery.suburbs.get().empty ? "melbourne-region-vic" : searchQuery.suburbs.get().join(",")
    }

    static String bedrooms(SearchQuery searchQuery) {
        switch (searchQuery) {
            case { it.minBedroom.empty && it.maxBedroom.empty }:
                return emptyString
            case { !it.minBedroom.empty && !it.maxBedroom.empty }:
                return "${searchQuery.minBedroom.get()}-${searchQuery.maxBedroom.get()}"
            case { !it.minBedroom.empty && it.maxBedroom.empty }:
                return "${searchQuery.minBedroom.get()}+"
            case { it.minBedroom.empty && !it.maxBedroom.empty }:
                return "0-${searchQuery.maxBedroom.get()}"
            default:
                throw new RuntimeException("shouldn't get here")
        }
    }

    static String prices(SearchQuery searchQuery) {
        switch (searchQuery) {
            case { it.minPrice.empty && it.maxPrice.empty }:
                return emptyString
            case { !it.minPrice.empty && !it.maxPrice.empty }:
                return "${searchQuery.minPrice.get()}-${searchQuery.maxPrice.get()}"
            case { !it.minPrice.empty && it.maxPrice.empty }:
                return "${searchQuery.minPrice.get()}+"
            case { it.minPrice.empty && !it.maxPrice.empty }:
                return "0-${searchQuery.maxPrice.get()}"
            default:
                throw new RuntimeException("shouldn't get here")
        }
    }
}
