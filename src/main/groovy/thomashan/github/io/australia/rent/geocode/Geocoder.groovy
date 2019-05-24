package thomashan.github.io.australia.rent.geocode

import thomashan.github.io.australia.rent.RentDetails

trait Geocoder {
    abstract List<RentDetails> geocode(List<RentDetails> rentDetails)
}
