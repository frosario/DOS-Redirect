package freddie

import freddie.Requests
import groovy.time.*

class AntiDOSService {

    public AntiDOSService(seconds=1,requests=100) {
        this.TIME_WINDOW_IN_SECONDS = seconds
        this.MAX_REQUESTS_IN_WINDOW = requests
        this.timeWindow = new TimeDuration(0,0,TIME_WINDOW_IN_SECONDS,0)
    }


    public Requests requestCountFor(address, action="") {
        request = Requests.findOrCreateWhere(ipaddress: address)
        switch (action.toLowerCase()) {
            case "":
                //Do nothing, will return request object after switch block
            case "increase":
                request.visits += 1
                request.lastVisit = new Date()
                request.save()
            case "reset":
                request.visits = 1
                request.lastVisit = new Date()
                request.save()
        return request
        }
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
