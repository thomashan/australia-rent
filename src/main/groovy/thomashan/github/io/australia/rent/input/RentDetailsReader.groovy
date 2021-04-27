package thomashan.github.io.australia.rent.input

import thomashan.github.io.australia.rent.RentDetails

import java.util.stream.Stream

trait RentDetailsReader {
    abstract List<RentDetails> getList(InputStream inputStream)

    abstract Stream<RentDetails> getStream(InputStream inputStream)
}
