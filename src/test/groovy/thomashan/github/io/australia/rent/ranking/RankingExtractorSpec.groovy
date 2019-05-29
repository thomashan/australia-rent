package thomashan.github.io.australia.rent.ranking

import spock.lang.Specification

import java.util.regex.Matcher
import java.util.regex.Pattern

class RankingExtractorSpec extends Specification {
    private Pattern pattern = Pattern.compile("(?ms)Ranked:\\s?(\\d+)(?:(?:.*)(?:Previous rank)(?:ing)?:\\s?(\\d+|–))?")
//    private Pattern pattern = Pattern.compile("(?ms)Ranked:\\s?(\\d+).*(?:Previous rank(ing)?:\\s?)(\\d|–)")

    def "should match ranking text 1"() {
        given:
        String text = """Ranked: 58
 Previous ranking: 80"""

        when:
        Matcher matcher = pattern.matcher(text)

        then:
        matcher[0][1].toInteger() == 58
        matcher[0][2].toInteger() == 80
    }

    def "should match ranking text 2"() {
        given:
        String text = """Ranked: 312
 Previous rank: –"""

        when:
        Matcher matcher = pattern.matcher(text)

        then:
        matcher[0][1].toInteger() == 312
        matcher[0][2] == "–"
    }

    def "should match ranking text 3"() {
        given:
        String text = """Ranked: 315"""

        when:
        Matcher matcher = pattern.matcher(text)

        then:
        matcher[0][1].toInteger() == 315
        matcher[0][2] == null
    }

    def "should match ranking text 4"() {
        given:
        String text = """Ranked:133
 Previous rank: 135"""

        when:
        Matcher matcher = pattern.matcher(text)

        then:
        matcher[0][1].toInteger() == 133
        matcher[0][2].toInteger() == 135
    }
}
