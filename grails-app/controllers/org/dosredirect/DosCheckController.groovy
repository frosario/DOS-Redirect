package org.dosredirect

class DosCheckController {

    AntiDOSService antiDOSService

    def index() { 
        def ip = "1.1.1.1"
        def visitRecord = antiDOSService.requestCountFor(ip)
        visitRecord = antiDOSService.changeCount("increase",visitRecord)
        render visitRecord.visits
        
        //render request.getHeader("Client-IP")
    }
}
//TODO: Reset visit count if timewindow expires
