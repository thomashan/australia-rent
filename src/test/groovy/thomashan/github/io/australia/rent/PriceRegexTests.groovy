package thomashan.github.io.australia.rent

import org.junit.jupiter.api.Test

class PriceRegexTests {
    @Test
    void "price regex"() {
        String price = '$600 Per Week'
        def result = getPrice(price)

        assert result == 600
    }

    @Test
    void "price regex decimal places"() {
        String price = '$600.15 Per Week'
        def result = getPrice(price)

        assert result == 600.15
    }

    @Test
    void "price regex case insensitive"() {
        String price = '$600 per week'
        def result = getPrice(price)

        assert result == 600
    }

    @Test
    void "price regex 3"() {
        String price = '$600pw'
        def result = getPrice(price)

        assert result == 600
    }

    @Test
    void "price regex 4"() {
        String price = '$600pw/$2400pcm'
        def result = getPrice(price)

        assert result == 600
    }

    @Test
    void "price regex 5"() {
        String price = '$600 weekly'
        def result = getPrice(price)

        assert result == 600
    }

    private Number getPrice(String price) {
        def matcher = price =~ /(\d+\.?\d+)/
        return matcher[0][0] as BigDecimal
    }
}
