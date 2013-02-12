package org.dosredirect
import org.slf4j.LoggerFactory;
import org.slf4j.Logger
import org.springframework.beans.factory.InitializingBean;
import groovy.time.*


class AntiDOSService implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger( AntiDOSService.class )
    protected Integer timeWindowInSeconds
    protected Integer maxRequestsInWindow
    protected TimeDuration timeWindow
    def grailsApplication

	
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            timeWindowInSeconds = grailsApplication.config.antiDOS.timeWindowInSeconds
        } catch (Exception e) {
            LOGGER.warn("Exception: " + e.getMessage())
            timeWindowInSeconds = 1
        }
        try {
            maxRequestsInWindow = grailsApplication.config.antiDOS.maxRequestsInWindow
        } catch (Exception e) {
            LOGGER.warn("Exception: " + e.getMessage())
            maxRequestsInWindow = 10
        }
        LOGGER.debug("timeWindowInSeconds is $timeWindowInSeconds")
        LOGGER.debug("maxRequestsInWindow is $maxRequestsInWindow")
        timeWindow = new TimeDuration(0,0,timeWindowInSeconds,0)
    }


    public changeCount(action,record) {
        switch (action.toLowerCase()) {
            case "increase":
        				LOGGER.info("increasing visit count on request with id $record.id")
                record.visits += 1
                record.lastVisit = new Date()
                record.save()
        				break
            case "reset":
				        LOGGER.info("resetting visit count on request with id $record.id")
                record.visits = 1
                record.lastVisit = new Date()
                record.save()
        				break
            return record
        }
    }


    public Requests requestCountFor(address) {
        LOGGER.info("Looking for Requests object with ipaddress $address")
        def requests = Requests.findOrSaveWhere(ipaddress: address)
        LOGGER.info("ID: $requests.id, VISITS: $requests.visits, LASTVISIT: $requests.lastVisit")
        return requests
    }


    public boolean ipThresholdReached(visit) {
        Requests request
        switch (visit.getClass()) {
            case String:
                request = Requests.findOrSaveWhere(ipaddress: visit)
                break
            case Requests:
                request = visit
                break
            default:
                throw new Exception("Argument must be a ip address string or Requests object")
        }
        def durationSinceLastRequest = TimeCategory.minus(new Date(), request.lastVisit)
        if (durationSinceLastRequest < timeWindow && request.visits > maxRequestsInWindow) {
            return true
        }
        else if (durationSinceLastRequest > timeWindow) {
            changeCount("reset",request)
            return false
        }
        else {
            return false
        }
   }

}


