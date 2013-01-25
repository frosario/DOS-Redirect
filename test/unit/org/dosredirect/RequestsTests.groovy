package org.dosredirect
import grails.test.*
import org.junit.*
import grails.test.mixin.domain.DomainClassUnitTestMixin


/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */


@TestFor(Requests)
@Mock(Requests)
@TestMixin(DomainClassUnitTestMixin)
class RequestsTests {

    def random = new Random()


    String randomIP(rand) {
        def tmp = rand.nextInt(255).toString() + "." \
                + rand.nextInt(255).toString() + "." \
                + rand.nextInt(255).toString() + "." \
                + rand.nextInt(255).toString()
        return tmp
    }


    Integer randomVisits(rand) {
        return rand.nextInt()
    }


    void testAttributes() {
        def ip = randomIP(random)
        def v = randomVisits(random)
        def lv = new Date()
        def request = new Requests(ipaddress: ip, visits: v, lastVisit: lv)
        assertEquals ip, request.ipaddress
        assertEquals v, request.visits
        assertEquals lv, request.lastVisit
    }


    void testBlankConstraints() {
        def request = new Requests(ipaddress: "", visits: "", lastVisit: "")
        assertFalse request.validate()
        assert request.errors["ipaddress"]
        assert request.errors["visits"]
        assert request.errors["lastVisit"]
    }


    void testIpAddressConstraints() {
        //IP address needs to be a minimum of a 7 character string
        def request = new Requests(ipaddress: random.nextInt(500).toString())
        assertFalse request.validate()
        assert request.errors["ipaddress"]

        //IP address can not be over 15 character string
        request = new Requests(ipaddress: "1000000000000000")
        assertFalse request.validate()
        assert request.errors["ipaddress"]

        //IP address must be unique
        def ip = randomIP(random)
        def mockRequest = new Requests(ipaddress: ip, visits: 1, lastVisit: new Date())
        mockDomain(Requests,[mockRequest])
        request = new Requests(ipaddress: ip)
        assertFalse request.validate()
        assert request.errors['ipaddress']
    }

    void testValidRequestObject() {
        //A valid request object should pass all the validations
        def request = new Requests(ipaddress: randomIP(random),\
                                   visits: randomVisits(random),\
                                   lastVisit: new Date())
        assert request.validate()
    }
}

