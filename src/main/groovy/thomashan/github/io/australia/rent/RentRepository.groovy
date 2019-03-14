package thomashan.github.io.australia.rent

trait RentRepository {
    abstract List<RentDetails> findAll(SearchQuery searchQuery)
}
