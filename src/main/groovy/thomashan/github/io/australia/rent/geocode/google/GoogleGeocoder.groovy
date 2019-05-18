package thomashan.github.io.australia.rent.geocode.google


import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.model.GeocodingResult
import thomashan.github.io.australia.rent.LatLongCoordinates
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.geocode.Geocoder

class GoogleGeocoder implements Geocoder {
    private static final String GOOGLE_API_KEY = System.getenv("GOOGLE_API_KEY")
    private GeoApiContext geoApiContext

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
                GeocodingResult[] geocodingResults = GeocodingApi.geocode(geoApiContext, "${it.address}, ${it.suburb}, ${it.state} ${it.postcode}").await()
                GeocodingResult geocodingResult = geocodingResults[0]
                LatLongCoordinates latLongCoordinates = new LatLongCoordinates(geocodingResult.geometry.location.lat, geocodingResult.geometry.location.lng)

                return new RentDetails(it.price, it.address, it.suburb, it.state, it.postcode, Optional.of(latLongCoordinates))
            }
        }
    }
}
