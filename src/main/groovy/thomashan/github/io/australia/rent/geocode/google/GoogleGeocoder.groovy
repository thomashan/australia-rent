package thomashan.github.io.australia.rent.geocode.google


import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
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
                String fullAddress = "${it.address}, ${it.suburb}, ${it.state} ${it.postcode}"
                println("Geocoding: ${fullAddress}")
                GeocodingResult[] geocodingResults = GeocodingApi.geocode(geoApiContext, fullAddress).await()
                if (geocodingResults.size() > 0) {
                    GeocodingResult geocodingResult = geocodingResults[0]
                    LatLongCoordinates latLongCoordinates = new LatLongCoordinates(geocodingResult.geometry.location.lat, geocodingResult.geometry.location.lng)

                    return new RentDetails(it.price, it.address, it.suburb, it.state, it.postcode, it.bedrooms, it.bathrooms, it.parking, Optional.of(latLongCoordinates))
                } else {
                    String fullAddress1 = "${dropUnit(it.address)}, ${it.suburb}, ${it.state} ${it.postcode}"
                    println("Geocoding: ${fullAddress1}")

                    GeocodingResult[] geocodingResults1 = GeocodingApi.geocode(geoApiContext, fullAddress1).await()

                    if (geocodingResults1.size() > 0) {
                        GeocodingResult geocodingResult1 = geocodingResults1[0]
                        LatLongCoordinates latLongCoordinates = new LatLongCoordinates(geocodingResult1.geometry.location.lat, geocodingResult1.geometry.location.lng)

                        return new RentDetails(it.price, it.address, it.suburb, it.state, it.postcode, it.bedrooms, it.bathrooms, it.parking, Optional.of(latLongCoordinates))
                    }

                    return it
                }
            }
        }
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
