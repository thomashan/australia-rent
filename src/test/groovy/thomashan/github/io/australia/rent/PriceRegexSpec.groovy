package thomashan.github.io.australia.rent


import spock.lang.Specification

import java.util.regex.Matcher

class PriceRegexSpec extends Specification {
    def "price is not stated"() {
        when:
        String price = "Contact Agent"
        def result = getPrice(price)

        then:
        result.isEmpty()
    }

    def "price regex 1"() {
        when:
        String price = '$600'
        def result = getPrice(price)

        then:
        result.get() == 600
    }

    def "price regex 2"() {
        when:
        String price = '$600 Per Week'
        def result = getPrice(price)

        then:
        result.get() == 600
    }


    def "price regex decimal places"() {
        when:
        String price = '$600.15 Per Week'
        def result = getPrice(price)

        then:
        result.get() == 600.15
    }


    def "price regex case insensitive"() {
        when:
        String price = '$600 per week'
        def result = getPrice(price)

        then:
        result.get() == 600
    }


    def "price regex 3"() {
        when:
        String price = '$600pw'
        def result = getPrice(price)

        then:
        result.get() == 600
    }


    def "price regex 4"() {
        when:
        String price = '$600pw/$2400pcm'
        def result = getPrice(price)

        then:
        result.get() == 600
    }


    def "price regex 5"() {
        when:
        String price = '$600 weekly'
        def result = getPrice(price)

        then:
        result.get() == 600
    }

    private Optional<Number> getPrice(String price) {
        Matcher matcher = price =~ /(\d+\.?\d+)/

        return matcher.count > 0 ? Optional.of(matcher[0][0] as BigDecimal) : Optional.empty()
    }
}
