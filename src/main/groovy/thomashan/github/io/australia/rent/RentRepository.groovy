package thomashan.github.io.australia.rent

import thomashan.github.io.australia.rent.search.SearchQuery

trait RentRepository {
    abstract List<RentDetails> findAll(SearchQuery searchQuery)
}
