package org.dosredirect
import org.dosredirect.Requests
import groovy.time.*

class AntiDOSService {
    public AntiDOSService() {
        def TIME_WINDOW_IN_SECONDS = 1
        def MAX_REQUESTS_IN_WINDOW = 100
        def timeWindow = new TimeDuration(0,0,TIME_WINDOW_IN_SECONDS,0)
    }


    public AntiDOSService(seconds, requests) {
        def TIME_WINDOW_IN_SECONDS = seconds
        def MAX_REQUESTS_IN_WINDOW = requests
        def timeWindow = new TimeDuration(0,0,TIME_WINDOW_IN_SECONDS,0)
    }


    private countSwitch(action,record) {
        switch (action.toLowerCase()) {
            case "":
                //Do nothing, will return request object after switch block
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
        def visitRecord = Requests.findOrCreateWhere(ipaddress: address)
        visitRecord = countSwitch("",visitRecord)
        return visitRecord
    }


    public Requests requestCountFor(address,operation) {
      def visitRecord = Requests.findOrCreateWhere(ipaddress: address)
      visitRecord = countSwitch(operation,visitRecord)
      return visitRecord
    }


    public boolean ipThresholdReached(address, increase=true) {
        request = Requests.findOrCreateWhere(ipaddress: address)
        durationSinceLastRequest = TimeCategory.minus(new Date(), request.lastVisit)
        if (durationSinceLastRequest < timeWindow && request.visits > MAX_REQUESTS_IN_WINDOW) {
            return true
        }
        else if (increase == true) {
            requestCountFor(address, action="increase")
            return false
        }
        else {
            return false
        }

   }
}


//TODO: Reset visit count if timewindow expires
