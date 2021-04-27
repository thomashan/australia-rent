package thomashan.github.io.australia.rent.geocode.google


import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.errors.OverQueryLimitException
import com.google.maps.model.GeocodingResult
import thomashan.github.io.australia.rent.LatLongCoordinates
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.geocode.Geocoder

import java.util.regex.Matcher
import java.util.regex.Pattern

class GoogleGeocoder implements Geocoder {
    private static final String GOOGLE_API_KEY = System.getenv("GOOGLE_API_KEY")
    private GeoApiContext geoApiContext
    private Pattern dropUnitPattern = ~/\d*\/(\d.*)/

    GoogleGeocoder() {
        if (!GOOGLE_API_KEY) {
            throw new RuntimeException("Please specify GOOGLE_API_KEY")
        }
        geoApiContext = new GeoApiContext.Builder()
                .apiKey(GOOGLE_API_KEY)
                .build()
    }


    @Override
    List<RentDetails> geocode(List<RentDetails> rentDetails) {
        return rentDetails.collect {
            if (!it.coordinates.empty) {
                return it
            } else {
                String fullAddress = getFullAddress(it)
                println("Geocoding: ${fullAddress}")

                try {
                    GeocodingResult[] geocodingResults = GeocodingApi.geocode(geoApiContext, fullAddress).awaitIgnoreError()
                    if (geocodingResults) {
                        switch (geocodingResults.size()) {
                            case 0:
                                println("Failed to geocode ${fullAddress}")
                                return it
                            default:
                                GeocodingResult geocodingResult = geocodingResults[0]
                                LatLongCoordinates latLongCoordinates = new LatLongCoordinates(geocodingResult.geometry.location.lat, geocodingResult.geometry.location.lng)

                                return it.copyWith(coordinates: Optional.of(latLongCoordinates))
                        }
                    } else {
                        println("Failed to geocode ${fullAddress}")
                        return it
                    }
                } catch (OverQueryLimitException e) {
                    println("OverQueryLimitException ${fullAddress}")
                    return it
                }
            }
        }
    }

    private String getFullAddress(RentDetails rentDetails) {
        return "${dropUnit(rentDetails.address)}, ${rentDetails.suburb}, ${rentDetails.state} ${rentDetails.postcode}"
    }

    private String dropUnit(String address) {
        Matcher matcher = dropUnitPattern.matcher(address)

        if (matcher.size() > 0) {
            int size = matcher[0].size()

            return matcher[0][size - 1]
        }

        return address
    }
}
