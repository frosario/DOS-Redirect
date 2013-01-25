package org.dosredirect
import org.dosredirect.Requests
import groovy.time.*

class AntiDOSService {

    protected Integer timeWindowInSeconds
    protected Integer maxRequestsInWindow
    protected TimeDuration timeWindow


    public AntiDOSService() {
        timeWindowInSeconds = 1
        maxRequestsInWindow = 100
        timeWindow = new TimeDuration(0,0,timeWindowInSeconds,0)
    }


    public AntiDOSService(seconds, requests) {
        timeWindowInSeconds = seconds
        maxRequestsInWindow = requests
        timeWindow = new TimeDuration(0,0,timeWindowInSeconds,0)
    }


    public changeCount(action,record) {
        switch (action.toLowerCase()) {
            case "increase":
                record.visits += 1
                record.lastVisit = new Date()
                record.save()
            case "reset":
                record.visits = 1
                record.lastVisit = new Date()
                record.save()
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


