package thomashan.github.io.australia.rent.input

import com.xlson.groovycsv.CsvParser
import com.xlson.groovycsv.PropertyMapper
import thomashan.github.io.australia.rent.DataSource
import thomashan.github.io.australia.rent.LatLongCoordinates
import thomashan.github.io.australia.rent.RentDetails

import java.util.stream.Collectors
import java.util.stream.Stream

class CsvRentDetailsReader implements RentDetailsReader {
    @Override
    Stream<RentDetails> getStream(InputStream inputStream) {
        return getList(inputStream).stream()
    }

    @Override
    List<RentDetails> getList(InputStream inputStream) {
        Reader reader = new BufferedReader(new InputStreamReader(inputStream))

        reader.withCloseable {
            return CsvParser.parseCsv(reader).collect { PropertyMapper propertyMapper ->
                Map<String, Integer> columns = propertyMapper.columns

                return new RentDetails(price(propertyMapper),
                        propertyMapper["address"],
                        propertyMapper["suburb"],
                        propertyMapper["state"],
                        propertyMapper["postcode"],
                        propertyMapper["bedrooms"] as Integer,
                        propertyMapper["bathrooms"] as Integer,
                        propertyMapper["parking"] as Integer,
                        coordinates(propertyMapper),
                        columns.containsKey("daysInMarket") ? daysInMarket(propertyMapper) : new EnumMap<>(DataSource))
            }
        }
    }

    private EnumMap<DataSource, Long> daysInMarket(PropertyMapper propertyMapper) {
        String daysInMarket = propertyMapper["daysInMarket"]
        return new EnumMap<DataSource, Long>(Arrays.stream(daysInMarket.split("\\|"))
                .map(dataSourceDaysInMarket -> new Tuple(dataSourceDaysInMarket.split(":")))
                .collect(Collectors.toMap(tuple -> DataSource.valueOf(tuple[0]), tuple -> Long.parseLong(tuple[1]))))
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
