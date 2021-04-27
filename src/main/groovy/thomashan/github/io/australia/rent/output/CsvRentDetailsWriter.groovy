package thomashan.github.io.australia.rent.output

import thomashan.github.io.australia.rent.RentDetails

import static thomashan.github.io.australia.rent.output.CsvWriterHelper.h
import static thomashan.github.io.australia.rent.output.CsvWriterHelper.q

class CsvRentDetailsWriter implements RentDetailsWriter {
    private static String newline = "\n"

    @Override
    File file(List<RentDetails> rentDetails) {
        File rentDetailsCsvFile = File.createTempFile("rent_details", ".csv")
        rentDetailsCsvFile.with {
            rentDetailsCsvFile.write(headerLine)
            rentDetails.each { RentDetails r ->
                rentDetailsCsvFile << line(r)
            }

            println("generating csv file: ${rentDetailsCsvFile.absolutePath}")
        }
        rentDetailsCsvFile.deleteOnExit()

        return rentDetailsCsvFile
    }

    private String getHeaderLine() {
        return h(["price", "bedrooms", "bathrooms", "parking", "address", "suburb", "state", "postcode", "latitude", "longitude", "daysInMarket"])
    }

    private String line(RentDetails rentDetails) {
        return [price(rentDetails),
                bedrooms(rentDetails),
                bathrooms(rentDetails),
                parking(rentDetails),
                q(address(rentDetails)),
                q(suburb(rentDetails)),
                q(state(rentDetails)),
                q(postcode(rentDetails)),
                latitude(rentDetails),
                longitude(rentDetails),
                q(daysInMarket(rentDetails)),
        ]
                .inject { result, field -> "${result},${field}" } + newline
    }

    private String daysInMarket(RentDetails rentDetails) {
        return rentDetails.daysInMarket.entrySet().stream()
                .map(entry -> entry.getKey().toString() + ":" + entry.getValue().toString())
                .reduce((a, b) -> a + "|" + b)
                .orElse("")
    }

    private String price(RentDetails rentDetails) {
        return !rentDetails.price.empty ? rentDetails.price.get().toString() : ""
    }

    private String bedrooms(RentDetails rentDetails) {
        return rentDetails.bedrooms.toString()
    }

    private String bathrooms(RentDetails rentDetails) {
        return rentDetails.bathrooms.toString()
    }

    private String parking(RentDetails rentDetails) {
        return rentDetails.parking.toString()
    }

    private String address(RentDetails rentDetails) {
        return rentDetails.address
    }

    private String suburb(RentDetails rentDetails) {
        return rentDetails.suburb
    }

    private String state(RentDetails rentDetails) {
        return rentDetails.state
    }

    private String postcode(RentDetails rentDetails) {
        return rentDetails.postcode
    }

    private String latitude(RentDetails rentDetails) {
        return !rentDetails.coordinates.empty ? rentDetails.coordinates.get().latitude.toString() : ""
    }

    private String longitude(RentDetails rentDetails) {
        return !rentDetails.coordinates.empty ? rentDetails.coordinates.get().longitude.toString() : ""
    }
}
