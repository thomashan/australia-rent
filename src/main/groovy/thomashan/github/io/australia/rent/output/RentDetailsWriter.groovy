package thomashan.github.io.australia.rent.output

import thomashan.github.io.australia.rent.RentDetails

trait RentDetailsWriter {
    abstract File file(List<RentDetails> rentDetails)
}
