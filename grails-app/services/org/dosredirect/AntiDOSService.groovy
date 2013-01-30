package org.dosredirect
import groovy.time.*

class AntiDOSService {

    protected Integer timeWindowInSeconds
    protected Integer maxRequestsInWindow
    protected TimeDuration timeWindow


    def grailsApplication


    public AntiDOSService() {
        timeWindowInSeconds = grailsApplication.config.antiDOS.timeWindowInSeconds
        maxRequestsInWindow = grailsApplication.config.antiDOS.maxRequestsInWindow
        timeWindow = new TimeDuration(0,0,timeWindowInSeconds,0)
    }


    public changeCount(action,record) {
        switch (action.toLowerCase()) {
            case "increase":
                record.visits += 1
                record.lastVisit = new Date()
                record.save(flush:true)
            case "reset":
                record.visits = 1
                record.lastVisit = new Date()
                record.save(flush:true)
            return record
        }
    }


    public Requests requestCountFor(address) {
        return Requests.findOrCreateWhere(ipaddress: address)
    }


    public boolean ipThresholdReached(address) {
        def request = Requests.findOrCreateWhere(ipaddress: address)
        def durationSinceLastRequest = TimeCategory.minus(new Date(), request.lastVisit)
        if (durationSinceLastRequest < timeWindow && request.visits > maxRequestsInWindow) {
            return true
        }
        else {
            return false
        }
   }

}


