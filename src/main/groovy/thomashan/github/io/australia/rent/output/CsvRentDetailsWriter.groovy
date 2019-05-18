package thomashan.github.io.australia.rent.output

import thomashan.github.io.australia.rent.RentDetails

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

        return rentDetailsCsvFile
    }

    private String getHeaderLine() {
        return ["price", "address", "suburb", "state", "postcode", "latitude", "longitude"].inject { result, field -> "${result},${field}" } + newline
    }

    private String line(RentDetails r) {
        return [price(r), address(r), suburb(r), state(r), postcode(r), latitude(r), longitude(r)]
                .collect { "\"${it}\"" }
                .inject { result, field -> "${result},${field}" } + newline
    }

    private String price(RentDetails rentDetails) {
        return !rentDetails.price.empty ? rentDetails.price.get().toString() : ""
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
