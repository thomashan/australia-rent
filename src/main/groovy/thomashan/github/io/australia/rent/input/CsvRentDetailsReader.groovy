package thomashan.github.io.australia.rent.input


import com.xlson.groovycsv.CsvParser
import com.xlson.groovycsv.PropertyMapper
import thomashan.github.io.australia.rent.LatLongCoordinates
import thomashan.github.io.australia.rent.RentDetails

class CsvRentDetailsReader implements RentDetailsReader {
    @Override
    List<RentDetails> read(InputStream inputStream) {
        Reader reader = new BufferedReader(new InputStreamReader(inputStream))

        reader.withCloseable {
            return CsvParser.parseCsv(reader).collect {
                return new RentDetails(price(it),
                        it["address"],
                        it["suburb"],
                        it["state"],
                        it["postcode"],
                        it["bedrooms"] as Integer,
                        it["bathrooms"] as Integer,
                        it["parking"] as Integer,
                        coordinates(it))
            }
        }
    }

    private Optional<BigDecimal> price(PropertyMapper propertyMapper) {
        String price = propertyMapper["price"]

        return price ? Optional.of(price.toBigDecimal()) : Optional.empty()
    }

    private Optional<LatLongCoordinates> coordinates(PropertyMapper propertyMapper) {
        String latitude = propertyMapper["latitude"]
        String longitude = propertyMapper["longitude"]

        return latitude && longitude ? Optional.of(new LatLongCoordinates(latitude.toDouble(), longitude.toDouble())) : Optional.empty()
    }
}
