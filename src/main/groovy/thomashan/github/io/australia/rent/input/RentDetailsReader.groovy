package thomashan.github.io.australia.rent.input

import thomashan.github.io.australia.rent.RentDetails

trait RentDetailsReader {
    abstract List<RentDetails> read(InputStream inputStream)
}
