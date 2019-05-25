package thomashan.github.io.australia.rent.search

import geb.spock.GebReportingSpec
import spock.lang.Ignore

class Step1Spec extends GebReportingSpec {
    @Ignore
    def "search test"() {
        when:
        Step1 step1 = new Step1()
        step1.search()

        then:
        1 == 1
    }
}
