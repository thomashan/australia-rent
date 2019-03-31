package thomashan.github.io.australia.rent.geocode

import com.google.common.geometry.S2LatLng
import com.xlson.groovycsv.CsvParser

import java.util.zip.ZipInputStream

class PostcodeGeocoder {
    private static Map<Integer, S2LatLng> postcodeLatLongs = readZip()

    private static Map<Integer, S2LatLng> readZip() {
        ZipInputStream zipIs = new ZipInputStream(this.getResourceAsStream("/data/Australian_Post_Codes_Lat_Lon.zip"))

        Map<Integer, S2LatLng> data = null
        zipIs.withCloseable {
            zipIs.getNextEntry()

            Reader reader = new BufferedReader(new InputStreamReader(zipIs))

            data = CsvParser.parseCsv(reader).findAll {
                return it["postcode"] != "0"
            }.collectEntries {
                return [(it["postcode"].toInteger()), S2LatLng.fromDegrees(it["lat"].toDouble(), it["lon"].toDouble())]
            }
        }

        return data
    }

    static Optional<S2LatLng> postcode(int postcode) {
        S2LatLng latLong = postcodeLatLongs[postcode]
        if (latLong) {
            return Optional.of(latLong)
        }

        return Optional.empty()
    }
}
