package org.dosredirect
import grails.test.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */


@TestFor(AntiDOSService)
@Mock(Requests)
class AntiDOSServiceTests {


    def ads = new AntiDOSService()
    def random = new Random()


    String randomIP() {
        def tmp = random.nextInt(255).toString() + "." \
                + random.nextInt(255).toString() + "." \
                + random.nextInt(255).toString() + "." \
                + random.nextInt(255).toString()
        return tmp 
    } 


    def randomDate() {
        def second = random.nextInt(59)
        def minute = random.nextInt(59)
        def hour = random.nextInt(23)
        def day = random.nextInt(30)
        def month = random.nextInt(12)
        def year = random.nextInt(2012)
        def date = Date.parse("ss-mm-hh-dd-MM-yyyy",\
        "${second}-${minute}-${hour}-${day}-${month}-${year}")
        return date
    }


    void testChangeCountIncrease() {
        //Should increase count my 1 and update to current timestamp
        def request = new Requests()
        def beforeCount = request.visits
        def beforeDate = request.lastVisit
        ads.changeCount("increase",request)
        def afterCount = request.visits
        def afterDate = request.lastVisit
        assert beforeCount < afterCount
        assertFalse beforeDate > afterDate
    }


    void testChangeCountReset() {
        //Should reset count to 1 and lastVisit to current timestamp
        def request = new Requests(visits: 10, lastVisit: randomDate())
        def beforeCount = request.visits
        def beforeDate = request.lastVisit
        ads.changeCount("reset",request)
        def resetCount = request.visits
        def resetDate = request.lastVisit
        assert resetCount < beforeCount
        assert resetDate > beforeDate
    }


    void testRequestCountForWithSingleArgument() {
        def record = ads.requestCountFor(randomIP())
        assertEquals record.getClass(), Requests
    }


    void testIpThresholdReached() {
        def ip = randomIP()
        def request = new Requests(ipaddress: ip, visits: random.nextInt(100)+2, lastVisit: randomDate())
        request.save()
        def answer = ads.ipThresholdReached(request)

        //Make sure this method always returns a boolean
        assertEquals Boolean, answer.getClass()

        //Make sure this method resets counter if enough time has passed
        request = Requests.findByIpaddress(ip)
        assertEquals 1, request.visits
    }

}
