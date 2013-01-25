package org.dosredirect
import org.dosredirect.*

class DosCheckController {

    def index() { 
        def ads = new AntiDOSService()
        def ip = "1.1.1.1"
        def visitRecord = ads.requestCountFor(ip)
        visitRecord = ads.changeCount("increase",visitRecord)
        render visitRecord.visits
        
        //render request.getHeader("Client-IP")
    }
}
//TODO: Reset visit count if timewindow expires
